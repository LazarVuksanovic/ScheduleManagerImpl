package rs.raf.schedulemanagerimpl;

import rs.raf.Place;
import rs.raf.Schedule;
import rs.raf.ScheduleImplManager;
import rs.raf.Term;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            Class.forName("rs.raf.schedulemanagerimpl.ScheduleImpl");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Schedule schedule = ScheduleImplManager.getScheduleSpecification();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("H:mm");

        while(true){
            System.out.println("Available commands:\n" +
                    "0 : Exit.\n" +
                    "1 : Load places.\n" +
                    "2 : Load free days.\n" +
                    "3 : Load terms.\n" +
                    "4 : Add term.\n" +
                    "5 : Delete term.\n" +
                    "6 : Move term.\n" +
                    "7 : Search term.\n" +
                    "8 : Search available terms.\n" +
                    "9 : Export schedule.");

            System.out.print("$: ");
            Scanner input = new Scanner(System.in);

            switch (input.nextLine()){
                case "0" -> { return; }
                case "1" -> {
                    System.out.println("Enter file path:");
                    File places = new File("src\\main\\resources\\" + input.nextLine());
                    schedule.loadPlaces(places);
                    System.out.println("Done.");
                }
                case "2" -> {
                    System.out.println("Enter file path:");
                    File freeDays = new File("src\\main\\resources\\" + input.nextLine());
                    schedule.loadFreeDays(freeDays);
                    System.out.println("Done.");
                }
                case "3" -> {
                    System.out.println("Enter file path:");
                    File terms = new File("src\\main\\resources\\" + input.nextLine());
                    schedule.makeSchedule(terms);
                    System.out.println("Done.");
                }

                case "4" -> {

                }

                case "5" -> {
                    System.out.println("Choose parameter input:\n" +
                            "1 : <dateFrom> <dateTo>\n" +
                            "2 : <dateFrom> <dateTo> <timeFrom> <timeTo>\n" +
                            "3 : <date> <timeFrom> <timeTo>\n" +
                            "4 : <placeName> <date> <timeFrom> <timeTo>\n" +
                            "5 : <placeName> <dateFrom> <dateTo> <timeFrom> <timeTo>\n");
                    String[] p = input.nextLine().split(" ");
                    switch(p[0]){
                        case "1" -> schedule.deleteTermInDateSpan(LocalDate.parse(p[1], df), LocalDate.parse(p[2], df));
                        case "2" -> schedule.deleteTermInDateAndTimeSpan(LocalDate.parse(p[1], df), LocalDate.parse(p[2], df), LocalTime.parse(p[3], tf), LocalTime.parse(p[4], tf));
                        case "3" -> schedule.deleteTerm(LocalDate.parse(p[1], df), LocalTime.parse(p[2], tf), LocalTime.parse(p[2], tf));
                        case "4" -> schedule.deleteTerm(findPlace(schedule, p[1]),LocalDate.parse(p[2], df), LocalTime.parse(p[3], tf), LocalTime.parse(p[4], tf));
                        case "5" -> schedule.deleteTerm(findPlace(schedule, p[1]),LocalDate.parse(p[2], df), LocalDate.parse(p[3], df), LocalTime.parse(p[4], tf), LocalTime.parse(p[5], tf));
                    }
                    System.out.println("\nDone.");
                }

                case "6" -> {
                    System.out.println("Move first term. Choose parameter input:\n" +
                            "1 : <newDate> <newTime>\n" +
                            "2 : <newDate> <newTime> <newPlaceName>\n" +
                            "3 : <newDate> <newTimeStart> <newTimeEnd>\n" +
                            "5 : <newDate> <newTimeStart> <newTimeEnd> <newPlaceName>\n");
                    String[] p = input.nextLine().split(" ");
                    switch(p[0]){
                        case "1" -> schedule.moveTerm(schedule.getTerms().get(0), LocalDate.parse(p[1], df), LocalTime.parse(p[2], tf));
                        case "2" -> schedule.moveTerm(schedule.getTerms().get(0), LocalDate.parse(p[1], df), LocalTime.parse(p[2], tf), findPlace(schedule, p[3]));
                        case "3" -> schedule.moveTerm(schedule.getTerms().get(0), LocalDate.parse(p[1], df), LocalTime.parse(p[2], tf), LocalTime.parse(p[3], tf));
                        case "4" -> schedule.moveTerm(schedule.getTerms().get(0), LocalDate.parse(p[1], df), LocalTime.parse(p[2], tf), LocalTime.parse(p[3], tf), findPlace(schedule, p[4]));
                    }
                    System.out.println("\nDone.");
                }

                case "7" -> {
                    System.out.println("Search terms by:\n" +
                            "1 : <placeName>\n" +
                            "2 : <dateFrom> <dateTo>\n" +
                            "3 : <placeName> <dateFrom> <dateTo>\n" +
                            "4 : <placeName> (grupa 101)\n" +
                            "5 : <placeName> (Racunari > 10)\n");

                    String[] p = input.nextLine().split(" ");
                    switch(p[0]){
                        case "1" -> System.out.println(schedule.searchTerm(findPlace(schedule, p[1])));
                        case "2" -> System.out.println(schedule.searchTerm(LocalDate.parse(p[1], df), LocalDate.parse(p[2], df)));
                        case "3" -> System.out.println(schedule.searchTerm(findPlace(schedule, p[1]), LocalDate.parse(p[2], df), LocalDate.parse(p[3], df)));
                        case "4" -> {
                            Map<String, String> props = new HashMap<>();
                            props.put("Grupe", "101");
                            System.out.println(schedule.searchTerm(findPlace(schedule, p[1]),props));
                        }
                        case "5" -> {
                            Map<String, Integer> props = new HashMap<>();
                            props.put("Racunari", 10);
                            System.out.println(schedule.searchTerm(props));
                        }
                    }
                    System.out.println("\nDone.");
                }

                case "8" -> {
                    System.out.println("Search available terms by:\n" +
                            "1 : <date>\n" +
                            "2 : <placeName>\n" +
                            "3 : <dateFrom> <dateTo> <timeFrom> <timeTo>\n" +
                            "4 : <date> <timeFrom> <timeTo>\n" +
                            "5 : Place prop (Racunari > 10)\n");

                    String[] p = input.nextLine().split(" ");
                    switch(p[0]){
                        case "1" -> System.out.println(schedule.searchAvailableTerms(LocalDate.parse(p[1], df)));
                        case "2" -> System.out.println(schedule.searchAvailableTerms(findPlace(schedule, p[1])));
                        case "3" -> System.out.println(schedule.searchAvailableTerms(LocalDate.parse(p[1], df), LocalDate.parse(p[2], df), LocalTime.parse(p[3], tf), LocalTime.parse(p[4], tf)));
                        case "4" -> System.out.println(schedule.searchAvailableTerms(LocalDate.parse(p[1], df), LocalTime.parse(p[2], tf), LocalTime.parse(p[3], tf)));
                        case "5" ->{
                            Map<String, Integer> props = new HashMap<>();
                            props.put("Racunari", 10);
                            System.out.println(schedule.searchAvailableTerms(props));
                        }
                    }
                    System.out.println("Done.");
                }

                case "9" ->{
                    System.out.println("Enter file path:");
                    File exportFile = new File("src\\main\\resources\\" + input.nextLine());
                    schedule.exportSchedule(exportFile);
                    System.out.println("Done.");
                }
            }
        }
    }

    static public Place findPlace(Schedule s, String name){
        for(Place p : s.getPlaces())
            if(p.getName().equals(name.replace("_", " ")))
                return p;
        return null;
    }
}
