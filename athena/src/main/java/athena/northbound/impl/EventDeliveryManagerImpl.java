package athena.northbound.impl;

import athena.northbound.EventDeliveryManager;
import athena.util.ControllerConnector;
import org.onosproject.core.ApplicationId;
import org.onosproject.athena.ExternalDataType;
import org.onosproject.athena.SerializerWrapper;
import org.onosproject.athena.database.AthenaFeatureEventListener;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.QueryIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of EventDeliveryManager.
 * Created by seunghyeon on 4/3/16.
 */
public class EventDeliveryManagerImpl implements EventDeliveryManager {
    ControllerConnector controllerConnector;
    ControllerConnector controllerConnectorReceiver;
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Construct with ControllerConnector and AthenaFeatureEventListener.
     *
     * @param listener user-defined listener.
     */
    public EventDeliveryManagerImpl(AthenaFeatureEventListener listener) {
        controllerConnector = new ControllerConnector();
        controllerConnectorReceiver = new ControllerConnector(listener);

        //start incomming connection
        Thread receiverThread = new Thread(controllerConnectorReceiver);
        receiverThread.start();
    }

    public EventDeliveryManagerImpl(ControllerConnector controllerConnector, AthenaFeatureEventListener listener) {
        this.controllerConnector = controllerConnector;
        controllerConnectorReceiver = new ControllerConnector(listener);

        //start incomming connection
        Thread receiverThread = new Thread(controllerConnectorReceiver);
        receiverThread.start();
    }

    public ControllerConnector getControllerConnector() {
        return controllerConnector;
    }

    public ControllerConnector getControllerConnectorReceiver() {
        return controllerConnectorReceiver;
    }

    /**
     *
     * @param controllerConnector
     * @param controllerConnectorReceiver
     */
    public EventDeliveryManagerImpl(ControllerConnector controllerConnector,
                                    ControllerConnector controllerConnectorReceiver) {
        this.controllerConnector = controllerConnector;
        this.controllerConnectorReceiver = controllerConnectorReceiver;

        //start incomming connection
        Thread receiverThread = new Thread(this.controllerConnectorReceiver);
        receiverThread.start();
    }

    /**
     * Register AthenaFeatureEventListener.
     *
     * @param listener AthenaFeatureEventListener.
     */
    public void registerAthenaFeatureEventListener(AthenaFeatureEventListener listener) {
        controllerConnectorReceiver.addAthenaFeatureEventListener(listener);
    }

    @Override
    public void registerOnlineAthenaFeature(ApplicationId applicationId,
                                            QueryIdentifier identifier,
                                            FeatureConstraint featureConstraint) {

        sendRegisterOnlineFeature(new ExternalDataType(ExternalDataType.ONLINE_FEATURE_REQUEST_REGISTER),
                applicationId,
                identifier,
                featureConstraint);

    }

    @Override
    public void unRegisterOnlineAthenaFeature(ApplicationId applicationId, QueryIdentifier identifier) {
        sendUnRegisterOnlineFeature(new ExternalDataType(ExternalDataType.ONLINE_FEATURE_REQUEST_UNREGISTER),
                applicationId,
                identifier);
    }

    private void sendUnRegisterOnlineFeature(ExternalDataType dataType,
                                             ApplicationId applicationId,
                                             QueryIdentifier queryIdentifier) {
        SerializerWrapper serializerWrapper = new SerializerWrapper();
        serializerWrapper.setExternalDataType(dataType);
        serializerWrapper.setQueryIdentifier(queryIdentifier);
        controllerConnector.sendToController(serializerWrapper);
    }

    private void sendRegisterOnlineFeature(ExternalDataType dataType,
                                           ApplicationId applicationId,
                                           QueryIdentifier queryIdentifier,
                                           FeatureConstraint featureConstraint) {
        System.out.println("Register");
        SerializerWrapper serializerWrapper = new SerializerWrapper();
        serializerWrapper.setExternalDataType(dataType);
        serializerWrapper.setQueryIdentifier(queryIdentifier);
        serializerWrapper.setFeatureConstraint(featureConstraint);
        controllerConnector.sendToController(serializerWrapper);
    }

}
