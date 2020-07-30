package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampUtil {
	public static String getTimestamp() {
	    return new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()).substring(0, 12);
	}
}
