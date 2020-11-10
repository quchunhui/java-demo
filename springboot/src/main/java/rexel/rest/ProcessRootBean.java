package rexel.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessRootBean extends ResultBean{
    private List<ProcessBean> data = new ArrayList<>();

    public ProcessRootBean(JSONObject jsonObject) {
        super(jsonObject);

        JSONObject data = jsonObject.getJSONObject("data");
        if (data == null) {
            return;
        }

        JSONArray jsonArray = data.getJSONArray("datas");
        if (jsonArray == null) {
            return;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            ProcessBean processBean = new ProcessBean(jsonArray.getJSONObject(i));
            this.data.add(processBean);
        }
    }
}