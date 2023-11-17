package rs.raf.schedulemanagerimpl;

import rs.raf.Place;
import rs.raf.Term;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public class TermImpl extends Term implements Comparable<Term> {
    private Map<String, String> info;
    public TermImpl(LocalDate date, LocalTime timeStart, LocalTime timeEnd, Place place, Map<String, String> info) {
        super(date, timeStart, timeEnd, place);
        this.info = info;
    }

    @Override
    public int compareTo(Term o) {
        int dateComparison = this.getDate().compareTo(o.getDate());

        if (dateComparison == 0)
            return this.getTimeStart().compareTo(o.getTimeStart());

        return dateComparison;
    }

    @Override
    public String toString() {
        return "TermImpl{" +
                "info=" + info +
                "date=" + super.getDate().toString() +
                "start=" + super.getTimeStart().toString() +
                "end=" + super.getTimeEnd().toString() +
                "place=" + super.getPlace().toString() +
                "}\n";
    }
}
