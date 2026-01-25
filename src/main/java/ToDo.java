public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toFileString() {
        return "T | " + (isDone() ? 1 : 0) + " | " + getDescription();
    }

    public String toString() {
        return "[T]" + super.toString();
    }
}