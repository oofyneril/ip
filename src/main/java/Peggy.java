import java.util.ArrayList;
import java.util.Scanner;

public class Peggy {
    private static final String LINE = "---------------------------------------------";

    public static void main(String[] args) {
        Storage storage = new Storage("data/peggy.txt");
        ArrayList<Task> tasks;
        try {
            tasks = storage.load();
        } catch (Exception e) {
            tasks = new ArrayList<>();
        }

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
                        int idx = Parser.parseIndex(input, tasks.size(), "mark");
                        Task t = tasks.get(idx);
                        t.markAsDone();
                        saveQuietly(storage, tasks);

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
                        int idx = Parser.parseIndex(input, tasks.size(), "unmark");
                        Task t = tasks.get(idx);
                        t.markAsNotDone();
                        saveQuietly(storage, tasks);

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
                        int idx = Parser.parseIndex(input, tasks.size(), "delete");
                        Task t = tasks.get(idx);
                        tasks.remove(idx);
                        saveQuietly(storage, tasks);
                        printDeleted(t, tasks.size());
                    } catch (IllegalArgumentException e) {
                        printError(e.getMessage());
                    } catch (Exception e) {
                        printError("Please give a valid task number, e.g. delete 2");
                    }
                    break;

                case TODO:
                    try {
                        String desc = Parser.parseTodoDesc(input);
                        Task t = new ToDo(desc);
                        tasks.add(t);
                        saveQuietly(storage, tasks);
                        printAdded(t, tasks.size());
                    } catch (IllegalArgumentException e) {
                        printError(e.getMessage());
                    }
                    break;

                case DEADLINE:
                    try {
                        String[] dl = Parser.parseDeadline(input);
                        Task t = new Deadline(dl[0], dl[1]);
                        tasks.add(t);
                        saveQuietly(storage, tasks);
                        printAdded(t, tasks.size());
                    } catch (IllegalArgumentException e) {
                        printError(e.getMessage());
                    }
                    break;

                case EVENT:
                    try {
                        String[] ev = Parser.parseEvent(input);
                        Task t = new Event(ev[0], ev[1], ev[2]);
                        tasks.add(t);
                        saveQuietly(storage, tasks);
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

    private static void saveQuietly(Storage storage, ArrayList<Task> tasks) {
        try {
            storage.save(tasks);
        } catch (Exception e) {
            printError("OOPS!!! I couldn't save your tasks to disk.");
        }
    }

}
