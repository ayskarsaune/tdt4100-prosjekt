package attendance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Subject {
    private String name;
    private String code;
    private List<Lecture> lectures = new ArrayList<>();
    private List<Integer> daysOfWeek = new ArrayList<>();
    private Semester semester;
    private List<LectureListener> listeners = new ArrayList<>();

    public Subject(String name, String code, List<Integer> daysOfWeek, Semester semester) {
        setName(name);
        setCode(code);
        setDaysOfWeek(daysOfWeek);
        setSemester(semester);
        

        for (int i = 0; i < semester.getEndWeek()-semester.getStartWeek();i++) {
            for (int j = 0; j < this.daysOfWeek.size();j++) {
                Calendar date = Calendar.getInstance();
                date.setWeekDate(semester.getYear(), semester.getStartWeek()+i, daysOfWeek.get(j)+1);
                lectures.add(new Lecture(date,this));
            }
        }
    }

    public Subject(String name, String code, Semester semester) {
        setName(name);
        setCode(code);
        setSemester(semester);
        
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
        //TODO: Sjekk at det ikke er duplikate dager, kanskje også slette funksjonen og legge til i konstruktør siden det ikke brukes på noe annet punkt
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

    public void addLecture(int week, int day) {
        if (day > 7 || day < 1) {
            throw new IllegalArgumentException("Day of week must be between 1 and 7");
        } else if (week < this.semester.getStartWeek() || week > this.semester.getEndWeek()) {
            throw new IllegalArgumentException("Week must be in semester");
        }
        Calendar date = Calendar.getInstance();
        date.setWeekDate(semester.getYear(), week, day+1);
        lectures.add(new Lecture(date,this));
        lectures.sort(new LectureComparator());
    }
    public void addLecture(Calendar date) {
        lectures.add(new Lecture(date,this));
        lectures.sort(new LectureComparator());
    }

    public void removeLecture(Lecture lecture) {
        if (lecture == null) {
            throw new IllegalArgumentException("Cannot remove lecture if it is null");
        } else if (!this.lectures.contains(lecture)) {
            throw new IllegalStateException("Cannot remove lecture that this subject does not have");
        }
        this.lectures.remove(lecture);
        for (LectureListener listener : this.listeners) {
            listener.updateLecture(lecture);
        }
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

    public void addListener(LectureListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        } else if (this.listeners.contains(listener)) {
            throw new IllegalStateException("Listener is already added");
        }
        this.listeners.add(listener);
    }
    public void removeListener(LectureListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        } else if (!this.listeners.contains(listener)) {
            throw new IllegalStateException("Cannot remove as it is not listener");
        }
        this.listeners.remove(listener);
    }

    public static void main(String[] args) {
        Semester v2026 = new Semester(2, 22, 2026);
        Semester v2025 = new Semester(2, 22, 2025);
        Subject matte = new Subject("Matte 3", "TMA4422", List.of(2,4), v2026);
        Subject objekt = new Subject("Objektorientert Programmering", "TDT4100", List.of(2,5), v2025);
        


        List<Lecture> matteTimer = matte.getLectures();
        for (Lecture lecture : matteTimer) {
            System.out.println(lecture);
        }

        System.out.println(matte.getWeekLectures(7));

        PersonContainer info = PersonContainer.getInstance();
        List<Person> people = info.getPeople();
        List<Lecture> lectures = info.getWeekLectures(7, info.getSemester());
        //System.out.println(lectures);
        Person person = new Person("Angelica");
        person.addSubject(matte);
        person.addSubject(objekt);
        person.addLecture(matte.getWeekLectures(7).getFirst());
        person.addLecture(objekt.getWeekLectures(7).getLast());
        /* System.out.println(person.getAttendedLectures());
        System.out.println(person.getAttendedLectures(v2026));
        System.out.println(person.getAttendedLectures(matte));
 */

        Calendar startOfWeek = Calendar.getInstance();
        startOfWeek.setWeekDate(info.getSemester().getYear(),7,Calendar.MONDAY);

        Calendar endOfWeek = Calendar.getInstance();
        endOfWeek.setWeekDate(info.getSemester().getYear(),8,Calendar.SUNDAY);

        System.out.println(person.getTotalLectures());
        System.out.println(person.getTotalLectures(startOfWeek, endOfWeek));
        System.out.println(startOfWeek);
        System.out.println(endOfWeek);
        System.out.println(person.getTotalLectures().stream().filter((lecture) -> (lecture.getDate().after(startOfWeek) && lecture.getDate().before(endOfWeek))).toList());
        System.out.println(person.getTotalLectures().stream().filter((lecture) -> (lecture.getDate().after(startOfWeek))).filter((lecture) -> lecture.getDate().before(endOfWeek)).toList());


        //TODO: Fillagring og lesing, faktisk lære JavaFX
    }
}
