package athena.util;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.onosproject.athena.database.AthenaFeatureField;
import org.onosproject.athena.database.AthenaIndexField;

/**
 * Created by seunghyeon on 4/4/16.
 */
public class ReplicateDatabaseData {
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    HashMap<String, MongoCollection> dbCollectionList;

    AthenaIndexField athenaIndexField = new AthenaIndexField();
    AthenaFeatureField athenaFeatureField = new AthenaFeatureField();
    DatabaseConnector databaseConnector = new DatabaseConnector();

    public void insertAll() {
//        databaseConnector.DatabaseConnector("127.0.0.1");
        databaseConnector.DatabaseConnector("192.168.0.31");
        databaseConnector.DatabaseClusterConnector(Arrays.asList("192.168.0.31", "192.168.0.32",
                "192.168.0.33"));
        MongoCollection inputCollection = mongoDatabase.getCollection(athenaFeatureField.FLOW_STATS);
        MongoCollection outputCollection = mongoDatabase.getCollection("terabyte");
        Iterable<Document> input = inputCollection.find();

        for (int j = 0 ; j < 600 ; j++){
            List<Document> innerRow = new ArrayList();
            int i = 0;
            for (Document entry : input) {
                i++;

                entry.remove("_id");
                innerRow.add(entry);
                if( (i % 50000) == 0){
                    outputCollection.insertMany(innerRow);
                    innerRow = new ArrayList();
                    System.out.println("Inserted");
                }
            }
            System.out.println("Phase" +j + "done");
        }
    }


    public HashMap<String, MongoCollection> getCollectionList(MongoDatabase mongoDatabase) {
        HashMap<String, MongoCollection> dbCollectionList = new HashMap<String, MongoCollection>();
        String[] tableNameList = {athenaFeatureField.ERROR_MSG, athenaFeatureField.FLOW_REMOVED, athenaFeatureField.PACKET_IN,
                athenaFeatureField.PORT_STATUS, athenaFeatureField.FLOW_STATS, athenaFeatureField.QUEUE_STATS,
                athenaFeatureField.AGGREGATE_STATS, athenaFeatureField.TABLE_STATS, athenaFeatureField.PORT_STATS, "terabyte"};
        String teraBytes = "terabyte";

        for (String tableName : tableNameList) {
            MongoCollection dbCollection = mongoDatabase.getCollection(tableName);
            if (dbCollection == null) {
                mongoDatabase.createCollection(tableName);
                dbCollection = mongoDatabase.getCollection(tableName);
            }
            dbCollectionList.put(tableName, dbCollection);
        }

        return dbCollectionList;
    }

}
