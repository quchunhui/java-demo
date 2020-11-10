package rexel.service;

import rexel.pojo.ResultBean;

public interface ITimeSeriesAggregates extends IBaseService {
    ResultBean aggregates(String token,
        String entityId, String propertySetName,
        String from, String to,
        String intervalUnit, Integer intervalValue,
        String select) throws Exception;
}
