import java.util.ArrayList;
import java.util.Scanner;

public class Peggy {
    private static final String LINE = "---------------------------------------------";

    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();

        System.out.println(LINE);
        System.out.println("Hello! I'm Peggy\nWhat can I do for you?");
        System.out.println(LINE);

        Scanner sc = new Scanner(System.in);

        while (true) {
            String input = sc.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println(LINE);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            }

            if (input.equals("list")) {
                System.out.println(LINE);
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + "." + tasks.get(i).toString());
                }
                System.out.println(LINE);
                continue;
            }

            if (input.startsWith("mark ")) {
                try {
                    int idx = parseIndex(input); // 0-based
                    Task t = tasks.get(idx);
                    t.markAsDone();

                    System.out.println(LINE);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + t.toString());
                    System.out.println(LINE);
                } catch (Exception e) {
                    System.out.println(LINE);
                    System.out.println("Please give a valid task number, e.g. mark 2");
                    System.out.println(LINE);
                }
                continue;
            }

            if (input.startsWith("unmark ")) {
                try {
                    int idx = parseIndex(input); // 0-based
                    Task t = tasks.get(idx);
                    t.markAsNotDone();

                    System.out.println(LINE);
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + t.toString());
                    System.out.println(LINE);
                } catch (Exception e) {
                    System.out.println(LINE);
                    System.out.println("Please give a valid task number, e.g. unmark 2");
                    System.out.println(LINE);
                }
                continue;
            } else {
                System.out.println(LINE);
                System.out.println("Got it. I've added this task:");
                if (input.startsWith("todo ")) {
                    Task t = new ToDo(getDesc(input));
                    tasks.add(t);
                    System.out.println("  " + t.toString());
                } else if (input.startsWith("deadline ")) {
                    Task t = new Deadline(getDesc(input), getBy(input));
                    tasks.add(t);
                    System.out.println("  " + t.toString());
                } else if (input.startsWith("event ")) {
                    Task t = new Event(getDesc(input), getFromTime(input), getToTime(input));
                    tasks.add(t);
                    System.out.println("  " + t.toString());
                }
                System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                System.out.println(LINE);
            }
        }
        sc.close();
    }
    private static int parseIndex(String input) {
        String[] parts = input.split("\\s+");   // ["mark","2"]
        int taskNum = Integer.parseInt(parts[1]); // 2
        return taskNum - 1; // convert to 0-based
    }
    private static String getBy(String input) {
        String rest = input.substring("deadline".length()).trim();
        String[] parts = rest.split(" /by ", 2);
        return parts[1].trim();
    }
    private static String getDesc(String input) {
        return input.split(" ", 2)[1];
    }
    private static String getFromTime(String input) {
        String rest = input.substring("event".length()).trim();
        String[] parts = rest.split(" /from ", 2);
        return parts[1].trim();
    }
    private static String getToTime(String input) {
        String rest = input.substring("event".length()).trim();
        String[] parts = rest.split(" /to ", 2);
        return parts[1].trim();
    }
}