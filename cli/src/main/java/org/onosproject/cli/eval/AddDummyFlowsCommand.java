
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

import org.apache.commons.lang.math.RandomUtils;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.onlab.packet.MacAddress;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.net.Device;
import org.onosproject.net.PortNumber;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.flow.DefaultTrafficSelector;
import org.onosproject.net.flow.DefaultTrafficTreatment;
import org.onosproject.net.flow.TrafficSelector;
import org.onosproject.net.flow.TrafficTreatment;
import org.onosproject.net.flowobjective.DefaultForwardingObjective;
import org.onosproject.net.flowobjective.FlowObjectiveService;
import org.onosproject.net.flowobjective.ForwardingObjective;

/**
 * Installs dumy flows to all devices - Jinwoo Kim.
 */
@Command(scope = "onos", name = "add-dummy-flows",
         description = "Installs a number of test flow rules - for testing only")
public class AddDummyFlowsCommand extends AbstractShellCommand {

    @Argument(index = 0, name = "flowPerDevice", description = "Number of flows to add per device",
              required = true, multiValued = false)
    String flows = null;

    @Override
    protected void execute() {
        DeviceService deviceService = get(DeviceService.class);
        CoreService coreService = get(CoreService.class);
        FlowObjectiveService flowObjectiveService = get(FlowObjectiveService.class);

        ApplicationId appId = coreService.registerApplication("eval.add.dummy");

        int flowsPerDevice = Integer.parseInt(flows);

        Iterable<Device> devices = deviceService.getDevices();
        TrafficTreatment treatment = DefaultTrafficTreatment.builder()
                .setOutput(PortNumber.portNumber(1)).build();
        TrafficSelector.Builder sbuilder;

        for (Device d : devices) {
            for (int i = 0; i < flowsPerDevice; i++) {
                sbuilder = DefaultTrafficSelector.builder();

                sbuilder.matchEthSrc(MacAddress.valueOf(RandomUtils.nextInt() * i))
                        .matchEthDst(MacAddress.valueOf((Integer.MAX_VALUE - i) * RandomUtils.nextInt()));

                ForwardingObjective forwardingObjective = DefaultForwardingObjective.builder()
                        .withSelector(sbuilder.build())
                        .withTreatment(treatment)
                        .withPriority(100) //solved !!
                        .withFlag(ForwardingObjective.Flag.VERSATILE)
                        .fromApp(appId)
                        .makePermanent()
                        .add();

                flowObjectiveService.forward(d.id(), forwardingObjective);
            }
        }
    }
}