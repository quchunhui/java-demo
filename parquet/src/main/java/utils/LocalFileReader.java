package utils;

import java.io.*;

public class LocalFileReader {
    private static LocalFileReader fileReader = null;
    private static FileInputStream fis = null;
    private static InputStreamReader isw = null;
    private static BufferedReader reader = null;
    private LocalFileReader() {}

    public static LocalFileReader getInstance(String inputFullPath) {
        if (fileReader != null) {
            return fileReader;
        }

        //初始化FileReader
        fileReader = new LocalFileReader();

        File file = new File(inputFullPath);
        if (!file.exists()) {
            return null;
        }

        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }

        try {
            isw = new InputStreamReader(fis, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        reader = new BufferedReader(isw);

        return fileReader;
    }

    /**
     * 获取BufferedReader
     * @return BufferedReader
     */
    public BufferedReader getReader() {
        return reader;
    }

    /**
     * 释放相关资源
     *
     * @throws IOException e
     */
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }

        if (isw != null) {
            isw.close();
        }

        if (fis != null) {
            fis.close();
        }
    }
}
