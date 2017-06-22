
/*
 * Copyright 2014-2015 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.cli.eval;

import org.apache.karaf.shell.commands.Command;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.net.Device;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.flow.FlowEntry;
import org.onosproject.net.flow.FlowRule;
import org.onosproject.net.flow.FlowRuleService;

/**
 * Removes dummy flows to all devices - Jinwoo Kim.
 */
@Command(scope = "onos", name = "remove-dummy-flows",
         description = "Removes a number of test flow rules - for testing only")
public class RemoveDummyFlowsCommand extends AbstractShellCommand {

      @Override
    protected void execute() {
        DeviceService deviceService = get(DeviceService.class);
        CoreService coreService = get(CoreService.class);
        FlowRuleService flowRuleService = get(FlowRuleService.class);
        ApplicationId appId = coreService.registerApplication("eval.add.dummy");

        Iterable<Device> devices = deviceService.getDevices();

        for (Device d : devices) {
            for (FlowEntry r : flowRuleService.getFlowEntries(d.id())) {
                if (r.appId() == appId.id()) {
                    flowRuleService.removeFlowRules((FlowRule) r);
                }
            }
        }
    }
}