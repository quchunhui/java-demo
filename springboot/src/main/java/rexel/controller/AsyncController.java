package rexel.controller;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import rexel.service.AsyncService;

@RestController
@CrossOrigin
@Slf4j
public class AsyncController {
    private AsyncService asyncService;

    @Autowired
    public void setAsyncService(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @RequestMapping(value = "/async/none", method = RequestMethod.GET)
    public String returnNone() {
        long start = System.currentTimeMillis();
        asyncService.returnNone();
        return String.format("任务执行成功,耗时{%s}", System.currentTimeMillis() - start);
    }

    @ResponseBody
    @RequestMapping(value = "/async/json", method = RequestMethod.GET)
    public Map<String, Object> returnJson() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        List<Future<String>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Future<String> future = asyncService.returnJson(i);
            futures.add(future);
        }

        Map<String, Object> map = new HashMap<>();
        List<String> response = new ArrayList<>();
        for (Future future : futures) {
            String string = (String) future.get();
            response.add(string);
        }
        map.put("data", response);
        map.put("消耗时间", String.format("任务执行成功,耗时{%s}毫秒", System.currentTimeMillis() - start));
        return map;
    }
}
