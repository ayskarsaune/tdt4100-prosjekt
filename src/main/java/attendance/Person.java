package attendance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Person implements LectureListener{
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
        subject.addListener(this);
    }

    public void removeSubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        } else if (!subjects.contains(subject)) {
            throw new IllegalStateException("Cannot remove a subject one does not have");
        }
        subjects.remove(subject);
    }

    public List<Subject> getSubjects() {
        return List.copyOf(this.subjects);
    }

    public void addLecture(Lecture lecture) {
        if (lecture == null) {
            throw new IllegalArgumentException("Lecture cannot be null");
        } else if (attendedLectures.contains(lecture)) {
            throw new IllegalStateException("Cannot attend the same lecture twice");
        } else if (!subjects.contains(lecture.getSubject())) {
            throw new IllegalStateException("Attended lectures in subjects one does not have do not count");
        } else if (!lecture.getSubject().getLectures().contains(lecture)) {
            throw new IllegalArgumentException("The subject does not have this lecture");
        }
        attendedLectures.add(lecture);
        attendedLectures.sort(new LectureComparator());
    }

    public void updateLecture(Lecture lecture) {
        if (this.attendedLectures.contains(lecture)) {
            removeLecture(lecture);
        }
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

    public List<Lecture> getAttendedLectures(Semester semester) {
        if (semester == null) {
            throw new IllegalArgumentException("Semester cannot be null");
        }
        return attendedLectures.stream().filter((lecture) -> lecture.getSubject().getSemester()==semester).toList();
    }

    public List<Lecture> getAttendedLectures(Calendar start, Calendar end) {
        return attendedLectures.stream().filter((lecture) -> lecture.getDate().after(start) && lecture.getDate().before(end)).toList();
    }

    public List<Lecture> getTotalLectures() {
        return subjects.stream().flatMap(subject -> subject.getLectures().stream()).toList();
    }

    public List<Lecture> getTotalLectures(Subject subject) {
        return subject.getLectures();
    }

    public List<Lecture> getTotalLectures(Calendar start, Calendar end) {
        return getTotalLectures().stream().filter((lecture) -> lecture.getDate().after(start) && lecture.getDate().before(end)).toList();
    }

}
