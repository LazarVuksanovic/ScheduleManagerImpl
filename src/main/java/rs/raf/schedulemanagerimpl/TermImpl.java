package rs.raf.schedulemanagerimpl;

import rs.raf.Place;
import rs.raf.Term;

import java.time.LocalDate;
import java.time.LocalTime;

public class TermImpl extends Term {
    public TermImpl(LocalDate date, LocalTime timeStart, LocalTime timeEnd, Place place) {
        super(date, timeStart, timeEnd, place);
    }
}
