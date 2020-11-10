package rexel.common.simulate;

import com.alibaba.fastjson.JSONObject;
import rexel.common.constants.Constants;
import rexel.common.utils.Utils;

import java.util.List;
import java.util.Random;

public class DataRandom {
    private static DataRandom dataRandom = null;
    private static List<JSONObject> jsonList = null;

    public static DataRandom getInstance() {
        if (dataRandom == null) {
            dataRandom = new DataRandom();
            SaxAddress saxAddress = new SaxAddress();
            jsonList = saxAddress.getJsonList();
        }
        return dataRandom;
    }

    /**
     * EnterpriseCode:SF
     * MailNo:580367837369
     * Weight:10
     * ProvCode:230000
     * CityCode:230100
     * CountyCode:230110
     * Address:新成街道建北街63号
     * Name:******
     * Mobile:******
     * Phone:******
     */
    public JSONObject getRandomExpressData() {
        JSONObject result = jsonList.get(new Random().nextInt(jsonList.size()));
        result.put(Constants.EnterpriseCode, randomEnterpriseCode());
        result.put(Constants.MailNo, randomMailNo());
        result.put(Constants.Weight, randomWeight());
        return result;
    }

    private String randomEnterpriseCode() {
        String[] data = new String[] {
                "SF", "STO", "YTO", "YUNDA", "BSHT", "ZTO", "EMS",
                "UPS", "TNT", "TTKD", "DHL", "ZJS", "FEDEX"
        };
        return data[new Random().nextInt(data.length)];
    }

    private String randomMailNo() {
        return "667748" + (Utils.StringFormat(6, new Random().nextInt(999999)));
    }

    private String randomWeight() {
        return Integer.toString(new Random().nextInt(100));
    }
}
