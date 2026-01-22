import java.util.Scanner;

public class Peggy {
    public static void main(String[] args) {
        String name = "Peggy";
        System.out.println("Hello I'm " + name + "\n" + "What can I do for you today?");
        while(true) {
            Scanner input = new Scanner(System.in);
            String inputName = input.nextLine();
            if (inputName.equals("bye")) {
                System.out.println("Goodbye!");
                break;
            }
        }



    }
}
