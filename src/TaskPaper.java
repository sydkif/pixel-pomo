import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// FILENAME: TaskPaper.java

/**
 * The TaskPaper class store data of task label in string, and task duration in
 * minutes (int)
 */
public class TaskPaper {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy | HH:mm");
    LocalDateTime now = LocalDateTime.now();
    private String label; // Task label
    private int duration; // Task duration in minute(s)

    /**
     * The no-arg constructor initializes label with null and duration with 0.
     */
    public TaskPaper() {
        label = null;
        duration = 0;
    }

    /**
     * The setDuration method sets task duration in minute(s).
     * 
     * @param duration The duration in minute(s)
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * The setLabel method sets the label string.
     * 
     * @param label The task label string
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * The getDuration method returns the value of duration in minute(s).
     * 
     * @return value of duration in minute(s)
     */
    public int getDuration() {
        return duration;
    }

    /**
     * The getLabel method returns the string for task label.
     * 
     * @return task string
     */
    public String getLabel() {
        return label;
    }

    public String log() {
        return dtf.format(now) + " | " + label + " for " + duration + " minute(s)";
    }

}
