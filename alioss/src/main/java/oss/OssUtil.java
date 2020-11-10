package oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OssUtil {
    // OSS服务的Endpoint
   private static final String endpoint = PropertyUtil.getValue("oss", "endpoint");
    //访问OSS的Access Key ID
    private static final String accessKeyId = PropertyUtil.getValue("oss", "accessKey");
    //访问OSS的Access Key Secret
    private static final String accessKeySecret = PropertyUtil.getValue("oss", "accessSecret");
    // OSS空间
    private static final String bucketName = PropertyUtil.getValue("oss", "bucketName");
    // OSS目录
    private static final String filePath = PropertyUtil.getValue("oss", "filePath");

    private static OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);

    private OssUtil() {

    }

    /**
     * OSS上传
     *
     * @param filePath 文件名(含路径)
     * @return
     * @throws FileNotFoundException
     */
    public static String ossUpload(String filePath) {
        String uuid = UUID.randomUUID().toString();
        PutObjectResult result = putObject(bucketName, uuid, filePath);
        if (result == null) {

            return null;
        }
        return uuid;
    }

    /**
     * OSS上传
     *
     * @param input 文件流
     * @return
     * @throws FileNotFoundException
     */
    public static String ossUpload(InputStream input) {
        String uuid = UUID.randomUUID().toString() + ".jpg";
        PutObjectResult result = putObject(bucketName, uuid, input);
        if (result == null) {

            return null;
        }
        return uuid;
    }

    /**
     * oss上传
     */
    public static String ossUpload(InputStream input ,String suffix){
        if (StringUtils.isBlank(suffix)) {
            return ossUpload(input);
        }
        String uuid = UUID.randomUUID().toString() + suffix;
        PutObjectResult result = putObject(bucketName, uuid, input);
        if (result == null) {

            return null;
        }
        return uuid;
    }

    /**
     * OSS上传
     *
     * @param input 文件流
     * @return
     * @throws FileNotFoundException
     */
    public static String ossUploadPrefix(InputStream input, String prefix) {
        String uuid = UUID.randomUUID().toString() + "." + prefix;
        PutObjectResult result = putObject(bucketName, uuid, input);
        if (result == null) {

            return null;
        }
        return uuid;
    }

    /**
     * OSS下载
     *
     * @param key        object对应key
     * @return 文件字节流
     * @throws IOException
     */
    public static byte[] ossDownload(String key) {
        boolean blnBucket = hasBucket(bucketName);
        byte[] ossByte = null;
        // 判断命名空间是否存在 不存在则建立新的空间
        if (blnBucket) {
            ossByte = getObject(bucketName, key);
            return ossByte;
        }
        return ossByte;
    }

    /**
     * 上传文件
     *
     * @param bucketName
     * @param key
     * @param filePath   文件名(含路径)
     * @throws FileNotFoundException
     */
    private static PutObjectResult putObject(String bucketName, String key, String filePath) {
        // 上传Object.
        PutObjectResult result = null;
        InputStream content = null;
        try {
            // 获取指定文件的输入流
            File file = new File(filePath);
            content = new FileInputStream(file);
            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            // 必须设置ContentLength
            meta.setContentLength(file.length());
            result = client.putObject(bucketName, OssUtil.filePath + key, content, meta);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (content != null) {
                try {
                    content.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 上传文件
     *
     * @param bucketName
     * @param key
     * @throws FileNotFoundException
     */
    private static PutObjectResult putObject(String bucketName, String key, InputStream content) {
        // 上传Object.
        PutObjectResult result = null;

        try {
            byte[] temp = toByteArray(content);
            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            // 必须设置ContentLength
            meta.setContentLength(temp.length);
            meta.setHeader("x-oss-object-acl", "public-read");
            result = client.putObject(bucketName, OssUtil.filePath + key, new ByteArrayInputStream(temp), meta);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (content != null) {
                try {
                    content.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 获取指定的文件流
     *
     * @param bucketName 空间名称
     * @param key
     * @throws IOException
     */
    public static byte[] getObject(String bucketName, String key) {
        OSSObject object = null;
        byte[] arrByte = null;
        InputStream objectContent = null;
        try {
            // 获取Object，返回结果为OSSObject对象
            object = client.getObject(bucketName, filePath + key);
            // 获取Object的输入流
            objectContent = object.getObjectContent();
            // 处理Object
            arrByte = toByteArray(objectContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                // update by luyj 20151221 追加判定条件
                if (objectContent != null) {
                    objectContent.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return arrByte;
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    /**
     * 查询指定命名空间下所有文件
     *
     * @param bucketName
     * @return
     */
    public static List<OSSObjectSummary> listObjects(String bucketName) {
        // 获取指定bucket下的所有Object信息
        ObjectListing listing = client.listObjects(bucketName);
        return listing.getObjectSummaries();
    }

    /**
     * 新建Bucket
     *
     * @param bucketName
     */
    public static void createBucket(String bucketName) {
        // 新建一个Bucket
        client.createBucket(bucketName);
    }

    /**
     * 判断Bucket是否存在
     *
     * @param bucketName
     * @return
     */
    private static boolean hasBucket(String bucketName) {
        boolean exists = client.doesBucketExist(bucketName);
        return exists;
    }

    /**
     * 列出用户所有的Bucket
     */
    public static void getListBucket() {
        // 获取用户的Bucket列表
        List<Bucket> buckets = client.listBuckets();
        // 遍历Bucket
        for (Bucket bucket : buckets) {
        }
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public static String getUrl(String key) {
        Date expires = new Date(new Date().getTime() + 1000 * 60); // 1 minute to expire
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key);
        generatePresignedUrlRequest.setExpiration(expires);
        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    public static File operaFileData(String path, byte[] by) {
        String fileName = path + "baidu.jpg";
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fileout = null;
        try {
            fileout = new FileOutputStream(file);
            fileout.write(by, 0, by.length);
            fileout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileout != null) {
                    fileout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}
