package co.edu.escuelaing.virtualization.dockerdemo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.ConnectionString;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;

public class Connection {
    MongoClient mongoClient;

    public Connection() {
        mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost:27017"));
    }

    public ArrayList<String[]> getNames(){
        MongoDatabase database = mongoClient.getDatabase("AREPMongo");
        MongoCollection<Document> collection =database.getCollection("logs");
        FindIterable fit = collection.find();
        ArrayList<Document> docs = new ArrayList<Document>();
        ArrayList<String[]> results = new ArrayList<>();
        fit.into(docs);
        for (Document doc : docs) {
            if (doc.get("cadena")!= null && doc.get("fecha")!=null){
                results.add(new String[]{doc.get("cadena").toString(), doc.get("fecha").toString()});
            }
        }
        return results;
    }

    public void insertData(String cadena){
        Date fecha = new Date();
        System.out.println("Inserta: "+ cadena);
        MongoDatabase database = mongoClient.getDatabase("AREPMongo");
        MongoCollection<Document> collection =database.getCollection("logs");
        Document document=new Document();
        document.put("cadena",cadena);
        document.put("fecha",fecha.toString());
        collection.insertOne(document);
        System.out.println("ala"+collection);
    }
}
