package attendance;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Lecture {
    private Calendar date;
    private Subject subject;

    public Lecture(Calendar date, Subject subject) {
        //TODO: Legg til validering
        this.date = date;
        this.subject = subject;
    }

    public Calendar getDate() {
        return this.date;
    }

    public Subject getSubject() {
        return this.subject;
    }




    @Override
    public String toString() {
        SimpleDateFormat format1 = new SimpleDateFormat("d. MMMM"); 
        return subject.getName() + " - " + subject.getCode() + " forelesning " + format1.format(this.date.getTime());
    }
}
