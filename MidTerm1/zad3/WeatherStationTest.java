package NP.MidTerm1.zad3;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class Days{
    float temperature;
    float wind;
    float humidity;
    float visibility;
    Date date;

    public Days(float temperature, float wind, float humidity, float visibility, Date date) {
        super();
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getWind() {
        return wind;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getVisibility() {
        return visibility;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        //41.8 9.4 km/h 40.8% 20.7 km Tue Dec 17 23:35:15 GMT 2013
        StringBuilder sb=new StringBuilder();
        sb.append(this.temperature).append(" ");
        sb.append(this.wind).append(" km/h ");
        sb.append(this.humidity).append("% ");
        sb.append(this.visibility).append(" km ");
        sb.append(this.date);
        return sb.toString();
    }




}

class WeatherStation {
    int days;
    List<Days> lista;

    public WeatherStation(int days) {
        super();
        this.days = days;
        lista=new ArrayList<>();
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date) {

        for(Days d: lista) {
            if(Math.abs(d.getDate().getTime()-date.getTime()) < 150 * 1000 ) {
                return ;
            }
        }

        lista.add(new Days(temperature, wind, humidity, visibility, date));



        Iterator<Days> it=lista.iterator();

        while(it.hasNext()) {
            Days d=it.next();

            long t1= d.getDate().getMonth() * 31 + d.getDate().getDate();
            long t2= date.getMonth() * 31 + date.getDate();

            if( t2-t1 >= days) {
                it.remove();
            }


        }
    }

    public int total() {
        return lista.size();
    }


    public void status(Date from, Date to) {
        float avg=0;

        if(lista.isEmpty()) {
            throw new RuntimeException ();
        }


        List<Days> pom= lista.stream()
                .filter( o -> ( o.getDate().getTime() >= from.getTime()&&o.getDate().getTime() <= to.getTime() ) )
                .collect(Collectors.toList());

        if(pom.isEmpty()) {
            throw new RuntimeException ();
        }

        pom=pom.stream().sorted((o1,o2) -> ( o1.getDate().compareTo( o2.getDate() ) ) ).collect(Collectors.toList());



        Iterator<Days> it=pom.iterator();

        while(it.hasNext()) {

            Days d =it.next();
            System.out.println(d.toString());
            avg+=d.getTemperature();
        }
        //Avarage temperature: 20.43
        System.out.format("Average temperature: %.2f",avg/pom.size());



    }


}

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde
