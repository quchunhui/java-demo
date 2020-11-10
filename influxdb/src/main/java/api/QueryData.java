package api;

import cons.Constants;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

public class QueryData {
    public static void main(String[] args) {
        InfluxDB influxDB = InfluxDBFactory.connect(Constants.INFLUX_URL, Constants.INFLUX_USERNAME, Constants.INFLUX_PASSWORD);
        String dbName = "rexel_airfilm_dev";

        QueryResult queryResult = influxDB.query(new Query("SELECT * FROM device_data_up limit 5", dbName));
        queryResult.getResults().forEach(result -> result.getSeries().forEach(series -> {
            System.out.println(series.toString());
        }));

        influxDB.close();
    }
}
