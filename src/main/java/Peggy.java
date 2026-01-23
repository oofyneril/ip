import java.util.ArrayList;
import java.util.Scanner;

public class Peggy {
    private static final String LINE = "---------------------------------------------";

    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();

        System.out.println(LINE);
        System.out.println("Hello! I'm Peggy");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        Scanner sc = new Scanner(System.in);

        while (true) {
            String input = sc.nextLine().trim();

            if (input.isBlank()) {
                printError("OOPS!!! I don't know what that means :-(");
                continue;
            }

            String[] parts = input.split("\\s+", 2);
            String cmdWord = parts[0];
            CommandType cmd = CommandType.from(cmdWord);

            switch (cmd) {
                case BYE:
                    System.out.println(LINE);
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(LINE);
                    sc.close();
                    return;

                case LIST:
                    System.out.println(LINE);
                    if (tasks.isEmpty()) {
                        System.out.println("Your list is empty.");
                    } else {
                        System.out.println("Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println((i + 1) + ". " + tasks.get(i));
                        }
                    }
                    System.out.println(LINE);
                    break;

                case MARK:
                    try {
                        int idx = parseIndex(input, tasks.size(), "mark");
                        Task t = tasks.get(idx);
                        t.markAsDone();

                        System.out.println(LINE);
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + t);
                        System.out.println(LINE);
                    } catch (IllegalArgumentException e) {
                        printError(e.getMessage());
                    } catch (Exception e) {
                        printError("Please give a valid task number, e.g. mark 2");
                    }
                    break;

                case UNMARK:
                    try {
                        int idx = parseIndex(input, tasks.size(), "unmark");
                        Task t = tasks.get(idx);
                        t.markAsNotDone();

                        System.out.println(LINE);
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + t);
                        System.out.println(LINE);
                    } catch (IllegalArgumentException e) {
                        printError(e.getMessage());
                    } catch (Exception e) {
                        printError("Please give a valid task number, e.g. unmark 2");
                    }
                    break;

                case DELETE:
                    try {
                        int idx = parseIndex(input, tasks.size(), "delete");
                        Task t = tasks.get(idx);
                        tasks.remove(idx);
                        printDeleted(t, tasks.size());
                    } catch (IllegalArgumentException e) {
                        printError(e.getMessage());
                    } catch (Exception e) {
                        printError("Please give a valid task number, e.g. delete 2");
                    }
                    break;

                case TODO:
                    try {
                        String desc = parseTodoDesc(input);
                        Task t = new ToDo(desc);
                        tasks.add(t);
                        printAdded(t, tasks.size());
                    } catch (IllegalArgumentException e) {
                        printError(e.getMessage());
                    }
                    break;

                case DEADLINE:
                    try {
                        String[] dl = parseDeadline(input);
                        Task t = new Deadline(dl[0], dl[1]);
                        tasks.add(t);
                        printAdded(t, tasks.size());
                    } catch (IllegalArgumentException e) {
                        printError(e.getMessage());
                    }
                    break;

                case EVENT:
                    try {
                        String[] ev = parseEvent(input);
                        Task t = new Event(ev[0], ev[1], ev[2]);
                        tasks.add(t);
                        printAdded(t, tasks.size());
                    } catch (IllegalArgumentException e) {
                        printError(e.getMessage());
                    }
                    break;

                default:
                    printError("OOPS!!! I don't know what that means :-(");
                    break;
            }
        }
    }

    // ---------------- Helpers ----------------

    private static int parseIndex(String input, int size, String cmd) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new IllegalArgumentException("Please give a task number, e.g. " + cmd + " 2");
        }
        int idx = Integer.parseInt(parts[1]) - 1;
        if (idx < 0 || idx >= size) {
            throw new IllegalArgumentException("Task number out of range.");
        }
        return idx;
    }

    private static String parseTodoDesc(String input) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The description of a todo cannot be empty.");
        }
        return parts[1].trim();
    }

    private static String[] parseDeadline(String input) {
        String rest = input.substring("deadline".length()).trim();
        String[] parts = rest.split(" /by ", 2);

        if (parts.length < 2) {
            throw new IllegalArgumentException("Deadline format: deadline <desc> /by <when>");
        }

        String desc = parts[0].trim();
        String by = parts[1].trim();

        if (desc.isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The description of a deadline cannot be empty.");
        }
        if (by.isBlank()) {
            throw new IllegalArgumentException("OOPS!!! The date of a deadline cannot be empty.");
        }

        return new String[] { desc, by };
    }

    private static String[] parseEvent(String input) {
        String rest = input.substring("event".length()).trim();

        String[] p1 = rest.split(" /from ", 2);
        if (p1.length < 2) {
            throw new IllegalArgumentException("Event format: event <desc> /from <start> /to <end>");
        }

        String desc = p1[0].trim();

        String[] p2 = p1[1].split(" /to ", 2);
        if (p2.length < 2) {
            throw new IllegalArgumentException("Event format: event <desc> /from <start> /to <end>");
        }

        String from = p2[0].trim();
        String to = p2[1].trim();

        if (desc.isBlank()) throw new IllegalArgumentException("OOPS!!! The description of a event cannot be empty.");
        if (from.isBlank()) throw new IllegalArgumentException("OOPS!!! The 'from time' of a event cannot be empty.");
        if (to.isBlank()) throw new IllegalArgumentException("OOPS!!! The 'to time' of a event cannot be empty.");

        return new String[] { desc, from, to };
    }

    private static void printAdded(Task t, int size) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + size + " tasks in the list.");
        System.out.println(LINE);
    }

    private static void printDeleted(Task t, int size) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + size + " tasks in the list.");
        System.out.println(LINE);
    }

    private static void printError(String msg) {
        System.out.println(LINE);
        System.out.println(msg);
        System.out.println(LINE);
    }
}
