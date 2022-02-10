package NP.Exam.lab1;


import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде
class Subtitles {
    public List<String> li;
    public Subtitles() {
        li=new ArrayList<>();
    }
    public int loadSubtitles(InputStream in) {
        Scanner input=new Scanner(in);
        String s="";
        int i=0;
        while(input.hasNextLine()){
            s=input.nextLine();
            li.add(s);
            try {
                Integer.parseInt(s);
                i++;
            } catch (Exception e) {

            }
        }
        return i;
    }

    public void print() {
        for (String string : li) {
            System.out.println(string);
        }
        System.out.println();
    }
    private String change(String timeStamp,int mili){
        DateFormat date=new SimpleDateFormat("HH:mm:ss,SSS");
        String[] tmp=timeStamp.split(" --> ");
        try {
            Date d1=date.parse(tmp[0]);
            Date d2=date.parse(tmp[1]);
            d1=new Date(d1.getTime()+mili);
            d2=new Date(d2.getTime()+mili);
            return date.format(d1)+" --> "+date.format(d2);
        } catch (ParseException e) {

        }
        return "";
    }
    public void shift(int shift) {
        List<String> tmpList=new ArrayList<>();
        for(int i=0;i<li.size();i++){
            String s=li.get(i);
            try {
                Integer.parseInt(s);
                tmpList.add(s);
                tmpList.add(change(li.get(++i),shift));

            } catch (Exception e) {
                tmpList.add(s);
            }

        }
        li=tmpList;
    }

}
