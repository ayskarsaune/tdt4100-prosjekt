package attendance;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class PersonContainer {
    private List<Person> people = new ArrayList<>();
    private final static PersonContainer INSTANCE = new PersonContainer();
    private List<Semester> semesters = new ArrayList<>();
    private List<Subject> subjects = new ArrayList<>();
    
    public static PersonContainer  getInstance() {
        return INSTANCE;
    }
    
    public void addPerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        } else if (people.contains(person)) {
            throw new IllegalStateException("Cannot add same person twice");
        }
        this.people.add(person);
    }

    public void removePerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        } else if (!people.contains(person)) {
            throw new IllegalStateException("Cannot remove person who is not in people");
        }
        this.people.remove(person);
    }
    
    public List<Person> getPeople() {
        return List.copyOf(this.people);
    }

    public void addSubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        } else if (subjects.contains(subject)) {
            throw new IllegalArgumentException("Cannot add same subject twice");
        }

        this.subjects.add(subject);
    }

    public List<Subject> getSubjects() {
        return List.copyOf(this.subjects);
    }
    public List<Subject> getSubjects(Semester semester) {
        return getPeople().stream().flatMap(person -> person.getSubjects().stream()).filter(subject -> subject.getSemester()==semester).distinct().toList();
    }

    public void removeSubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject removed cannot be null");
        } else if (!subjects.contains(subject)) {
            throw new IllegalStateException("Cannot remove a subject which isnt in subjects");
        }
        subjects.remove(subject);
        people.stream()
              .filter(person -> person.getSubjects().contains(subject))
              .forEach(person -> person.removeSubject(subject));
    }


    public void addSemester(Semester semester) {
        if (semester == null) {
            throw new IllegalArgumentException("Semester cannot be null");
        } else if (semesters.contains(semester)) {
            throw new IllegalStateException("Semester already in semesters");
        }
        System.out.println("Semester added");
        semesters.add(semester);
    }

    public void removeSemester(Semester semester) {
        if (semester == null) {
            throw new IllegalArgumentException("Semester cannot be null");
        } else if (!semesters.contains(semester)) {
            throw new IllegalStateException("Cannot remove semester that does not exist in list");
        }  
        for (Subject subject : semester.getSubjects()) {
            removeSubject(subject);
        }  
        this.semesters.remove(semester);
    }

    public List<Semester> getSemesters() {
        return List.copyOf(semesters);
    }

    public List<Lecture> getWeekLectures(int week, int year) {
        LocalDate startOfWeek = LocalDate.of(year,1,1).with(IsoFields.WEEK_OF_WEEK_BASED_YEAR,week).with(DayOfWeek.MONDAY);

        LocalDate endOfWeek = LocalDate.of(year,1,1).with(IsoFields.WEEK_OF_WEEK_BASED_YEAR,week).with(DayOfWeek.SUNDAY);

        return people.stream().flatMap(person -> person.getTotalLectures(startOfWeek, endOfWeek).stream()).distinct().sorted(new LectureComparator()).toList();
        
    }

    public void clear() {
        this.people.clear();
        this.semesters.clear();
        this.subjects.clear();
    }

    public void writeToFile(String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            List<Lecture> lectures = subjects.stream().flatMap(p -> p.getLectures().stream()).toList();
            for (Lecture lecture : lectures) {
                String[] lines = new String[10];
                lines[0] = lecture.getSubject().getCode();
                lines[1] = lecture.getSubject().getName();
                lines[2] = Integer.toString(lecture.getDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
                lines[3] = Integer.toString(lecture.getDate().getDayOfWeek().getValue());
                lines[4] = Integer.toString(lecture.getSubject().getSemester().getStartWeek());
                lines[5] = Integer.toString(lecture.getSubject().getSemester().getEndWeek());
                lines[6] = Integer.toString(lecture.getSubject().getSemester().getYear());
                lines[7] = lecture.getSubject().getSemester().getName();
                lines[8] = people.stream().filter(p -> p.getAttendedLectures().contains(lecture)).map(p -> p.getName()).collect(Collectors.joining(","));
                lines[9] = people.stream().filter(p -> p.getTotalLectures().contains(lecture)).map(p -> p.getName()).collect(Collectors.joining(","));

                fileWriter.write(String.join(";", lines));
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    public void readFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            this.people = new ArrayList<>();
            this.semesters = new ArrayList<>();
            this.subjects = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] strings = line.split("[;]");
                Semester semester = this.semesters.stream().filter(s -> (Integer.toString(s.getStartWeek()).equals(strings[4]) && 
                                                        Integer.toString(s.getEndWeek()).equals(strings[5])) && 
                                                        Integer.toString(s.getYear()).equals(strings[6]) &&
                                                        s.getName().equals(strings[7])
                                                    )
                                                        .findAny()
                                                        .orElse(new Semester(Integer.parseInt(strings[4]), Integer.parseInt(strings[5]), Integer.parseInt(strings[6]),strings[7]));
                if (!this.semesters.contains(semester)) {
                    this.semesters.add(semester);
                }

                Subject subject = this.subjects.stream().filter(s -> s.getCode().equals(strings[0]))
                                                        .findAny()
                                                        .orElse(new Subject(strings[1], strings[0], semester));
                if (!this.subjects.contains(subject)) {
                    this.subjects.add(subject);
                }

                LocalDate date = LocalDate.of(Integer.parseInt(strings[6]),1,1)
                                .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, Integer.parseInt(strings[2]))
                                .with(DayOfWeek.of(Integer.parseInt(strings[3])));
                

                subject.addLecture(date);

                String[] peopleData = strings[9].split("[,]");
                for (String person : peopleData) {
                    if (person.isBlank()) {
                        break;
                    }
                    Person newPerson = people.stream().filter(p -> p.getName().equals(person)).findAny().orElse(new Person(person));
                    if (!this.people.contains(newPerson)) {
                        this.people.add(newPerson);
                    }
                    if (!newPerson.getSubjects().contains(subject)) {
                        newPerson.addSubject(subject);
                    }
                    String[] attendedData = strings[8].split("[,]");
                    if (List.of(attendedData).contains(newPerson.getName())) {
                        newPerson.addLecture(subject.getLectures().getLast());
                    } 
                }/*  */
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }


}
