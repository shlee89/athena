package athena.util;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by seunghyeon on 1/24/16.
 */
public class TimeSeriesData {
    Date date;

    HashMap<String, Long> dataEntry = new HashMap<>();

    public TimeSeriesData(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public boolean insertdataEntry(String index, Long value) {
        if (index == null || value == null) {
            return false;
        }
        dataEntry.put(index, value);
        return true;
    }

    public HashMap<String, Long> getDataEntry() {
        return dataEntry;
    }
}
