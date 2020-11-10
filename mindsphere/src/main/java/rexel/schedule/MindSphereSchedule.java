package rexel.schedule;

import com.alibaba.fastjson.JSONArray;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rexel.constants.Constants;
import rexel.pojo.ResultBean;
import rexel.service.ITimeSeries;
import rexel.service.ITokenManager;

@Component
@Slf4j
public class MindSphereSchedule {
    private ITokenManager tokenManager;
    private ITimeSeries timeSeriesService;

    @Autowired
    public void setTokenManager(ITokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Autowired
    public void setTimeSeriesService(ITimeSeries timeSeriesService) {
        this.timeSeriesService = timeSeriesService;
    }

    @Scheduled(fixedRateString = "300000")
    public void refreshToken() throws URISyntaxException {
        log.info("refreshToken.");
        tokenManager.getToken(true);
    }

    @Scheduled(fixedRateString = "60000")
    public void getTimeSeriesData() throws Exception {
        log.info("getTimeSeriesData.");

        // 获取Token
        String token = tokenManager.getToken(false);

        // 发起请求
        ResultBean resultBean = timeSeriesService.getTimeSeriesData(
            token, Constants.ENTITY_ID, Constants.PROPERTY_SET_NAME, null, null);

        // 请求数据
        JSONArray jsonArray = resultBean.getJSONArray();
        if (jsonArray == null) {
            return;
        }

        log.info("jsonArray=" + jsonArray.toJSONString());
    }
}
