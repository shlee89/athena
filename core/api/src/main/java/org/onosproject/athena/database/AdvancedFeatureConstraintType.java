package org.onosproject.athena.database;



/**
 * Types for supported advanced options.
 * Created by seunghyeon on 1/2/16.
 */
public enum AdvancedFeatureConstraintType {
    /*
    Just Limit number of features
     */
    LIMIT_FEATURE_COUNTS,
    /*
    Aggreagate features
     */
    AGGREGATE,
    /*
    Sorting features
     */
    SORTING,
}