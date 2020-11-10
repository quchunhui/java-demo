package rexel.service;

import rexel.pojo.ResultBean;

public interface ITimeSeries extends IBaseService {
    ResultBean getTimeSeriesData(String token,
        String entityId, String propertySetName, String from, String to) throws Exception;
}
