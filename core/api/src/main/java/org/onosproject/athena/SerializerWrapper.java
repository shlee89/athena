package org.onosproject.athena;


import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.QueryIdentifier;
import org.onosproject.athena.database.TargetAthenaValue;
import org.onosproject.net.flow.FlowRule;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by seunghyeon on 1/20/16.
 */
public class SerializerWrapper implements Serializable {

    private static final long serialVersionUID = -2518143671167959230L;

    ExternalDataType externalDataType;
    QueryIdentifier queryIdentifier = null;
    HashMap<String, Object> event = null;
    FeatureConstraint featureConstraint = null;
    TargetAthenaValue ipSrc = null;
    TargetAthenaValue ipDst = null;
    String deviceUri = null;
    FlowRuleActionType actionType = null;

    public String getDeviceUri() {
        return deviceUri;
    }

    public void setDeviceUri(String deviceUri) {
        this.deviceUri = deviceUri;
    }

    List list = null;

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public SerializerWrapper() {
    }

    public FeatureConstraint getFeatureConstraint() {
        return featureConstraint;
    }

    public void setFeatureConstraint(FeatureConstraint featureConstraint) {
        this.featureConstraint = featureConstraint;
    }

    public ExternalDataType getExternalDataType() {
        return externalDataType;
    }

    public void setExternalDataType(ExternalDataType externalDataType) {
        this.externalDataType = externalDataType;
    }

    public QueryIdentifier getQueryIdentifier() {
        return queryIdentifier;
    }

    public void setQueryIdentifier(QueryIdentifier queryIdentifier) {
        this.queryIdentifier = queryIdentifier;
    }

    public HashMap<String, Object> getEvent() {
        return event;
    }

    public void setEvent(HashMap<String, Object> event) {
        this.event = event;
    }

    //TODO not implemented yet
    FlowRule flowRule = null;

    public FlowRule getFlowRule() {
        return flowRule;
    }

    public void setFlowRule(FlowRule flowRule) {
        this.flowRule = flowRule;
    }

    public TargetAthenaValue getIpSrc() {
        return ipSrc;
    }

    public void setIpSrc(TargetAthenaValue ipSrc) {
        this.ipSrc = ipSrc;
    }

    public TargetAthenaValue getIpDst() {
        return ipDst;
    }

    public void setIpDst(TargetAthenaValue ipDst) {
        this.ipDst = ipDst;
    }

    public FlowRuleActionType getActionType() {
        return actionType;
    }

    public void setActionType(FlowRuleActionType actionType) {
        this.actionType = actionType;
    }
}
