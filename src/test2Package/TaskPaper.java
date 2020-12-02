package test2Package;

public class TaskPaper {

    private String label;
    private int duration;
    private boolean alarmOn;

    public TaskPaper() {
        label = null;
        duration = 0;
    }

    public void setAlarmOn(boolean alarmOn) {
        this.alarmOn = alarmOn;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isAlarmOn() {
        return alarmOn;
    }

    public int getDuration() {
        return duration;
    }

    public String getLabel() {
        return label;
    }

}
