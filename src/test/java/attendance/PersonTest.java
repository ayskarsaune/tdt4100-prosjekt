package attendance;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PersonTest {
    private Person person;
    private Semester semester;
    private Subject subject;
    

    @BeforeEach
    public void setUp() {
        semester = new Semester(2, 17, 2026, "V2026");
        subject = new Subject("Matte 3", "TMA4422", semester);
        person = new Person("Stine");
    }

    @Test
    public void testAddSubjects() {
        assertThrows(IllegalArgumentException.class, () -> person.addSubject(null));
        assertDoesNotThrow(() -> person.addSubject(subject));
    }

    @Test
    public void testRemoveSubjects() {
        person.addSubject(subject);
        assertThrows(IllegalArgumentException.class, () -> person.removeSubject(null));
        assertDoesNotThrow(() -> person.removeSubject(subject));
        assertThrows(IllegalStateException.class,() -> person.removeSubject(subject));
    }
}
