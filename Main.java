import java.util.Scanner;

public class Main {
    
    private static Scanner kb = new Scanner(System.in);

    public static void main(String[] args) {
        boolean run = true;
        User user = new User();
        PSS pss = new PSS();
        char option;

        // Keeps the entire program running
        while(run) {
            // Keeps the login/signup menu running
            while(user.username == null && run) {
                option = getStartOption();

                switch(option) {
                    case'a':
                        user.login();
                        break;
                    case'b':
                        user.signup();
                        break;
                    case'c':
                        run = false;
                        break;
                }
            }

            if(user.username != null && run) {
                pss.loadSchedule(user.username);
            }

            // Keeps the pss actions menu running
            while(user.username != null && run) {
                option = getActionOption();    

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
                        pss.writeDaySchedule(user.username);
                        break;
                    case'k':
                        pss.writeWeekSchedule(user.username);
                        break;
                    case'l':
                        pss.writeMonthSchedule(user.username); 
                        break;
                    case'm':
                        pss = new PSS();
                        user = new User();
                        break;
                    case'n':
                        pss.exitWriteSchedule(user.username);
                        run = false;
                        break;
                }
            }
        }
    }

    /**
     * Gets user input for pss action
     * @return
     */
    private static char getActionOption() {
        String option;
        String prompt = "\nChoose PSS action.\n" +
            "\ta) Create a task\n\tb) View a task\n" +
            "\tc) Delete a task\n\td) Edit a task\n" +
            "\te) Write schedule to a file\n\tf) Read schedule from a file\n" +
            "\tg) View schedule for a day\n\th) View schedule for a week\n" +
            "\ti) View schedule for a month\n\tj) Write schedule for a day\n" +
            "\tk) Write schedule for a week\n\tl) Write schedule for a month\n" +
            "\tm) Logout\n\tn) Exit\nEnter option: ";

        do {
            System.out.print(prompt);
            option = kb.nextLine();

            if(!isOptionValid(option, 'a', 'n'))
                System.out.println("\nInvalid input.");
        } while(!isOptionValid(option, 'a', 'n'));

        return option.toLowerCase().charAt(0);
    }

    /**
     * Gets user input for login option
     * @return
     */
    private static char getStartOption() {
        String option;

        do {
            System.out.print("Welcome to PSS!\n\ta) Login\n\tb) Signup\n\tc) Exit\nEnter option: ");
            option = kb.nextLine();

            if(!isOptionValid(option, 'a', 'c'))
                System.out.println("\nInvalid input.\n");
        } while(!isOptionValid(option, 'a', 'c'));

        return option.toLowerCase().charAt(0);
    }

    /**
     * Checks if the input is valid within the given bound
     * @param option
     * @param lower
     * @param upper
     * @return
     */
    private static boolean isOptionValid(String option, char lower, char upper) {
        if(option.length() != 1)
            return false;

        char op = option.toLowerCase().charAt(0);

        if(op > upper || op < lower)
            return false;

        return true;
    }

}
