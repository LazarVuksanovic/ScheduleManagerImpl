package rs.raf.schedulemanagerimpl;

import rs.raf.Place;
import rs.raf.Term;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public class TermImpl extends Term {
    private Map<String, String> info;
    public TermImpl(LocalDate date, LocalTime timeStart, LocalTime timeEnd, Place place, Map<String, String> info) {
        super(date, timeStart, timeEnd, place);
        this.info = info;
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
