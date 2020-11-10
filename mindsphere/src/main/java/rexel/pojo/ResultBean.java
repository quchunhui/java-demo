package rexel.pojo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Author: quchunhui
 * @Date: 2020/1/4
 * @Description:
 */
@Data
public class ResultBean {
    private int status;
    private Object object;

    public JSONArray getJSONArray() {
        return (JSONArray)object;
    }

    public JSONObject getJSONObject() {
        return (JSONObject)object;
    }
}
