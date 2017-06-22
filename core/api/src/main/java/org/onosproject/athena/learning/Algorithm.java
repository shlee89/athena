package org.onosproject.athena.learning;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by seunghyeon on 9/2/15.
 */
public class Algorithm {

    private final Logger log = getLogger(getClass());

    private AlgorithmSelector algorithmSelector;

    protected Multimap<String, String> options =
            ArrayListMultimap.create();

    public Algorithm(AlgorithmSelector algorithmSelector) {
        this.algorithmSelector = algorithmSelector;
    }

    public Algorithm() {
    }

    public void setAlgorithmSelector(AlgorithmSelector algorithmSelector) {
        this.algorithmSelector = algorithmSelector;
    }

    public void setOptions(String option, String value) {
        if (option == null || value == null) {
            log.warn("option or value cannot be null");
        }
        options.put(option, value);
    }

    public AlgorithmSelector getAlgorithmSelector() {
        return algorithmSelector;
    }

    public Multimap<String, String> getOptions() {
        return options;
    }
}
