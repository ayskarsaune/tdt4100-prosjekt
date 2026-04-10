package attendance;

import java.time.LocalDate;

public class Lecture {
    private LocalDate date;
    private Subject subject;

    public Lecture(LocalDate date, Subject subject) {
        this.subject = subject;
        setDate(date);
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        if (!subject.getSemester().contains(date)) {
            throw new IllegalArgumentException("Date of lecture cannot be outside of the subject semester");
        }
        this.date = date;
    }

    public Subject getSubject() {
        return this.subject;
    }



}
