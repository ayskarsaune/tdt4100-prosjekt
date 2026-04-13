package attendance;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PersonContainerTest {
    private Person person;
    private Semester semester;
    private Subject subject;
    private PersonContainer container;
    

    @BeforeEach
    public void setUp() {
        semester = new Semester(2, 17, 2026, "V2026");
        subject = new Subject("Matte 3", "TMA4422", List.of(1,2),semester);
        person = new Person("Stine");
        container = PersonContainer.getInstance();
        container.clear();
    }

    @Test
    public void testAddPeople() {
        assertThrows(IllegalArgumentException.class, () -> container.addPerson(null));
        assertDoesNotThrow(() -> container.addPerson(person));
        assertEquals(List.of(person), container.getPeople());
        assertThrows(IllegalStateException.class, () -> container.addPerson(person));
        
    }
    @Test
    public void testRemovePeople() {
        container.addPerson(person);
        assertThrows(IllegalArgumentException.class, () -> container.removePerson(null));
        assertDoesNotThrow(() -> container.removePerson(person));
        assertEquals(List.of(), container.getPeople());
        assertThrows(IllegalStateException.class, () -> container.removePerson(person));
        
    }

    public void checkPersonList(List<String> names, List<Person> people) {
        for (int i = 0; i < names.size();i++) {
            assertEquals(names.get(i), people.get(i).getName());
        }
    }


    @Test
    public void testWriteAndReadFile() {
        container.addPerson(person);
        person.addSubject(subject);
        container.addSubject(subject);
        person.addLecture(subject.getLectures().stream().findAny().orElseThrow());
        container.writeToFile("test.csv");
        container.clear();
        container.readFromFile("test.csv");
        assertEquals(person.getName(), container.getPeople().getFirst().getName());
        assertEquals(person.getSubjects().getFirst().getCode(), container.getPeople().getFirst().getSubjects().getFirst().getCode());

    }

}
