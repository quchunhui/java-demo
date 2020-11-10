package tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;

import java.io.*;
import java.util.Map;

public class file_to_table {
    private static final String COLUMN_FAMILY = "if";
    private static Connection connection = null;
    private static BufferedMutator table = null;

    public static void main(String[] args) {
        System.out.print("[-----]Start MD_SF.\n");
        execute("/home/hadmin/post_data/post_sf_md_20150301_1000000.json", "MD_SF");
        System.out.print("[-----]End MD_SF.\n");

        System.out.print("[-----]Start ZT_SF.\n");
        execute("/home/hadmin/post_data/post_sf_zt_20150301_1000000.json", "ZT_SF");
        System.out.print("[-----]End ZT_SF.\n");
    }

    private static void execute(String file_name, String table_name) {
        FileInputStream fis = null;
        InputStreamReader isw = null;
        try {
            try {
                fis = new FileInputStream(file_name);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }

            try {
                isw = new InputStreamReader(fis);
                toHBase(new BufferedReader(isw), table_name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            if (table != null) {
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (isw != null) {
                try {
                    isw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void toHBase(BufferedReader br, String table_name) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.1.80,192.168.1.81,192.168.1.82");
        conf.set("hbase.client.write.buffer", "1048576");//1M
        connection = ConnectionFactory.createConnection(conf);
        table = connection.getBufferedMutator(TableName.valueOf(table_name));

        String line_str;
        while ((line_str = br.readLine()) != null) {
            JSONObject jsonObject = convertToJson(line_str);
            Put put = new Put(jsonObject.getString("MAIL_NO").getBytes());
            for (Map.Entry entry : jsonObject.entrySet()) {
                put.addColumn(COLUMN_FAMILY.getBytes(),
                        entry.getKey().toString().getBytes(), entry.getValue().toString().getBytes());
            }
            table.mutate(put);
        }
    }

    private static JSONObject convertToJson(String str) {
        return JSONObject.parseObject(JSON.toJSON(str).toString());
    }
}