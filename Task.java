public class Task {

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

    public boolean conflicts(Task t) {
        if(t.date == date) {
            if(t.startTime == startTime)
                return true;
            else if(t.startTime < startTime && t.startTime + t.duration > startTime)
                return true;
            else if(t.startTime > startTime && startTime + duration > t.startTime)
                return true;
        }

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

}