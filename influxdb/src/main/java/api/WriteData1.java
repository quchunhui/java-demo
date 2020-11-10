package api;

import cons.Constants;
import java.util.concurrent.TimeUnit;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

public class WriteData1 {
    public static void main(String[] args) {
        InfluxDB influxDB = InfluxDBFactory.connect(Constants.INFLUX_URL, Constants.INFLUX_USERNAME, Constants.INFLUX_PASSWORD);
        String dbName = "test1";

        Point point1 = Point.measurement("cpu")
            .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .tag("tag1", "ab")
            .tag("tag2", "c")
            .tag("tag3", "ds")
            .addField("idle", 101L)
            .addField("user", 9L)
            .addField("system", 1L)
            .addField("system2", 1123L)
            .build();

        Point point2 = Point.measurement("disk")
            .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .addField("used", 80L)
            .addField("free", 1L)
            .build();

        influxDB.write(dbName, null, point1);
        influxDB.write(dbName, null, point2);
        influxDB.close();
    }
}
