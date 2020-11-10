package rexel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import rexel.service.CollectSwitchService;
import rexel.service.ProcessDataService;

@RestController
@CrossOrigin
@Slf4j
public class MiddleWareController {
    private ProcessDataService processDataService;
    private CollectSwitchService collectSwitchService;

    @Autowired
    public void setProcessDataService(ProcessDataService processDataService) {
        this.processDataService = processDataService;
    }

    @Autowired
    public void setCollectSwitchService(CollectSwitchService collectSwitchService) {
        this.collectSwitchService = collectSwitchService;
    }

    @ResponseBody
    @RequestMapping(value = "/hzzg/process/query", method = RequestMethod.POST)
    public JSONObject processQuery(
        @RequestBody String body, @RequestParam("page")int page, @RequestParam("size")int size) {
        log.info(String.format(
            "[REXEL]method=%s, body=%s, page=%d, size=%d", "query", body, page, size));
        if (page >= 1) {
            page -= 1;
        }
        JSONObject jsonObject = JSON.parseObject(body);
        return processDataService.findProcess(jsonObject, page, size);
    }

    @ResponseBody
    @RequestMapping(value = "/hzzg/process/commit", method = RequestMethod.POST)
    public JSONObject processStatusCommit(@RequestBody String body) throws Exception {
        log.info(String.format("[REXEL]method=%s, body=%s", "commit", body));
        JSONArray jsonArray = JSONArray.parseArray(body);
        return processDataService.commitProcess(jsonArray);
    }

    @RequestMapping(value = "/hzzg/process/pause", method = RequestMethod.POST)
    public JSONObject pauseCollect() {
        log.info(String.format("[REXEL]method=%s", "pause"));
        return collectSwitchService.update(0);
    }

    @RequestMapping(value = "/hzzg/process/open", method = RequestMethod.POST)
    public JSONObject openCollect() {
        log.info(String.format("[REXEL]method=%s", "open"));
        return collectSwitchService.update(1);
    }
}
