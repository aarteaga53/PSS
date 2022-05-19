import java.util.Scanner;

public class Main {
    
    private static Scanner kb = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        String loginPrompt = "\nChoose start option.\n\ta) Login\n\tb) Signup\n\tc) Exit\nEnter option: ";
        String actionPrompt = "\nChoose PSS action.\n" +
            "\ta) Create a task\n\tb) View a task\n\tc) Delete a task\n\td) Edit a task\n" +
            "\te) Write schedule to a file\n\tf) Read schedule from a file\n" +
            "\tg) View schedule for a day\n\th) View schedule for a week\n" +
            "\ti) View schedule for a month\n\tj) Write schedule for a day\n" +
            "\tk) Write schedule for a week\n\tl) Write schedule for a month\n" +
            "\tm) Logout\n\tn) Exit\nEnter option: ";
        char option;

        System.out.println("Welcome to PSS!");

        // Keeps the entire program running
        while(running) {
            User user = new User();
            PSS pss = new PSS();

            // Keeps the login/signup menu running
            while(user.getUsername() == null && running) {
                option = getOption('a', 'c', loginPrompt);

                switch(option) {
                    case'a':
                        user.login();
                        break;
                    case'b':
                        user.signup();
                        break;
                    case'c':
                    running = false;
                        break;
                }
            }

            if(user.getUsername() != null && running) {
                pss.loadSchedule(user.getUsername());
            }

            // Keeps the pss actions menu running
            while(user.getUsername() != null && running) {
                option = getOption('a', 'n', actionPrompt);    

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
                        pss.writeSchedule(user.getUsername());
                        break;
                    case'f':
                        pss.readSchedule(user.getUsername());
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
                        pss.writeDaySchedule(user.getUsername());
                        break;
                    case'k':
                        pss.writeWeekSchedule(user.getUsername());
                        break;
                    case'l':
                        pss.writeMonthSchedule(user.getUsername()); 
                        break;
                    case'm':
                        pss.exitWriteSchedule(user.getUsername());
                        user.setUsername(null);
                        break;
                    case'n':
                        pss.exitWriteSchedule(user.getUsername());
                        running = false;
                        break;
                }
            }
        }
    }

    /**
     * Gets user input for option they would like to perform
     * @param lower
     * @param upper
     * @param prompt
     * @return
     */
    private static char getOption(char lower, char upper, String prompt) {
        String option;

        do {
            System.out.print(prompt);
            option = kb.nextLine();

            if(!isOptionValid(option, lower, upper))
                System.out.println("\nInvalid input.");
        } while(!isOptionValid(option, lower, upper));

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
