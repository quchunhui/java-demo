package rexel.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rexel.service.ProcessDataService;

@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class MiddleWareSchedule {
    private ProcessDataService processDataService;

    @Autowired
    public void setProcessDataService(ProcessDataService processDataService) {
        this.processDataService = processDataService;
    }

    /**
     * 1、每天定时执行
     * 2、删除1个月以前的数据
     */
//    @Scheduled(cron = "${schedule.jobs.cron}")
    public void deleteProcess() {
        log.info(String.format("[REXEL]method=%s", "schedule delete"));
        processDataService.deleteProcess();
    }

    /**
     * 1、定时执行
     * 2、需要从云平台获取访问令牌
     * 3、根据访问令牌获取待加工数据
     * 4、将待加工数据持久化至数据库中
     * @throws Exception e
     */
//    @Scheduled(fixedRateString = "${schedule.jobs.fixedRate}")
    public void pullProcess() throws Exception {
        log.info(String.format("[REXEL]method=%s", "schedule query"));
        processDataService.pullProcess();
    }

    @Scheduled(cron = "0/5 0 0 * * ?")
    public void test() {
        System.out.println("-----------test------------");
    }
}