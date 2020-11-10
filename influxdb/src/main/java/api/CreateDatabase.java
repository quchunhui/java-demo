package api;

import cons.Constants;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;

public class CreateDatabase {
    public static void main(String[] args) {
        InfluxDB influxDB = InfluxDBFactory.connect(Constants.INFLUX_URL, Constants.INFLUX_USERNAME, Constants.INFLUX_PASSWORD);
        String dbName = "test1";
        String rpName = "test1_rp";

        influxDB.query(new Query("CREATE DATABASE " + dbName));
        influxDB.setDatabase(dbName);
        // Flush every 2000 Points, at least every 100ms
        influxDB.enableBatch(BatchOptions.DEFAULTS.actions(2000).flushDuration(100));
//        influxDB.enableBatch(BatchOptions.DEFAULTS);

        influxDB.query(new Query("CREATE RETENTION POLICY " + rpName + " ON " + dbName + " DURATION 30h REPLICATION 2 DEFAULT"));

        influxDB.close();
    }
}
