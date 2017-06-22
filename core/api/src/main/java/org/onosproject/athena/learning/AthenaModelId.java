package org.onosproject.athena.learning;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 12/29/15.
 */
public class AthenaModelId {


    private final Logger log = getLogger(getClass());

    private short id;
    private String name;


    public AthenaModelId(short id, String name) {
        if (name == null) {
            log.warn("name MUST not NULL");
        }

        this.id = id;
        this.name = name;
    }

    public short getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}