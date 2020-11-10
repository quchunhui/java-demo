package api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import cons.Constants;
import java.util.List;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BoundParameterQuery.QueryBuilder;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import pojo.Cpu;

public class QueryDataByParam {
    public static void main(String[] args) {
        InfluxDB influxDB = InfluxDBFactory.connect(Constants.INFLUX_URL, Constants.INFLUX_USERNAME, Constants.INFLUX_PASSWORD);
        String dbName = "test1";

        Query query = QueryBuilder.newQuery("SELECT * FROM cpu WHERE idle > $idle AND system > $system")
            .forDatabase(dbName)
            .bind("idle", 100)
            .bind("system", 5)
            .create();
        QueryResult results = influxDB.query(query);

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper(); // thread-safe - can be reused
        List<Cpu> cpuList = resultMapper.toPOJO(results, Cpu.class);
        for (Cpu cpu : cpuList) {
            System.out.println(JSON.toJSON(cpu).toString());
        }

        influxDB.close();
    }
}
