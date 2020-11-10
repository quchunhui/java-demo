package rexel.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import rexel.pojo.ResultBean;
import rexel.service.IAssetManager;
import rexel.service.IDeviceManager;
import rexel.service.ITimeSeries;
import rexel.service.ITimeSeriesAggregates;
import rexel.service.ITokenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/rexel/mindsphere")
public class MindSphereController {
    private ITokenManager tokenManager;
    private ITimeSeries timeSeries;
    private IAssetManager assetManager;
    private IDeviceManager deviceManager;
    private ITimeSeriesAggregates timeSeriesAggregates;

    @Autowired
    public void setDeviceManager(IDeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    @Autowired
    public void setTimeSeriesAggregates(ITimeSeriesAggregates timeSeriesAggregates) {
        this.timeSeriesAggregates = timeSeriesAggregates;
    }

    @Autowired
    public void setAssetManager(IAssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Autowired
    public void setTokenManager(ITokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Autowired
    public void setTimeSeries(ITimeSeries timeSeries) {
        this.timeSeries = timeSeries;
    }

    @ResponseBody
    @GetMapping(value = "/timeseries/{entityId}/{propertySetName}")
    public JSONArray timeSeriesByEntity(
        @PathVariable String entityId,
        @PathVariable String propertySetName,
        @RequestParam(value = "from", required = false) String from,
        @RequestParam(value = "to", required = false) String to) throws Exception {

        log.info("timeSeries. from=" + from + ", to=" + to);

        // 获取Token
        String token = tokenManager.getToken(false);

        // 发起请求
        ResultBean resultBean = timeSeries.getTimeSeriesData(token, entityId, propertySetName, from, to);

        // 结果数据
        JSONArray jsonArray = resultBean.getJSONArray();
        if (jsonArray == null) {
            return new JSONArray();
        }

        log.info("timeSeries. jsonArray=" + jsonArray.toJSONString());
        return jsonArray;
    }

    @GetMapping("/assettypes")
    public JSONObject getAssetTypes(
        @RequestParam(value = "size", required = false) String size,
        @RequestParam(value = "page", required = false) String page,
        @RequestParam(value = "sort", required = false) String sort) throws Exception {
        log.info("getAssetTypes.");

        // 获取Token
        String token = tokenManager.getToken(false);

        // 发起请求
        ResultBean resultBean = assetManager.getAssetTypes(token, size, page, sort);

        // 结果数据
        JSONObject jsonObject = resultBean.getJSONObject();
        if (jsonObject == null) {
            return new JSONObject();
        }

        log.info("getAssetTypes. jsonObject=" + jsonObject.toJSONString());
        return jsonObject;
    }

    @GetMapping("/assets")
    public JSONObject getAssets(
        @RequestParam(value = "size", required = false) String size,
        @RequestParam(value = "page", required = false) String page,
        @RequestParam(value = "sort", required = false) String sort) throws Exception {
        log.info("getAssets.");

        // 获取Token
        String token = tokenManager.getToken(false);

        // 发起请求
        ResultBean resultBean = assetManager.getAssetTypes(token, size, page, sort);

        // 结果数据
        JSONObject jsonObject = resultBean.getJSONObject();
        if (jsonObject == null) {
            return new JSONObject();
        }

        log.info("getAssets. jsonObject=" + jsonObject.toJSONString());
        return jsonObject;
    }

    @GetMapping("/aggregates/{entity}/{propertyset}")
    public JSONArray aggregates(
        @PathVariable String entity,
        @PathVariable String propertyset,
        @RequestParam String from,
        @RequestParam String to,
        @RequestParam String intervalUnit,
        @RequestParam Integer intervalValue,
        @RequestParam(value = "select", required = false) String select) throws Exception {

        log.info("aggregates.");

        // 获取Token
        String token = tokenManager.getToken(false);

        // 发起请求
        ResultBean resultBean = timeSeriesAggregates
            .aggregates(token, entity, propertyset, from, to, intervalUnit, intervalValue, select);

        // 结果数据
        JSONArray jsonArray = resultBean.getJSONArray();
        if (jsonArray == null) {
            return new JSONArray();
        }

        log.info("aggregates. jsonArray=" + jsonArray.toJSONString());
        return jsonArray;
    }

    @GetMapping("/devicetypes")
    public JSONObject getDeviceTypes() throws Exception {
        log.info("getDeviceTypes.");

        // 获取Token
        String token = tokenManager.getToken(false);

        // 发起请求
        ResultBean resultBean = deviceManager.getDeviceTypes(token);

        // 结果数据
        JSONObject jsonObject = resultBean.getJSONObject();
        if (jsonObject == null) {
            return new JSONObject();
        }

        log.info("getDeviceTypes. jsonObject=" + jsonObject.toJSONString());
        return jsonObject;
    }

    @GetMapping("/devices")
    public JSONObject getDevices() throws Exception {
        log.info("getDevices.");

        // 获取Token
        String token = tokenManager.getToken(false);

        // 发起请求
        ResultBean resultBean = deviceManager.getDevices(token);

        // 结果数据
        JSONObject jsonObject = resultBean.getJSONObject();
        if (jsonObject == null) {
            return new JSONObject();
        }

        log.info("getDevices. jsonObject=" + jsonObject.toJSONString());
        return jsonObject;
    }
}