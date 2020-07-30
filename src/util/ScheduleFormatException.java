package util;

/**
 * Exception for malformed schedule entries
 */
public class ScheduleFormatException extends Exception{
    public ScheduleFormatException(String message) {
        super(message);
    }
}
