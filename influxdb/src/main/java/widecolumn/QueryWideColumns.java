package widecolumn;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import pojo.*;

public class QueryWideColumns {
    public static void main(String[] args) {
        InfluxDB influxDB = InfluxDBFactory.connect(
            "http://47.116.1.70:8086", "root", "root");
        if (influxDB == null) {
            return;
        }

//        QueryResult queryResult = influxDB.query(new Query(
//            AirFilmInfo.querySql(AirFilmInfo.class, ""), "rexel_airfilm"));
        QueryResult queryResult = influxDB.query(new Query(
            "select * from device_property order by time desc", "rexel_airfilm"), TimeUnit.MILLISECONDS);
        if (queryResult == null || queryResult.getResults().size() <= 0) {
            return;
        }

        MyInfluxDBResultMapper resultMapper = new MyInfluxDBResultMapper();
        List<AirFilmInfo> airFilmInfoList = resultMapper.toPOJO(queryResult, AirFilmInfo.class);
        if (airFilmInfoList == null || airFilmInfoList.size() <= 0) {
            return;
        }

        AirFilmInfo airFilmInfo = airFilmInfoList.get(0);
        System.out.println(airFilmInfo.toString());
        System.exit(1);
    }
}
