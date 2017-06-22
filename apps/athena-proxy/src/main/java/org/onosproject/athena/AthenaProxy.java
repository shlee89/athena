package org.onosproject.athena;

/**
 * Created by seunghyeon on 1/12/16.
 */


import com.esotericsoftware.kryo.Kryo;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.onlab.packet.Ethernet;
import org.onlab.packet.Ip4Prefix;
import org.onosproject.athena.database.TargetAthenaValue;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.athena.database.AthenaFeatureField;
import org.onosproject.athena.database.AthenaFeatureRequester;
import org.onosproject.athena.database.CommunicatorOption;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.FeatureConstraintOperatorType;
import org.onosproject.athena.database.FeatureConstraintType;
import org.onosproject.athena.database.AthenaFeatureRequestrType;
import org.onosproject.athena.database.AthenaFeatureEventListener;
import org.onosproject.athena.database.AthenaFeatures;
import org.onosproject.athena.database.FeatureDatabaseService;
import org.onosproject.athena.database.QueryIdentifier;
import org.onosproject.athena.database.FeatureConstraintOperator;
import org.onosproject.net.DeviceId;
import org.onosproject.net.flow.DefaultTrafficSelector;
import org.onosproject.net.flow.DefaultTrafficTreatment;
import org.onosproject.net.flow.FlowRuleService;
import org.onosproject.net.flow.TrafficSelector;
import org.onosproject.net.flow.TrafficTreatment;
import org.onosproject.net.flowobjective.DefaultForwardingObjective;
import org.onosproject.net.flowobjective.FlowObjectiveService;
import org.onosproject.net.flowobjective.ForwardingObjective;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.onlab.util.Tools.groupedThreads;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Sample reactive forwarding application.
 */
@Component(immediate = true)
public class AthenaProxy {
    private static final int DEFAULT_TIMEOUT = 10000;
    private static final int DEFAULT_PRIORITY = 1000;

    private int flowPriority = DEFAULT_PRIORITY;
    private int flowTimeout = DEFAULT_TIMEOUT;

    private final Logger log = getLogger(getClass());

    private ApplicationId appId;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected CoreService coreService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected FeatureDatabaseService featureDatabaseService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected FlowObjectiveService flowObjectiveService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected FlowRuleService flowRuleService;

    private InternalAthenaFeatureEventListener databaseEventListener
            = new InternalAthenaFeatureEventListener();


    protected ExecutorService nettyExcutors =
            Executors.newFixedThreadPool(2, groupedThreads("onos/athena", "athena-netty-%d"));

    AthenaCommunicatorIn receiver;
    AthenaCommunicatorIn sender;

    @Activate
    public void activate(ComponentContext context) throws IOException {
        appId = coreService.registerApplication("org.onosproject.athenaProxy");
        log.info("Started", appId.id());
        //run athena communicator
        receiver = new AthenaCommunicatorIn(CommunicatorOption.RECEIVER);
        sender = new AthenaCommunicatorIn(CommunicatorOption.SENDER);
        nettyExcutors.execute(receiver);

        featureDatabaseService.addDatabaseEventListener(1, databaseEventListener);
        System.out.println("[Debug]Run proxy!!!!!!!!!!!!!!!!!");
        log.info("[Debug]Run !!!!!!!!!!!!!!!!!");
//        checkOnlineEventDelivery();
    }

    @Deactivate
    public void deactivate() {

        featureDatabaseService.removeDatabaseEventListener(databaseEventListener);
        featureDatabaseService.unRegisterOnlineFeature(appId, new QueryIdentifier(appId.id()));
        flowRuleService.removeFlowRulesById(appId);
        log.info("Stopped");
    }

    @Modified
    public void modified(ComponentContext context) {

    }

    public void checkOnlineEventDelivery() {


//        AthenaIndexField indexName = new AthenaIndexField();
        FeatureConstraint dataRequestobject =
                new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                        new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        FeatureConstraint flowStatsDuration =
                new FeatureConstraint(FeatureConstraintType.FEATURE,
                        FeatureConstraintOperatorType.COMPARABLE,
                        new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT),
                        new AthenaFeatureField(AthenaFeatureField.FLOW_STATS_DURATION_N_SEC),
                        new TargetAthenaValue(1));
        dataRequestobject.appenValue(new TargetAthenaValue(flowStatsDuration));
        AthenaFeatureRequester athenaFeatureRequester = new AthenaFeatureRequester(
                AthenaFeatureRequestrType.REGISTER_ONLINE_HANDLER,
                dataRequestobject, null, null);

        featureDatabaseService.registerOnlineFeature(appId, new QueryIdentifier(appId.id()), athenaFeatureRequester);
    }


    public class InternalAthenaFeatureEventListener implements AthenaFeatureEventListener {

        @Override
        public void getRequestedFeatures(ApplicationId applicationId, AthenaFeatures athenaFeatures) {
            //to be called by external athena application
        }

        @Override
        public void getFeatureEvent(ApplicationId applicationId, QueryIdentifier id, HashMap<String, Object> event) {
            sender.sendEvent(new ExternalDataType(ExternalDataType.ONLINE_FEATURE_EVENT),
                    applicationId,
                    id,
                    event);

        }
    }

    public class AthenaCommunicatorIn implements Runnable {

        private static final String OUT_IP = "127.0.0.1";
        private static final int OUT_PORT = 5000;

        private static final int IN_PORT = 5001;

        SerializerHelper serializerHelper = new SerializerHelper();

        Kryo kryo;
        CommunicatorOption communicatorOption;
        ServerSocket serverSocket = null;

        public AthenaCommunicatorIn(CommunicatorOption communicatorOption) {
            this.communicatorOption = communicatorOption;
            kryo = serializerHelper.initializeSerializeKryo();
        }

        ObjectOutputStream outStream = null;


        public void sendEvent(ExternalDataType dataType,
                              ApplicationId applicationId,
                              QueryIdentifier queryIdentifier,
                              HashMap<String, Object> event) {
            event.put("dataType", dataType.getType());
            event.put("queryIdentifier", Integer.toString(queryIdentifier.getId()));
            sendToOut(event);
//            System.out.println("[Debug] Send Features to athena application" + event.toString());


        }


        public void sendToOut(HashMap<String, Object> obj) {
            Socket client = null;
            try {
                client = new Socket(OUT_IP, OUT_PORT);
                OutputStream outToServer = client.getOutputStream();
                ObjectOutputStream outO = new ObjectOutputStream(outToServer);
                outO.writeObject(obj);
                InputStream inFromServer = client.getInputStream();
                ObjectInputStream in =
                        new ObjectInputStream(inFromServer);
                log.info("Sever says:" + in.readObject().toString());
                client.close();
            } catch (IOException | ClassNotFoundException e) {
                log.warn("External Athena application is not available now");
            }

        }

        public void startReceiver() {
            ServerSocket server = null;
            Socket s = null;
            ObjectInputStream in = null;
            try {
                server = new ServerSocket(IN_PORT);
                while (true) {
                    try {
                        s = server.accept();
                        in = new ObjectInputStream(s.getInputStream());
                        SerializerWrapper serializerWrapper = (SerializerWrapper) in.readObject();
                        handleExternalRequest(serializerWrapper);
                    } finally {
                        s.close();
                    }
                }
            } catch (IOException e) {
                log.warn("External Athena application is not available now");
            } catch (ClassNotFoundException e) {
                log.warn("Kryo class exception");
            }
        }


        public void handleExternalRequest(SerializerWrapper serializerWrapper) {
            log.info("Get Athena commands :" + serializerWrapper.getExternalDataType().getType());
            AthenaFeatureRequester athenaFeatureRequester;
            switch (serializerWrapper.getExternalDataType().getEventType()) {
                case 2:
                    athenaFeatureRequester = new AthenaFeatureRequester(
                            AthenaFeatureRequestrType.REGISTER_ONLINE_HANDLER,
                            serializerWrapper.getFeatureConstraint(), null, null);
                    featureDatabaseService.registerOnlineFeature(
                            appId,
                            serializerWrapper.getQueryIdentifier(),
                            athenaFeatureRequester);
                    break;
                case 3:
                    featureDatabaseService.unRegisterOnlineFeature(
                            appId,
                            serializerWrapper.getQueryIdentifier());
                    break;
                case 4:
                    //flow rule
                    installBlockRule(serializerWrapper.getIpSrc(),
                            serializerWrapper.getIpDst(),
                            serializerWrapper.getDeviceUri());
                    break;
                default:
                    break;
            }
        }

        @Override
        public void run() {
            switch (communicatorOption) {
                case RECEIVER:
                    startReceiver();
                    break;
                case SENDER:
                    //;
                    break;
                default:
                    startReceiver();
                    break;
            }
        }

        public void installBlockRule(TargetAthenaValue ipSrc, TargetAthenaValue ipDst, String deviceUri) {

            ///////////////////////////////Debug///////////////////////////////////////
            Integer sip = (Integer) ipSrc.getTargetAthenaValue();
            Integer dip = (Integer) ipDst.getTargetAthenaValue();
            byte[] sbytes = BigInteger.valueOf(sip).toByteArray();
            byte[] dbytes = BigInteger.valueOf(sip).toByteArray();
            InetAddress saddress = null;
            InetAddress daddress = null;
            try {
                saddress = InetAddress.getByAddress(sbytes);
                daddress = InetAddress.getByAddress(dbytes);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            DeviceId deviceId = DeviceId.deviceId(deviceUri);

            TrafficSelector.Builder selectorBuilder = DefaultTrafficSelector.builder();

            Ip4Prefix matchIp4SrcPrefix =
                    Ip4Prefix.valueOf(((Integer) ipSrc.getTargetAthenaValue()).intValue(),
                            Ip4Prefix.MAX_MASK_LENGTH);
            Ip4Prefix matchIp4DstPrefix =
                    Ip4Prefix.valueOf(((Integer) ipDst.getTargetAthenaValue()).intValue(),
                            Ip4Prefix.MAX_MASK_LENGTH);
            selectorBuilder.matchEthType(Ethernet.TYPE_IPV4)
                    .matchIPSrc(matchIp4SrcPrefix)
                    .matchIPDst(matchIp4DstPrefix);

            TrafficTreatment treatment = DefaultTrafficTreatment.builder()
                    .build();

            ForwardingObjective forwardingObjective = DefaultForwardingObjective.builder()
                    .withSelector(selectorBuilder.build())
                    .withTreatment(treatment)
                    .withPriority(flowPriority)
                    .withFlag(ForwardingObjective.Flag.VERSATILE)
                    .fromApp(appId)
                    .makeTemporary(flowTimeout)
                    .add();

            log.info("Block!!" + saddress.getHostAddress() + daddress.getHostAddress() + deviceUri);
            flowObjectiveService.forward(deviceId,
                    forwardingObjective);
        }
    }

}
