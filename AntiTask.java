import java.util.ArrayList;

public class AntiTask extends Task implements Cloneable {

    ArrayList<TransientTask> links;
    RecurringTask linkedTo;
    
    public AntiTask(String name, String type, float startTime, float duration, int date) {
        super(name, type, date, startTime, duration);
        links = new ArrayList<>();
        linkedTo = null;
    }

    public void addLink(TransientTask link) {
        links.add(link);

        try {
            link.linkTo(clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void removeLink(TransientTask link) {
        if (links.contains(link)) {
            links.remove(link);
        }
        else {
            System.out.println("Warning: AntiTask's remove link tried" +
                                " to remove a link that wasn't in the list");
        }
    }

    public boolean hasLink(Task link) {        
        for(TransientTask t : links) {
            if(t == link)
                return false;
            if(t.conflicts(link))
                return true;
        }

        return false;
    }

    public void linkTo(RecurringTask r) {
        linkedTo = r;
    }

    public boolean conflicts(Task task) {
        if(task.date == date && overlaps(task)) {
            if(task.isTransient() && !hasLink(task)) {
                if(!links.contains(task))
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

    @Override
    public AntiTask clone() throws CloneNotSupportedException {
        return (AntiTask) super.clone();    // return shallow copy
    }

}
