import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PSS {
    
    private Scanner kb = new Scanner(System.in);
    ArrayList<Task> tasks;
    DataFile dataFile;
    /**
     * Whether or not the currently loaded schedule and the changes the user
     * made to it, if any, have been saved to a file. Is true only if the
     * entire schedule was saved to a file at once since the last change to
     * the schedule, or if no changes were made to the schedule. Saving a 
     * schedule for a day, week, or month does not count as saving the 
     * currently loaded file.
     */
    boolean isSaved = true;

    public PSS() {
        tasks = new ArrayList<>();
        dataFile = new DataFile();
    }

    /**
     * Upon logging in the user is asked if they would like to load a file
     * that is already in their account, if they have any
     * @param username
     */
    public void loadSchedule(String username) {
        String[] schedules = dataFile.readSchedules(username);

        if(schedules.length > 0) {
            System.out.println("\nWould you like to load a schedule?\n\t0) Continue");

            for(int i = 0; i < schedules.length; i++) {
                System.out.println("\t" + (i + 1) + ") " + schedules[i]);
            }
            
            int num = getOptionInput(0, schedules.length);

            if(num <= 0) {
                return;
            }

            if(num <= schedules.length) {
                String filename = username + "/" + schedules[num-1];
                dataFile.read(tasks, filename);
                System.out.println("\nSchedule loaded.");
            }
        }
    }

    /**
     * Upon exiting the user is asked if they would like to save their
     * currently loaded schedule, if it has not been saved already.
     * @param username
     */
    public void exitWriteSchedule(String username) {    
        String menu =   "\nYou have not saved your whole raw schedule to a file" +
                        "\nWould you like to save your whole schedule to a file?" +
                        "\n\t1) Yes" +
                        "\n\t2) No";

        // remember to set isSaved to false when changes have been made
        // and true when they have been saved
        if(!isSaved) {
            System.out.println(menu);

            int num = getOptionInput(1, 2);

            if(num == 1) {     // if the user chose to save to a file
                writeSchedule(username);
            }
        }
    }

    /**
     * Gets user input for loadSchedule and exitWriteSchedule
     * @param lower
     * @param upper
     * @return
     */
    private int getOptionInput(int lower, int upper) {
        String option;
        int count = 0;

        do {
            System.out.print("Enter option: ");
            option = kb.nextLine();
            count++;   
        } while(!isValid(lower, upper, option) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            return -1;
        }

        return Integer.parseInt(option);
    }

    /**
     * Checks if the option for loadSchedule() is a valid input
     * @param lower
     * @param upper
     * @param input
     * @return
     */
    private boolean isValid(int lower, int upper, String input) {
        int num;

        try {
            num = Integer.parseInt(input);
        } catch(NumberFormatException e) {
            return false;
        }

        if(num >= lower && num <= upper) {
            return true;
        }

        return false;
    }

    /**
     * Creates a task based on type
     */
    public void createTask() {
        String type = chooseType();
        String name = getNameInput("Enter task name: ");

        if(name.equals("")) {
            return;
        }
        
        // checks what type of task is going to be created
        if(new Task(type).isRecurring()) {
            createRecurring(name, type);
        }
        else if(new Task(type).isTransient()) {
            createTransient(name, type);
        }
        else {
            createAnti(name, type);
        }
    }

    /**
     * Gets user input for task type
     */
    private String chooseType() {
        Task type;
        String option;
        String prompt = "Choose task type:\n\tRecurring - Class\n\tRecurring - Study\n\tRecurring - Sleep\n\tRecurring - Exercise\n\tRecurring - Work\n\tRecurring - Meal" +
            "\n\tTransient - Visit\n\tTransient - Shopping\n\tTransient - Appointment\n\t     Anti - Cancellation\nEnter type: ";
        
        // Gets user input for task type
        do {
            System.out.print(prompt);
            option = kb.nextLine();
            type = new Task(option);

            if(!type.isRecurring() && !type.isTransient() && !type.isAnti())
                System.out.println("\nInvalid input.\n");
        } while(!type.isRecurring() && !type.isTransient() && !type.isAnti());

        return option;
    }

    private String getNameInput(String prompt) {
        String name;
        int count = 0;

        do {
            // Gets user input for task name
            System.out.print(prompt);
            name = kb.nextLine();

            if(name.length() <= 1) {
                System.out.println("\nName is too short.\n");
                count++;
            }
        } while(name.length() <= 1 && count < 3);

        if(count == 3 && name.length() <= 1) {
            System.out.println("\nToo many attempts.");
            return "";
        }

        // checks if the name is unique
        if(!uniqueName(name)) {
            System.out.println("\nTask name is already taken.");
            return "";
        }

        return name;
    }

    /**
     * Gets user input for the start time of the task
     * @return
     */
    private String getStartTimeInput(String prompt) {
        String startTime;
        int count = 0;

        // gets user input for start time
        do {
            System.out.print(prompt);
            startTime = kb.nextLine();
            count++;
        } while(!isStartTimeCorrect(startTime) && count < 3);

        if(count == 3 && !isStartTimeCorrect(startTime)) {
            System.out.println("\nToo many attempts.");
            return "";
        }

        return startTime;
    }

    /**
     * Gets user input for the duration of the task
     * @return
     */
    private String getDurationInput(String prompt) {
        String duration;
        int count = 0;

        // gets user input for duration
        do {
            System.out.print(prompt);
            duration = kb.nextLine();
            count++;
        } while(!isDurationCorrect(duration) && count < 3);

        if(count == 3 && !isDurationCorrect(duration)) {
            System.out.println("\nToo many attempts.");
            return "";
        }

        return duration;
    }

    /**
     * Gets user input for start date of a task
     * @return
     */
    private String getDateInput(String prompt) {
        String startDate;
        int count = 0;

        // gets user input for start date
        do {
            System.out.print(prompt);
            startDate = kb.nextLine();
            count++;
        } while(!isDateCorrect(startDate) && count < 3);

        if(count == 3 && !isDateCorrect(startDate)) {
            System.out.println("\nToo many attempts.");
            return "";
        }

        return startDate;
    }

    /**
     * Gets user input for the end date of the task
     * @param startDate
     * @return
     */
    private String getEndDateInput(String prompt, String startDate) {
        String endDate;
        int count = 0;

        // gets user input for end date
        do {
            System.out.print(prompt);
            endDate = kb.nextLine();
            count++;
        } while(!isEndDateCorrect(startDate, endDate) && count < 3);

        if(count == 3 && !isEndDateCorrect(startDate, endDate)) {
            System.out.println("\nToo many attempts.");
            return "";
        }

        return endDate;
    }

    /**
     * Gets user input for the frequency of the task
     * @return
     */
    private String getFrequencyInput(String prompt) {
        String frequency;
        int count = 0;

        // gets user input for frequency
        do {
            System.out.print(prompt);
            frequency = kb.nextLine();
            count++;
        } while(!isFrequencyCorrect(frequency) && count < 3);

        if(count == 3 && !isFrequencyCorrect(frequency)) {
            System.out.println("\nToo many attempts.");
            return "";
        }

        return frequency;
    }

    /**
     * Creates an Anti-Task
     * @param name      
     * @param type      
     */
    private void createAnti(String name, String type) {
        String startTime = getStartTimeInput("Enter task start time(hh:mm am/pm): ");

        if(startTime.equals("")) {
            return;
        }

        String duration = getDurationInput("Enter task duration(hh:mm): ");

        if(duration.equals("")) {
            return;
        }

        String date = getDateInput("Enter task date(mm/dd/yyyy): ");

        if(date.equals("")) {
            return;
        }

        AntiTask newTask = new AntiTask(name, type, timeConversion(startTime), durationConversion(duration), dateConversion(date));

        if(conflicts(newTask)) {
            System.out.println("\nAnti Task does not cancel a recurring task.");
        }
        else {
            System.out.println("\nTask created.");
            tasks.add(newTask);
            isSaved = false;
        }
        
    }

    /**
     * Creates a Transient Task
     * @param name
     * @param type
     */
    private void createTransient(String name, String type) {
        String startTime = getStartTimeInput("Enter task start time(hh:mm am/pm): ");

        if(startTime.equals("")) {
            return;
        }

        String duration = getDurationInput("Enter task duration(hh:mm): ");

        if(duration.equals("")) {
            return;
        }

        String date = getDateInput("Enter task date(mm/dd/yyyy): ");

        if(date.equals("")) {
            return;
        }

        TransientTask newTask = new TransientTask(name, type, timeConversion(startTime), durationConversion(duration), dateConversion(date));

        if(conflicts(newTask)) {
            System.out.println("\nThis task conflicts with an existing task.");
        }
        else {
            System.out.println("\nTask created.");
            tasks.add(newTask);
            isSaved = false;
        } 
    }

    /**
     * Creates a Recurring Task
     * @param name
     * @param type
     */
    private void createRecurring(String name, String type) {
        String startTime = getStartTimeInput("Enter task start time(hh:mm am/pm): ");

        if(startTime.equals("")) {
            return;
        }

        String duration = getDurationInput("Enter task duration(hh:mm): ");

        if(duration.equals("")) {
            return;
        }

        String startDate = getDateInput("Enter task start date(mm/dd/yyyy): ");

        if(startDate.equals("")) {
            return;
        }
            
        String endDate = getEndDateInput("Enter task end date(mm/dd/yyyy): ", startDate);

        if(endDate.equals("")) {
            return;
        }

        String frequency = getFrequencyInput("Enter task frequency(1/7): ");

        if(frequency.equals("")) {
            return;
        }

        RecurringTask newTask = new RecurringTask(name, type, timeConversion(startTime), durationConversion(duration), dateConversion(startDate), dateConversion(endDate), Integer.parseInt(frequency));

        if(conflicts(newTask)) {
            System.out.println("\nThis task conflicts with an existing task.");
        }
        else {
            System.out.println("\nTask created.");
            tasks.add(newTask);
            isSaved = false;
        }  
    }

    /**
     * Checks if there is any conflicts with a new task being created
     * @param newTask
     * @return
     */
    private boolean conflicts(Task newTask) {
        boolean recurringPresent = false;

        for(Task task : tasks) {
            if(newTask == task) {
                continue;
            }
            if(task.isRecurring()) {
                recurringPresent = true;
            }
            if(!task.conflicts(newTask) && newTask.isAnti()) {
                return false;
            }
            if(task.conflicts(newTask) && !newTask.isAnti()) {
                if(newTask.isTransient()) {
                    TransientTask t = (TransientTask) newTask;
    
                    if(t.isLinkedTo()) {
                        AntiTask a = t.linkedTo;
    
                        a.removeLink(t);
                    }
                }
                return true;
            }
        }

        // if no recurring tasks were found then anti cannot be created
        if(newTask.isAnti() && !recurringPresent) {
            return true;
        }
            
        return false; 
    }

    /**
     * Checks if the end date is correctly formatted and later than the start date
     * @param startDate
     * @param endDate
     * @return
     */
    private boolean isEndDateCorrect(String startDate, String endDate) {
        if(!isDateCorrect(endDate)) {
            return false;
        }

        return dateConversion(endDate) > dateConversion(startDate);
    }

    /**
     * Checks if the frequency is correct
     * @param frequency
     * @return
     */
    private boolean isFrequencyCorrect(String frequency) {
        int freq;

        try {
            freq = Integer.parseInt(frequency);
        } catch(NumberFormatException e) {
            return false;
        }

        if(freq != 1 && freq != 7) {
            return false;
        }

        return true;
    }

    /**
     * Checks if the given duration string is formatted correctly
     * @param duration
     * @return
     */
    private boolean isDurationCorrect(String duration) {
        int hour;
        int minute;

        // length must be 4 or 5
        if(duration.length() > 5 || duration.length() < 4) {
            return false;
        }

        try {
            String[] split = duration.split(":");
            hour = Integer.parseInt(split[0]);
            minute = Integer.parseInt(split[1]);
        } catch(NumberFormatException e) {
            return false;
        }

        if(hour > 23 || hour < 0) {
            return false;
        }
        else if(minute < 0 || minute > 59) {
            return false;
        }
        else if(hour == 23 && minute > 45) {
            return false;
        }
        else if(hour == 0 && minute == 0) {
            return false;
        }

        return true;
    }

    /**
     * Checks if the given start time string is formateed correctly
     * @param startTime
     * @return
     */
    private boolean isStartTimeCorrect(String startTime) {
        int hour;
        int minute;
        String time;

        // length must be 7 or 8
        if(startTime.length() > 8 || startTime.length() < 7) {
            return false;
        }

        try {
            String[] split = startTime.split("[: ]");
            hour = Integer.parseInt(split[0]);
            minute = Integer.parseInt(split[1]);
            time = split[2];
        } catch(NumberFormatException e) {
            return false;
        }
        
        if(hour > 12 || hour < 1) {
            return false;
        }
        else if(minute < 0 || minute > 59) {
            return false;
        }
        else if(!time.equals("am") && !time.equals("pm")) {
            return false;
        }

        return true;
    }

    /**
     * Checks if the given date string is formatted correctly and the date is proper
     * @param date
     * @return
     */
    private boolean isDateCorrect(String date) {
        int month;
        int day;
        int year;

        // length must be 10
        if(date.length() != 10) {
            return false;
        }

        try {
            String[] split = date.split("/");
            month = Integer.parseInt(split[0]);
            day = Integer.parseInt(split[1]);
            year = Integer.parseInt(split[2]);
        } catch(NumberFormatException e) {
            return false;
        }

        if(month > 12 || month < 1) {
            return false;
        }
        else if(day < 1) {
            return false;
        }
        else if((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day > 31) {
            return false;
        }
        else if((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
            return false;
        }
        else if(month == 2) {
            if(year % 4 == 0) {
                if(year % 100 == 0) {
                    if(year % 400 == 0 && day > 29) {
                        return false;
                    }
                }
            }
            else if(day > 28) {
                return false;
            }
        }
        else if(year < 0) {
            return false;
        }

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

        if((t[2].equals("am") && hour != 12) || (t[2].equals("pm") && hour == 12)) {
            startTime += hour;
        }
        else if(hour != 12 && t[2].equals("pm")) {
            startTime += hour + 12;
        }

        return startTime + minute(minute);
    }

    /**
     * Converts a minute to decimal point, and rounds to nearest 15 minutes
     * @param minute
     * @return
     */
    private float minute(float minute) {
        if(minute <= 15 && minute > 0) {
            return (float) 0.25;
        }
        else if(minute <= 30 && minute > 15) {
            return (float) 0.5;
        }
        else if(minute <= 45 && minute > 30) {
            return (float) 0.75;
        }
        else if(minute <= 59 && minute > 45) {
            return 1;
        }
        else {
            return 0;
        }
    }

    /**
     * Checks if the given name is not the same as any existing task's name
     * @param name
     * @return
     */
    private boolean uniqueName(String name) {
        for(Task task : tasks) {
            if(task.name.equals(name))
                return false;
        } 

        return true;
    }

    /**
     * Searches for a task by name and prints out details
     * @param name
     */
    public void viewTask(String name) {
        boolean taskFound = false;

        for(Task task : tasks) {
            if(task.name.equals(name)) {
                System.out.println("\n" + task.toString());
                taskFound = true;
            }
        }

        if (taskFound == false) {
            System.out.println("\nTask not found.");
        }
    }

    /**
     * Deletes a task with the given name
     * @param name
     */
    public void deleteTask(String name) {
        boolean taskFound = false;
        List<Task> candidatesForDeletion = new ArrayList<Task>();

        for(Task task : tasks)
            if(task.name.equals(name)) {
                if(task.isAnti()) {
                    AntiTask temp = (AntiTask) task;

                    if(temp.links.size() > 0) { // checks if there are any transient tasks that depend on this anti task
                        System.out.println("\nTask could not be removed.");
                        return;
                    }
                    else { // removes any links to a recurring task
                        RecurringTask link = temp.linkedTo;

                        link.removeLink(temp);
                    }          
                }
                else if(task.isRecurring()) { // removes any anti tasks that are linked to this recurring task
                    RecurringTask temp = (RecurringTask) task;

                    for (AntiTask a : temp.links) {
                        tasks.remove(a);
                        isSaved = false;
                    }
                }
                else if(task.isTransient()) { // removes a link to anti task if there is one
                    TransientTask temp = (TransientTask) task;
                    AntiTask link = temp.linkedTo;

                    if (link != null) {
                        link.removeLink(temp);
                    }
                }

                candidatesForDeletion.add(task);
                taskFound = true;
            }

            if (!taskFound) {
                System.out.println("\nTask not found.");
            }
            else {
                if (candidatesForDeletion.size() > 1) {
                    viewTask(name); // should print multiple tasks with the same name.

                    System.out.println("\nAbove are the tasks with that name.");
                    int date = Integer.parseInt(getDateInput("Input the date of the task you want deleted (mm/dd/yyyy): "));

                    for (Task task : candidatesForDeletion) {
                        if (task.date == date) {
                            tasks.remove(task);
                            isSaved = false;
                        }
                    }
                }
                else {
                    tasks.remove(candidatesForDeletion.get(0));
                    isSaved = false;
                }

                System.out.println("\nTask deleted.");
            }
    }

    /**
     * Edits a task with the given name
     * @param name
     */
    public void editTask(String name) {
        boolean exit = false;

        for(Task task : tasks) {
            if(task.name.equals(name)) {
                System.out.println("\n" + task.toString());

                if(task.isTransient()) {
                    do {
                        System.out.println("\tEdit Task: \n"+
                                            "\t 1: Change Task Name\n"+
                                            "\t 2: Change Task Date\n"+
                                            "\t 3: Change Task Start Time\n"+
                                            "\t 4: Change Task Duration\n"+
                                            "\t 5: Exit Editor");
                        String option = kb.nextLine();

                        switch(option){
                            case "1":
                                String newName = getNameInput("Enter new name: ");
                                
                                if(!newName.equals("")) {
                                    task.name = newName;
                                    System.out.println("\nChanges Saved.\n");
                                }
                                else
                                    System.out.println("New Name not Unique or Empty Input.");
                                break;
                            case "2":
                                int oldDate = task.date;
                                String newDate = getDateInput("Enter new date(hh:mm am/pm): ");

                                if(!newDate.equals(""))
                                    task.date = dateConversion(newDate);
                                if(conflicts(task)) {
                                    task.date = oldDate;
                                    System.out.println("\nConflicts Detected. Changes not saved.\n");
                                }
                                else {
                                    removeTransientLink((TransientTask) task);

                                    System.out.println("\nChanges Saved\n");
                                }
                                break;
                            case "3":
                                float oldTime = task.startTime;
                                String newTime = getStartTimeInput("Enter new start time(hh:mm am/pm): ");

                                if(!newTime.equals(""))
                                    task.startTime = timeConversion(newTime);
                                if(conflicts(task)) {
                                    task.startTime = oldTime;
                                    System.out.println("\nConflicts Detected. Changes not saved.\n");
                                }
                                else {
                                    removeTransientLink((TransientTask) task);

                                    System.out.println("\nChanges Saved\n");
                                }
                                break;
                            case "4":
                                float oldDuration = task.duration;
                                String newDuration = getDurationInput("Enter new duration(hh:mm): ");

                                if(!newDuration.equals(""))
                                    task.duration = durationConversion(newDuration);
                                if(conflicts(task)) {
                                    task.duration = oldDuration;
                                    System.out.println("\nConflicts Detected. Changes not saved.\n");
                                }
                                else {
                                    removeTransientLink((TransientTask) task);

                                    System.out.println("\nChanges Saved\n");
                                }
                                break;
                            case "5":
                                exit = true;
                                break;
                            default: 
                                System.out.println("\nInvaild Input\n");
                                break;
                        }
                    }while(!exit);
                }
                else if(task.isRecurring()){
                    do{ 
                        RecurringTask temp = (RecurringTask) task;
                        System.out.println("\tEdit Task: \n"+
                                            "\t 1: Change Task Name\n"+
                                            "\t 2: Change Task Start Date\n"+
                                            "\t 3: Change Task Start Time\n"+
                                            "\t 4: Change Task Duration\n"+
                                            "\t 5: Change Task End Date\n"+
                                            "\t 6: Change Task Frequency\n"+
                                            "\t 7: Exit Editor");
                        String option = kb.nextLine();

                        switch(option) {
                            case "1":
                                String newName = getNameInput("Enter new name: ");
                                    
                                if(!newName.equals("")) {
                                    task.name = newName;
                                    System.out.println("\nChanges Saved.\n");
                                }
                                else
                                    System.out.println("New Name not Unique or Empty Input.");
                                break;
                            case "2":
                                int oldDate = task.date;
                                String newDate = getDateInput("Enter new date(hh:mm am/pm): ");

                                if(!newDate.equals(""))
                                    task.date = dateConversion(newDate);
                                if(conflicts(task)) {
                                    task.date = oldDate;
                                    System.out.println("\nConflicts Detected. Changes not saved.\n");
                                }
                                else {
                                    removeTransientLink((TransientTask) task);

                                    System.out.println("\nChanges Saved\n");
                                }
                                break;
                            case "3":
                                float oldTime = task.startTime;
                                String newTime = getStartTimeInput("Enter new start time(hh:mm am/pm): ");

                                if(!newTime.equals(""))
                                    task.startTime = timeConversion(newTime);
                                if(conflicts(task)) {
                                    task.startTime = oldTime;
                                    System.out.println("\nConflicts Detected. Changes not saved.\n");
                                }
                                else {
                                    removeTransientLink((TransientTask) task);

                                    System.out.println("\nChanges Saved\n");
                                }
                                break;
                            case "4":
                                float oldDuration = task.duration;
                                String newDuration = getDurationInput("Enter new duration(hh:mm): ");

                                if(!newDuration.equals(""))
                                    task.duration = durationConversion(newDuration);
                                if(conflicts(task)) {
                                    task.duration = oldDuration;
                                    System.out.println("\nConflicts Detected. Changes not saved.\n");
                                }
                                else {
                                    removeTransientLink((TransientTask) task);

                                    System.out.println("\nChanges Saved\n");
                                }
                                break;
                            case "5":
                                int oldEnd = temp.endDate;
                                String newEnd = getEndDateInput("Enter new end date(mm/dd/yyyy): ", temp.dateConversion(temp.date));

                                if(!newEnd.equals(""))
                                    temp.endDate = dateConversion(newEnd);
                                if(conflicts(task)){
                                    temp.endDate = oldEnd;
                                    System.out.println("\nConflicts Detected. Changes not saved.\n");
                                }
                                else{
                                    removeRecurringLink((RecurringTask) task);

                                    System.out.println("\nChanges Saved\n");
                                }
                                break;
                            case "6":
                                int oldFreq = temp.frequency;
                                String newFreq = getFrequencyInput("Enter new frequency(1/7): ");

                                if(!newFreq.equals(""))
                                    temp.frequency = Integer.parseInt(newFreq);
                                if(conflicts(task)){
                                    temp.frequency = oldFreq;
                                    System.out.println("\nConflicts Detected. Changes not saved.\n");
                                }
                                else {
                                    removeRecurringLink((RecurringTask) task);

                                    System.out.println("\nChanges Saved\n");
                                }
                                break;
                            case "7":
                                exit = true;
                                break;
                            default: 
                                System.out.println("\nInvaild Input\n");
                                break;
                        }
                    } while(!exit);
                }

                return;
            }
        }
            
        System.out.println("\nTask not found.");
    }

    /**
     * Removes anti links from a recurring task if it no longer needs those anti tasks
     * @param task
     */
    private void removeRecurringLink(RecurringTask task) {
        ArrayList<AntiTask> a = task.links;

        while(!a.isEmpty()) {
            int next = task.date;

            while(next <= task.endDate) {
                next = task.nextDate(next, task.frequency);

                if(next == a.get(0).date) {
                    break;
                }
            }

            if(next <= task.endDate) {
                a.get(0).startTime = task.startTime;
                a.get(0).duration = task.duration;
    
                for(TransientTask t : a.get(0).links) {
                    removeTransientLink(t);
                }
            }
            else {
                System.out.println("\nAntiTask \"" + a.get(0).name + "\" was deleted.\n");
                tasks.remove(a.get(0));
                task.removeLink(a.get(0));
            }
        }
    }

    /**
     * Removes an anti link from a transient task if it no longer depends on the anti task
     * @param task
     */
    private void removeTransientLink(TransientTask task) {
        if(task.isLinkedTo()) {
            AntiTask a = task.linkedTo;

            if(task.date != a.date || !a.overlaps(task)) {
                a.removeLink(task);
            }
        }
    }

    /**
     * Gets user input for filename and writes the user's entire schedule to a file
     * @param   username    The user who is writing their schedule to a file
     */
    public void writeSchedule(String username) {
        writeSchedule(username, tasks);
        isSaved = true;
    }

    /**
     * Gets user input for filename and writes the specified schedule to a file
     * @param username
     * @param schedule  The schedule that is to be written to a file.
     */
    public void writeSchedule(String username, List<Task> schedule) {
        String filepath = username + "/";
        String filename = getFilename();

        if(filename.equals("")) {
            return;
        }
        
        // checks if the file exists
        if(new File(filepath + filename).exists()) {
            System.out.print("\nFile already exists.\nWould you like to overwrite?(y/n): ");
            
            // asks to overwrite if the file exists
            if(kb.nextLine().toLowerCase().charAt(0) == 'y') {
                dataFile.write(schedule, filepath + filename);
                System.out.println("\nSchedule saved.");
            }
        }
        else {
            dataFile.write(schedule, filepath + filename);
        }
    }

    /**
     * Gets user input for filename and reads from the file
     * @param username
     */
    public void readSchedule(String username) {
        String filepath = username + "/";
        String filename = getFilename();

        if(filename.equals("")) {
            return;
        }

        // checks if the file exists
        if(new File(filepath + filename).exists()) {
            dataFile.read(tasks, filepath + filename);
            System.out.println("\nSchedule read.");
        }
        else {
            System.out.println("\nFile does not exist.");
        }
    }

    /**
     * Gets user input for the filename to write or read from
     * @return
     */
    private String getFilename() {
        String filename;
        int count = 0;

        // makes sure file is json
        do {
            System.out.print("Enter filename: ");
            filename = kb.nextLine();
            count++;

            if(!filename.contains(".json")) {
                System.out.println("\nMust be JSON file.");
            }
        } while(!filename.contains(".json") && count < 3);

        if(count == 3 && !filename.contains(".json")) {
            System.out.println("\nToo many attempts.");
            return "";
        }

        return filename;
    }

    /**
     * Creates a schedule for a single day in increasing order from time
     */
    public void daySchedule() {
        String startDate = getDateInput("Enter date of tasks(mm/dd/yyyy): ");

        if(startDate.equals("")) {
            System.out.println("\nToo many attempts.");
            return;
        }

        int date = dateConversion(startDate);
        ArrayList<Task> tasksInDay = getTasksInPeriod(date, 1);

        for (Task task : tasksInDay) {
            System.out.println("\n" + task.toString());
        }
    }

    /**
     * Creates a schedule for a week in increasing order from time and day
     */
    public void weekSchedule() {
        String startDate = getDateInput("Enter date of tasks(mm/dd/yyyy): ");

        if(startDate.equals("")) {
            System.out.println("\nToo many attempts.");
            return;
        }

        int date = dateConversion(startDate);
        ArrayList<Task> tasksInWeek = getTasksInPeriod(date, 7);

        for (Task task : tasksInWeek) {
            System.out.println("\n" + task.toString());
        }
    }

    /**
     * Creates a schedule for a month in increasing order from time and day
     */
    public void monthSchedule() {
        String startDate = getDateInput("Enter date of tasks(mm/dd/yyyy): ");

        if(startDate.equals("")) {
            System.out.println("\nToo many attempts.");
            return;
        }

        int date = dateConversion(startDate);
        ArrayList<Task> tasksInMonth = getTasksInPeriod(date, 30);

        for (Task task : tasksInMonth) {
            System.out.println("\n" + task.toString());
        }
    }

    public void writeDaySchedule(String username) {
        String startDate = getDateInput("Enter date of tasks(mm/dd/yyyy): ");

        if(startDate.equals("")) {
            System.out.println("\nToo many attempts.");
            return;
        }

        int date = dateConversion(startDate);
        int day = 1;
        ArrayList<Task> tasksInDay = getTasksInPeriod(date, day);
        writeSchedule(username, tasksInDay);
    }

    public void writeWeekSchedule(String username) {
        String startDate = getDateInput("Enter date of tasks(mm/dd/yyyy): ");

        if(startDate.equals("")) {
            System.out.println("\nToo many attempts.");
            return;
        }

        int date = dateConversion(startDate);
        int week = 7;
        ArrayList<Task> tasksInWeek = getTasksInPeriod(date, week);
        writeSchedule(username, tasksInWeek);
    }

    public void writeMonthSchedule(String username) {
        String startDate = getDateInput("Enter date of tasks(mm/dd/yyyy): ");

        if(startDate.equals("")) {
            System.out.println("\nToo many attempts.");
            return;
        }

        int date = dateConversion(startDate);
        int month = 30;
        ArrayList<Task> tasksInMonth = getTasksInPeriod(date, month);
        writeSchedule(username, tasksInMonth);
    }

    /**
     * Returns a list of tasks occuring within a given period
     * @param startDate
     * @param durationInDays
     * @return
     */
    private ArrayList<Task> getTasksInPeriod(int date, int durationInDays) {
        ArrayList<Task> tasksInPeriod = new ArrayList<Task>();
        RecurringTask day = new RecurringTask("Day", "Class", 0, (float) 23.75, date, date, 1);
        int next = date;
        int count = 0;

        while(count < durationInDays) {
            for(Task task : getTasksInDay(day)) {
                tasksInPeriod.add(task);
            }

            next = day.nextDate(next, 1);
            day.date = next;
            day.endDate = next;
            count++;
        }

        Collections.sort(tasksInPeriod);

        return tasksInPeriod;
    }

    /**
     * Returns a list of tasks for a given date
     * @param date
     * @return 
     */
    private ArrayList<Task> getTasksInDay(RecurringTask day) {
        ArrayList<Task> tasksInDay = new ArrayList<Task>();

        for(Task task : tasks) {
            if (day.conflicts(task)) {
                if(task.isRecurring()) {
                    RecurringTask temp = (RecurringTask) task;
                    TransientTask t = new TransientTask(temp.name, temp.type, temp.startTime, temp.duration, day.date);

                    if(temp.links.size() > 0) {
                        for(AntiTask a : temp.links) {
                            if(!day.conflicts(a)) {
                                tasksInDay.add(t);
                            }
                        }
                    }
                    else {
                        tasksInDay.add(t);
                    }
                }
                else if(task.isTransient()) {
                    tasksInDay.add(task);
                }
            }
        }

        return tasksInDay;
    }

}
