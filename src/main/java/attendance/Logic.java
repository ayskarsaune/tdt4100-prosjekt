package attendance;

import java.util.ArrayList;
import java.util.List;

public final class Logic {
    private List<Person> people = new ArrayList<>();
    private final static Logic INSTANCE = new Logic();
    private Semester v2026 = new Semester(2, 22, 2026);
    
    private Logic() {}
    
    public static Logic  getInstance() {
        return INSTANCE;
    }
    
    public void setPerson(Person person) {
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


}
