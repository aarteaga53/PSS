public class RecurringTask extends Task {

    int startDate;
    int endDate;
    int frequency;

    public RecurringTask(String name, String type, float startTime, float duration, int startDate, int endDate, int frequency) {
        super(name, type, startTime, duration);
        this.startDate = startDate;
        this.endDate = endDate;
        this.frequency = frequency;
    }

    public String convertJSON() {
        return "\t{\n\t\t\"Name\" : \"" + name + "\",\n" +
                "\t\t\"Type\" : \"" + type + "\",\n" +
                "\t\t\"StartDate\" : " + startDate + ",\n" +
                "\t\t\"StartTime\" : " + startTime + ",\n" +
                "\t\t\"Duration\" : " + duration + ",\n" +
                "\t\t\"EndDate\" : " + endDate + ",\n" +
                "\t\t\"Frequency\" : " + frequency + "\n\t}";
    }

    public String toString() {
        return name + "\n" + type + "\n" + timeConversion() + "\n" + durationConversion() + "\n" + dateConversion(startDate) + "\n" + dateConversion(endDate) + "\n" + ((frequency == 1) ? "Daily" : "Weekly");
    }

}