package examples;


import com.alibaba.fastjson.JSONArray;
import com.mongodb.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {
    public static void main(String[] args) {
        MongoClientOptions.Builder mcob = MongoClientOptions.builder();
        mcob.connectionsPerHost(1000);
        mcob.socketKeepAlive(true);
        mcob.threadsAllowedToBlockForConnectionMultiplier(100);
        MongoClientOptions mco = mcob.build();
        MongoClient mongoClient = new MongoClient(Arrays.asList(
                new ServerAddress("10.11.0.224", 27017),
                new ServerAddress("10.11.0.225", 27017),
                new ServerAddress("10.11.0.226", 27017)), mco);
        MongoDatabase db = mongoClient.getDatabase("expressbox");

        String strTime = "20180419";
        JSONArray arrayJson = new JSONArray();
        //设置查条件
        BasicDBObject cond = new BasicDBObject();
        Long oneDayStart =  Long.valueOf(strTime+"000000");
        Long oneDayEnd =  Long.valueOf(strTime+"235959");
        BasicDBObject[] array = {
                new BasicDBObject("InCabinetTime", new BasicDBObject("$gte",oneDayStart)),
                new BasicDBObject("InCabinetTime", new BasicDBObject("$lte",oneDayEnd)) };
        cond.put("$and", array);
        DBObject match = new BasicDBObject("$match", cond);
        //group设置分组
        DBObject gf = new BasicDBObject();
        gf.put("enterpriseId", "$enterpriseId");
        gf.put("facilityCode", "$facilityCode");
        gf.put("facilityName", "$facilityName");
        gf.put("cabinetNumber", "$cabinetNumber");
        gf.put("cabinetName", "$cabinetName");
        DBObject groupFields = new BasicDBObject();
        //设置聚合中通过count累加
        DBObject sum = new BasicDBObject("$sum",1);
//        DBObject count = new BasicDBObject("$count",sum);
        groupFields.put("_id",gf);
        groupFields.put("inccount", sum);

        DBObject group = new BasicDBObject("$group", groupFields);
        List list = new ArrayList<>();
        list.add(match);
        list.add(group);
        System.out.print(match.toString() + "," + group.toString());
        MongoCollection collection = db.getCollection("box");
        AggregateIterable iterableshow = collection.aggregate(list);
    }
}
