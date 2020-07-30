package parser;

import models.Schedule;
import util.ScheduleFormatException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ScheduleParser {
    public static Schedule loadSchedule(String fileName) throws IOException, ScheduleFormatException {
        Schedule schedule = new Schedule();
        BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));

        // Read each line
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(",");
            // If the task is valid, add it to schedule
            if (validateTask(tokens)) {
                schedule.addTask(Integer.parseInt(tokens[0]), tokens[1].trim(), tokens[2].trim());
            }
            else {
                throw new ScheduleFormatException("Schedule file not formatted properly!");
            }
        }

        return schedule;
    }

    /**
     * Validate a Schedule Task
     * @param tokens String array of tokens
     * @return True is task is valid, else false
     */
    private static boolean validateTask(String[] tokens) {
        // Check that there are exactly 3 tokens and that the first one is a number
        return tokens.length == 3 && tokens[0].chars().allMatch(Character::isDigit);

        // Explanation for tokens[0].chars().allMatch(Character::isDigit):
        // Get first token from array with [0], convert to a [Stream] with `chars()`, and then check
        // that all the chars are digits with `allMatch(Character:isDigit)`.
    }
}
