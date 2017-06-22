package athena.util;

import org.onosproject.athena.database.AthenaFeatures;
import org.onosproject.athena.database.AthenaFeatureRequester;
import org.onosproject.athena.database.impl.FeatureDatabaseMgmtManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * The DatabaseConnector initiates a connection between Athena and a distributed databases (or single database instance), and it handles a query request from an user.
 * Created by seunghyeon on 4/3/16.
 */
public class DatabaseConnector implements Serializable {

    String DATABASE_IP = null;
    FeatureDatabaseMgmtManager featureDatabaseMgmtManager = new FeatureDatabaseMgmtManager();
    List<String> DATABASE_IPs = null;
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Construct with single database instance.
     *
     * @param ip IP of a DB instance.
     */
    public DatabaseConnector(String ip) {
        DATABASE_IP = ip;
        featureDatabaseMgmtManager.connectToDatabase(ip);
    }

    /**
     * Construct with distributed database instances.
     *
     * @param DATABASE_IPs A set of IPs of DB instances.
     */
    public DatabaseConnector(List<String> DATABASE_IPs) {
        this.DATABASE_IPs = DATABASE_IPs;
        featureDatabaseMgmtManager.connectToDatabaseCluster(DATABASE_IPs);
    }

    /**
     * Construct with no parameter
     */
    public DatabaseConnector() {
        if (System.getenv("MD1").isEmpty()) {
            System.err.println("Please set environment variables for DB cluster");
            System.exit(1);
        }
        this.DATABASE_IP = System.getenv("MD1");
    }

    /**
     * Set IP address with single database instance.
     *
     * @param ip IP of a DB instance.
     * @return success/fail
     */
    public boolean DatabaseConnector(String ip) {
        return featureDatabaseMgmtManager.connectToDatabase(ip);
    }

    /**
     * Set IP addresses with a distributed database instances.
     *
     * @param DATABASE_IPs A set of IPs of DB instances.
     * @return success/fail
     */
    public boolean DatabaseClusterConnector(final List<String> DATABASE_IPs) {
        return featureDatabaseMgmtManager.connectToDatabaseCluster(DATABASE_IPs);
    }

    /**
     * Request query to database.
     *
     * @param athenaFeatureRequester AthenaFetureRequester with REQUEST_FEATURES type.
     * @return A set of Athena features
     */
    public AthenaFeatures requestToDatabase(AthenaFeatureRequester athenaFeatureRequester) {
        if (athenaFeatureRequester == null) {
            log.warn("[requestDataToDB] datarequest MUST not null");
            return null;
        }
        return featureDatabaseMgmtManager.requestDataToMongoDB(athenaFeatureRequester);
    }

    /**
     * Display Athena features to CLI Interface.
     *
     * @param athenaFeatureRequester Requested athenaFeatureRequester to highlight certain features.
     * @param athenaFeatures         A set of Athena features
     */
    public void printAllFeatures(AthenaFeatureRequester athenaFeatureRequester, AthenaFeatures athenaFeatures) {
        if (athenaFeatureRequester == null || athenaFeatures == null) {
            log.warn("[displayFeatures] datarequest/athenaFeatures MUST not null");
            return;
        }
        featureDatabaseMgmtManager.printAllDBFeatures(athenaFeatureRequester, athenaFeatures);
    }


    public String getDATABASE_IP() {
        return DATABASE_IP;
    }

    public void setDATABASE_IP(String DATABASE_IP) {
        this.DATABASE_IP = DATABASE_IP;
    }

    public List<String> getDATABASE_IPs() {
        return DATABASE_IPs;
    }

    public void setDATABASE_IPs(List<String> DATABASE_IPs) {
        this.DATABASE_IPs = DATABASE_IPs;
    }
}
