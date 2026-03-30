package attendance;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public final class PersonContainer {
    private List<Person> people = new ArrayList<>();
    private final static PersonContainer INSTANCE = new PersonContainer();
    private Semester v2026 = new Semester(2, 22, 2026, "v2026");
    private List<Semester> semesters = new ArrayList<>();
    private List<Subject> subjects = new ArrayList<>();
    
    private PersonContainer() {
        /* people.add(new Person("Angelica"));
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
        people.getLast().removeSubject(ade); */
        semesters.add(v2026);
    }
    
    public static PersonContainer  getInstance() {
        return INSTANCE;
    }
    
    public void addPerson(Person person) {
        //TODO: validering
        this.people.add(person);
    }

    public void removePerson(Person person) {
        //TODO: Validering
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
        //TODO: Validering
        subjects.remove(subject);
        people.stream()
              .filter(person -> person.getSubjects().contains(subject))
              .forEach(person -> person.removeSubject(subject));
    }


    public void addSemester(Semester semester) {
        if (semester == null) {
            throw new IllegalArgumentException("Semester cannot ve null");
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
    }

    public List<Semester> getSemesters() {
        return List.copyOf(semesters);
    }

    public List<Lecture> getWeekLectures(int week, int year) {
        //TODO: Check if week is in semester
        Calendar startOfWeek = Calendar.getInstance();
        startOfWeek.setWeekDate(year,week,Calendar.SUNDAY);

        Calendar endOfWeek = Calendar.getInstance();
        endOfWeek.setWeekDate(year,week+1,Calendar.SUNDAY);
        

        return people.stream().flatMap(person -> person.getTotalLectures(startOfWeek, endOfWeek).stream()).distinct().sorted((o1,o2)->o1.getDate().compareTo(o2.getDate())).toList();
        
    }

    public void writeToFile(String filePath) {
        File file = new File(filePath);
        
        try  {
            FileWriter output = new FileWriter(file);
            CSVWriter writer = new CSVWriter(output);
            List<Lecture> lectures = subjects.stream().flatMap(p -> p.getLectures().stream()).toList();
            for (Lecture lecture : lectures) {
                String[] lines = new String[9];
                lines[0] = lecture.getSubject().getCode();
                lines[1] = lecture.getSubject().getName();
                lines[2] = Long.toString(lecture.getDate().getTimeInMillis());
                lines[3] = Integer.toString(lecture.getSubject().getSemester().getStartWeek());
                lines[4] = Integer.toString(lecture.getSubject().getSemester().getEndWeek());
                lines[5] = Integer.toString(lecture.getSubject().getSemester().getYear());
                lines[6] = lecture.getSubject().getSemester().getName();
                lines[7] = people.stream().filter(p -> p.getAttendedLectures().contains(lecture)).map(p -> p.getName()).collect(Collectors.joining(";"));
                lines[8] = people.stream().filter(p -> p.getTotalLectures().contains(lecture)).map(p -> p.getName()).collect(Collectors.joining(";"));

                writer.writeNext(lines);
            }
                writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    public void readFromFile(String filePath) {
        try  {
            this.people = new ArrayList<>();
            this.semesters = new ArrayList<>();
            this.subjects = new ArrayList<>();
            FileReader input = new FileReader(filePath);
            CSVReader reader = new CSVReader(input);

            List<String[]> output = reader.readAll();

            for (String[] strings : output) {
                Semester semester = this.semesters.stream().filter(s -> (Integer.toString(s.getStartWeek()).equals(strings[3]) && 
                                                        Integer.toString(s.getEndWeek()).equals(strings[4])) && 
                                                        Integer.toString(s.getYear()).equals(strings[5]) &&
                                                        s.getName().equals(strings[6])
                                                    )
                                                        .findAny()
                                                        .orElse(new Semester(Integer.parseInt(strings[3]), Integer.parseInt(strings[4]), Integer.parseInt(strings[5]),strings[6]));
                if (!this.semesters.contains(semester)) {
                    this.semesters.add(semester);
                }

                Subject subject = this.subjects.stream().filter(s -> s.getCode().equals(strings[0]))
                                                        .findAny()
                                                        .orElse(new Subject(strings[1], strings[0], semester));
                if (!this.subjects.contains(subject)) {
                    this.subjects.add(subject);
                }

                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(Long.parseLong(strings[2]));
                subject.addLecture(date);

                String[] peopleData = strings[8].split("[;]");
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
                    String[] attendedData = strings[7].split("[;]");
                    if (List.of(attendedData).contains(newPerson.getName())) {
                        newPerson.addLecture(subject.getLectures().getLast());
                    }
                    
                }

            }

        


            reader.close();;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CsvException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }


}
