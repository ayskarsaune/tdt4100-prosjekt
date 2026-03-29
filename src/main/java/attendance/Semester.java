package attendance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Semester {
    private int startWeek;
    private int endWeek;
    private int year;
    private String name;
    private List<Subject> subjects = new ArrayList<>();

    public Semester(int startWeek, int endWeek, int year, String name) {
        if (startWeek < 1 || startWeek > 51 || endWeek < 2 || endWeek > 52) {
            throw new IllegalArgumentException("Start and endweek must be between 1 and 52");
        } else if (endWeek-startWeek <= 0) {
            throw new IllegalArgumentException("Startweek must be before endWeek");
        } 
        

        this.startWeek = startWeek;
        this.endWeek = endWeek;
        setYear(year);
        setName(name);

    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        if (startWeek < 1 || startWeek > 51) {
            throw new IllegalArgumentException("startWeek must be between 1 and 51");
        } else if (endWeek-this.startWeek <= 0) {
            throw new IllegalArgumentException("Startweek must be before endWeek");
        }
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        if (endWeek < 2 || endWeek > 52) {
            throw new IllegalArgumentException("endWeek must be between 2 and 52");
        } else if (this.endWeek-startWeek <= 0) {
            throw new IllegalArgumentException("Startweek must be before endWeek");
        }
        this.endWeek = endWeek;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year < 1970 || year > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Year can neither be in the future, nor too far in the past");
        }
        this.year = year;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Semester must have name");
        }
        this.name = name;
    }

    public void addSubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        } else if (subjects.contains(subject)) {
            throw new IllegalStateException("Cannot add same subject twice");
        }
        subjects.add(subject);
    }

    public List<Subject> getSubjects() {
        return List.copyOf(this.subjects);
    }

    @Override
    public String toString() {
        return this.name;
    }

    
}
