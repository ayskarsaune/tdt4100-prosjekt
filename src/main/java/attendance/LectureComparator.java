package attendance;

import java.util.Comparator;

public class LectureComparator implements Comparator<Lecture> {
    @Override
    public int compare(Lecture o1, Lecture o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
    
}
