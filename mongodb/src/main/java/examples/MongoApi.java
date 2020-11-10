package examples;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;

public class MongoApi {
    private static MongoCollection coll = null;
    private static List<JSONObject> batch = new ArrayList<>();

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
        coll = db.getCollection("qch_test");

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("_id", "FC:889195171639872996");
        jsonObject1.put("arriveTime", "0");
        jsonObject1.put("batchNumber", null);
        jsonObject1.put("cabinetBoxDoorNumber", null);
        jsonObject1.put("cabinetBoxType", null);
        jsonObject1.put("cabinetDoorNumber", null);
        jsonObject1.put("cabinetName", "FC5180251");
        jsonObject1.put("cabinetNumber", "FC5180251");
        jsonObject1.put("enterpriseId", "FC");
        jsonObject1.put("expressName", "圆通速递");
        jsonObject1.put("facilityAddress", "null");
        jsonObject1.put("facilityCode", "518M");
        jsonObject1.put("facilityName", "中翔钢材市场速运营业点");
        jsonObject1.put("recipientPhoneNumber", "18261332898");
        jsonObject1.put("trackNumber", "889195171639872996");
        jsonObject1.put("InCabinetTime", "20180423131231");
        jsonObject1.put("takeAwayTime", "0");
        batch.add(jsonObject1);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("_id", "FC:889195171639872996");
        jsonObject2.put("arriveTime", "0");
        jsonObject2.put("batchNumber", null);
        jsonObject2.put("cabinetBoxDoorNumber", null);
        jsonObject2.put("cabinetBoxType", null);
        jsonObject2.put("cabinetDoorNumber", null);
        jsonObject2.put("cabinetName", "FC5180251");
        jsonObject2.put("cabinetNumber", "FC5180251");
        jsonObject2.put("enterpriseId", "FC");
        jsonObject2.put("expressName", "圆通速递");
        jsonObject2.put("facilityAddress", "null");
        jsonObject2.put("facilityCode", "518M");
        jsonObject2.put("facilityName", "中翔钢材市场速运营业点");
        jsonObject2.put("recipientPhoneNumber", "18261332898");
        jsonObject2.put("trackNumber", "889195171639872996");
        jsonObject2.put("InCabinetTime", "0");
        jsonObject2.put("takeAwayTime", "20180423131444");
        batch.add(jsonObject2);

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("_id", "FC:889195171639872996");
        jsonObject3.put("arriveTime", "0");
        jsonObject3.put("batchNumber", null);
        jsonObject3.put("cabinetBoxDoorNumber", null);
        jsonObject3.put("cabinetBoxType", null);
        jsonObject3.put("cabinetDoorNumber", null);
        jsonObject3.put("cabinetName", "FC5180251");
        jsonObject3.put("cabinetNumber", "FC5180251");
        jsonObject3.put("enterpriseId", "FC");
        jsonObject3.put("expressName", "圆通速递");
        jsonObject3.put("facilityAddress", "null");
        jsonObject3.put("facilityCode", "518M");
        jsonObject3.put("facilityName", "中翔钢材市场速运营业点");
        jsonObject3.put("recipientPhoneNumber", "18261332898");
        jsonObject3.put("trackNumber", "889195171639872996");
        jsonObject3.put("InCabinetTime", 0);
        jsonObject3.put("takeAwayTime", 0);
        batch.add(jsonObject3);

        bulkWrite(batch);
    }

    private static void updateExpress(JSONObject expressBox) {
        long time = Long.parseLong(getDateStr());

        //插入时间
        expressBox.put("inserttime", time);

        //放入时间
        if (!checkDateValid(expressBox, "InCabinetTime")) {
            expressBox.remove("InCabinetTime");
            System.out.print("[-----]remove InCabinetTime. \n");
        }

        //取走时间
        if (!checkDateValid(expressBox, "takeAwayTime")) {
            expressBox.remove("takeAwayTime");
            System.out.print("[-----]remove takeAwayTime. \n");
        }
    }

    private static String getDateStr() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(calendar.getTime());
    }

    private static boolean checkDateValid (JSONObject json, String name) {
        if (json == null || !json.containsKey(name)) {
            return false;
        }

        String value = json.getString(name);
        if (value == null || value.isEmpty() || value.length() < 6) {
            return false;
        }

        return true;
    }

    private static void bulkWrite(List<JSONObject> list) {
        printLog(list);

        List<WriteModel<Document>> requests = new ArrayList<>();
        UpdateOptions uo = new UpdateOptions();
        uo.upsert(true);

        for (JSONObject jsonObject : list) {
            updateExpress(jsonObject);
            Document filter = new Document("_id", jsonObject.get("_id"));
            Document value = new Document("$set", new Document(jsonObject));
            requests.add(new UpdateOneModel<>(filter, value, uo));
        }

        coll.bulkWrite(requests);
    }

    private static void printLog(List<JSONObject> list) {
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < list.size(); i++) {
            jsonObject.put(String.valueOf(i), list.get(i).get("_id"));
        }
        System.out.print("[-----]" + jsonObject.toJSONString() + "\n");
    }
}
