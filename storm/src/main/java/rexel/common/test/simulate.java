package rexel.common.test;

import com.alibaba.fastjson.JSONObject;
import rexel.common.simulate.DataRandom;

public class simulate {
    public static void main(String[] args) {
        DataRandom dr = DataRandom.getInstance();
        for (int i = 0; i < 10; i++) {
            JSONObject jsonObject = dr.getRandomExpressData();
            System.out.print(jsonObject + "\n");
        }
    }
}
