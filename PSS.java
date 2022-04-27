import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class PSS {
    
    private Scanner kb = new Scanner(System.in);
    ArrayList<Task> tasks;
    DataFile dataFile;

    public PSS() {
        tasks = new ArrayList<>();
        dataFile = new DataFile();
    }

    public void createTask() {
        chooseType();
    }

    /**
     * Creates a task based on a user input type
     */
    private void chooseType() {
        String option;
        Task newTask;
        String name;
        String startTime;
        String duration;
        String prompt = "Choose task type:\n" +
                        "\tClass\n\tStudy\n\tSleep\n\tExercise\n\tWork\n\tMeal\n" +
                        "\tVisit\n\tShopping\n\tAppointment\n" +
                        "\tCancellation\nEnter option: ";

        // Gets user input for task type
        do {
            System.out.print(prompt);
            option = kb.nextLine();
            newTask = new Task(option);

            if(!newTask.isRecurring() && !newTask.isTransient() && !newTask.isAnti()) {
                System.out.println("\nInvalid input.\n");
            }

        } while(!newTask.isRecurring() && !newTask.isTransient() && !newTask.isAnti());

        // Gets user input for task name
        System.out.print("Enter task name: ");
        name = kb.nextLine();

        // checks if the name is unique
        if(!uniqueName(name)) {
            System.out.println("Task name is already taken.");
            return;
        }

        // gets user input for start time
        do {
            System.out.print("Enter task start time(hh:mm am/pm): ");
            startTime = kb.nextLine();
        } while(!isStartTimeCorrect(startTime));

        // gets user input for duration
        do {
            System.out.print("Enter task duration(hh:mm): ");
            duration = kb.nextLine();
        } while(isDurationCorrect(duration));
        
        // checks what type of task is going to be created
        if(newTask.isRecurring()) {
            createRecurring(name, option, timeConversion(startTime), durationConversion(duration));
        }
        else if(newTask.isTransient()) {
            createTransient(name, option, timeConversion(startTime), durationConversion(duration));
        }
        else {
            createAnti(name, option, timeConversion(startTime), durationConversion(duration));
        }
    }

    /**
     * Creates an Anti-Task
     * @param name      
     * @param type      
     * @param startTime 
     * @param duration
     */
    private void createAnti(String name, String type, float startTime, float duration) {
        String date;

        // gets user input for date
        do {
            System.out.print("Enter task date(mm/dd/yyyy): ");
            date = kb.nextLine();
        } while(!isDateCorrect(date));
        
        AntiTask newTask = new AntiTask(name, type, startTime, duration, dateConversion(date));
        tasks.add(newTask);
    }

    /**
     * Creates a Transient Task
     * @param name
     * @param type
     * @param startTime
     * @param duration
     */
    private void createTransient(String name, String type, float startTime, float duration) {
        String date;

        // gets uer input for date
        do {
            System.out.print("Enter task date(mm/dd/yyyy): ");
            date = kb.nextLine();
        } while(!isDateCorrect(date));

        TransientTask newTask = new TransientTask(name, type, startTime, duration, dateConversion(date));
        tasks.add(newTask);
    }

    /**
     * Creates a Recurring Task
     * @param name
     * @param type
     * @param startTime
     * @param duration
     */
    private void createRecurring(String name, String type, float startTime, float duration) {
        String startDate;
        String endDate;
        int frequency;

        // gets user input for start date
        do {
            System.out.print("Enter task start date(mm/dd/yyyy): ");
            startDate = kb.nextLine();
        } while(!isDateCorrect(startDate));
        
        // gets user input for end date
        do {
            System.out.print("Enter task end date(mm/dd/yyyy): ");
            endDate = kb.nextLine();
        } while(!isDateCorrect(startDate));

        // gets user input for frequency
        do {
            System.out.print("Enter task frequency(1/7): ");
            frequency = kb.nextInt();
        } while(frequency != 7 && frequency != 1);
        

        RecurringTask newTask = new RecurringTask(name, type, startTime, duration, dateConversion(startDate), dateConversion(endDate), frequency);
        tasks.add(newTask);
    }

    /**
     * Checks if the given duration string is formatted correctly
     * @param duration
     * @return
     */
    private boolean isDurationCorrect(String duration) {
        int hour = 0;
        int minute = 0;

        // length must be 4 or 5
        if(duration.length() > 5 || duration.length() < 4)
            return false;

        try {
            hour = Integer.parseInt(duration.substring(0, duration.indexOf(":"))); // the first two chars
            minute = Integer.parseInt(duration.substring(duration.indexOf(":") + 1, duration.indexOf(" "))); // the last two chars
        } catch(NumberFormatException e) {
            return false;
        }

        if(hour > 12 || hour < 1)
            return false;
        else if(minute < 0 || minute > 59)
            return false;

        return true;
    }

    /**
     * Checks if the given start time string is formateed correctly
     * @param startTime
     * @return
     */
    private boolean isStartTimeCorrect(String startTime) {
        int hour = 0;
        int minute = 0;

        // length must be 7 or 8
        if(startTime.length() > 8 || startTime.length() < 7)
            return false;

        try {
            hour = Integer.parseInt(startTime.substring(0, startTime.indexOf(":"))); // first two chars
            minute = Integer.parseInt(startTime.substring(startTime.indexOf(":") + 1, startTime.indexOf(" "))); // second two chars
        } catch(NumberFormatException e) {
            return false;
        }
        
        String time = startTime.substring(startTime.indexOf(" ") + 1); // the last two chars

        if(hour > 12 || hour < 1)
            return false;
        else if(minute < 0 || minute > 59)
            return false;
        else if(!time.equals("am") && !time.equals("pm"))
            return false;

        return true;
    }

    /**
     * Checks if the given date string is formatted correctly
     * @param date
     * @return
     */
    private boolean isDateCorrect(String date) {
        int month = 0;
        int day = 0;
        int year = 0;

        // length must be 10
        if(date.length() < 10 || date.length() > 10) 
            return false;


        try {
            month = Integer.parseInt(date.substring(0, 2));
            day = Integer.parseInt(date.substring(3, 5));
            year = Integer.parseInt(date.substring(7, 11));
        } catch(NumberFormatException e) {
            return false;
        }

        if(month > 12 || month < 1) 
            return false;
        else if(day < 1)
            return false;
        else if((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day > 31)
            return false;
        else if((month == 4 || month == 6 || month == 9 || month == 11) && day > 30)
            return false;
        else if(month == 2 && day > 28)
            return false;
        else if(year < 0)
            return false;

        return true;
    }

    /**
     * Converts the string date into int representation
     * @param date
     * @return
     */
    private int dateConversion(String date) {
        String[] split = date.split("/");
        return Integer.parseInt(split[2] + split[0] + split[1]);
    }

    /**
     * Converts the string duration into float representation
     * @param duration
     * @return
     */
    private float durationConversion(String duration) {
        String[] split = duration.split(":");
        return Float.parseFloat(split[0]) + minute(Float.parseFloat(split[1]));
    }

    /**
     * Converts the string time into float representation
     * @param time
     * @return
     */
    private float timeConversion(String time) {
        float startTime = 0;
        String[] t = time.split("[: ]"); // splits at : or space
        float hour = Float.parseFloat(t[0]);
        float minute = Float.parseFloat(t[1]);

        if((t[2].equals("am") && hour != 12) || (t[2].equals("pm") && hour == 12))
            startTime += hour;
        else if(hour != 12 && t[2].equals("pm"))
            startTime += hour + 12;

        return startTime + minute(minute);
    }

    /**
     * Converts a minute to decimal
     * @param minute
     * @return
     */
    private float minute(float minute) {
        if(minute <= 15 && minute > 0)
            return (float) 0.25;
        else if(minute <= 30 && minute > 15)
            return (float) 0.5;
        else if(minute <= 45 && minute > 30)
            return (float) 0.75;
        else if(minute <= 59 && minute > 45)
            return 1;
        else
            return 0;
    }

    /**
     * Checks if the given name is not the same as any existing task's name
     * @param name
     * @return
     */
    private boolean uniqueName(String name) {
        for(Task t : tasks) 
            if(t.name.equals(name))
                return false;

        return true;
    }

    /**
     * Searches for a task by name and prints out details
     * @param name
     */
    public void viewTask(String name) {
        for(Task t : tasks) {
            if(t.name.equals(name)) {
                System.out.println("\n" + t.toString() + "\n");
                return;
            }
        }

        System.out.println("\nTask not found.\n");
    }

    /**
     * Deletes a task with the given name
     * @param name
     */
    public void deleteTask(String name) {
        for(Task t : tasks)
            if(t.name.equals(name)) {
                tasks.remove(t);
                System.out.println("\nTask deleted.\n");
                return;
            }

        System.out.println("\nTask not found.\n");
    }

    /**
     * Edits a task with the given name
     * @param name
     */
    public void editTask(String name) {
        for(Task t : tasks)
            if(t.name.equals(name)) {

                return;
            }

        System.out.println("\nTask not found.\n");
    }

    /**
     * Gets user input for filename and writes it to a file
     * @param username
     */
    public void writeSchedule(String username) {
        System.out.print("Enter filename: ");
        String filename = username + "/" + kb.nextLine();

        if(new File(filename).exists()) {
            System.out.print("\nFile already exists.\nWould you like to overwrite?(y/n): ");
            
            if(kb.nextLine().toLowerCase().charAt(0) == 'y')
                dataFile.write(filename, tasks);
        }
        else {
            dataFile.write(filename, tasks);
        }
    }

    /**
     * Gets user input for filename and reads from the file
     * @param username
     */
    public void readSchedule(String username) {
        String filename;

        do {
            System.out.print("Enter filename: ");
            filename = username + "/" + kb.nextLine();
        } while(!filename.contains(".json"));
        
        if(new File(filename).exists()) {
            dataFile.read(tasks, filename);
        }
        else {
            System.out.println("\nFile does not exist.\n");
        }
    }

    public void daySchedule() {

    }

    public void weekSchedule() {

    }

    public void monthSchedule() {

    }

}
