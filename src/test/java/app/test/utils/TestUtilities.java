package app.test.utils;

import java.sql.Timestamp;
import java.util.Date;

public class TestUtilities {

    public static Date getCurrentTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
