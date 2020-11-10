package api;

import cons.Constants;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;

public class WriteData2 {
    public static void main(String[] args) {
        InfluxDB influxDB = InfluxDBFactory.connect(Constants.INFLUX_URL, Constants.INFLUX_USERNAME, Constants.INFLUX_PASSWORD);
        String dbName = "test1";

        Map<String, String> tags = new HashMap<>();
        tags.put("tag1", "1");
        tags.put("tag2", "2");
        tags.put("tag3", "3");

        Map<String, Object> fields = new HashMap<>();
        fields.put("idle", 10);
        fields.put("user", 98);
        fields.put("system", 2);
        fields.put("storm", 3);

        Builder builder = Point.measurement(dbName);
        builder.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        builder.tag(tags);
        builder.fields(fields);
        influxDB.write("cpu", null, builder.build());
        influxDB.close();
    }
}
