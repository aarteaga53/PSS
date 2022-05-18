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

            fw.write(text + "\n]\n");
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
        int count = 0;

        try {
            File file = new File(filename);
            Scanner infile = new Scanner(file);
 
            while(infile.hasNext()) {
                String line = infile.nextLine();

                if(line.equals("[") || line.equals("]") || line.equals("  {") || line.equals("")) {
                    continue;
                }

                // creates a new task if the necessary variables have been read for a specific task type
                if(line.equals("  },") || line.equals("  }")) {
                    Task newTask = new Task(type);

                    if(newTask.isRecurring() && count == 7) { // creates a recurring
                        newTask = new RecurringTask(name, type, startTime, duration, date, endDate, frequency);
                    }  
                    else if(newTask.isTransient() || (newTask.isRecurring() && count == 5)) { // creates a transient
                        newTask = new TransientTask(name, type, startTime, duration, date);
                    }
                    else if(newTask.isAnti()) { // creates an anti
                        newTask = new AntiTask(name, type, startTime, duration, date);
                    }

                    // checks that the task is of recurring, transient, or anti
                    if(newTask.isRecurring() || newTask.isTransient() || newTask.isAnti()) {
                        int i;

                        // checks that there is no conflicts with tasks
                        for(i = 0; i < tasks.size(); i++) {
                            if(newTask.isAnti()) {
                                if(tasks.get(i).isRecurring()) {
                                    if(!tasks.get(i).conflicts(newTask)) {
                                        continue;
                                    }
                                }
                            }
                            else {
                                if(tasks.get(i).conflicts(newTask)) {
                                    if(newTask.isTransient()) {
                                        TransientTask t = (TransientTask) newTask;
                        
                                        if(t.isLinkedTo()) {
                                            AntiTask a = t.linkedTo;
                        
                                            a.removeLink(t);
                                        }
                                    }
                                    break;
                                }
                            }
                        }

                        // if the task does not conflict it is added to the schedule
                        if(i == tasks.size()) {
                            tasks.add(newTask);
                        }
                        else {
                            System.out.println("\nTask \"" + name + "\" was not added to schedule, because of a conflict.");
                        }
                    }

                    count = 0;
                }
                else { // Reads the next line and assigns the value to the according variable of the task
                    String[] split = line.split(" : ");

                    // checks key and reads the value
                    if(split[0].equals("    \"Name\"")) { // name value
                        name = split[1].substring(split[1].indexOf("\"") + 1, split[1].lastIndexOf("\""));
                    }
                    else if(split[0].equals("    \"Type\"")) { // type value
                        type = split[1].substring(split[1].indexOf("\"") + 1, split[1].lastIndexOf("\""));
                    }
                    else if(split[0].equals("    \"StartDate\"") || split[0].equals("    \"Date\"")) { // date value
                        date = Integer.parseInt(split[1].substring(0, split[1].indexOf(",")));
                    }
                    else if(split[0].equals("    \"StartTime\"")) { // start time value
                        startTime = Float.parseFloat(split[1].substring(0, split[1].indexOf(",")));
                    }
                    else if(split[0].equals("    \"Duration\"")) { // duration value
                        if(split[1].indexOf(",") < 0) {
                            duration = Float.parseFloat(split[1]);
                        }
                        else {
                            duration = Float.parseFloat(split[1].substring(0, split[1].indexOf(",")));
                        }
                    }  
                    else if(split[0].equals("    \"EndDate\"")) { // end date value
                        endDate = Integer.parseInt(split[1].substring(0, split[1].indexOf(",")));
                    }
                    else if(split[0].equals("    \"Frequency\"")) { // frequency value
                        frequency = Integer.parseInt(split[1]);
                    }

                    count++;
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
