package app;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;
    private List<Subject> subjects = new ArrayList<>();
    private List<Lecture> attendedLectures = new ArrayList<>();

    public Person(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public void addSubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        } else if (subjects.contains(subject)) {
            throw new IllegalStateException("Cannot have the same subject twice");
        }
        subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        } else if (!subjects.contains(subject)) {
            throw new IllegalStateException("Cannot remove a subject one does not have");
        }
        subjects.remove(subject);
    }

    public void addLecture(Lecture lecture) {
        if (lecture == null) {
            throw new IllegalArgumentException("Lecture cannot be null");
        } else if (attendedLectures.contains(lecture)) {
            throw new IllegalStateException("Cannot attend the same lecture twice");
        } else if (!subjects.contains(lecture.getSubject())) {
            throw new IllegalStateException("Attended lectures in subject one does not have do not count");
        }
        attendedLectures.add(lecture);
    }

    

    public void removeLecture(Lecture lecture) {
        if (lecture == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        } else if (!this.attendedLectures.contains(lecture)) {
            throw new IllegalStateException("Cannot remove a lecture that wasnt attended");
        }
        this.attendedLectures.remove(lecture);
    }

    public List<Lecture> getAttendedLectures() {
        return List.copyOf(this.attendedLectures);
    }

    public List<Lecture> getAttendedLectures(Subject subject) {
        if (!this.subjects.contains(subject)) {
            throw new IllegalStateException("Cannot get attendance from subject one is not enrolled in");
        }
        return attendedLectures.stream().filter((lecture) -> lecture.getSubject()==subject).toList();
    }

}
