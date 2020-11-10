package mapreduce;

import com.mongodb.BasicDBObject;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;
import com.mongodb.hadoop.util.MongoConfigUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.mapreduce.MultiTableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JobControlSample {
    private static String processDay = null;
    private static List<String> companyList = null;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        processDay = initProcDay(args[0]);   //yyyyMMdd
        companyList = initCompanyList(args[1]);

        createUserCardIdTable();
        createSocialCreditCodeTable();

        JobConf jobConf = new JobConf(JobControlSample.class);
        ControlledJob controlledJob1 = new ControlledJob(jobConf);
        controlledJob1.setJob(getDrawnJob());

        ControlledJob controlledJob2 = new ControlledJob(jobConf);
        controlledJob2.setJob(getUserCardJob());
        controlledJob2.addDependingJob(controlledJob1);

        ControlledJob controlledJob3 = new ControlledJob(jobConf);
        controlledJob3.setJob(getSocialCreditCodeJob());
        controlledJob3.addDependingJob(controlledJob1);

        JobControl jobControl = new JobControl("BusinessOriginJob");
        jobControl.addJob(controlledJob1);
        jobControl.addJob(controlledJob2);
        jobControl.addJob(controlledJob3);

        Thread thread = new Thread(jobControl);
        thread.start();

        while (true) {
            if (jobControl.allFinished()) {
                System.out.println(jobControl.getSuccessfulJobList());
                jobControl.stop();
                break;
            }
        }
    }

    private static Job getDrawnJob() throws IOException {
        Configuration config = new Configuration();
        config.set("dfs.socket.timeout", "180000");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set("hbase.zookeeper.quorum", "10.11.2.4,10.11.2.5,10.11.2.6");
        config.set("processDay", processDay);

        Job job = Job.getInstance(config);
        job.setJobName("DrawnJob");
        job.setJarByClass(JobControlSample.class);
        job.setNumReduceTasks(0);
        job.setOutputFormatClass(MultiTableOutputFormat.class);
        TableMapReduceUtil.initTableMapperJob(
                getScans1(), DrawnMapper.class, ImmutableBytesWritable.class, Put.class, job);

        return job;
    }

    private static Job getUserCardJob() throws IOException {
        Configuration config = new Configuration();
        config.set("dfs.socket.timeout", "180000");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set("hbase.zookeeper.quorum", "10.11.2.4,10.11.2.5,10.11.2.6");

        String uri = "mongodb://10.11.2.15:27017,10.11.2.16:27017,10.11.2.17:27017/postal.user_card_id";
        MongoConfigUtil.setOutputURI(config, uri);

        Job job = Job.getInstance(config);
        job.setJobName("UserCardIdJob");
        job.setJarByClass(JobControlSample.class);
        job.setReducerClass(UserCardIdReducer.class);
        job.setNumReduceTasks(1);
        job.setOutputFormatClass(MongoOutputFormat.class);
        TableMapReduceUtil.initTableMapperJob(
                getScans2(), UserCardIdMapper.class, Text.class, LongWritable.class, job);

        return job;
    }

    private static Job getSocialCreditCodeJob() throws IOException {
        Configuration config = new Configuration();
        config.set("dfs.socket.timeout", "180000");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set("hbase.zookeeper.quorum", "10.11.2.4,10.11.2.5,10.11.2.6");

        String uri = "mongodb://10.11.2.15:27017,10.11.2.16:27017,10.11.2.17:27017/postal.social_credit_code";
        MongoConfigUtil.setOutputURI(config, uri);

        Job job = Job.getInstance(config);
        job.setJobName("SocialCreditCodeJob");
        job.setJarByClass(JobControlSample.class);
        job.setReducerClass(SocialCreditCodeReducer.class);
        job.setNumReduceTasks(1);
        job.setOutputFormatClass(MongoOutputFormat.class);
        TableMapReduceUtil.initTableMapperJob(
                getScans3(), SocialCreditCodeMapper.class, Text.class, LongWritable.class, job);

        return job;
    }

    private static List<Scan> getScans1(){
        List<Scan> scans = new ArrayList<>();

        for (String enterprise_code : companyList) {
            Scan scan = new Scan().setAttribute(
                    Scan.SCAN_ATTRIBUTES_TABLE_NAME, getTableNameDrawn(enterprise_code).getBytes());
            scan.setCacheBlocks(false);
            scan.addColumn(Bytes.toBytes("if"), Bytes.toBytes("qi"));
            scan.addColumn(Bytes.toBytes("if"), Bytes.toBytes("sm"));
            scan.addColumn(Bytes.toBytes("if"), Bytes.toBytes("bd"));
            scan.addColumn(Bytes.toBytes("if"), Bytes.toBytes("ut"));
            scan.addColumn(Bytes.toBytes("if"), Bytes.toBytes("uci"));
            scan.addColumn(Bytes.toBytes("if"), Bytes.toBytes("uscc"));
            scan.addColumn(Bytes.toBytes("if"), Bytes.toBytes("sci"));
            scans.add(scan);
        }

        return scans;
    }

    private static List<Scan> getScans2(){
        List<Scan> scans = new ArrayList<>();

        for (String enterprise_code : companyList) {
            Scan scan = new Scan().setAttribute(
                    Scan.SCAN_ATTRIBUTES_TABLE_NAME, getTableNameUser(enterprise_code).getBytes());
            scans.add(scan);
        }

        return scans;
    }

    private static List<Scan> getScans3(){
        List<Scan> scans = new ArrayList<>();

        for (String enterprise_code : companyList) {
            Scan scan = new Scan().setAttribute(
                    Scan.SCAN_ATTRIBUTES_TABLE_NAME, getTableNameSocial(enterprise_code).getBytes());
            scans.add(scan);
        }

        return scans;
    }

    private static void createUserCardIdTable() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "10.11.2.4,10.11.2.5,10.11.2.6");
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();

        byte[][] splitKeys = {
                Bytes.toBytes("1"),
                Bytes.toBytes("2"),
                Bytes.toBytes("3"),
                Bytes.toBytes("4"),
                Bytes.toBytes("5"),
                Bytes.toBytes("6"),
                Bytes.toBytes("7"),
                Bytes.toBytes("8"),
                Bytes.toBytes("9"),
        };

        for (String ent_code : companyList) {
            TableName tableName = TableName.valueOf(getTableNameUser(ent_code));
            if (admin.tableExists(tableName)) {
                if (admin.isTableEnabled(tableName)) {
                    admin.disableTable(tableName);
                }
                admin.truncateTable(tableName, true);
                if (admin.isTableDisabled(tableName)) {
                    admin.enableTable(tableName);
                }
                System.out.println("[------]truncateTable:" + tableName);
                continue;
            }

            HTableDescriptor desc = new HTableDescriptor(tableName);
            HColumnDescriptor family = new HColumnDescriptor(Bytes.toBytes("if"));
            family.setTimeToLive(7 * 60 * 60 * 24);
            family.setCompressionType(Compression.Algorithm.SNAPPY);
            family.setBloomFilterType(BloomType.ROW);
            desc.addFamily(family);

            admin.createTable(desc, splitKeys);
            System.out.println("[------]createTable:" + tableName);
        }

        admin.close();
        connection.close();
    }

    private static void createSocialCreditCodeTable() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "10.11.2.4,10.11.2.5,10.11.2.6");
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();

        byte[][] splitKeys = {
                Bytes.toBytes("1"),
                Bytes.toBytes("2"),
                Bytes.toBytes("3"),
                Bytes.toBytes("4"),
                Bytes.toBytes("5"),
                Bytes.toBytes("6"),
                Bytes.toBytes("7"),
                Bytes.toBytes("8"),
                Bytes.toBytes("9"),
        };

        for (String ent_code : companyList) {
            TableName tableName = TableName.valueOf(getTableNameSocial(ent_code));
            if (admin.tableExists(tableName)) {
                if (admin.isTableEnabled(tableName)) {
                    admin.disableTable(tableName);
                }
                admin.truncateTable(tableName, true);
                if (admin.isTableDisabled(tableName)) {
                    admin.enableTable(tableName);
                }
                System.out.println("[------]truncateTable:" + tableName);
                continue;
            }

            HTableDescriptor desc = new HTableDescriptor(tableName);
            HColumnDescriptor family = new HColumnDescriptor(Bytes.toBytes("if"));
            family.setTimeToLive(7 * 60 * 60 * 24);
            family.setCompressionType(Compression.Algorithm.SNAPPY);
            family.setBloomFilterType(BloomType.ROW);
            desc.addFamily(family);

            admin.createTable(desc, splitKeys);
            System.out.println("[------]createTable:" + tableName);
        }

        admin.close();
        connection.close();
    }

    static class DrawnMapper extends TableMapper<ImmutableBytesWritable, Put> {
        private String processDay = null;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            processDay = context.getConfiguration().get("processDay");
        }

        @Override
        protected void map(ImmutableBytesWritable key,
                           Result value, Context context) throws IOException, InterruptedException {
            if (processDay == null || processDay.isEmpty()) {
                context.getCounter("[return]", "[" + processDay + "]").increment(1);
                return;
            }

            String biz_date = getStrByByte(value.getValue(Bytes.toBytes("if"), Bytes.toBytes("bd")));
            if (biz_date == null) {
                context.getCounter("[return]", "[biz_date == null]").increment(1);
                return;
            }

            if (!biz_date.equals(processDay)) {
                context.getCounter("[return]", "[biz_date != processDay]").increment(1);
                return;
            }
            context.getCounter("[target]", "[biz_date = processDay]").increment(1);

            String enterprise_code =
                    getStrByByte(value.getValue(Bytes.toBytes("if"), Bytes.toBytes("qi")));
            String user_card_id =
                    getStrByByte(value.getValue(Bytes.toBytes("if"), Bytes.toBytes("uci")));
            String social_credit_code =
                    getStrByByte(value.getValue(Bytes.toBytes("if"), Bytes.toBytes("uscc")));

            if (user_card_id != null && !user_card_id.isEmpty()) {
                context.getCounter("[user_card_id]", "[user_card_id OK.]").increment(1);

                Put put = new Put(user_card_id.getBytes());
                addColumn(put, "BusyDate", biz_date);
                addColumn(put, "EnterpriseCode", enterprise_code);
                addColumn(put, "UserCardID", user_card_id);

                String table_name = getTableNameUser(enterprise_code);
                ImmutableBytesWritable ibw = new ImmutableBytesWritable(table_name.getBytes());

                context.write(ibw, put);
            } else {
                context.getCounter("[user_card_id]", "[user_card_id NG.]").increment(1);
            }

            if (social_credit_code != null && !social_credit_code.isEmpty()) {
                context.getCounter("[social_credit_code]", "[social_credit_code OK.]").increment(1);

                Put put = new Put(social_credit_code.getBytes());
                addColumn(put, "BusyDate", biz_date);
                addColumn(put, "EnterpriseCode", enterprise_code);
                addColumn(put, "UnifiedSocialCreditCode", social_credit_code);

                String table_name = getTableNameSocial(enterprise_code);
                ImmutableBytesWritable ibw = new ImmutableBytesWritable(table_name.getBytes());

                context.write(ibw, put);
            } else {
                context.getCounter("[social_credit_code]", "[social_credit_code NG.]").increment(1);
            }
        }

        private void addColumn(Put put, String qualifier, String value) {
            put.addColumn(Bytes.toBytes("if"), Bytes.toBytes(qualifier), Bytes.toBytes(value));
        }
    }

    static class UserCardIdMapper extends TableMapper<Text, LongWritable> {
        @Override
        protected void map(ImmutableBytesWritable key,
                           Result value, Context context) throws IOException, InterruptedException {
            String busy_date = getStrByByte(value.getValue(Bytes.toBytes("if"), "BusyDate".getBytes()));
            String enterprise_code = getStrByByte(value.getValue(Bytes.toBytes("if"), "EnterpriseCode".getBytes()));

            String out_key = append(busy_date, enterprise_code);
            context.write(new Text(out_key), new LongWritable(1));
        }
    }

    static class UserCardIdReducer extends Reducer<Text, LongWritable, Text, BSONWritable> {
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context)
                throws IOException, InterruptedException {
            String[] es = split(key.toString());

            String biz_date = "";
            if (es.length > 0) {
                biz_date = es[0];
            }
            String enterprise_code = "";
            if (es.length > 1) {
                enterprise_code = es[1];
            }

            long count = 0;
            for(LongWritable value : values) {
                count += value.get();
            }

            String _id = biz_date.concat(":").concat(enterprise_code);

            BSONWritable bsonWritable = new BSONWritable();
            BasicDBObject doc = new BasicDBObject();
            doc.put("_id", _id);
            doc.put("biz_date", biz_date);
            doc.put("enterprise_code", enterprise_code);
            doc.put("count", count);
            bsonWritable.setDoc(doc);

            context.write(new Text(_id), bsonWritable);
        }
    }

    static class SocialCreditCodeMapper extends TableMapper<Text, LongWritable> {
        @Override
        protected void map(ImmutableBytesWritable key,
                           Result value, Context context) throws IOException, InterruptedException {
            String busy_date = getStrByByte(value.getValue(Bytes.toBytes("if"), "BusyDate".getBytes()));
            String enterprise_code = getStrByByte(value.getValue(Bytes.toBytes("if"), "EnterpriseCode".getBytes()));

            String out_key = append(busy_date, enterprise_code);
            context.write(new Text(out_key), new LongWritable(1));
        }
    }

    static class SocialCreditCodeReducer extends Reducer<Text, LongWritable, Text, BSONWritable> {
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context)
                throws IOException, InterruptedException {
            context.getCounter("[reduce key]", key.toString());
            String[] es = split(key.toString());

            String biz_date = "";
            if (es.length > 0) {
                biz_date = es[0];
            }
            String enterprise_code = "";
            if (es.length > 1) {
                enterprise_code = es[1];
            }

            long count = 0;
            for(LongWritable value : values) {
                count += value.get();
            }

            String _id = biz_date.concat(":").concat(enterprise_code);

            BSONWritable bsonWritable = new BSONWritable();
            BasicDBObject doc = new BasicDBObject();
            doc.put("_id", _id);
            doc.put("biz_date", biz_date);
            doc.put("enterprise_code", enterprise_code);
            doc.put("count", count);
            bsonWritable.setDoc(doc);

            context.write(new Text(_id), bsonWritable);
        }
    }

    private static String append(String... args) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i != 0) {
                result.append("-");
            }
            result.append(args[i]);
        }
        return result.toString();
    }

    private static String[] split(String str) {
        return str.split("-");
    }

    private static String getTableNameDrawn(String enterprise_code) {
        return "DBN_" + enterprise_code;
    }

    private static String getTableNameUser(String enterprise_code) {
        return "USER_CARD_ID_" + enterprise_code;
    }

    private static String getTableNameSocial(String enterprise_code) {
        return "SOCIAL_CREDIT_CODE_" + enterprise_code;
    }

    private static String initProcDay(String param_day) {
        try {
            if (param_day == null || param_day.isEmpty()) {
                return dateTodayStr(dateAdd(-7));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return param_day;
    }

    private static List<String> initCompanyList(String param_company) {
        if (param_company == null || param_company.isEmpty()) {
            List<String> scanNames = new ArrayList<>();
            scanNames.add("ANE");
            scanNames.add("APEX");
            return scanNames;
        }

        List<String> scanNames = new ArrayList<>();
        scanNames.add(param_company);
        return scanNames;
    }

    private static String getStrByByte(byte[] by) {
        String str = "";
        if (by != null && by.length > 0) {
            str = Bytes.toString(by);
        }
        return str;
    }

    private static String dateTodayStr(Date date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(date);
    }

    private static Date dateAdd(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}