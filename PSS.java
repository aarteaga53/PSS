import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class PSS {
    
    private Scanner kb = new Scanner(System.in);
    ArrayList<Task> tasks;
    DataFile dataFile;

    public PSS() {
        tasks = new ArrayList<>();
        dataFile = new DataFile();
    }

    /**
     * Creates a task based on type
     */
    public void createTask() {
        String type = chooseType();
        String name;
        int count = 0;

        do {
            // Gets user input for task name
            System.out.print("Enter task name: ");
            name = kb.nextLine();

            if(name.length() <= 1) {
                System.out.println("\nName is too short.\n");
                count++;
            }
        } while(name.length() <= 1 && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            return;
        }

        // checks if the name is unique
        if(!uniqueName(name)) {
            System.out.println("\nTask name is already taken.");
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
        String prompt = "Choose task type:\n" +
                        "\tClass - Recurring\n\tStudy - Recurring\n\tSleep - Recurring\n\tExercise - Recurring\n\tWork - Recurring\n\tMeal - Recurring\n" +
                        "\tVisit - Transient\n\tShopping - Transient\n\tAppointment - Transient\n" +
                        "\tCancellation - Anti\nEnter type: ";
        String option;
        Task newTask;

        // Gets user input for task type
        do {
            System.out.print(prompt);
            option = kb.nextLine();
            newTask = new Task(option);

            if(!newTask.isRecurring() && !newTask.isTransient() && !newTask.isAnti())
                System.out.println("\nInvalid input.\n");
        } while(!newTask.isRecurring() && !newTask.isTransient() && !newTask.isAnti());

        return option;
    }

    /**
     * Creates an Anti-Task
     * @param name      
     * @param type      
     */
    private void createAnti(String name, String type) {
        AntiTask newTask;
        String startTime;
        String duration;
        String date;
        int count = 0;

        // gets user input for start time
        do {
            System.out.print("Enter task start time(hh:mm am/pm): ");
            startTime = kb.nextLine();

            if(!isStartTimeCorrect(startTime))
                count++;
        } while(!isStartTimeCorrect(startTime) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            return;
        }
        else
            count = 0;

        // gets user input for duration
        do {
            System.out.print("Enter task duration(hh:mm): ");
            duration = kb.nextLine();

            if(!isDurationCorrect(duration))
                count++;
        } while(!isDurationCorrect(duration) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            count++;
        }
        else
            count = 0;

        // gets user input for date
        do {
            System.out.print("Enter task date(mm/dd/yyyy): ");
            date = kb.nextLine();

            if(!isDateCorrect(date))
                count++;
        } while(!isDateCorrect(date) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            count++;
        }

        newTask = new AntiTask(name, type, timeConversion(startTime), durationConversion(duration), dateConversion(date));

        if(conflicts(newTask))
            System.out.println("\nAnti Task does not cancel a recurring task.");
        else
            tasks.add(newTask);
    }

    /**
     * Creates a Transient Task
     * @param name
     * @param type
     */
    private void createTransient(String name, String type) {
        TransientTask newTask;
        String startTime;
        String duration;
        String date;
        int count = 0;

        // gets user input for start time
        do {
            System.out.print("Enter task start time(hh:mm am/pm): ");
            startTime = kb.nextLine();

            if(!isStartTimeCorrect(startTime))
                count++;
        } while(!isStartTimeCorrect(startTime) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            return;
        }
        else
            count = 0;

        // gets user input for duration
        do {
            System.out.print("Enter task duration(hh:mm): ");
            duration = kb.nextLine();

            if(!isDurationCorrect(duration))
                count++;
        } while(!isDurationCorrect(duration) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            count++;
        }
        else
            count = 0;

        // gets user input for date
        do {
            System.out.print("Enter task date(mm/dd/yyyy): ");
            date = kb.nextLine();

            if(!isDateCorrect(date))
                count++;
        } while(!isDateCorrect(date) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            count++;
        }

        newTask = new TransientTask(name, type, timeConversion(startTime), durationConversion(duration), dateConversion(date));

        if(conflicts(newTask))
            System.out.println("\nThis task conflicts with an existing task.");
        else
            tasks.add(newTask);
    }

    /**
     * Creates a Recurring Task
     * @param name
     * @param type
     */
    private void createRecurring(String name, String type) {
        RecurringTask newTask;
        String startTime;
        String duration;
        String startDate;
        String endDate;
        String frequency;
        int count = 0;

        // gets user input for start time
        do {
            System.out.print("Enter task start time(hh:mm am/pm): ");
            startTime = kb.nextLine();

            if(!isStartTimeCorrect(startTime))
                count++;
        } while(!isStartTimeCorrect(startTime) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            return;
        }
        else
            count = 0;

        // gets user input for duration
        do {
            System.out.print("Enter task duration(hh:mm): ");
            duration = kb.nextLine();

            if(!isDurationCorrect(duration))
                count++;
        } while(!isDurationCorrect(duration) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            return;
        }
        else
            count = 0;

        // gets user input for start date
        do {
            System.out.print("Enter task start date(mm/dd/yyyy): ");
            startDate = kb.nextLine();

            if(!isDateCorrect(startDate))
                count++;
        } while(!isDateCorrect(startDate) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            return;
        }
        else
            count = 0;
            
        // gets user input for end date
        do {
            System.out.print("Enter task end date(mm/dd/yyyy): ");
            endDate = kb.nextLine();

            if(!isEndDateCorrect(startDate, endDate))
                count++;
        } while(!isEndDateCorrect(startDate, endDate) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            return;
        }
        else
            count = 0;

        // gets user input for frequency
        do {
            System.out.print("Enter task frequency(1/7): ");
            frequency = kb.nextLine();

            if(!isFrequencyCorrect(frequency))
                count++;
        } while(!isFrequencyCorrect(frequency) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            return;
        }

        newTask = new RecurringTask(name, type, timeConversion(startTime), durationConversion(duration), dateConversion(startDate), dateConversion(endDate), Integer.parseInt(frequency));

        if(conflicts(newTask))
            System.out.println("\nThis task conflicts with an existing task.");
        else
            tasks.add(newTask);
    }

    /**
     * Checks if there is any conflicts with a new task being created
     * @param newTask
     * @return
     */
    private boolean conflicts(Task newTask) {
        for(Task task : tasks) {
            if(task.conflicts(newTask))
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
        if(!isDateCorrect(endDate))
            return false;

        return dateConversion(endDate) > dateConversion(startDate);
    }

    /**
     * Checks if the frequency is correct
     * @param frequency
     * @return
     */
    private boolean isFrequencyCorrect(String frequency) {
        int f = 0;

        try {
            f = Integer.parseInt(frequency);
        } catch(NumberFormatException e) {
            return false;
        }

        if(f != 1 && f != 7)
            return false;

        return true;
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
            String[] split = duration.split(":");
            hour = Integer.parseInt(split[0]);
            minute = Integer.parseInt(split[1]);
        } catch(NumberFormatException e) {
            return false;
        }

        if(hour > 23 || hour < 0)
            return false;
        else if(minute < 0 || minute > 59)
            return false;
        else if(hour == 23 && minute > 45)
            return false;
        else if(hour == 0 && minute == 0)
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
        String time = "";

        // length must be 7 or 8
        if(startTime.length() > 8 || startTime.length() < 7)
            return false;

        try {
            String[] split = startTime.split("[: ]");
            hour = Integer.parseInt(split[0]);
            minute = Integer.parseInt(split[1]);
            time = split[2];
        } catch(NumberFormatException e) {
            return false;
        }
        
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
            String[] split = date.split("/");
            month = Integer.parseInt(split[0]);
            day = Integer.parseInt(split[1]);
            year = Integer.parseInt(split[2]);
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
        else if(month == 2) {
            if(year % 4 == 0 && year % 100 == 0 && year % 400 == 0 && day > 29)
                return false;
            else if(day > 28)
                return false;
        }
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
        for(Task task : tasks) 
            if(task.name.equals(name))
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
                System.out.println("\n" + t.toString());
                return;
            }
        }

        System.out.println("\nTask not found.");
    }

    /**
     * Deletes a task with the given name
     * @param name
     */
    public void deleteTask(String name) {
        for(Task t : tasks)
            if(t.name.equals(name)) {
                if(t.isAnti()){
                    
                }
                else if(t.isRecurring()){
                    RecurringTask temp = (RecurringTask) t;

                    if(temp.links.size() > 0){
                        for (AntiTask anti : temp.links) {
                            tasks.remove(anti);
                        }
                    }

                    tasks.remove(t);
                }
                else{
                    tasks.remove(t);
                }

                System.out.println("\nTask deleted.");
                return;
            }

        System.out.println("\nTask not found.");
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

        System.out.println("\nTask not found.");
    }

    /**
     * Gets user input for filename and writes it to a file
     * @param username
     */
    public void writeSchedule(String username) {
        String filepath = username + "/";
        String filename;

        // makes sure file is json
        do {
            System.out.print("Enter filename: ");
            filename = kb.nextLine();

            if(!filename.contains(".json"))
                System.out.println("\nMust be JSON file.");
        } while(!filename.contains(".json"));
        
        // checks if the file exists
        if(new File(filepath + filename).exists()) {
            System.out.print("\nFile already exists.\nWould you like to overwrite?(y/n): ");
            
            // asks to overwrite if the file exists
            if(kb.nextLine().toLowerCase().charAt(0) == 'y')
                dataFile.write(tasks, filepath + filename);
        }
        else {
            dataFile.write(tasks, filepath + filename);
        }
    }

    /**
     * Gets user input for filename and reads from the file
     * @param username
     */
    public void readSchedule(String username) {
        String filepath = username + "/";
        String filename;

        // makes sure file is json
        do {
            System.out.print("Enter filename: ");
            filename = kb.nextLine();

            if(!filename.contains(".json"))
                System.out.println("\nMust be JSON file.");
        } while(!filename.contains(".json"));
        
        // checks if the file exists
        if(new File(filepath + filename).exists()) {
            dataFile.read(tasks, filepath + filename);
        }
        else {
            System.out.println("\nFile does not exist.");
        }
    }

    /**
     * Creates a schedule for a single day in increasing order from time
     */
    public void daySchedule() {
        String startDate;
        int count = 0;

        // gets user input for start date
        do {
            System.out.print("Enter day to view tasks(mm/dd/yyyy): ");
            startDate = kb.nextLine();

            if(!isDateCorrect(startDate))
                count++;
        } while(!isDateCorrect(startDate) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            return;
        }

        int date = dateConversion(startDate);
        ArrayList<Task> tasksInDay = getTasksInPeriod(date, 1);

        Collections.sort(tasksInDay);

        for (Task task : tasksInDay) {
            System.out.println("\n" + task.toString());
        }
    }

    /**
     * Creates a schedule for a week in increasing order from time and day
     */
    public void weekSchedule() {
        String startDate;
        int count = 0;

        // gets user input for start date
        do {
            System.out.print("Enter day to view tasks(mm/dd/yyyy): ");
            startDate = kb.nextLine();

            if(!isDateCorrect(startDate))
                count++;
        } while(!isDateCorrect(startDate) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            return;
        }

        int date = dateConversion(startDate);
        ArrayList<Task> tasksInWeek = getTasksInPeriod(date, 7);

        Collections.sort(tasksInWeek);

        for (Task task : tasksInWeek) {
            System.out.println("\n" + task.toString());
        }
    }

    /**
     * Creates a schedule for a month in increasing order from time and day
     */
    public void monthSchedule() {
        String startDate;
        int count = 0;

        // gets user input for start date
        do {
            System.out.print("Enter day to view tasks(mm/dd/yyyy): ");
            startDate = kb.nextLine();

            if(!isDateCorrect(startDate))
                count++;
        } while(!isDateCorrect(startDate) && count < 3);

        if(count == 3) {
            System.out.println("\nToo many attempts.");
            return;
        }

        int date = dateConversion(startDate);
        ArrayList<Task> tasksInMonth = getTasksInPeriod(date, 30);

        Collections.sort(tasksInMonth);

        for (Task task : tasksInMonth) {
            System.out.println("\n" + task.toString());
        }
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
                        for(AntiTask a : temp.links)
                            if(!day.conflicts(a))
                                tasksInDay.add(t);
                    }
                    else
                        tasksInDay.add(t);
                }
                else if(task.isTransient()) {
                    tasksInDay.add(task);
                }
            }
        }

        return tasksInDay;
    }

}
