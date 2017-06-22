package org.onosproject.athena.learning;

import java.nio.file.Path;

/**
 * Created by seunghyeon on 9/2/15.
 */
public interface LearningEventListener {
    /**
     * Deliver complete detection result with file.
     * After on-line detection is done, this method will be called.
     *
     * @param id   registered id
     * @param path result file path
     */
    void getCompeleteWithFile(int id, Path path);

    /**
     * Deliver online detection result to apps.
     *
     * @param id     registered id
     * @param result result
     */
    void getRuntimeEvent(int id, String result);
}
