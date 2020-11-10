package DesignModel.Singleton;

public class Singleton2 {
    private Singleton2() {
        System.out.println("生成了一个实例2。");
    }

    private static class SingletonInstance {
        private static final Singleton2 instance = new Singleton2();
    }

    public static Singleton2 getInstance() {
        return SingletonInstance.instance;
    }
}
