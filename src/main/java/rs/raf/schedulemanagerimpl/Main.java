package rs.raf.schedulemanagerimpl;

import rs.raf.Place;
import rs.raf.Term;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
//        File csv = new File("D:\\Projekti\\ScheduleManagerImpl\\src\\main\\resources\\csv.csv");
//        ScheduleImpl schedule = new ScheduleImpl(csv);
//        System.out.println(schedule.getTerms());
//        System.out.println(schedule.getTerms().size());


//        System.out.println("===================");
//        ScheduleImpl s = new ScheduleImpl();
//        File places = new File("D:\\Projekti\\ScheduleManagerImpl\\src\\main\\resources\\places.csv");
//        s.loadPlaces(places);
//        System.out.println(s.getPlaces());

        File oneterm = new File("D:\\Projekti\\ScheduleManagerImpl\\src\\main\\resources\\oneterm.csv");
        ScheduleImpl schedule = new ScheduleImpl(oneterm);
        System.out.println("Pre promena:\n" + schedule.getTerms());
        List<Object> lista = List.of(schedule.getTerms().toArray());
        //new TermImpl(LocalDate.of(2022, 1,1), LocalTime.of(0, 0), LocalTime.of(0, 0), new PlaceImpl()
        schedule.moveTerm((Term)lista.get(1), LocalDate.of(2023, 4,4), LocalTime.of(10, 0));
        System.out.println("Posle promena:\n" + schedule.getTerms());
    }
}
