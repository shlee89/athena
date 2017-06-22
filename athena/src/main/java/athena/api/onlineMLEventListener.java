package athena.api;

import java.util.HashMap;

/**
 * EventListener for an online detection phase.
 * Created by seunghyeon on 4/7/16.
 */
public interface onlineMLEventListener {

    void getValidationResultOnlineResult(HashMap<String, Object> event, double result);
}
