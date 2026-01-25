package peggy;
import java.util.Scanner;
import peggy.task.*;

public class UI {
    private static final String LINE = "---------------------------------------------";
    private final Scanner sc = new Scanner(System.in);

    public void showWelcome() {
        println(LINE);
        println("Hello! I'm Peggy");
        println("What can I do for you?");
        println(LINE);
    }

    public void showBye() {
        println(LINE);
        println("Bye. Hope to see you again soon!");
        println(LINE);
    }

    public String readCommand() {
        return sc.nextLine().trim();
    }

    public void showError(String msg) {
        println(LINE);
        println(msg);
        println(LINE);
    }

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

    public void showAdded(Task t, int size) {
        println(LINE);
        println("Got it. I've added this task:");
        println("  " + t);
        println("Now you have " + size + " tasks in the list.");
        println(LINE);
    }

    public void showDeleted(Task t, int size) {
        println(LINE);
        println("Noted. I've removed this task:");
        println("  " + t);
        println("Now you have " + size + " tasks in the list.");
        println(LINE);
    }

    public void showFindResults(TaskList matches) {
        println(LINE);
        println("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            // match sample format: 1.[T][X] ...
            println((i + 1) + "." + matches.get(i));
        }
        println(LINE);
    }

    public void showMarked(Task t) {
        println(LINE);
        println("Nice! I've marked this task as done:");
        println("  " + t);
        println(LINE);
    }

    public void showUnmarked(Task t) {
        println(LINE);
        println("OK, I've marked this task as not done yet:");
        println("  " + t);
        println(LINE);
    }

    public void close() {
        sc.close();
    }

    private void println(String s) {
        System.out.println(s);
    }
}
