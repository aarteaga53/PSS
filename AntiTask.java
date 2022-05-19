// All accesses in the Task class have been replaced with getters and setters, if applicable.
// All accesses to the Task class's members have also been replaced with getters and setters, if applicable.
// It is not guaranteed that accesses to the members of other classes, like this one, are done through
// getters and setters. Thank you for allowing us to stop replacing direct accesses with getters and setters after
// one class, in response to my email.



import java.util.ArrayList;


public class AntiTask extends Task implements Cloneable {

    ArrayList<TransientTask> links;
    RecurringTask linkedTo;

    public AntiTask(String name, String type, float startTime, float duration, int date) {
        super(name, type, date, startTime, duration);
        links = new ArrayList<>();
        linkedTo = null;
    }

    /**
     * Adds a transient task to links list, then that transient task
     * will have a link to this anti task
     * @param link
     */
    public void addLink(TransientTask link) {
        links.add(link);

        try {
            link.linkTo(clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a transient task link
     * @param link
     */
    public void removeLink(TransientTask link) {
        if (links.contains(link)) {
            link.removeLinkedTo();
            links.remove(link);
        }
        else {
            System.out.println("Warning: AntiTask's remove link tried" +
                                " to remove a link that wasn't in the list");
        }
    }

    /**
     * Checks if it has any transient task links
     * @param link
     * @return
     */
    public boolean hasLink(Task link) {
        for(TransientTask t : links) {
            if(t == link)
                return false;
            if(t.conflicts(link))
                return true;
        }

        return false;
    }

    /**
     * Links this anti task to the recurring task that it cancels
     * @param link
     */
    public void linkTo(RecurringTask link) {
        linkedTo = link;
    }

    /**
     * Checks if there any conflicts
     */
    public boolean conflicts(Task task) {
        if(task.getDate() == getDate() && overlaps(task)) {
            if(task.isTransient() && !hasLink(task)) {
                if(!links.contains(task)) {
                    addLink((TransientTask) task);
                }
                return false;
            }
            else
                return true;
        }

        return false;
    }

    public String toString() {
        return getName() + "\n" + getType() + "\n" + timeConversion() + "\n" + durationConversion() + "\n" + dateConversion(getDate());
    }

    @Override
    public AntiTask clone() throws CloneNotSupportedException {
        return (AntiTask) super.clone();
    }

}
