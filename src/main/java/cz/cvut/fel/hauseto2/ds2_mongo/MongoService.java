package cz.cvut.fel.hauseto2.ds2_mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MongoService implements ApplicationRunner {
    @Autowired
    MongoTemplate mongoTemplate;

    public MongoService() {
    }

    public ResponseEntity<List<String>> find(String collection, String query) {
        BasicDBObject dbObject = BasicDBObject.parse(query);
        MongoCollection<Document> retDocuments = mongoTemplate.getCollection(collection);
        List<String> ret = new ArrayList<>();

        try {
            MongoCursor<Document> cursor = retDocuments.find(dbObject).iterator();
            while (cursor.hasNext()) {
                Document d = cursor.next();
                ret.add(d.toJson());
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonList(e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    public ResponseEntity<String> delete(String collection, String query) {
        if (!mongoTemplate.collectionExists(collection)) {
            return new ResponseEntity<>("There is no collection named '" + collection + "'.", HttpStatus.NOT_FOUND);
        }

        try {
            MongoCollection<Document> documents = mongoTemplate.getCollection(collection);
            BasicDBObject queryObject = BasicDBObject.parse(query);
            documents.deleteMany(queryObject);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Deletion was successful.", HttpStatus.OK);
    }

    public ResponseEntity<String> update(String collection, String query, String newData) {
        if (!mongoTemplate.collectionExists(collection)) {
            return new ResponseEntity<>("There is no collection named '" + collection + "'.", HttpStatus.NOT_FOUND);
        }

        try {
            BasicDBObject queryObject = BasicDBObject.parse(query);
            BasicDBObject newDataObject = BasicDBObject.parse(newData);

            MongoCollection<Document> documents = mongoTemplate.getCollection(collection);
            documents.updateMany(queryObject, newDataObject);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Update was successful.", HttpStatus.OK);
    }

    public ResponseEntity<String> emptyCollection(String collection) {
        if (!mongoTemplate.collectionExists(collection)) {
            return new ResponseEntity<>("There is no collection named '" + collection + "'.", HttpStatus.NOT_FOUND);
        }

        try {
            mongoTemplate.findAllAndRemove(new Query(), collection);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("The collection '" + collection + "' has been emptied.", HttpStatus.OK);
    }

    public ResponseEntity<String> dropCollection(String collection) {
        if (!mongoTemplate.collectionExists(collection)) {
            return new ResponseEntity<>("There is no collection named '" + collection + "'.", HttpStatus.NOT_FOUND);
        }

        try {
            mongoTemplate.dropCollection(collection);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("The collection '" + collection + "' has been dropped.", HttpStatus.OK);
    }

    public ResponseEntity<String> insert(String collection, String document) {
        String retMessage = "";

        if (!mongoTemplate.collectionExists(collection)) {
            retMessage += "A collection named '" + collection + "' has been created. ";
        }

        BasicDBObject dbObject = BasicDBObject.parse(document);

        try {
            mongoTemplate.insert(dbObject, collection);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(retMessage + "Document successfully added to the collection.", HttpStatus.OK);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //  System.out.println("HHHHHHHHHHHH");
    }
}
