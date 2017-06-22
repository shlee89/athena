package athena.util;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;
import java.util.List;

/**
 * Created by seunghyeon on 4/3/16.
 */
public class ComputingClusterConnector implements Serializable {
    public String SINGLE = "SINGLE";
    String masterIP = "spark://";
    String applicationName = "Athena Anomaly Detection";
    String serializerStrategy = "org.apache.spark.serializer.KryoSerializer";
    String artifactName;
    List<String> registeredClasses;
    JavaSparkContext sc = null;

    public ComputingClusterConnector() {
    }

    public void setMasterIP(String masterIP) {
        this.masterIP = this.masterIP + masterIP;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setSerializerStrategy(String serializerStrategy) {
        this.serializerStrategy = serializerStrategy;
    }

    public void setArtifactName(String artifactName) {
        this.artifactName = artifactName;
    }

    public void setRegisteredClasses(List<String> registeredClasses) {
        this.registeredClasses = registeredClasses;
    }


    public JavaSparkContext generateConnector() {

        String registeredClass = "";

        for (int i = 0; i < registeredClasses.size(); i++) {
            if (i != 0) {
                registeredClass = registeredClass + ",";
            }
            registeredClass = registeredClass + registeredClasses.get(i);
        }
        SparkConf conf;
        if (masterIP.endsWith(SINGLE)) {
            conf = new SparkConf().setAppName(applicationName).setMaster("local")
                    .set("spark.driver.maxResultSize", "16g");
            sc = new JavaSparkContext(conf);
            return sc;
        } else {
            conf = new SparkConf().setAppName(applicationName).setMaster(masterIP)
                    .set("spark.serializer", serializerStrategy)
                    .set("spark.kryo.classesToRegister", registeredClass)
                    .set("spark.driver.maxResultSize", "2g");
        }

        sc = new JavaSparkContext(conf);
        sc.addJar("target/" + artifactName + ".jar");
        sc.addJar("target/" + artifactName + "-jar-with-dependencies.jar");

        return sc;
    }

    public void destroySC() {
        if (sc != null) {
            sc.close();
        }
    }

}
