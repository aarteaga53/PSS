import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataFile {

    public void write(String filename, ArrayList<Task> tasks) {
        try {
            File file = new File(filename);
            FileWriter fw = new FileWriter(file);
            String text = "[\n";

            for(int i = 0; i < tasks.size(); i++) {
                text += tasks.get(i).convertJSON();

                if(i != tasks.size() - 1)
                    text += ",\n";
            }

            fw.write(text + "\n]");
            fw.close();
        } catch(IOException e) {
            System.out.println("\nError writing to file.\n");
        }
    }

    public void read(ArrayList<Task> tasks, String filename) {
        ArrayList<Task> newTasks = new ArrayList<>();
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

                if(line.equals("\t},") || line.equals("\t}")) {
                    Task newTask;

                    if(new Task(type).isRecurring()) {
                        newTask = new RecurringTask(name, type, startTime, duration, date, endDate, frequency);
                        newTasks.add(newTask);
                    }  
                    else if(new Task(type).isTransient()) {
                        newTask = new TransientTask(name, type, startTime, duration, date);
                        newTasks.add(newTask);
                    }
                    else {
                        newTask = new AntiTask(name, type, startTime, duration, date);
                        newTasks.add(newTask);
                    }

                    int i;

                    for(i = 0; i < tasks.size(); i++) {
                        if(tasks.get(i).name.equals(newTask.name))
                            break;
                    }

                    if(i == tasks.size())
                            tasks.add(newTask);
                }
                else {
                    String[] split = line.split(" : ");

                    if(split[0].equals("\t\t\"Name\""))
                        name = split[1].substring(split[1].indexOf("\"") + 1, split[1].lastIndexOf("\""));
                    else if(split[0].equals("\t\t\"Type\""))
                        type = split[1].substring(split[1].indexOf("\"") + 1, split[1].lastIndexOf("\""));
                    else if(split[0].equals("\t\t\"StartDate\"") || line.split(":")[0].equals("\t\t\"Date\""))
                        date = Integer.parseInt(split[1].substring(0, split[1].indexOf(",")));
                    else if(split[0].equals("\t\t\"StartTime\""))
                        startTime = Float.parseFloat(split[1].substring(0, split[1].indexOf(",")));
                    else if(split[0].equals("\t\t\"Duration\"")) {
                        if(split[1].indexOf(",") < 0)
                            duration = Float.parseFloat(split[1]);
                        else
                            duration = Float.parseFloat(split[1].substring(0, split[1].indexOf(",")));
                    }  
                    else if(split[0].equals("\t\t\"EndDate\""))
                        endDate = Integer.parseInt(split[1].substring(0, split[1].indexOf(",")));
                    else if(split[0].equals("\t\t\"Frequency\""))
                        frequency = Integer.parseInt(split[1]);
                }
            }

            infile.close();
        } catch(IOException e) {
            System.out.println("\nError reading from file.\n");
        }
    }

}
