import java.util.ArrayList;
import java.util.Scanner;

public class Peggy {
    public static void main(String[] args) {
        ArrayList<String> txts = new ArrayList<>();
        System.out.println("---------------------------------------------");
        System.out.println("Hello! I'm Peggy\n" + "What can I do for you?");
        System.out.println("---------------------------------------------");
        while(true) {
            Scanner input = new Scanner(System.in);
            String inputName = input.nextLine();
            if (inputName.equals("bye")) {
                System.out.println("---------------------------------------------");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("---------------------------------------------");
                break;
            } else if (inputName.equals("list")) {
                int count = 0;
                System.out.println("---------------------------------------------");
                for (String s : txts) {
                    count+=1;
                    System.out.println(count + ". " + s);
                }
                System.out.println("---------------------------------------------");
            } else {
                txts.add(inputName);
                System.out.println("---------------------------------------------");
                System.out.println("added: " + inputName);
                System.out.println("---------------------------------------------");
            }
        }
    }
}
