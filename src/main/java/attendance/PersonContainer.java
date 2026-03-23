package attendance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public final class PersonContainer {
    private List<Person> people = new ArrayList<>();
    private final static PersonContainer INSTANCE = new PersonContainer();
    private Semester v2026 = new Semester(2, 22, 2026);
    
    private PersonContainer() {
        people.add(new Person("Angelica"));
        people.add(new Person("Nikolai"));
        people.add(new Person("Sofus"));
        people.add(new Person("Kristoffer"));
        people.add(new Person("Oliver"));
        people.add(new Person("Oskar"));
        people.add(new Person("Sander"));
        people.add(new Person("Tony"));
        people.add(new Person("Sven"));
        people.add(new Person("Don"));
        


        
        Subject matte = new Subject("Matte 3", "TMA4422", List.of(2,4), v2026);
        Subject objekt = new Subject("Objektorientert", "TDT4100", List.of(2,5), v2026);
        Subject ade = new Subject("ADE", "TTT4203", List.of(1,5), v2026);
        Subject mmi = new Subject("MMI", "TDT4180", List.of(3), v2026);
        

        List<Subject> subjects = List.of(matte,objekt,ade,mmi);

        for (Person person : people) {
            for (Subject subject : subjects) {
                person.addSubject(subject);
            }
        }
        people.getLast().removeSubject(ade);
    }
    
    public static PersonContainer  getInstance() {
        return INSTANCE;
    }
    
    public void addPerson(Person person) {
        //TODO: validering
        this.people.add(person);
    }
    
    public List<Person> getPeople() {
        return List.copyOf(this.people);
    }

    public List<Subject> getSubjects(Semester semester) {
        return getPeople().stream().flatMap(person -> person.getSubjects().stream()).filter(subject -> subject.getSemester()==semester).distinct().toList();
    }

    public Semester getSemester() {
        return this.v2026;
    }

    public List<Lecture> getWeekLectures(int week, Semester semester) {
        //TODO: Check if week is in semester
        Calendar startOfWeek = Calendar.getInstance();
        startOfWeek.setWeekDate(semester.getYear(),week,Calendar.SUNDAY);

        Calendar endOfWeek = Calendar.getInstance();
        endOfWeek.setWeekDate(semester.getYear(),week+1,Calendar.SUNDAY);
        
        return people.stream().flatMap(person -> person.getTotaLectures(startOfWeek, endOfWeek).stream()).distinct().sorted((o1,o2)->o1.getDate().compareTo(o2.getDate())).toList();

    }


}
