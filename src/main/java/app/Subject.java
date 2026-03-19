package app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Subject {
    private String name;
    private String code;
    private List<Lecture> lectures = new ArrayList<>();
    private List<Integer> daysOfWeek = new ArrayList<>();
    private Semester semester;

    public Subject(String name, String code, List<Integer> daysOfWeek, Semester semester) {
        setName(name);
        setCode(code);
        setDaysOfWeek(daysOfWeek);
        setSemester(semester);
        

        for (int i = 0; i < semester.getEndWeek()-semester.getStartWeek();i++) {
            for (int j = 0; j < this.daysOfWeek.size();j++) {
                Calendar date = Calendar.getInstance();
                date.setWeekDate(semester.getYear(), semester.getStartWeek()+i, daysOfWeek.get(j)+1);
                System.out.println();
                lectures.add(new Lecture(date,this));
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Subject name must be defined");
        }
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Subject code must be defined");
        }
        this.code = code;
    }

    public List<Integer> getDaysOfWeek() {
        return List.copyOf(daysOfWeek);
    }

    public void setDaysOfWeek(List<Integer> daysOfWeek) {
        //TODO: Sjekk at det ikke er duplikate dager
        if (daysOfWeek.isEmpty() || daysOfWeek == null) {
            throw new IllegalArgumentException("Subject cannot have no days of lectures");
        } else if (daysOfWeek.stream().anyMatch((day) -> day > 7 || day < 1)) {
            throw new IllegalArgumentException("Day of week must be between 1 and 7");
        }
        this.daysOfWeek = daysOfWeek;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
         if (semester == null) {
            throw new IllegalArgumentException("Semester cannot be null");
        }
        this.semester = semester;
    }

    public List<Lecture> getLectures() {
        return List.copyOf(this.lectures);
    }

    public Lecture getLecture(int index) {
        return this.lectures.get(index);
    }

    public List<Lecture> getWeekLectures(int week) {
        if (week < semester.getStartWeek() || week > semester.getEndWeek()) {
            throw new IllegalArgumentException("The subject is not this week");
        }
        return this.lectures.stream().filter((lecture) -> (lecture.getDate().get(Calendar.WEEK_OF_YEAR))==week).toList();
    }

    public static void main(String[] args) {
        Semester v2026 = new Semester(2, 22, 2026);
        Subject matte = new Subject("Matte 3", "TMA4422", List.of(2,4), v2026);
        Subject objekt = new Subject("Objektorientert Programmering", "TDT4100", List.of(2,5), v2026);
        


        List<Lecture> matteTimer = matte.getLectures();
        for (Lecture lecture : matteTimer) {
            System.out.println(lecture);
        }

        System.out.println(matte.getWeekLectures(7));


        Person person = new Person("Angelica");
        person.addSubject(matte);
        person.addSubject(objekt);
        person.addLecture(matte.getWeekLectures(7).getFirst());
        person.addLecture(objekt.getWeekLectures(7).getLast());
        System.out.println(person.getAttendedLectures());
        System.out.println(person.getAttendedLectures(matte));
    }
}
