import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataFile {

    /**
     * Writes tasks to a file
     * @param filename
     * @param schedule
     */
    public void write(List<Task> schedule, String filename) {
        try {
            File file = new File(filename);
            FileWriter fw = new FileWriter(file);
            String text = "[\n";

            // converts every task to json format
            for(int i = 0; i < schedule.size(); i++) {
                text += schedule.get(i).convertJSON();

                // if it is not the last task, add comma
                if(i != schedule.size() - 1)
                    text += ",\n";
            }

            fw.write(text + "\n]");
            fw.close();
        } catch(IOException e) {
            System.out.println("\nError writing to file.");
        }
    }

    /**
     * Reads tasks from a file
     * @param tasks
     * @param filename
     */
    public void read(ArrayList<Task> tasks, String filename) {
        String name = "";
        String type = "";
        int date = 0;
        float startTime = 0;
        float duration = 0;
        int endDate = 0;
        int frequency = 0;

        try {
            File file = new File(filename);
            Scanner infile = new Scanner(file);
 
            while(infile.hasNext()) {
                String line = infile.nextLine();

                if(line.equals("[") || line.equals("]") || line.equals("\t{"))
                    continue;

                // creates a new task if the necessary variables have been read for a specific task type
                if(line.equals("\t},") || line.equals("\t}")) {
                    Task newTask = new Task(type);

                    if(newTask.isRecurring()) {
                        newTask = new RecurringTask(name, type, startTime, duration, date, endDate, frequency);
                    }  
                    else if(newTask.isTransient()) {
                        newTask = new TransientTask(name, type, startTime, duration, date);
                    }
                    else {
                        newTask = new AntiTask(name, type, startTime, duration, date);
                    }

                    int i;

                    // checks that there is no conflicts with tasks
                    for(i = 0; i < tasks.size(); i++) {
                        if(tasks.get(i).conflicts(newTask))
                            break;
                    }

                    // adds a new task if it is not a repeat
                    if(i == tasks.size())
                        tasks.add(newTask);
                }
                else {
                    String[] split = line.split(" : ");

                    // checks key and reads the value
                    if(split[0].equals("\t\t\"Name\"")) {
                        name = split[1].substring(split[1].indexOf("\"") + 1, split[1].lastIndexOf("\""));
                    }
                    else if(split[0].equals("\t\t\"Type\"")) {
                        type = split[1].substring(split[1].indexOf("\"") + 1, split[1].lastIndexOf("\""));
                    }
                    else if(split[0].equals("\t\t\"StartDate\"") || split[0].equals("\t\t\"Date\"")) {
                        date = Integer.parseInt(split[1].substring(0, split[1].indexOf(",")));
                    }
                    else if(split[0].equals("\t\t\"StartTime\"")) {
                        startTime = Float.parseFloat(split[1].substring(0, split[1].indexOf(",")));
                    }
                    else if(split[0].equals("\t\t\"Duration\"")) {
                        if(split[1].indexOf(",") < 0) {
                            duration = Float.parseFloat(split[1]);
                        }
                        else {
                            duration = Float.parseFloat(split[1].substring(0, split[1].indexOf(",")));
                        }
                    }  
                    else if(split[0].equals("\t\t\"EndDate\"")) {
                        endDate = Integer.parseInt(split[1].substring(0, split[1].indexOf(",")));
                    }
                    else if(split[0].equals("\t\t\"Frequency\"")) {
                        frequency = Integer.parseInt(split[1]);
                    }
                }
            }

            infile.close();
        } catch(IOException e) {
            System.out.println("\nError reading from file.");
        }
    }

    /**
     * Reads all files/schedules that are in the account,
     * and returns the names of all files
     * @param username
     * @return
     */
    public String[] readSchedules(String username) {
        File[] files = new File(username).listFiles();
        String[] schedules = new String[files.length];

        for(int i = 0; i < files.length; i++) {
            schedules[i] = files[i].getName();
        }

        return schedules;
    }

}
