package DesignModel.Singleton;

public class Singleton1 {
    private static Singleton1 singleton = new Singleton1();
    private Singleton1() {
        System.out.println("生成了一个实例1.");
    }

    public static Singleton1 getInstance() {
        return singleton;
    }
}
