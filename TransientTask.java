// All accesses in the Task class have been replaced with getters and setters, if applicable.
// All accesses to the Task class's members have also been replaced with getters and setters, if applicable.
// It is not guaranteed that accesses to the members of other classes, like this one, are done through
// getters and setters. Thank you for allowing us to stop replacing direct accesses with getters and setters after
// one class, in response to my email.



public class TransientTask extends Task {

    AntiTask linkedTo;
    
    public TransientTask(String name, String type, float startTime, float duration, int date) {
        super(name, type, date, startTime, duration);
        linkedTo = null;
    }

    /**
     * Links this transient task to an anti task
     * @param link
     */
    public void linkTo(AntiTask link) {
        linkedTo = link;
    }

    /**
     * Removes a link to an anti task
     */
    public void removeLinkedTo() {
        linkedTo = null;
    }

    /**
     * Checks if it is linked to an anti task
     * @return
     */
    public boolean isLinkedTo() {
        return linkedTo != null;
    }

    public String toString() {
        return getName() + "\n" + getType() + "\n" + timeConversion() + "\n" + durationConversion() + "\n" + dateConversion(getDate());
    }

    /**
     * Checks if this TransientTask class is less than, equal to, or greater than a specified
     * other Task. The otehr Task should only be another TransientTask.
     * @param taskOther     The task to be compared to this Task.
     * @return      an integer representing whether this class is less than, equal to,
     *              or greater than the specified task.
     */
    public int compareTo(Task taskOther) {
        if(getDate() < taskOther.getDate()) {
            return -1;
        }
        else if(getDate() > taskOther.getDate()) {
            return 1;
        }
        else {
            if (getStartTime() < taskOther.getStartTime()) {
                return -1;
            }
            else if (getStartTime() > taskOther.getStartTime()) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

}
