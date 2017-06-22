package org.onosproject.cli.athena;

import com.google.common.base.Splitter;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.onosproject.athena.database.AdvancedFeatureConstraint;
import org.onosproject.athena.database.AdvancedFeatureConstraintType;
import org.onosproject.athena.database.AdvancedFeatureConstraintValue;
import org.onosproject.athena.database.AthenaFeatureField;
import org.onosproject.athena.database.AthenaFeatureRequester;
import org.onosproject.athena.database.AthenaFeatureRequestrType;
import org.onosproject.athena.database.AthenaIndexField;
import org.onosproject.athena.database.AthenaValueGenerator;
import org.onosproject.athena.database.FeatureConstraint;
import org.onosproject.athena.database.FeatureConstraintOperator;
import org.onosproject.athena.database.FeatureConstraintOperatorType;
import org.onosproject.athena.database.FeatureConstraintType;
import org.onosproject.athena.database.FeatureDatabaseService;
import org.onosproject.athena.database.TargetAthenaValue;
import org.onosproject.cli.AbstractShellCommand;
import org.slf4j.Logger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 01/05/16.
 * The basic CLI command for athena.
 * This works for querying to athena distributed storage with constraints and options.
 * athena-query feature:comparator:value,... option:param1:param2,...
 */
@Command(scope = "onos", name = "athena-query",
        description = "Athena debugging CLI")
public class AthenaQueryCommand extends AbstractShellCommand {
    private final Logger log = getLogger(getClass());

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    @Argument(index = 0, name = "queryArguments", description = "arguments for range query",
            required = true, multiValued = false)
    String queryArguments = null;

    @Argument(index = 1, name = "options", description = "Advanced options",
            required = false, multiValued = false)
    String options = null;


    @Override
    protected void execute() {
        FeatureDatabaseService featureDatabaseService = get(FeatureDatabaseService.class);


        if (queryArguments.startsWith("?")) {
            printUsage();
            return;
        }

        if (queryArguments.startsWith("view")) {
            printFeatures();
            return;
        }

        //generate dataRequestobj
        FeatureConstraint dataRequestobject = generateDataRequestObject();

        //generate dataRequestAdvancedOps
        AdvancedFeatureConstraint dataRequestAdvancedOptions = null;

        if (options != null) {
            dataRequestAdvancedOptions = dataRequestAdvancedOptions();
        }


        AthenaFeatureRequester athenaFeatureRequester = new AthenaFeatureRequester(
                AthenaFeatureRequestrType.REQUEST_FEATURES,
                dataRequestobject, dataRequestAdvancedOptions, null);


        featureDatabaseService.requestFeatures(null, athenaFeatureRequester);
    }

    public AdvancedFeatureConstraint dataRequestAdvancedOptions() {
        AdvancedFeatureConstraint dataRequestAdvancedOptions = new AdvancedFeatureConstraint();

        Iterable<String> ops;
        ops = Splitter.on(",").split(options);
        String[] param = new String[3];
        String sb;

        for (String params : ops) {
            //LIMIT_FEATURE_COUNTS
            if (params.startsWith("L")) {
                param[0] = params.substring(2);
                dataRequestAdvancedOptions.addAdvancedOptions(AdvancedFeatureConstraintType.LIMIT_FEATURE_COUNTS,
                        new AdvancedFeatureConstraintValue(param[0]));

                //SORTING_RAW_FEATURES
            } else if (params.startsWith("S")) {
                param[0] = params.substring(2);
                dataRequestAdvancedOptions.addAdvancedOptions(AdvancedFeatureConstraintType.SORTING,
                        new AdvancedFeatureConstraintValue(param[0]));
                //SORTING_AGGREGATED_INDEX_FEATURES
            } else if (params.startsWith("A")) {
                sb = params.substring(2);
                dataRequestAdvancedOptions.addAdvancedOptions(AdvancedFeatureConstraintType.AGGREGATE,
                        new AdvancedFeatureConstraintValue(splitConstraintsAdvanced(sb)));
            }

        }
        return dataRequestAdvancedOptions;
    }


    public List<String> splitConstraintsAdvanced(String params) {
        List<String> returnValue = new ArrayList<>();
        Iterable<String> value;
        value = Splitter.on(":").split(params);

        for (String v : value) {
            returnValue.add(v);
        }

        return returnValue;
    }

    public String[] splitConstraints(String params) {
        String comparator = null;
        String[] param = null;
        String[] returnValue = new String[3];
        String[] compratorSet = {"=", ">", "<", ">=", "<="};

        for (int i = 0; i < compratorSet.length; i++) {
            param = params.split(compratorSet[i]);
            if (param[0].length() != params.length()) {
                returnValue[0] = param[0];
                returnValue[1] = compratorSet[i];
                returnValue[2] = param[1];
                break;
            }
        }
        return returnValue;
    }

    //generate DataRequestObjects.
    private FeatureConstraint generateDataRequestObject() {
        AthenaFeatureField athenaFeatureField = new AthenaFeatureField();
        AthenaIndexField athenaIndexField = new AthenaIndexField();
        FeatureConstraintType featureConstraintType;
        FeatureConstraintOperator featureConstraintOperator = null;
        Object value = null;
        String type;

        FeatureConstraint completeDataRequestobject =
                new FeatureConstraint(FeatureConstraintOperatorType.LOGICAL,
                        new FeatureConstraintOperator(FeatureConstraintOperator.LOGICAL_AND));

        Iterable<String> constraints;
        constraints = Splitter.on(",").split(queryArguments);
        String[] param;

        for (String params : constraints) {
            param = splitConstraints(params);

            if (param[1].startsWith("=")) {
                featureConstraintOperator = new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_EQ);
            } else if (param[1].startsWith(">")) {
                featureConstraintOperator = new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GT);
            } else if (param[1].startsWith(">=")) {
                featureConstraintOperator = new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_GTE);
            } else if (param[1].startsWith("<")) {
                featureConstraintOperator = new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_LT);
            } else if (param[1].startsWith("<=")) {
                featureConstraintOperator = new FeatureConstraintOperator(FeatureConstraintOperator.COMPARISON_LTE);
            } else {
                System.out.println("Not support operator : " + param[1]);
            }

            type = athenaIndexField.getTypeOnDatabase(param[0]);

            if (type == null) {
                type = athenaFeatureField.getTypeOnDatabase(param[0]);
            }

            if (type.startsWith(athenaFeatureField.varintType)) {
                value = new BigInteger(param[2]);
            } else if (type.startsWith(athenaFeatureField.bigintType)) {
                value = new Long(param[2]);
            } else if (type.startsWith(athenaFeatureField.boolType)) {
                value = Boolean.valueOf(param[2]);
            } else if (type.startsWith(athenaFeatureField.stringType)) {
                value = param[2];
            } else if (type.startsWith(athenaFeatureField.doubleType)) {
                value = new Double(param[2]);
            } else if (type.startsWith(athenaFeatureField.timestampType)) {
                value = AthenaValueGenerator.parseDataToAthenaValue(param[2]);
            } else {
                System.out.println("Not supported feature : " + param[2]);
            }


            FeatureConstraint featureConstraint = null;

            if (athenaIndexField.getListOfFeatures().contains(param[0])) {
                featureConstraintType = FeatureConstraintType.INDEX;
                athenaIndexField = new AthenaIndexField();
                athenaIndexField.setValue(param[0]);
                featureConstraint = new FeatureConstraint(featureConstraintType,
                        FeatureConstraintOperatorType.COMPARABLE,
                        featureConstraintOperator,
                        athenaIndexField,
                        new TargetAthenaValue(value));
            } else if (athenaFeatureField.getListOfFeatures().contains(param[0])) {
                featureConstraintType = FeatureConstraintType.FEATURE;
                athenaFeatureField = new AthenaFeatureField();
                athenaFeatureField.setValue(param[0]);
                featureConstraint = new FeatureConstraint(featureConstraintType,
                        FeatureConstraintOperatorType.COMPARABLE,
                        featureConstraintOperator,
                        athenaFeatureField,
                        new TargetAthenaValue(value));
            } else {
                System.out.println("Not support feature : " + param[0]);
            }
            if (featureConstraint == null) {
                System.out.println("Cannot create datareuqestObject! ");
            }
            completeDataRequestobject.appenValue(new TargetAthenaValue(featureConstraint));
        }


        return completeDataRequestobject;

    }


    public void printUsage() {
        System.out.println("athena-query FeatureComaratorValue Ops:Pramgs");
        System.out.println("Timestamp format: yyyy-MM-dd-HH:mm");
        System.out.println("Available advanced options are :");
        System.out.println("        L    - Limit features (param1 = number of entires");
        System.out.println("        S    - Sorting with a certain feature (param1 = name of feature ");
        System.out.println("        A    - Sorting entires with a certain condition by an index");
        System.out.println("ex) athena-query FSSdurationNSec>10,timestamp>2016-01-03-11:45,FSSactionOutput=true," +
                "AappName=org.onosproject.fwd" +
                " L:100,S:FSSbyteCount,A:Feature1:Feature2");
    }

    public void printFeatures() {
        AthenaIndexField in = new AthenaIndexField();
        AthenaFeatureField fn = new AthenaFeatureField();
        System.out.print(ANSI_PURPLE + "Index: ");
        List<String> features = in.getListOfFeatures();
        for (int i = 0; i < features.size(); i++) {
            System.out.print(features.get(i));
            if (i != (features.size() - 1)) {
                System.out.print(", ");
            }
        }
        System.out.println(ANSI_RESET + "");

        System.out.print(ANSI_GREEN + "Feature: ");
        features = fn.getListOfFeatures();
        for (int i = 0; i < features.size(); i++) {
            if (!fn.isElementofTables(features.get(i))) {
                System.out.print(features.get(i));
            }
            if (i != (features.size() - 1)) {
                System.out.print(", ");
            }
        }
        System.out.println("" + ANSI_RESET);

    }

}