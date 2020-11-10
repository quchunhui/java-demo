package api;

import cons.Constants;
import java.time.Instant;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import pojo.Cpu;

public class WriteDataUsePojo {
    public static void main(String[] args) {
        InfluxDB influxDB = InfluxDBFactory.connect(Constants.INFLUX_URL, Constants.INFLUX_USERNAME, Constants.INFLUX_PASSWORD);

        String dbName = "test1";
        String rpName = null;//可以设置为null

        Cpu cpu = new Cpu();
        cpu.setTime(Instant.now());
        cpu.setIdle((long)123);
        cpu.setUser((long)2341);
        cpu.setSystem((long)98732);

        Point point = Point.measurementByPOJO(cpu.getClass()).addFieldsFromPOJO(cpu).build();
        influxDB.write(dbName, rpName, point);
        influxDB.close();
    }
}
