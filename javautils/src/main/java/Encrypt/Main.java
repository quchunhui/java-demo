package Encrypt;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Main {
    private final static String priFilePath = "C:\\test\\my_rsa";
    private final static String pubFilePath = "C:\\test\\my_rsa.pub";

    public static void main(String[] args) throws Exception {
        // 随机生成一对密钥（包含公钥和私钥）
        KeyPair keyPair = RSAUtils.generateKeyPair();

        // 获取 公钥 和 私钥
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey priKey = keyPair.getPrivate();

        // 保存 公钥 和 私钥
        RSAUtils.saveKeyForEncodedBase64(pubKey, pubFilePath);
        RSAUtils.saveKeyForEncodedBase64(priKey, priFilePath);

        // 原文数据
        String data = "hello my first rsa java.";

        // 客户端: 加密
        byte[] cipherData = clientEncrypt(data.getBytes());

        // 服务端: 解密
        byte[] plainData = serverDecrypt(cipherData);

        // 输出查看原文
        System.out.println(new String(plainData));
    }

    /**
     * 客户端加密, 返回加密后的数据
     */
    private static byte[] clientEncrypt(byte[] plainData) throws Exception {
        // 读取公钥文件, 创建公钥对象
        PublicKey pubKey = RSAUtils.getPublicKey(IOUtils.readFile(pubFilePath));

        // 用公钥加密数据
        return RSAUtils.encrypt(plainData, pubKey);
    }

    /**
     * 服务端解密, 返回解密后的数据
     */
    private static byte[] serverDecrypt(byte[] cipherData) throws Exception {
        // 读取私钥文件, 创建私钥对象
        PrivateKey priKey = RSAUtils.getPrivateKey(IOUtils.readFile(priFilePath));

        // 用私钥解密数据
        return RSAUtils.decrypt(cipherData, priKey);
    }
}
