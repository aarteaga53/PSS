import java.util.ArrayList;

public class RecurringTask extends Task implements Cloneable {

    int endDate;
    int frequency;
    ArrayList<AntiTask> links;

    public RecurringTask(String name, String type, float startTime, float duration, int startDate, int endDate, int frequency) {
        super(name, type, startDate, startTime, duration);
        this.endDate = endDate;
        this.frequency = frequency;
        links = new ArrayList<>();
    }

    public void addLink(AntiTask link) {
        links.add(link);

        try {
            link.linkTo(clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void removeLink(AntiTask link) {
        links.remove(link);
    }

    public boolean hasLink(Task link) {
        for(AntiTask a : links) {
            if((!a.conflicts(link) && a.date == link.date) && link.isTransient())
                return true;
            else if(a.conflicts(link) && !link.isTransient())
                return true;
        }

        return false;
    }

    /**
     * Checks if a task's times conflict
     */
    public boolean conflicts(Task task) {
        if(overlaps(task)) {
            int next = date;

            if(!task.isRecurring()) {
                while(next <= endDate && next <= task.date) {
                    if(next == task.date) {
                        if((task.isAnti() && (task.startTime == startTime && task.duration == duration)) && !hasLink(task)) {
                            addLink((AntiTask) task);
                            return false;
                        }
                        else if(task.isTransient() && hasLink(task))
                            return false;   
                        else
                            return true;
                    }
    
                    next = nextDate(next, frequency);
                }
            }
            else {
                ArrayList<Integer> dates = new ArrayList<>();
                ArrayList<Integer> otherDates = new ArrayList<>();
                RecurringTask r = (RecurringTask) task;

                while(next <= endDate) {
                    dates.add(next);
                    next = nextDate(next, frequency);
                }

                next = r.date;

                while(next <= r.endDate) {
                    otherDates.add(next);
                    next = nextDate(next, r.frequency);
                }

                for(int d : dates) {
                    if(otherDates.contains(d))
                        return true;
                }
            }
            
        }

        return false;
    }

    /**
     * Increments the date by the frequency, 1 or 7 days
     * @param next
     * @param freq
     * @return
     */
    public int nextDate(int next, int freq) {
        int year = (next - next % 10000) / 10000;
        int month = ((next - next % 100) - year * 10000) / 100;
        int day = next - year * 10000 - month * 100;

        day += freq;

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
        else if(month == 2) {
            if(year % 4 == 0) {
                if(year % 100 == 0) {
                    if(year % 400 == 0 && day > 29){
                        day -= 29;
                        month++;
                    }
                }
                else{
                    if(day > 29){
                        day -= 29;
                        month++;
                    }
                }
            }
            else if(day > 28){
                day -= 28;
                month++;
            }
        }
        // else if(month == 2 && day > 28) {
        //     if(year % 4 == 0 && year % 100 == 0 && year % 400 == 0 && day > 29)
        //         day--;
        
        //     month++;
        //     day -= 28;
        // }

        return year * 10000 + month * 100 + day;
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

    @Override
    public RecurringTask clone() throws CloneNotSupportedException {
        return (RecurringTask) super.clone();    // return shallow copy
    }

}