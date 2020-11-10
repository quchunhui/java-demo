package api;

import cons.Constants;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;

public class DropDatabase {
    public static void main(String[] args) {
        InfluxDB influxDB = InfluxDBFactory.connect(Constants.INFLUX_URL, Constants.INFLUX_USERNAME, Constants.INFLUX_PASSWORD);

        String dbName = "test1";
        String rpName = "test1_rp";

        influxDB.query(new Query("DROP RETENTION POLICY " + rpName + " ON " + dbName));
        influxDB.query(new Query("DROP DATABASE " + dbName));

        influxDB.close();
    }
}
