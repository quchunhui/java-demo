package examples;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class TextMongoDBReplSet {
    public static void main(String[] args) {
        List<ServerAddress> addresses = new ArrayList<>();
        ServerAddress address1 = new ServerAddress("10.11.2.52", 27017);
        ServerAddress address2 = new ServerAddress("10.11.2.53", 27017);
        ServerAddress address3 = new ServerAddress("10.11.2.54", 27017);
        addresses.add(address1);
        addresses.add(address2);
        addresses.add(address3);

        MongoClient client = new MongoClient(addresses);
        MongoDatabase db = client.getDatabase("qch");
        MongoCollection<Document> coll = db.getCollection("say");

        Document doc = new Document();
        doc.append("morning", "Good Morning");
        coll.insertOne(doc);

        FindIterable<Document> fi = coll.find();
        for (Document rs : fi) {
            System.out.println(rs.toString());
        }
    }
}
