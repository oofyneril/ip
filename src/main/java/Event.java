public class Event extends Task {
    private final String fromTime;
    private final String toTime;

    public Event(String description, String fromTime, String toTime) {
        super(description);
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public String getFromTime() {
        return fromTime;
    }
    public String getToTime() {
        return toTime;
    }

    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.fromTime + " to: " + this.toTime + ")";
    }
}
