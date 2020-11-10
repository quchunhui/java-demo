package examples;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class deleteMany {
    private static final List<ServerAddress> MONGO_ADDR = Arrays.asList(
            new ServerAddress("10.11.2.15", 27017),
            new ServerAddress("10.11.2.16", 27017),
            new ServerAddress("10.11.2.17", 27017));

    public static void main(String[] args) {
        MongoClientOptions.Builder mcob = MongoClientOptions.builder();
        mcob.connectionsPerHost(1000);
        mcob.socketKeepAlive(true);
        mcob.writeConcern(WriteConcern.MAJORITY);
        MongoClientOptions mco = mcob.build();
        MongoClient client = new MongoClient(MONGO_ADDR, mco);
        MongoDatabase database = client.getDatabase("postal");

        MongoCollection<Document> collection = database.getCollection("social_credit_code");
        Document doc = new Document();
        doc.put("busy_date", "20180721");
        collection.deleteMany(doc);

        client.close();
    }
}
