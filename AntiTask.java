import java.util.ArrayList;

public class AntiTask extends Task {

    ArrayList<TransientTask> links;
    
    public AntiTask(String name, String type, float startTime, float duration, int date) {
        super(name, type, date, startTime, duration);
        links = new ArrayList<>();
    }

    public void addLink(TransientTask link) {
        links.add(link);
    }

    public void removeLink(TransientTask link) {
        links.remove(link);
    }

    public boolean hasLink(Task link) {        
        for(TransientTask t : links) {
            if(t == link)
                return false;
            else if(t.conflicts(link))
                return true;
        }

        return false;
    }

    public boolean conflicts(Task task) {
        if(task.date == date && overlaps(task)) {
            if(task.isTransient() && !hasLink(task)) {
                addLink((TransientTask) task);
                return false;
            }
            else
                return true;
        }

        return false;
    }

    /**
     * Converts the task to JSON
     */
    public String convertJSON() {
        return "\t{\n\t\t\"Name\" : \"" + name + "\",\n" +
                "\t\t\"Type\" : \"" + type + "\",\n" +
                "\t\t\"Date\" : " + date + ",\n" +
                "\t\t\"StartTime\" : " + startTime + ",\n" +
                "\t\t\"Duration\" : " + duration + "\n\t}";
    }

    public String toString() {
        return name + "\n" + type + "\n" + timeConversion() + "\n" + durationConversion() + "\n" + dateConversion(date);
    }

}
