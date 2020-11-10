package api;

import cons.Constants;
import java.util.List;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import pojo.Cpu;

public class QueryDataToPojo {
    public static void main(String[] args) {
        InfluxDB influxDB = InfluxDBFactory.connect(Constants.INFLUX_URL, Constants.INFLUX_USERNAME, Constants.INFLUX_PASSWORD);
        String dbName = "test1";

        QueryResult queryResult = influxDB.query(new Query("SELECT * FROM cpu", dbName));

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper(); // thread-safe - can be reused
        List<Cpu> cpuList = resultMapper.toPOJO(queryResult, Cpu.class);
        for (Cpu cpu : cpuList) {
            System.out.println(cpu);
        }

        influxDB.close();
    }
}
