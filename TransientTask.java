public class TransientTask extends Task {

    AntiTask linkedTo;
    
    public TransientTask(String name, String type, float startTime, float duration, int date) {
        super(name, type, date, startTime, duration);
        linkedTo = null;
    }

    /**
     * Links this transient task to an anti task
     * @param a
     */
    public void linkTo(AntiTask a) {
        linkedTo = a;
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

    /**
     * Checks if this TransientTask class is less than, equal to, or greater than a specified
     * other Task. The otehr Task should only be another TransientTask.
     * @param taskOther     The task to be compared to this Task.
     * @return      an integer representing whether this class is less than, equal to,
     *              or greater than the specified task.
     */
    public int compareTo(Task taskOther) {
        if(date < taskOther.date) {
            return -1;
        }
        else if(date > taskOther.date) {
            return 1;
        }
        else {
            if (startTime < taskOther.startTime) {
                return -1;
            }
            else if (startTime > taskOther.startTime) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

}
