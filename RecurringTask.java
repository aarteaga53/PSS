public class RecurringTask extends Task {

    int endDate;
    int frequency;

    public RecurringTask(String name, String type, float startTime, float duration, int startDate, int endDate, int frequency) {
        super(name, type, startDate, startTime, duration);
        this.endDate = endDate;
        this.frequency = frequency;
    }

    public boolean conflicts(Task t) {
        if(frequency == 1 && (t.date >= date && t.date <= endDate)) {
            return overlaps(t.startTime, t.duration);
        }
        else if(overlaps(t.startTime, t.duration)) {
            int next = date;
            int year = (next - next % 10000) / 10000;
            int month = ((next - next % 100) - year * 10000) / 100;
            int day = next - year * 10000 - month * 100;

            while(next < endDate && next < t.date) {
                day += 7;

                if((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day > 31) {
                    if(month == 12) {
                        year++;
                        month = 1;
                    }
                    else
                        month++;

                    day -= 31;
                }
                else if((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
                    month++;
                    day -= 30;
                }
                else if(month == 2 && day > 28) {
                    if(year % 4 == 0 && year % 100 == 0 && year % 400 == 0 && day > 29)
                        day--;
        
                    month++;
                    day -= 28;
                }

                next = year * 10000 + month * 100 + day;

                if(next == t.date)
                    return true;
            }
        }

        return false;
    }

    public boolean overlaps(float time, float dur) {
        if(time == startTime)
            return true;
        else if(time < startTime && time + dur > startTime)
            return true;
        else if(time > startTime && startTime + duration > time)
            return true;

        return false;
    }

    /**
     * Converts the task to JSON
     */
    public String convertJSON() {
        return "\t{\n\t\t\"Name\" : \"" + name + "\",\n" +
                "\t\t\"Type\" : \"" + type + "\",\n" +
                "\t\t\"StartDate\" : " + date + ",\n" +
                "\t\t\"StartTime\" : " + startTime + ",\n" +
                "\t\t\"Duration\" : " + duration + ",\n" +
                "\t\t\"EndDate\" : " + endDate + ",\n" +
                "\t\t\"Frequency\" : " + frequency + "\n\t}";
    }

    public String toString() {
        return name + "\n" + type + "\n" + timeConversion() + "\n" + durationConversion() + "\n" + dateConversion(date) + "\n" + dateConversion(endDate) + "\n" + ((frequency == 1) ? "Daily" : "Weekly");
    }

}