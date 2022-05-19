
public class Task implements Comparable<Task> {

    String name;
    String type;
    int date;
    float startTime;
    float duration;

    public Task(String type) {
        this.setType(type);
    }

    public Task(String name, String type, int date, float startTime, float duration) {
        this.setName(name);
        this.setType(type);
        this.setDate(date);
        this.setStartTime(startTime);
        this.setDuration(duration);
    }

    /**
     * Checks if the task is Anti
     * @return
     */
    public boolean isAnti() {
        boolean isAnti = getType().equals("Cancellation");

        return isAnti;
    }

    /**
     * Checks if the task is Transient
     * @return
     */
    public boolean isTransient() {
        boolean isTransient = getType().equals("Visit") || 
        getType().equals("Shopping") || getType().equals("Appointment");

        if(getName() != null) {
            try {
                TransientTask temp = (TransientTask) this;
                temp.setName(getName());
                isTransient = true;
            } catch(ClassCastException e) {
                isTransient = false;
            }
        }

        return isTransient;
    }

    /**
     * Checks if the task is Recurring
     * @return
     */
    public boolean isRecurring() {
        boolean isRecurring = getType().equals("Class") ||
        getType().equals("Study") || getType().equals("Sleep") ||
        getType().equals("Sleep") || getType().equals("Exercise") ||
        getType().equals("Work") || getType().equals("Meal");

        if(getName() != null) {
            try {
                RecurringTask temp = (RecurringTask) this;
                temp.setName(getName());
                isRecurring = true;
            } catch(ClassCastException e) {
                isRecurring = false;
            }
        }

        return isRecurring;
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
        int hour = (int) getDuration();
        float minute = getDuration() - hour;

        if(minute == 0) {
            dur = ":00";
        }
        else if(minute == .25) {
            dur = ":15";
        }
        else if(minute == .5) {
            dur = ":30";
        }
        else if(minute == .75) {
            dur = ":45";
        }

        return hour + dur;
    }

    /**
     * Converts float time to a user readable String
     * @return  String of time
     */
    public String timeConversion() {
        String time = "";
        int hour = (int) getStartTime();
        float minute = getStartTime() - hour;

        if(minute == 0) {
            time = ":00 ";
        }
        else if(minute == .25) {
            time = ":15 ";
        }
        else if(minute == .5) {
            time = ":30 ";
        }
        else if(minute == .75) {
            time = ":45 ";
        }

        if(hour == 0) {
            time = "12" + time + "am";
        }
        else if(hour == 12) {
            time = "12" + time + "pm";
        }
        else if(hour < 12) {
            time = hour + time + "am";
        }
        else if(hour > 12) {
            time = (hour - 12) + time + "pm";
        }

        return time;
    }

    /**
     * If the dates match, checks if there is overlap
     * @param task
     * @return
     */
    public boolean conflicts(Task task) {
        if(task.isRecurring()) {
            return task.conflicts(this);
        }
        if(task.getDate() == getDate()) {
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
        if(task.getStartTime() == getStartTime()) {
            return true;
        }
        else if(task.getStartTime() < getStartTime() && task.getStartTime() + task.getDuration() > getStartTime()) {
            return true;
        }
        else if(task.getStartTime() > getStartTime() && getStartTime() + getDuration() > task.getStartTime()) {
            return true;
        }

        return false;
    }

    /**
     * JSON format of one task
     * @return  String of task in JSON
     */
    public String convertJSON() {
        return "  {\n    \"Name\" : \"" + getName() + "\",\n" +
                "    \"Type\" : \"" + getType() + "\",\n" +
                "    \"Date\" : " + getDate() + ",\n" +
                "    \"StartTime\" : " + getStartTime() + ",\n" +
                "    \"Duration\" : " + getDuration() + "\n  }";
    }

    public String toString() {
        return getName() + "\n" + getType() + "\n" + timeConversion() + "\n" + durationConversion();
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
        if (getStartTime() < taskOther.getStartTime()) {
            return -1;
        }
        else if (getStartTime() == taskOther.getStartTime()) {
            return 0;
        }
        else {
            return 1;
        }
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDate() {
        return this.date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public float getStartTime() {
        return this.startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getDuration() {
        return this.duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
    
}
