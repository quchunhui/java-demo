package rexel.rest;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class ResultBean {
    private int code;
    private String msg;

    public ResultBean(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        code = jsonObject.getInteger("code");
        msg = jsonObject.getString("msg");
    }
}
