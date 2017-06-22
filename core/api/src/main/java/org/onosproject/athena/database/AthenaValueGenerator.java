package org.onosproject.athena.database;

import com.google.common.net.InetAddresses;

import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by seunghyeon on 1/22/16.
 */
public final class AthenaValueGenerator {
    private AthenaValueGenerator() {

    }

    public static Date parseDataToAthenaValue(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Integer parseIPv4ToAthenaValue(String ip) {
        InetAddress addr = InetAddresses.forString(ip);
        int address = InetAddresses.coerceToInteger(addr);

        return address;
    }

    public static Long generateAthenaValue(String value) {
        return Long.parseLong(value);

    }
}