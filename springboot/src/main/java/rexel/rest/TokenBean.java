package rexel.rest;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TokenBean extends  ResultBean {
    private String token;

    public TokenBean(JSONObject jsonObject) {
        super(jsonObject);
        JSONObject dataJson = jsonObject.getJSONObject("data");
        if (dataJson != null && dataJson.containsKey("token")) {
            token = dataJson.getString("token");
        }
    }
}
