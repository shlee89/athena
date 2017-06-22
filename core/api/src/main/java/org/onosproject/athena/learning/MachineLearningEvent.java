package org.onosproject.athena.learning;

import org.onosproject.event.AbstractEvent;

/**
 * Created by seunghyeon on 12/26/15.
 */
public class MachineLearningEvent
        extends AbstractEvent<MachineLearningEvent.Type, MachineLearningEventMessage> {


    protected MachineLearningEvent(Type type, MachineLearningEventMessage subject) {
        super(type, subject);
    }

    protected MachineLearningEvent(Type type, MachineLearningEventMessage subject, long time) {
        super(type, subject, time);
    }

    /**
     * Type of ML events.
     */
    public enum Type {
        /**
         * Signifies that Sample event1.
         */
        EVENT_ONE,

        /**
         * Signifies that Sample event2.
         */
        EVENT_TWO
    }

}
