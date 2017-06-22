package org.onosproject.athena.feature;



/**
 * Created by seunghyeon on 8/13/15.
 */
public enum FeatureType {
    /**
     * Indicates that a type of feature.
     */

    /**
     * Indicates that a type of feature is Synchronous (e.g., Information in statistics).
     */
    SYNCHRONOUS_STATISTICS,

    /**
     * Indicates that a type of feature is Asynchronous (e.g., Flow_Removed).
     */
    ASYNCHRONOUS_EVENT,

    /**
     * Inidicates that a type of feature is Synchronous with management (e.g., TCP success ratio).
     */
    SYNCHRONOUS_MANAGE
}
