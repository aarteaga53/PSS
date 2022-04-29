import java.util.ArrayList;

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

    public boolean overlaps(float time, float dur, int date) {
        if(frequency == 1 && (date >= startDate && date <= endDate)) {
            if(time == startTime)
                return true;
            else if(time < startTime && time + dur > startTime)
                return true;
            else if(time > startTime && startTime + duration > time)
                return true;
        }
        else {
            ArrayList<Integer> dates = new ArrayList<>();
            int i = 0;

            dates.add(startDate);

            while(true) {
                int year = (dates.get(i) - dates.get(i) % 10000) / 10000;
                int month = ((dates.get(i) - dates.get(i) % 100) - year * 10000) / 100;
                int day = dates.get(i) - year * 10000 - month * 100;
                int nextWeek = dates.get(i) + 7;

                if(nextWeek > endDate)
                    return true;

                
            }
        }

        return false;
    }

    /**
     * Converts the task to JSON
     */
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