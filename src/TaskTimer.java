// FILENAME: TaskTimer.java

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TaskTimer class store the data of task duration, timer status, alarm status,
 * beeping status and Java ScheduledExecutorService.
 */
public class TaskTimer {

    private int duration; // Timer duration
    private boolean timerOn; // Timer status
    private boolean alarmOn; // Alarm status
    private boolean beeping; // Beeping status
    private ScheduledExecutorService scheduler;

    /**
     * The no-arg constructor initializes duration as 0.
     */
    public TaskTimer() {
        duration = 0;
    }

    /**
     * This constructor initializes duration with dur*60 tu covert the minute(s)
     * value into seconds.
     * 
     * @param dur duration in minute(s)
     */
    public TaskTimer(int dur) {
        duration = dur * 60;
    }

    /**
     * The start method will start the timer that will refresh every second.
     */
    public void start() {
        scheduler = Executors.newScheduledThreadPool(1);
        Runnable refresh = new Runnable() {
            public void run() {
                --duration;
                setTimerOn(true);
                if (duration == 0) {
                    System.out.println("Task Complete");
                    // TODO Add history log here
                    if (isAlarmOn())
                        setBeeping(true);
                    scheduler.shutdown();
                    setTimerOn(false);
                }
            }
        };
        scheduler.scheduleAtFixedRate(refresh, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * The stop method will stop the timer.
     */
    public void stop() {
        scheduler.shutdown();
        setTimerOn(false);
    }

    /**
     * The getLabel method returns timer duration in formatted string.
     * 
     * @return string of time left in minutes and seconds.
     */
    public String getLabel() {
        int min = duration / 60;
        int sec = duration % 60;

        return "" + min + "m " + sec + "s";
    }

    /**
     * The setDuration method sets the timer duration.
     * 
     * @param duration in seconds
     */
    public void setDuration(int duration) {
        this.duration = duration * 60 + 1;
    }

    /**
     * The setTimerOn method sets the timer status.
     * 
     * @param timerOn status (true/false)
     */
    public void setTimerOn(boolean timerOn) {
        this.timerOn = timerOn;
    }

    /**
     * The setAlarmOn method sets the alarm status.
     * 
     * @param alarmOn status (true/false)
     */
    public void setAlarmOn(boolean alarmOn) {
        this.alarmOn = alarmOn;
    }

    /**
     * The setBeeping method sets the beeping status.
     * 
     * @param beeping status (true/false)
     */
    public void setBeeping(boolean beeping) {
        this.beeping = beeping;
    }

    /**
     * The getDuration method returns duration.
     * 
     * @return returns duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * The isTimerOn method returns timer status.
     * 
     * @return timer status
     */
    public boolean isTimerOn() {
        return timerOn;
    }

    /**
     * The isAlarmOn method returns alarm status.
     * 
     * @return alarm status
     */
    public boolean isAlarmOn() {
        return alarmOn;
    }

    /**
     * The isBeeping method returns beeping status.
     * 
     * @return beeping status
     */
    public boolean isBeeping() {
        return beeping;
    }

}
