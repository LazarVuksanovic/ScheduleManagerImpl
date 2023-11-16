package rs.raf.schedulemanagerimpl;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import rs.raf.Place;
import rs.raf.Schedule;
import rs.raf.Term;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ScheduleImpl extends Schedule {
    List<String> headers;
    public ScheduleImpl(){
        super();
        headers = new ArrayList<>();
    }

    public ScheduleImpl(List<Term> terms){
        super(terms);
        List<String> headers;
    }

    public ScheduleImpl(File file) throws IOException {
        super();
        List<String> headers;
        this.makeSchedule(file);
    }

    public ScheduleImpl(List<Term> terms, List<Place> places){
        super(terms, places);
        List<String> headers;
    }

    @Override
    public void loadPlaces(File file) throws IOException {

    }

    @Override
    public void makeSchedule() {

    }

    @Override
    public void makeSchedule(File file) throws IOException {
        FileReader fr = new FileReader(file);
        CSVReader reader = new CSVReaderBuilder(fr).build();
        headers = Arrays.stream(reader.readNext()).toList();

    }

    @Override
    public void makeSchedule(List<Term> list) {

    }

    @Override
    public void makeSchedule(Schedule schedule) {

    }

    @Override
    public void addTerm(Term term) {

    }

    @Override
    public void addTerm(LocalDate localDate, LocalTime localTime, LocalTime localTime1, Place place) {

    }

    @Override
    public void addTerm(LocalDate localDate, LocalTime localTime) {

    }

    @Override
    public void deleteTerm(Term term) {

    }

    @Override
    public void deleteTermInDateSpan(LocalDate localDate, LocalDate localDate1) {

    }

    @Override
    public void deleteTermInDateAndTimeSpan(LocalDate localDate, LocalDate localDate1, LocalTime localTime, LocalTime localTime1) {

    }

    @Override
    public void deleteTerm(LocalDate localDate, LocalTime localTime) {

    }

    @Override
    public void deleteTerm(LocalDate localDate, LocalTime localTime, LocalTime localTime1) {

    }

    @Override
    public void deleteTerm(Place place) {

    }

    @Override
    public void deleteTerm(Place place, LocalDate localDate, LocalTime localTime, LocalTime localTime1) {

    }

    @Override
    public void deleteTerm(Place place, LocalDate localDate, LocalDate localDate1, LocalTime localTime, LocalTime localTime1) {

    }

    @Override
    public void moveTerm(Term term, LocalDate localDate, LocalTime localTime) {

    }

    @Override
    public void moveTerm(Term term, LocalDate localDate, LocalTime localTime, Place place) {

    }

    @Override
    public void searchTerm(Place place) {

    }

    @Override
    public void searchTerm(Place place, LocalDate localDate, LocalDate localDate1) {

    }

    @Override
    public void searchTerm(Place place, Map<String, Integer> map) {

    }

    @Override
    public void searchTerm(Place place, LocalDate localDate, LocalDate localDate1, LocalTime localTime, LocalTime localTime1) {

    }

    @Override
    public void searchTerm(Map<String, Integer> map) {

    }

    @Override
    public void searchTerm(LocalDate localDate, LocalDate localDate1, LocalTime localTime, LocalTime localTime1) {

    }
}
