package api;

import org.apache.hadoop.hbase.util.Bytes;

import java.util.Random;

public class random {
    private static String[] rows = new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"};
    private static String[] names = new String[] {"zhang san", "li si", "wang wu", "wei liu"};
    private static String[] sexs = new String[] {"men", "women"};
    private static String[] heights = new String[] {"165cm", "170cm", "175cm", "180cm"};
    private static String[] weights = new String[] {"50kg", "55kg", "60kg", "65kg", "70kg", "75kg", "80kg"};

    public static byte[] getRowKey() {
        Random random = new Random();
        return Bytes.toBytes("row" + rows[random.nextInt(rows.length)]);
    }

    public static byte[] getName() {
        Random random = new Random();
        return names[random.nextInt(names.length)].getBytes();
    }

    public static byte[] getSex() {
        Random random = new Random();
        return sexs[random.nextInt(sexs.length)].getBytes();
    }

    public static byte[] getHeight() {
        Random random = new Random();
        return heights[random.nextInt(heights.length)].getBytes();
    }

    public static byte[] getWeight() {
        Random random = new Random();
        return weights[random.nextInt(weights.length)].getBytes();
    }
}