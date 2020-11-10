package api;

import cons.Constants;
import java.util.concurrent.TimeUnit;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

public class WriteDataBatch {
    public static void main(String[] args) {
        InfluxDB influxDB = InfluxDBFactory.connect(Constants.INFLUX_URL, Constants.INFLUX_USERNAME, Constants.INFLUX_PASSWORD);
        String dbName = "test1";

        BatchPoints batchPoints = BatchPoints
            .database(dbName)
            .tag("async", "true")
            .retentionPolicy(null)
            .consistency(ConsistencyLevel.ALL)
            .build();

        Point point1 = Point.measurement("cpu")
            .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .addField("idle", 90L)
            .addField("user", 9L)
            .addField("system", 1L)
            .build();

        Point point2 = Point.measurement("disk")
            .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .addField("used", 80L)
            .addField("free", 1L)
            .build();

        batchPoints.point(point1);
        batchPoints.point(point2);
        influxDB.write(batchPoints);

        influxDB.close();
    }
}
