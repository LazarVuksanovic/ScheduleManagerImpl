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

public class ScheduleImpl implements Schedule {
    private Set<TermImpl> terms;
    private List<Place> places;
    private List<String> headersOrder;
    private List<String> headers;

    public ScheduleImpl(){
        this.terms = new TreeSet<>();
        this.headers = new ArrayList<>();
        this.headersOrder = new ArrayList<>();
    }

    public ScheduleImpl(Set<TermImpl> terms){
        this.terms = terms;
        this.headers = new ArrayList<>();
        this.headersOrder = new ArrayList<>();
    }

    public ScheduleImpl(File file) throws IOException {
        this.headers = new ArrayList<>();
        this.headersOrder = new ArrayList<>();
        this.makeSchedule(file);
    }

    public ScheduleImpl(Set<TermImpl> terms, List<Place> places){
        this.terms = terms;
        this.places = places;
        this.headers = new ArrayList<>();
        this.headersOrder = new ArrayList<>();
    }

    public List<String> getheadersOrder() {
        return headersOrder;
    }

    public void setheadersOrder(List<String> headersOrder) {
        this.headersOrder = headersOrder;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public Set<TermImpl> getTerms() {
        return terms;
    }

    public void setTerms(Set<TermImpl> terms) {
        this.terms = terms;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    @Override
    public void loadPlaces(File file) throws IOException {
        FileReader fr = new FileReader(file);
        CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();
        CSVReader reader = new CSVReaderBuilder(fr).withCSVParser(parser).build();

        String[] line;
        while ((line = reader.readNext()) != null) {
            PlaceImpl newPlace = new PlaceImpl(line[0], line[1]);
            if(line.length > 2){
                String[] inputProp = line[2].replace(" ", "").split(";");
                Map<String, Integer> properties = new HashMap<>();
                for (String property : inputProp) {
                    properties.put(property.substring(0, property.indexOf(":")),
                            Integer.parseInt(property.substring(property.indexOf(":")+1)));
                }
                newPlace.setProperties(properties);
            }

            this.places.add(newPlace);
        }
    }

    @Override
    public void makeSchedule() {
        this.headers = new ArrayList<>();
        this.headersOrder = new ArrayList<>();
    }

    @Override
    public void makeSchedule(File file) throws IOException {
        FileReader fr = new FileReader(file);
        CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();
        CSVReader reader = new CSVReaderBuilder(fr).withCSVParser(parser).build();

        for(String e : reader.readNext())
           headersOrder.add(e.replace("\"", ""));
        for(String e : reader.readNext())
            headers.add(e.replace("\"", ""));
        System.out.println(headersOrder);
        String[] line;
        while ((line = reader.readNext()) != null) {
            int headersOrderIndex = 0;
            Map<String, String> terminfo = new HashMap<>();
            LocalDate date = LocalDate.now();
            LocalTime timeStart = LocalTime.now();
            LocalTime timeEnd = LocalTime.now();
            Place place = new PlaceImpl();

            for (String cell : line) {
                cell = cell.replace("\"", "");
                //System.out.println("cell " + cell);
                switch (headersOrder.get(headersOrderIndex)) {
                    case "terminfo" -> terminfo.put(headers.get(headersOrderIndex), cell);
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
                        for (Place p : this.places) {
                            if (p.getName().equals(cell)) {
                                place = p;
                                exists = true;
                                break;
                            }
                        }
                        if(!exists){
                            place = new PlaceImpl(cell);
                            this.places.add(place);
                        }
                    }
                }
                headersOrderIndex++;
            }
            TermImpl newTerm = new TermImpl(date, timeStart, timeEnd, place, terminfo);
            place.getTerms().add(newTerm);
            this.terms.add(newTerm);

        }
    }

    @Override
    public void makeSchedule(List<Term> list) {

    }

    @Override
    public void makeSchedule(Schedule schedule) {
        //TODO beskorisno
    }

    @Override
    public void addTerm(Term term) {
        this.terms.add((TermImpl)term);
        boolean exists = false;
        for(Place p : this.places){
            if (p.getName().equals(term.getPlace().getName())){
                exists = true;
            }
        }
        if(!exists)
            this.places.add(term.getPlace());
    }

    @Override
    public void addTerm(LocalDate localDate, LocalTime localTime, LocalTime localTime1, Place place) {
        this.terms.add(new TermImpl(localDate, localTime, localTime1, place, new HashMap<>()));
    }
    public void addTerm(LocalDate localDate, LocalTime localTime, LocalTime localTime1, Place place, Map<String, String> info) {
        this.terms.add(new TermImpl(localDate, localTime, localTime1, place, info));
    }

    @Override
    public void addTerm(LocalDate localDate, LocalTime localTime) {
        //TODO beskorisno
    }

    @Override
    public void deleteTerm(Term term) {
        Iterator<TermImpl> iterator = this.terms.iterator();
        while (iterator.hasNext()) {
            TermImpl t = iterator.next();
            if (t.getPlace().equals(term.getPlace()) && t.getDate().equals(term.getDate())
                    && t.getTimeStart().equals(term.getTimeStart()) && t.getTimeEnd().equals(term.getTimeEnd())) {
                iterator.remove();
                return;
            }
        }
    }

    @Override
    public void deleteTermInDateSpan(LocalDate localDate, LocalDate localDate1) {
        this.terms.removeIf(t -> t.getDate().isAfter(localDate) && t.getDate().isBefore(localDate1));
    }

    @Override
    public void deleteTermInDateAndTimeSpan(LocalDate localDate, LocalDate localDate1, LocalTime localTime, LocalTime localTime1) {
        this.terms.removeIf(t -> t.getDate().isAfter(localDate) && t.getDate().isBefore(localDate1)
                                && t.getTimeStart().isAfter(localTime) && t.getTimeStart().isBefore(localTime1));
    }

    @Override
    public void deleteTerm(LocalDate localDate, LocalTime localTime) {
        //TODO beskorisno
    }

    @Override
    public void deleteTerm(LocalDate localDate, LocalTime localTime, LocalTime localTime1) {
        this.terms.removeIf(t -> t.getDate().equals(localDate) && t.getTimeStart().equals(localTime)
                                                                && t.getTimeEnd().equals(localTime1));
    }

    @Override
    public void deleteTerm(Place place) {

    }

    @Override
    public void deleteTerm(Place place, LocalDate localDate, LocalTime localTime, LocalTime localTime1) {
        this.deleteTerm(new TermImpl(localDate, localTime, localTime1, place, null));
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
    public List<Term> searchTerm(Place place) {
        return null;
    }

    @Override
    public List<Term> searchTerm(Place place, LocalDate localDate, LocalDate localDate1) {
        return null;
    }

    @Override
    public List<Term> searchTerm(Place place, Map<String, Integer> map) {
        return null;
    }

    @Override
    public List<Term> searchTerm(Place place, LocalDate localDate, LocalDate localDate1, LocalTime localTime, LocalTime localTime1) {
        return null;
    }

    @Override
    public List<Term> searchTerm(Map<String, Integer> map) {
        return null;
    }

    @Override
    public List<Term> searchTerm(LocalDate localDate, LocalDate localDate1, LocalTime localTime, LocalTime localTime1) {
        return null;
    }

    @Override
    public String searchAvailableTerms(Date date) {
        return null;
    }

    @Override
    public List<Place> searchAvailableTerms(Date date, LocalTime localTime, LocalTime localTime1) {
        return null;
    }

    @Override
    public String searchAvailableTerms(Place place) {
        return null;
    }

    @Override
    public String searchAvailableTerms(Place place, LocalDate localDate, LocalDate localDate1) {
        return null;
    }

    @Override
    public String searchAvailableTerms(Place place, Map<String, Integer> map) {
        return null;
    }

    @Override
    public String searchAvailableTerms(Place place, LocalDate localDate, LocalDate localDate1, LocalTime localTime, LocalTime localTime1) {
        return null;
    }

    @Override
    public String searchAvailableTerms(Map<String, Integer> map) {
        return null;
    }

    @Override
    public String searchAvailableTerms(LocalDate localDate, LocalDate localDate1, LocalTime localTime, LocalTime localTime1) {
        return null;
    }
}
