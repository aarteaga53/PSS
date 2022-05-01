public class AntiTask extends Task {
    
    int date;

    public AntiTask(String name, String type, float startTime, float duration, int date) {
        super(name, type, startTime, duration);
        this.date = date;
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
     * Checks if this task occurs on the specified date. It is not meant to be called
     * in this class. It is only implemented here to give a descriptive error message.
     * @param date
     */
    public boolean doesOccurOn(int date) {
        System.out.println("occursOn() is not meant to be supported by the AntiTask class");
        return false;
    }
}
