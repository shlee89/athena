package org.onosproject.athena.database.impl;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;


import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 9/15/15.
 */
public class FeatureDatabaseManagerTest {
    private final Logger log = getLogger(getClass());


    Temp test = new Temp();

    @Before
    public void setUp() throws Exception {

        org.junit.Assert.fail("zzzzz");

    }

    @After
    public void tearDown() throws Exception {
        System.out.println("zzzzzzzz");
    }

    @Test
    public void testRequestAdvancedQuery() throws Exception {
        System.out.println("zz");
    }

    public class Temp {
        public void print() {
            log.info("ttttt");
        }
    }
}