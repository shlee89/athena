package athena.northbound;

import org.onosproject.athena.FlowRuleActionType;
import org.onosproject.athena.database.TargetAthenaValue;
import org.onosproject.core.ApplicationId;
import org.onosproject.net.flow.FlowRule;

/**
 * The FlowRuleManager issues a flow rule to install a flow entry to switches.
 * Created by seunghyeon on 4/7/16.
 */
public interface FlowRuleManager {

    /**
     * @param applicationId
     * @param IP_SRC
     * @param IP_DST
     * @param actionType
     */
    void issueFlowRule(ApplicationId applicationId,
                       TargetAthenaValue IP_SRC, TargetAthenaValue IP_DST,
                       String deviceUri, FlowRuleActionType actionType);
}
