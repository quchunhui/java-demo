package examples;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.DeleteOneModel;
import com.mongodb.client.model.WriteModel;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cabinet {
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

        MongoCollection<Document> cabinet = db.getCollection("cabinet");

        int input_counter = 1;
        int delete_counter = 1;
        System.out.println("[------]delete start.");
        List<WriteModel<Document>> requests = new ArrayList<>();
        for (Document doc : cabinet.find()) {
            input_counter++;
            if (input_counter % 100 == 0) {
                System.out.println("input_counter=" + input_counter);
                System.out.println("delete_counter=" + delete_counter);
            }

            String enterpriseId = doc.getString("enterpriseId");
            String cabinetcode = doc.getString("cabinetcode");
            String belongFacilityCode = doc.getString("belongFacilityCode");

            String _id = doc.getString("_id");
            String key = enterpriseId + ":" + cabinetcode + ":" + belongFacilityCode;
            if (_id.equals(key)) {
                continue;
            }

            JSONObject filter = new JSONObject();
            filter.put("_id", _id);
            Document doc1 = Document.parse(filter.toString());

            requests.add(new DeleteOneModel<>(doc1));
            delete_counter++;
        }

        System.out.println("[------]bulkWrite start.");
        cabinet.bulkWrite(requests);
        System.out.println("[------]bulkWrite end.");

        mongoClient.close();
        System.out.println("[------]delete end.");

        System.out.println("input_counter=" + input_counter);
        System.out.println("delete_counter=" + delete_counter);
    }
}