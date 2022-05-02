public class TransientTask extends Task {
    
    public TransientTask(String name, String type, float startTime, float duration, int date) {
        super(name, type, date, startTime, duration);
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
    public int compareTo(Task other) {
        if(date < other.date) {
            return -1;
        }
        else if(date > other.date) {
            return 1;
        }
        else {
            if (startTime < other.startTime) {
                return -1;
            }
            else if (startTime > other.startTime) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

}
