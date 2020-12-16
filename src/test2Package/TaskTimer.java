package test2Package;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.Toolkit;

public class TaskTimer {

    private int duration;
    private boolean on;
    private boolean alarmOn;
    private ScheduledExecutorService scheduler;

    public TaskTimer() {
        duration = 0;
    }

    public TaskTimer(int dur) {
        duration = dur * 60;
    }

    public void start() {

        scheduler = Executors.newScheduledThreadPool(1);
        Runnable refresh = new Runnable() {
            public void run() {
                --duration;
                setOn(true);
                if (duration == 0) {
                    System.out.println("Task Complete");
                    if (isAlarmOn())
                        Toolkit.getDefaultToolkit().beep();
                    scheduler.shutdown();
                    setOn(false);
                }
            }
        };
        scheduler.scheduleAtFixedRate(refresh, 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdown();
        setOn(false);
    }

    public String getLabel() {
        int min = duration / 60;
        int sec = duration % 60;

        return "" + min + "m " + sec + "s";
    }

    public void setDuration(int duration) {
        this.duration = duration * 60 + 1;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public void setAlarmOn(boolean alarmOn) {
        this.alarmOn = alarmOn;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isOn() {
        return on;
    }

    public boolean isAlarmOn() {
        return alarmOn;
    }

}
