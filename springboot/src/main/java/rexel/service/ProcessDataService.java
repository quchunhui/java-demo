package rexel.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface ProcessDataService {
    JSONObject findProcess(JSONObject jsonObject, int page, int size);
    JSONObject deleteProcess();
    JSONObject pullProcess() throws Exception;
    JSONObject commitProcess(JSONArray jsonArray) throws Exception;
}