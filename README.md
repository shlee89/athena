Athena: A Framework for Scalable Anomaly Detection in Software-Defined Networks
=========================================

## What is Athena?
Network-based anomaly detection has been regarded as a critical mission to find anomalous behavior on networks. Many researchers have focused on how to detect network threats intelligently and efficiently. In recent years, Software-defined networking has emerged as a new opportunity to implement network anomaly functions, since has lots of advantages such as a centralized network management, programmable network environment, and so forth. With its advantages, security experts have focused on implementing anomaly detection functions which work on SDN networks. Despite its popularity, designing and implementing still has unsolved challenges, such as a lack of network features, a scalability issue, and a hardness of deployment.

This project represents one of many ongoing research efforts that seek to develop new SDN-based network anomaly detection services. However, here our focus in on a development framework that scales to large networks that employ multiple controller instances across a distributed control plane. We introduce the Athena [1], which exports a well-structured development interface to overcome existing challenges. It allows network operators implement desired anomaly detection applications with minimum programming efforts and complete transparency of an underline infrastructure. The prototype implementation of the Athena framework hosted on the Open Network Operating System (ONOS) SDN controller, upon which we have isolated 125 network features for use by Athena applications. Thus, we provide well-structured APIs including core functions to implement anomaly detection applications.

We have been evolving the Athena framework to support more advanced features such as a high-level security intent enforcement mechanism, an automatic threat reaction mechanism, a network-wide disaster simulation and detection.

[1] Seunghyeon Lee, Jinwoo Kim, Seungwon Shin, Phillip Porras, Vinod Yegneswaran, “Athena: A Framework for Scalable Anomaly Detection in  Software-Defined Networks”, The 47th IEEE/IFIP International Conference on Dependable Systems and Networks, (to appear), Denver, CO, USA, June, 2017

## Prerequisites
+ ONOS v1.6
+ MongoDB v3.2
+ Spark v1.6
+ JfreeChart v1.0.13
+ OpenFlow v1.3, v1.0

## Installation

Athena installation procedure is based on ONOS, which is built upon maven build system, thus the mvn command is used to build both Athena and ONOS systems.

+ Get the source code of Athena.

```
$ git clone [address of github]
```

+ Install ONOS dependencies

```
$ cd <Athena>/tools/dev/bin/
$ ./onos-setup-ubuntu-devenv
```

+ Build Athena and ONOS using maven build

```
$ cd <Athena>
$ source <Athena>/athena-tool/dev/bash_profile
$ mvn clean install
```

## Configuring your own experiments

Basically, we support both *Single Mode* and *Distributed Mode* for collecting Athena features and performing a network anomaly detection task.

### Single Mode 
In the *Single Mode*, we assume that all instances (a SDN controller, a DB instance, and a computing instance) are running in a local machine.

+ Setup an environment for Single Mode 

```
$ cd <Athena>/athena-tool/dev/
$ ./athena-setup-single
```

+ Configure environment variables by editing the Athena configuration file

```

$ vi <Athena>/athena-tool/config/athena-config-env-single
```
```
# An example of the Athena configuration file on a single environment
...

# the mode for an Athena environment e.g., SINGLE or DISTRIBUTED
export MODE="SINGLE"

# the addresses of ONOS instances
export OC1="127.0.0.1"

# the addresses of MongoDB cluster
export MD1="127.0.0.1"

# the addresses of Spark cluster
export SP1="127.0.0.1"

# the path of Athena
export ATHENA_ROOT=~/athena-1.6
...
```

```
$ source ./athena-config-env-single
```

+ Run ONOS in a local mode

```
$ onos-karaf clean
```

#### Distributed Mode
In the *Distributed Mode*, Athena is hosted on three ONOS controllers, three DB instances, and three computing instances.

+ Install nine lxc containers

```
$ cd <Athena>/athena-tool/dev/
$ ./athena-setup-lxc

# LXC will creates nine containers and automatically assigns IP addresses.
```

+ Configure environment variables for the distributed environments

```

$ vi <Athena>/athena-tool/config/athena-config-env-distributed
```
```
# An example of the Athena configuraiton file
...
# the mode for an Athena environment e.g. SINGLE or DISTRIBUTED
export MODE="DISTRIBUTED"

# the addresses of ONOS instances
export OC1="10.0.3.22"
export OC2="10.0.3.158"
export OC3="10.0.3.108"

# the addressees of the MongoDB containers
export MD1="10.0.3.112"
export MD2="10.0.3.176"
export MD3="10.0.3.115"

# the addressees of the Spark containers
export SP1="10.0.3.226"
export SP2="10.0.3.63"
export SP3="10.0.3.60"

# the path of Athena
export ATHENA_ROOT=~/athena-1.6
...
```

```
$ source ./athena-config-env-distributed
```

+ Set passwordless container access (From https://wiki.onosproject.org/display/ONOS/ONOS+from+Scratch)

```
# password : ubuntu

$ onos-push-keys $OC1
ubuntu@10.0.3.22's password: ubuntu
$ onos-push-keys $OC2
ubuntu@10.0.3.158's password: ubuntu
$ onos-push-keys $OC3
ubuntu@10.0.3.108's password: ubuntu
$ onos-push-keys $MD1
ubuntu@10.0.3.112's password: ubuntu
$ onos-push-keys $MD2
ubuntu@10.0.3.176's password: ubuntu
$ onos-push-keys $MD3
ubuntu@10.0.3.115's password: ubuntu
$ onos-push-keys $SP1
ubuntu@10.0.3.226's password: ubuntu
$ onos-push-keys $SP2
ubuntu@10.0.3.63's password: ubuntu
$ onos-push-keys $SP3
ubuntu@10.0.3.60's password: ubuntu
```

+ Setup environments for Distributed Mode

```
$ cd <Athena>/athena-tool/dev/
$ ./athena-setup-distributed
```


+ Run ONOS in a distributed mode

```
$ op && onos-group install
```


## Install (activate) Athena framework

+ Execute DB and computing instances

```
$ cd <Athena>/athena-tool/bin/
$ athena-run-db-cluster
$ athena-run-computing-cluster
```

+ Activate Athena framework core system as ONOS subsystem.

```
ONOS> app activate org.onosproject.framework
```

+ Activate Athena proxy as ONOS network application.

```
ONOS> app activate org.onosproject.athenaproxy
```

<!-- ## Athena Application
One of the strengths of Athena framework is enabling fast and easy NAD application development by supporting off-the-shelf APIs. 

We provide a Athena programming guide and some sample application codes in *\<Athena>/athena-tester* for your reference.

You can refer the demo video that demonstrates NAD usecase with Athena.

[>> Click to see Athena DEMO video](http://seunghyeon.d.pr/TjkXn) -->

### Example Applications

<!-- Athena supports two example usecases which are composed of *1. Real-time Analysis* and *2. Big Data Analysis*.
**(See Athena Programming Guide)**

Use different scripts according to the usecases -->

#### 1. Real-time Detection

```
$ cd <Athena>/athena-tester/bin
$ ./athena-run-realtime
```
+ Before executing the Athena real-time script, you should edit it to set up the configuration script. See below. (Thanks to **James A** for the bug report)

```
...
source $ATHENA_ROOT/athena-tool/config/athena-config-env #athena-config-env-single or -distributed for the single and distribute modes respectively.
...
```

#### 2. Big Data Analysis

```
$ cd <Athena>/athena-tester/bin
$ ./athena-run-ml-task
```


### Programming Guide

#### 1. Real-time Analysis

```

```

#### 2. Big Data Analysis


#### Set the infrastructure information

```
// Initialize DB and Computing cluster manager

DatabaseConnector databaseConnector = new DatabaseConnector();

MachineLearningManagerImpl machineLearningManager = new MachineLearningManagerImpl();
machineLearningManager.setMainClass("athena.user.application.Main"); // the name of main class
machineLearningManager.setArtifactId("athena-tester-1.6.0"); // the artifact id of your Athena application
machineLearningManager.setDatabaseConnector(databaseConnector);
```

#### Specify feature constraints and data pre-processing

```
// Get Athena features which satisfy the condition "MATCH_IPV4_SRC==10.0.0.1 AND MATCH_IP_PROTO==6"

FeatureConstraint featureConstraint = new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
        new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));
FeatureConstraint featureConstraint2 = new FeatureConstraint(FeatureConstraintType.INDEX,
        FeatureConstraintOperatorType.COMPARABLE,
        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
        new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC),
        new TargetAthenaValue(AthenaValueGenerator.parseIPv4ToAthenaValue("10.0.0.1")));
FeatureConstraint featureConstraint3 = new FeatureConstraint(FeatureConstraintType.INDEX,
        FeatureConstraintOperatorType.COMPARABLE,
        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ),
        new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO),
        new TargetAthenaValue(AthenaValueGenerator.generateAthenaValue("6")));

featureConstraint.setLocation("model"); // the name of MongoDB collection which contains the train data set
featureConstraint.appenValue(new TargetAthenaValue(fc2));
featureConstraint.appenValue(new TargetAthenaValue(fc3));

// Configure Data Pre-processing; set normalization for ML feature scaling and add weight to FLOW_STATS_PAIR_FLOW_RATIO

AthenaMLFeatureConfiguration athenaMLFeatureConfiguration = new AthenaMLFeatureConfiguration();
athenaMLFeatureConfiguration.setNormalization(true);
athenaMLFeatureConfiguration.addWeight(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO), 1000);
```

#### Select Athena features and set parameters for ML algorithm

```
// Specify which Athena features are used for learning

athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_BYTE_COUNT));
athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PACKET_COUNT));
athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_PAIR_FLOW_RATIO));
athenaMLFeatureConfiguration.addTargetFeatures(new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_SEC));

// K-Means clustering (k=8, iteration=20, runs=10)

KMeansDetectionAlgorithm kMeansDetectionAlgorithm = new KMeansDetectionAlgorithm();
kMeansDetectionAlgorithm.setK(8);
kMeansDetectionAlgorithm.setMaxIterations(20);
kMeansDetectionAlgorithm.setRuns(10);

// (Optional) Set labeling for if the ML algorithm is classification

Marking marking = new Marking();
marking.setSrcMaskMarking(0x000000ff, 0x00000065);

// Select Athena index fields; below seven fields are used for identifying unique Athena features

Indexing indexing = new Indexing();
indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IP_PROTO));
indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_SRC));
indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_TCP_DST));
indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_SRC));
indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_UDP_DST));
indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_SRC));
indexing.addIndexingElements(new AthenaIndexField(AthenaIndexField.MATCH_IPV4_DST));
```

#### Generate a detection model

```
// Train the data set and generate a model

KMeansDetectionModel kMeansDetectionModel = (KMeansDetectionModel) machineLearningManager.
        generateAthenaDetectionModel(featureConstraint, athenaMLFeatureConfiguration, kMeansDetectionAlgorithm, indexing, marking);

// Save the generated model

machineLearningManager.saveDetectionModel(kMeansDetectionModel, "./AthenaModel.KMeansDetectionModel");

// Show the result

kMeansDetectionModel.getSummary().printSummary();
```

#### Validate features
```
// Load the trained model

KMeansDetectionModel kMeansDetectionModel =
        (KMeansDetectionModel) machineLearningManager.loadDetectionModel("./AthenaModel.KMeansDetectionModel"); // the name of the saved detection model


featureConstraint.setLocation("target"); // the name of MongoDB collection which contains a target data set

// Validate the target data set with the model

KmeansValidationSummary kmeansValidationSummary = (KmeansValidationSummary) machineLearningManager.
        validateAthenaFeatures(featureConstraint, athenaMLFeatureConfiguration, kMeansDetectionModel, indexing, marking);

// Show the result

kmeansValidationSummary.printResults();
```

### Athena Feature List

<table class="tg">
  <tbody style="font-size: 14px;">
  <tr>
    <th class="tg-baqh" rowspan="2">Classification</th>
    <th class="tg-baqh" rowspan="2">Feature Name</th>
    <th class="tg-baqh" colspan="3">Category</th>
    <th class="tg-baqh" rowspan="2">Formula</th>
  </tr>
  <tr>
    <td class="tg-baqh">Protocol<br>Centric</td>
    <td class="tg-baqh">Combination</td>
    <td class="tg-baqh">Stateful<br>(+Variation)</td>
  </tr>
  <tr>
    <td class="tg-baqh" rowspan="2">ERROR</td>
    <td class="tg-yw4l">ERRTYPE</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">ERRCODE</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-baqh" rowspan="8">FLOW<br>_REMOVED</td>
    <td class="tg-yw4l">DURATION_SECOND</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">DURATION_N_SECOND</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">IDLE_TIMEOUT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">HARD_TIEMOUT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">PACKET_COUNT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">BYTE_COUNT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">PACKET_PER_DURATION</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">PACKET_COUNT / DURATION_N_SEC</td>
  </tr>
  <tr>
    <td class="tg-yw4l">BYTE_PER_DURATION</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">BYTE_COUNT / DURATION_N_SEC</td>
  </tr>
  <tr>
    <td class="tg-baqh" rowspan="3">PACKET_IN</td>
    <td class="tg-yw4l">TOTAL_LEN</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">REASON</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">PAYLOAD_MATCHE<br>_FIELDS</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-baqh">PORT_STATUS</td>
    <td class="tg-yw4l">REASON</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-baqh" rowspan="16">FLOW_STATS</td>
    <td class="tg-yw4l">DURATION_SEC</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">DURATION_N_SEC</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">PRIORITY</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">IDLE_TIMEOUT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">HARD_TIMEOUT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">PACKET_COUNT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">BYTE_COUNT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">BYTE_PER_PACKET</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">BYTE_COUNT / PACKET_COUNT</td>
  </tr>
  <tr>
    <td class="tg-yw4l">PACKET_PER_DURATION</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">PACKET_COUNT / DURATION_SEC</td>
  </tr>
  <tr>
    <td class="tg-yw4l">BYTE_PER_DURATION</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">BYTE_COUNT / DURATION_SEC</td>
  </tr>
  <tr>
    <td class="tg-yw4l">ACTION_OUTPUT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">ACTION_OUTPUT_PORT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">ACTION_DROP</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">PAIR_FLOW</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">if bidirectional connection</td>
  </tr>
  <tr>
    <td class="tg-yw4l">TOTAL_FLOWS</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">Σ flows</td>
  </tr>
  <tr>
    <td class="tg-yw4l">PAIR_FLOW_RATIO</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">Σ PAIR_FLOWS / TOTAL_FLOWS</td>
  </tr>
  <tr>
    <td class="tg-baqh" rowspan="21">PORT_STATS</td>
    <td class="tg-yw4l">RX_PACKETS</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">TX_PACKETS</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">RX_BYTES</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">TX_BYTES</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">RX_DROPPED</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">TX_DROPPED</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">RX_ERRORS</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">TX_ERRORS</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">RX_FRAME_ERROR</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">RX_OVER_ERROR</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">RX_CRC_ERROR</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">COLLISIONS</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">RX_BYTES_PER_PACKET</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">RX_BYTE / RX_PACKET</td>
  </tr>
  <tr>
    <td class="tg-yw4l">TX_BYTES_PER_PACKET</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">TX_BYTE / TX_PACKET</td>
  </tr>
  <tr>
    <td class="tg-yw4l">RX_DROPPED_PER_PACKET</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">RX_DROPPED / RX_PACKET</td>
  </tr>
  <tr>
    <td class="tg-yw4l">TX_DROPPED_PER_PACKET</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">TX_DROPPED / TX_PACKET</td>
  </tr>
  <tr>
    <td class="tg-yw4l">RX_ERROR_PER_PACKET</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">RX_ERROR / RX_PACKET</td>
  </tr>
  <tr>
    <td class="tg-yw4l">TX_ERROR_PER_PACKET</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">TX_ERROR / TX_PACKET</td>
  </tr>
  <tr>
    <td class="tg-yw4l">RX_FRAME_ERROR<br>_PER_PACKET</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">RX_FRAME_ERROR / RX_PACKET</td>
  </tr>
  <tr>
    <td class="tg-yw4l">RX_OVER_ERROR<br>_PER_PACKET</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">RX_OVER_ERROR / RX_PACKET</td>
  </tr>
  <tr>
    <td class="tg-yw4l">RX_CRC_ERROR<br>_PER_PACKET</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">RX_CRC_ERROR / RX_PACKET</td>
  </tr>
  <tr>
    <td class="tg-baqh" rowspan="4">AGGREGATE<br>_STATS</td>
    <td class="tg-yw4l">PACKET_COUNT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">BYTE_COUNT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">FLOW_COUNT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">BYTE_PER_PACKET</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">BYTE_COUNT / PACKET_COUNT</td>
  </tr>
  <tr>
    <td class="tg-baqh" rowspan="3">QUEUE_STATS</td>
    <td class="tg-yw4l">TX_BYTES</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">TX_PACKETS</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">TX_ERROS</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-baqh" rowspan="8">TABLE_STATS</td>
    <td class="tg-yw4l">MAX_ENTRIES</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">ACTIVE_COUNT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">LOOKUP_COUNT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">MATCHED_COUNT</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">MATCHED_PER_LOOKUP</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">MATCHED_COUNT / LOOKUP_COUNT</td>
  </tr>
  <tr>
    <td class="tg-yw4l">ACTIVE_PER_MAX</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">ACTIVE_COUNT / MAX_ENTRIES</td>
  </tr>
  <tr>
    <td class="tg-yw4l">LOOKUP_PER_ACTIVE</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">LOOKUP_COUNT / ACTIVE_COUNT</td>
  </tr>
  <tr>
    <td class="tg-yw4l">MATCHED_PER_ACTIVE</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">✔</td>
    <td class="tg-baqh">MATCHED_COUNT / ACTIVE_COUNT</td>
  </tr>
  </tbody>
</table>


## Using Athena CLI

```
onos> athena-query ?
athena-query FeatureComaratorValue Ops:Pramgs
Timestamp format: yyyy-MM-dd-HH:mm
Available advanced options are :
        L    - Limit features (param1 = number of entires
        S    - Sorting with a certain feature (param1 = name of feature
        A    - Sorting entires with a certain condition by an index
ex) athena-query FSSdurationNSec>10,timestamp>2016-01-03-11:45,FSSactionOutput=true,AappName=org.onosproject.fwd L:100,S:FSSbyteCount,A:Feature1:Feature2
```

![Athena query example](athena/resources/images/query.png)

## Questions?
Visit http://sdnsecurity.org or http://nss.kaist.ac.kr.
