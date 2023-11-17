package rs.raf.schedulemanagerimpl;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import javafx.util.Pair;
import javafx.util.converter.LocalDateStringConverter;
import rs.raf.Place;
import rs.raf.Schedule;
import rs.raf.Term;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

public class ScheduleImpl extends Schedule {
    private List<String> order;
    private List<String> headers;
    public ScheduleImpl(){
        super();
        this.headers = new ArrayList<>();
        this.order = new ArrayList<>();
    }

    public ScheduleImpl(List<Term> terms){
        super(terms);
        this.headers = new ArrayList<>();
    }

    public ScheduleImpl(File file) throws IOException {
        super();
        this.headers = new ArrayList<>();
        this.order = new ArrayList<>();
        this.makeSchedule(file);
    }

    public ScheduleImpl(List<Term> terms, List<Place> places){
        super(terms, places);
        this.headers = new ArrayList<>();
        this.order = new ArrayList<>();
    }

    @Override
    public void loadPlaces(File file) throws IOException {
        FileReader fr = new FileReader(file);
        CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();
        CSVReader reader = new CSVReaderBuilder(fr).withCSVParser(parser).build();

        String[] line;
        while ((line = reader.readNext()) != null) {
            System.out.println(line[0]);
            PlaceImpl newPlace = new PlaceImpl(line[0], line[1]);
            if(line.length > 2){
                String[] inputProp = line[2].split(";");
                Map<String, Integer> properties = new HashMap<>();
                for (String property : inputProp) {
                    properties.put(property.substring(0, property.indexOf(":")),
                            Integer.parseInt(property.substring(property.indexOf(":")+1)));
                }
                newPlace.setProperties(properties);
            }

            this.getPlaces().add(newPlace);
        }
    }

    @Override
    public void makeSchedule() {
        this.headers = new ArrayList<>();
        this.order = new ArrayList<>();
    }

    @Override
    public void makeSchedule(File file) throws IOException {
        FileReader fr = new FileReader(file);
        CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();
        CSVReader reader = new CSVReaderBuilder(fr).withCSVParser(parser).build();

        for(String e : reader.readNext())
           order.add(e.replace("\"", ""));
        for(String e : reader.readNext())
            headers.add(e.replace("\"", ""));
        System.out.println(order);
        String[] line;
        while ((line = reader.readNext()) != null) {
            int orderIndex = 0;
            Map<String, String> terminfo = new HashMap<>();
            LocalDate date = LocalDate.now();
            LocalTime timeStart = LocalTime.now();
            LocalTime timeEnd = LocalTime.now();
            Place place = new PlaceImpl();

            for (String cell : line) {
                cell = cell.replace("\"", "");
                //System.out.println("cell " + cell);
                switch (order.get(orderIndex)) {
                    case "terminfo" -> terminfo.put(headers.get(orderIndex), cell);
                    case "date" -> date = LocalDate.of(2023, 11, 9);
                    case "time" -> {
                        String start = cell.substring(0, cell.indexOf('-'));
                        String end = cell.substring(cell.indexOf('-') + 1);
                        System.out.println("start: " + start + " end: " + end);
                        if (start.length() == 2)
                            timeStart = LocalTime.of(Integer.parseInt(start), 0); // Assuming the minute part is 0 for this case
                        else
                            timeStart = LocalTime.of(Integer.parseInt(start.substring(0, start.indexOf(":"))),
                                    Integer.parseInt(start.substring(start.indexOf(":") + 1)));

                        if (end.length() == 2)
                            timeEnd = LocalTime.of(Integer.parseInt(end), 0); // Assuming the minute part is 0 for this case
                        else
                            timeEnd = LocalTime.of(Integer.parseInt(start.substring(0, end.indexOf(":"))),
                                    Integer.parseInt(start.substring(end.indexOf(":") + 1)));
                    }
                    case "place" -> {
                        boolean exists = false;
                        for (Place p : super.getPlaces()) {
                            if (p.getName().equals(cell)) {
                                place = p;
                                exists = true;
                                break;
                            }
                        }
                        if(!exists){
                            place = new PlaceImpl(cell);
                            this.getPlaces().add(place);
                        }
                    }
                }
                orderIndex++;
            }
            TermImpl newTerm = new TermImpl(date, timeStart, timeEnd, place, terminfo);
            place.getTerms().add(newTerm);
            this.getTerms().add(newTerm);

        }
    }

    @Override
    public void makeSchedule(List<Term> list) {

    }

    @Override
    public void makeSchedule(Schedule schedule) {
        this.setTerms(schedule.getTerms());
        this.setPlaces(schedule.getPlaces());
    }

    @Override
    public void addTerm(Term term) {
        this.getTerms().add(term);
        boolean exists = false;
        for(Place p : this.getPlaces()){
            if (p.getName().equals(term.getPlace().getName())){
                exists = true;
            }
        }
        if(!exists)
            this.getPlaces().add(term.getPlace());
    }

    @Override
    public void addTerm(LocalDate localDate, LocalTime localTime, LocalTime localTime1, Place place) {
        this.getTerms().add(new TermImpl(localDate, localTime, localTime1, place, new HashMap<>()));
    }
    public void addTerm(LocalDate localDate, LocalTime localTime, LocalTime localTime1, Place place, Map<String, String> info) {
        this.getTerms().add(new TermImpl(localDate, localTime, localTime1, place, info));
    }

    @Override
    public void addTerm(LocalDate localDate, LocalTime localTime) {

    }

    @Override
    public void deleteTerm(Term term) {
        for(int i = 0; i < this.getTerms().size(); i++){
            Term t = this.getTerms().get(i);
            if(t.getPlace().equals(term.getPlace()) && t.getDate().equals(term.getDate())
                && t.getTimeStart().equals(term.getTimeStart()) && t.getTimeEnd().equals(term.getTimeEnd())){
                this.getTerms().remove(i);
                return;
            }
        }
    }

    @Override
    public void deleteTermInDateSpan(LocalDate localDate, LocalDate localDate1) {
        List<Integer> toDelete = new ArrayList<>();
        for(int i = 0; i < this.getTerms().size(); i++){
            Term t = this.getTerms().get(i);
            if(t.getDate().isAfter(localDate) && t.getDate().isBefore(localDate1))
                toDelete.add(i);
        }
        for(Integer i : toDelete)
            this.getTerms().remove(i);
    }

    @Override
    public void deleteTermInDateAndTimeSpan(LocalDate localDate, LocalDate localDate1, LocalTime localTime, LocalTime localTime1) {
        List<Integer> toDelete = new ArrayList<>();
        for(int i = 0; i < this.getTerms().size(); i++){
            Term t = this.getTerms().get(i);
            if(t.getDate().isAfter(localDate) && t.getDate().isBefore(localDate1) && t.getTimeStart().isAfter(localTime))
                toDelete.add(i);
        }
        for(Integer i : toDelete)
            this.getTerms().remove(i);
    }

    @Override
    public void deleteTerm(LocalDate localDate, LocalTime localTime) {

    }

    @Override
    public void deleteTerm(LocalDate localDate, LocalTime localTime, LocalTime localTime1) {
        List<Integer> toDelete = new ArrayList<>();
        for(int i = 0; i < this.getTerms().size(); i++){
            Term t = this.getTerms().get(i);
            if(t.getDate().equals(localDate) && t.getTimeStart().equals(localTime) && t.getTimeEnd().equals(localTime1))
                toDelete.add(i);
        }
        for(Integer i : toDelete)
            this.getTerms().remove(i);
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
