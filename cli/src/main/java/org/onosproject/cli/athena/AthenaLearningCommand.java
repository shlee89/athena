package org.onosproject.cli.athena;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.onosproject.athena.learning.MachineLearningService;
import org.onosproject.cli.AbstractShellCommand;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 12/27/15.
 * This is work for debugging with CLI.
 * Define function, then run... sth
 */
@Command(scope = "onos", name = "athena",
        description = "Athena debugging CLI")
public class AthenaLearningCommand extends AbstractShellCommand {
    private final Logger log = getLogger(getClass());

    @Argument(index = 0, name = "command", description = "Network name",
            required = true, multiValued = false)
    String command = null;

    @Override
    protected void execute() {
        //finding services
        MachineLearningService machineLearningService = get(MachineLearningService.class);
        if (command.startsWith("Command1")) {
            //run code
            log.info("[CLI-Debug-for-ML] run command 1");
        } else if (command.startsWith("command2")) {
            //run code
            log.info("[CLI-Debug-for-ML] run command 2");
        } else {
            log.info("code");
        }
    }
}

