package peggy.task;
/**
 * Priority levels for tasks.
 */
public enum Priority {
    NONE, LOW, MED, HIGH;

    /**
     * Parses user input token into a Priority.
     *
     * @param raw Raw user token (e.g. "high", "med", "low", "none").
     * @return Priority value.
     */
    public static Priority parse(String raw) {
        if (raw == null) {
            return NONE;
        }

        switch (raw.trim().toLowerCase()) {
            case "none":
            case "0":
                return NONE;
            case "low":
            case "3":
                return LOW;
            case "med":
            case "medium":
            case "2":
                return MED;
            case "high":
            case "1":
                return HIGH;
            default:
                throw new IllegalArgumentException("Priority must be one of: high, med, low, none.");
        }
    }

    /**
     * Returns the token to store in the save file.
     *
     * @return Storage token.
     */
    public String toStorageToken() {
        return this.name();
    }
}
