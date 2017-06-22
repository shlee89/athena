package org.onosproject.cli.athena;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.onosproject.athena.database.FeatureDatabaseService;
import org.onosproject.cli.AbstractShellCommand;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 12/27/15.
 * This is work for debugging with CLI.
 * Define function, then run... sth
 */
@Command(scope = "onos", name = "athena-db",
        description = "Athena debugging CLI")
public class AthenaDatabaseCommand extends AbstractShellCommand {
    private final Logger log = getLogger(getClass());

    @Argument(index = 0, name = "command", description = "commands",
            required = true, multiValued = false)
    String command = null;

    @Override
    protected void execute() {
        FeatureDatabaseService featureDatabaseService = get(FeatureDatabaseService.class);

        if (command.startsWith("a")) {
            //run code
            log.info("[CLI-Debug-for-DB] run command 1");
            featureDatabaseService.debugCommand1("a");
        } else if (command.startsWith("b")) {
            //run code
            featureDatabaseService.debugCommand2("a");
            log.info("[CLI-Debug-for-DB] run command 2");
        } else if (command.startsWith("c")) {
            //run code
            featureDatabaseService.debugCommand3("a");
            log.info("[CLI-Debug-for-DB] run command 2");
        } else {
            log.info("code");
        }
    }
}
