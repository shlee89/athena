package org.onosproject.athena.database;

import java.io.Serializable;

/**
 * COMPARISON_* indicates that it compares to one of the value with specific "key" in database.
 * Created by seunghyeon on 9/3/15.
 */
public class FeatureConstraintOperator implements Serializable {
    private String value;

    public FeatureConstraintOperator() {
    }

    public FeatureConstraintOperator(String value) {

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Matches values that are equal to a specified value.
     */
    public static final String COMPARISON_EQ = "comparisonEq";
    /**
     * Matches values that are greater than a specified value.
     */
    public static final String COMPARISON_GT = "comparisonGt";
    /**
     * Matches values that are greater than or equal to a specified value.
     */
    public static final String COMPARISON_GTE = "comparisonGte";
    /**
     * Matches values that are less than a specified value.
     */
    public static final String COMPARISON_LT = "comparisonLt";
    /**
     * Matches values that are less than or equal to a specified value.
     */
    public static final String COMPARISON_LTE = "comparisonLte";
    /**
     * Matches all values that are not equal to a specified value.
     */
    public static final String COMPARISON_NE = "comparisonNe";

    /**
     * Matches any of the values specified in an array.
     */
    public static final String LOGICAL_IN = "logicalIn";
    /**
     * Joins query clauses with a logical OR returns all documents that match the conditions of either clause.
     */
    public static final String LOGICAL_OR = "logicalOr";
    /**
     * Joins query clauses with a logical AND returns all documents that match the conditions of both clauses.
     */
    public static final String LOGICAL_AND = "logicalAnd";

}
