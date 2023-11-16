package rs.raf.schedulemanagerimpl;

import rs.raf.Place;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        File csv = new File("D:\\Projekti\\ScheduleManagerImpl\\src\\main\\resources\\csv.csv");
        ScheduleImpl schedule = new ScheduleImpl(csv);
        System.out.println(schedule.getTerms());
        System.out.println("===================");
        System.out.println(schedule.getPlaces());
    }
}
