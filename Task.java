public class Task implements Comparable<Task> {

    String name;
    String type;
    int date;
    float startTime;
    float duration;

    public Task(String type) {
        this.type = type;
    }

    public Task(String name, String type, int date, float startTime, float duration) {
        this.name = name;
        this.type = type;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
    }

    /**
     * Checks if the task is Anti
     * @return
     */
    public boolean isAnti() {
        return type.equals("Cancellation");
    }

    /**
     * Checks if the task is Transient
     * @return
     */
    public boolean isTransient() {
        return type.equals("Visit") || 
                type.equals("Shopping") || type.equals("Appointment");
    }

    /**
     * Checks if the task is Recurring
     * @return
     */
    public boolean isRecurring() {
        return type.equals("Class") ||
                type.equals("Study") || type.equals("Sleep") ||
                type.equals("Sleep") || type.equals("Exercise") ||
                type.equals("Work") || type.equals("Meal");
    }

    /**
     * Converts the date to be user readable
     * @param date
     * @return
     */
    public String dateConversion(int date) {
        String d = String.valueOf(date);
        return d.substring(4, 6) + "/" + d.substring(6, 8) + "/" + d.substring(0, 4);
    }

    /**
     * Converts float duration to a user readable String
     * @return  String of duration
     */
    public String durationConversion() {
        String dur = "";
        int hour = (int) duration;
        float minute = duration - hour;

        if(minute == 0)
            dur = ":00";
        else if(minute == .25)
            dur = ":15";
        else if(minute == .5)
            dur = ":30";
        else if(minute == .75)
            dur = ":45";

        return hour + dur;
    }

    /**
     * Converts float time to a user readable String
     * @return  String of time
     */
    public String timeConversion() {
        String time = "";
        int hour = (int) startTime;
        float minute = startTime - hour;

        if(minute == 0)
            time = ":00 ";
        else if(minute == .25)
            time = ":15 ";
        else if(minute == .5)
            time = ":30 ";
        else if(minute == .75)
            time = ":45 ";

        if(hour == 0)
            time = "12" + time + "am";
        else if(hour == 12)
            time = "12" + time + "pm";
        else if(hour < 12)
            time = hour + time + "am";
        else if(hour > 12)
            time = (hour - 12) + time + "pm";

        return time;
    }

    /**
     * If the dates match, checks if there is overlap
     * @param task
     * @return
     */
    public boolean conflicts(Task task) {
        if(task.date == date) {
            return overlaps(task);
        }

        return false;
    }

    /**
     * Checks if the times overlap
     * @param task
     * @return
     */
    public boolean overlaps(Task task) {
        if(task.startTime == startTime)
            return true;
        else if(task.startTime < startTime && task.startTime + task.duration > startTime)
            return true;
        else if(task.startTime > startTime && startTime + duration > task.startTime)
            return true;

        return false;
    }

    /**
     * JSON format of one task
     * @return  String of task in JSON
     */
    public String convertJSON() {
        return "\t{\n\t\t\"Name\" : \"" + name + "\",\n" +
                "\t\t\"Type\" : \"" + type + "\",\n" +
                "\t\t\"StartTime\" : " + startTime + ",\n" +
                "\t\t\"Duration\" : " + duration + ",\n\t}";
    }

    public String toString() {
        return name + "\n" + type + "\n" + timeConversion() + "\n" + durationConversion();
    }

    /**
     * Checks if this Task class is less than, equal to, or greater than a specified
     * other Task. This method is not meant to be called by this Task class, but it is
     * implemented here for error detection, in case one thought they were calling the
     * corresponding method of the TransientTask subclass.
     * @param taskOther     The task to be compared to this Task.
     * @return      an integer representing whether this class is less than, equal to,
     *              or greater than the specified task.
     */
    public int compareTo(Task taskOther) {
        //System.out.println("compareTo() is not meant to be supported by the Task class");
        if (startTime < taskOther.startTime) {
            return -1;
        }
        else if (startTime == taskOther.startTime) {
            return 0;
        }
        else {
            return 1;
        }
    }

}