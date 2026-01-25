package peggy;
import java.util.Scanner;
import peggy.task.*;

public class UI {
    /**
     * Handles all interactions with the user.
     * <p>
     *     Responsible for displaying messages and reading user input from standard input.
     *     Does not contain business logic (e.g., parsing, storage, task manipulation).
     * </p>
     */
    private static final String LINE = "---------------------------------------------";
    private final Scanner sc = new Scanner(System.in);
    /**
     * Displays the welcome message when the program starts.
     */
    public void showWelcome() {
        println(LINE);
        println("Hello! I'm Peggy");
        println("What can I do for you?");
        println(LINE);
    }
    /**
     * Displays the goodbye message when the program exits.
     */
    public void showBye() {
        println(LINE);
        println("Bye. Hope to see you again soon!");
        println(LINE);
    }

    public String readCommand() {
        return sc.nextLine().trim();
    }
    /**
     * Displays an error message in the standard error format.
     *
     * @param msg The error message to display.
     */
    public void showError(String msg) {
        println(LINE);
        println(msg);
        println(LINE);
    }
    /**
     * Displays all tasks currently stored in the task list.
     *
     * @param tasks The task list to display.
     */
    public void showList(TaskList tasks) {
        println(LINE);
        if (tasks.isEmpty()) {
            println("Your list is empty.");
        } else {
            println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                println((i + 1) + ". " + tasks.get(i));
            }
        }
        println(LINE);
    }
    /**
     * Displays a confirmation message after adding a task.
     *
     * @param t The task that was added.
     * @param size The new size of the task list.
     */
    public void showAdded(Task t, int size) {
        println(LINE);
        println("Got it. I've added this task:");
        println("  " + t);
        println("Now you have " + size + " tasks in the list.");
        println(LINE);
    }
    /**
     * Displays a confirmation message after deleting a task.
     *
     * @param t The task that was removed.
     * @param size The new size of the task list.
     */
    public void showDeleted(Task t, int size) {
        println(LINE);
        println("Noted. I've removed this task:");
        println("  " + t);
        println("Now you have " + size + " tasks in the list.");
        println(LINE);
    }
    /**
     * Displays a confirmation message after marking a task as done.
     *
     * @param t The task that was marked as done.
     */
    public void showMarked(Task t) {
        println(LINE);
        println("Nice! I've marked this task as done:");
        println("  " + t);
        println(LINE);
    }
    /**
     * Displays a confirmation message after unmarking a task (mark as not done).
     *
     * @param t The task that was unmarked.
     */
    public void showUnmarked(Task t) {
        println(LINE);
        println("OK, I've marked this task as not done yet:");
        println("  " + t);
        println(LINE);
    }
    /**
     * Closes any resources held by the UI (e.g., Scanner).
     */
    public void close() {
        sc.close();
    }

    private void println(String s) {
        System.out.println(s);
    }
}
