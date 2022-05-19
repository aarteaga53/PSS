public class TransientTask extends Task {

    AntiTask linkedTo;
    
    public TransientTask(String name, String type, float startTime, float duration, int date) {
        super(name, type, date, startTime, duration);
        linkedTo = null;
    }

    public AntiTask getLinkedTo() {
        return linkedTo;
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

    /**
     * Checks if this TransientTask class is less than, equal to, or greater than a specified
     * other Task. The otehr Task should only be another TransientTask.
     * @param taskOther     The task to be compared to this Task.
     * @return      an integer representing whether this class is less than, equal to,
     *              or greater than the specified task.
     */
    public int compareTo(Task taskOther) {
        if(date < taskOther.getDate()) {
            return -1;
        }
        else if(date > taskOther.getDate()) {
            return 1;
        }
        else {
            if(startTime < taskOther.getStartTime()) {
                return -1;
            }
            else if (startTime > taskOther.getStartTime()) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

}
