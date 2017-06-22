package athena.northbound.impl;

import athena.northbound.FlowRuleManager;
import athena.util.ControllerConnector;
import org.onosproject.athena.FlowRuleActionType;
import org.onosproject.athena.database.TargetAthenaValue;
import org.onosproject.core.ApplicationId;
import org.onosproject.athena.ExternalDataType;
import org.onosproject.athena.SerializerWrapper;
import org.onosproject.net.flow.FlowRule;

/**
 * Created by seunghyeon on 4/3/16.
 */
public class FlowRuleManagerImpl implements FlowRuleManager {

    ControllerConnector controllerConnector;

    public FlowRuleManagerImpl(ControllerConnector controllerConnector) {
        this.controllerConnector = controllerConnector;
    }

    public FlowRuleManagerImpl() {
        this.controllerConnector = new ControllerConnector();
    }


    public ControllerConnector getControllerConnector() {
        return controllerConnector;
    }

    private void sendFlowRule(ExternalDataType dataType,
                              ApplicationId appId,
                              FlowRule flowRule) {
        SerializerWrapper serializerWrapper = new SerializerWrapper();
        serializerWrapper.setExternalDataType(dataType);
        serializerWrapper.setFlowRule(flowRule);
        controllerConnector.sendToController(serializerWrapper);
    }

    @Override
    public void issueFlowRule(ApplicationId applicationId,
                              TargetAthenaValue IP_SRC,
                              TargetAthenaValue IP_DST,
                              String deviceUri,
                              FlowRuleActionType actionType) {
        //QURANTINE is under the construction
        actionType = FlowRuleActionType.BLOCK;

        sendFlowRuleRequest(new ExternalDataType(ExternalDataType.ISSUE_FLOW_RULE),
                applicationId,
                IP_SRC,
                IP_DST,
                deviceUri,
                actionType);
    }

    private void sendFlowRuleRequest(ExternalDataType dataType,
                                     ApplicationId applicationId,
                                     TargetAthenaValue IP_SRC,
                                     TargetAthenaValue IP_DST,
                                     String deviceUri,
                                     FlowRuleActionType actionType) {

        SerializerWrapper serializerWrapper = new SerializerWrapper();
        serializerWrapper.setExternalDataType(dataType);
        serializerWrapper.setIpSrc(IP_SRC);
        serializerWrapper.setIpDst(IP_DST);
        serializerWrapper.setDeviceUri(deviceUri);
        serializerWrapper.setActionType(actionType);
        controllerConnector.sendToController(serializerWrapper);
    }

}
