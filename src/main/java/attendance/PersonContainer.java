package attendance;

import java.util.List;

public class PersonContainer {
    private List<Person> people;

    public PersonContainer() {

    }

    public void addPerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        } else if (people.contains(person)) {
            throw new IllegalStateException("Person is already added");
        }
        people.add(person);
    }

    public void removePerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        } else if (!people.contains(person)) {
            throw new IllegalStateException("Person isnt in group and cannot be removed");
        }
        people.remove(person);
    }

    public List<Person> getPeople() {
        return List.copyOf(this.people);
    }

    public List<Subject> getSubjects(Semester semester) {
        return getPeople().stream().flatMap(person -> person.getSubjects().stream()).distinct().toList();
    }

}
