package oss;

public class OSSTestDemo {
    public static void main(String[] args){
        String s = OssUtil.ossUpload("C:/uploadPath/1.jpg");
        System.out.println(s);
    }
}
