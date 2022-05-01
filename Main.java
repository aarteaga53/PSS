import java.util.Scanner;

public class Main {
    
    private static Scanner kb = new Scanner(System.in);

    public static void main(String[] args) {
        boolean run = true;
        User user = new User();
        PSS pss = new PSS();
        char option;

        while(user.username == null) {
            option = getStartOption();

            switch(option) {
                case'a':
                    user.login();
                    break;
                case'b':
                    user.signup();
                    break;
                case'c':
                    System.exit(0);
                    break;
            }
        }

        while(run) {
            option = getOption();    

            switch(option) {
                case'a':
                    pss.createTask();
                    break;
                case'b':
                    System.out.print("Enter task name: ");
                    pss.viewTask(kb.nextLine());
                    break;
                case'c':
                    System.out.print("Enter task name: ");
                    pss.deleteTask(kb.nextLine());
                    break;
                case'd':
                    System.out.print("Enter task name: ");
                    pss.editTask(kb.nextLine());
                    break;
                case'e':
                    pss.writeSchedule(user.username);
                    break;
                case'f':
                    pss.readSchedule(user.username);
                    break;
                case'g':
                    pss.daySchedule();
                    break;
                case'h':
                    pss.weekSchedule();
                    break;
                case'i':
                    pss.monthSchedule();
                    break;
                case'j':
                    run = false;
                    break;
            }
        }
    }

    private static char getOption() {
        char option;
        String prompt = "\nChoose PSS action.\n" +
            "\ta) Create a task\n\tb) View a task\n" +
            "\tc) Delete a task\n\td) Edit a task\n" +
            "\te) Write schedule to a file\n\tf) Read schedule from a file\n" +
            "\tg) View schedule for a day\n\th) View schedule for a week\n" +
            "\ti) View schedule for a month\n\tj) Exit\nEnter option: ";

        do {
            System.out.print(prompt);
            option = kb.nextLine().toLowerCase().charAt(0);

            if(option > 'j' || option < 'a')
                System.out.println("\nInvalid input.\n");
        } while(option > 'j' && option < 'a');

        return option;
    }

    private static char getStartOption() {
        char option;

        do {
            System.out.print("Welcome to PSS!\n\ta) Login\n\tb) Signup\n\tc) Exit\nEnter option: ");
            option = kb.nextLine().toLowerCase().charAt(0);

            if(option > 'c' || option < 'a')
                System.out.println("\nInvalid input.\n");
        } while(option > 'c' && option < 'a');

        return option;
    }

}
