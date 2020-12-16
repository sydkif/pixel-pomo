package test2Package;

public class TaskPaper {

    private String label;
    private int duration;

    public TaskPaper() {
        label = null;
        duration = 0;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getDuration() {
        return duration;
    }

    public String getLabel() {
        return label;
    }

}
