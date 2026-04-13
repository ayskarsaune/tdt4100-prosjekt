package attendance;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubjectTest {
    private Person person;
    private Semester semester;
    private Subject subject;
    private Lecture lecture;
    

    @BeforeEach
    public void setUp() {
        semester = new Semester(2, 17, 2026, "V2026");
        subject = new Subject("Matte 3", "TMA4422", semester);
        person = new Person("Stine");
        lecture = new Lecture(LocalDate.of(2026, 4, 21), subject);
    }

    @Test
    public void testAddLecture() {
        assertEquals(subject.getLectures().size(), 1);
        assertDoesNotThrow(() -> subject.addLecture(LocalDate.of(2026, 4, 21)));
        assertEquals(subject.getLectures().size(), 2);
        assertDoesNotThrow(() -> subject.addLecture(4,5));
        assertEquals(subject.getLectures().size(), 3);

        assertThrows(IllegalArgumentException.class, () -> subject.addLecture(null));
        assertThrows(IllegalArgumentException.class, () -> subject.addLecture(1,5));
        assertThrows(IllegalArgumentException.class, () -> subject.addLecture(3,8));

    }

    @Test
    public void testRemoveLecture() {
    
        assertThrows(IllegalArgumentException.class, () -> subject.removeLecture(null));
        assertDoesNotThrow(() -> subject.removeLecture(new Lecture(LocalDate.of(2026, 4, 21), subject)));
        assertDoesNotThrow(() -> subject.removeLecture(lecture));
        assertThrows(IllegalStateException.class, () -> subject.removeLecture(lecture));
    }
}
