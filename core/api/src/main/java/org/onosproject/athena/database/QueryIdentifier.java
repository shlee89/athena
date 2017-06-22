package org.onosproject.athena.database;


import java.io.Serializable;

/**
 * Created by seunghyeon on 9/2/15.
 */
public class QueryIdentifier implements Serializable {
    private short id;

    public QueryIdentifier(short id) {
        this.id = id;
    }

    public QueryIdentifier() {
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }
}
