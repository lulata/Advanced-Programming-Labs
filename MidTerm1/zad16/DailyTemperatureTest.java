package NP.MidTerm1.zad16;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * I partial exam 2016
 */
public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

// Vashiot kod ovde
class DailyTemperature implements Comparable<DailyTemperature>{
    int day;
    List<Double> temp;
    public DailyTemperature(int d,List<Double> t){
        day=d;
        temp=t;
    }
    public static double ConvertToC(Double t){
        return (t-32)*5.0/9;
    }
    public static double ConvertToF(Double t){
        return (t*9.0/5)+32.0;
    }
    public double MaxTemp(){
        return Collections.max(temp);
    }
    public double MinTemp(){
        return Collections.min(temp);
    }
    public double AverageTemp(){
        double sum=0;
        for(int i=0;i<temp.size();++i){
            sum+=temp.get(i);
        }
        return sum/temp.size();
    }
    public String toStringC(){
        return String.format("%3d: Count: %3d Min: %6.2fC Max: %6.2fC Avg: %6.2fC",day, temp.size(),this.MinTemp(),this.MaxTemp(),this.AverageTemp());
    }
    public String toStringF(){
        return String.format("%3d: Count: %3d Min: %6.2fF Max: %6.2fF Avg: %6.2fF",
                day, temp.size(),DailyTemperature.ConvertToF(this.MinTemp()),DailyTemperature.ConvertToF(this.MaxTemp()),DailyTemperature.ConvertToF(this.AverageTemp()));
    }
    @Override
    public int compareTo(DailyTemperature dailyTemperature) {
        if(day==dailyTemperature.day){
            return 0;
        }
        else{
            if(day<dailyTemperature.day)
                return -1;
            else
                return 1;
        }
    }
}

class DailyTemperatures{
    List<DailyTemperature> lista;
    public DailyTemperatures(){
        lista=new ArrayList<>();
    }
    public void readTemperatures(InputStream inputStream){
        Scanner in=new Scanner(inputStream);
        while(in.hasNextLine()){
            String red=in.nextLine();
            String[] sred=red.split("\\s+");
            List<Double>temp=new ArrayList<>();
            for(int i=1;i<sred.length;++i){
                if(sred[i].charAt(sred[i].length()-1)=='F'){
                    temp.add(DailyTemperature.ConvertToC(Double.parseDouble(sred[i].substring(0,sred[i].length()-1))));
                }
                else{
                    temp.add(Double.parseDouble(sred[i].substring(0,sred[i].length()-1)));
                }
            }
            lista.add(new DailyTemperature(Integer.parseInt(sred[0]),temp));
        }

        in.close();
    }
    public void writeDailyStats(OutputStream outputStream, char scale){
        PrintWriter out=new PrintWriter(outputStream);
        Collections.sort(lista);
        if(scale=='F')
        {
            for(int i=0;i<lista.size();++i){
                out.println(lista.get(i).toStringF());
            }
        }
        else{
            for(int i=0;i<lista.size();++i){
                out.println(lista.get(i).toStringC());
            }
        }
        out.flush();
        // out.close();
    }

}