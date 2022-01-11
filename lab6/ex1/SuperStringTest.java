package NP.lab6.ex1;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

class SuperString{
    LinkedList<String> list;
    Stack<String> stack;

    public SuperString(){
        list = new LinkedList<>();
        stack = new Stack<>();
    }

    void append(String s){
        list.addLast(s);
        stack.push(s);
    }

    void insert(String s){
        list.addFirst(s);
        stack.push(s);
    }

    boolean contains(String s){
        return String.join("", list).contains(s);
    }

    void reverse(){
        LinkedList<String> reversedList = new LinkedList<>();
        String string;
        while (!list.isEmpty()){
            string = list.getLast();
            string = reverseString(string);
            reversedList.addLast(string);
            list.removeLast();
        } for (String s : reversedList) {
            list.addLast(s);
        }
    }

    @Override
    public String toString() {
        return String.join("", list);
    }

    private String reverseString(String s){
        StringBuilder sb = new StringBuilder(s);
        return sb.reverse().toString();
    }

    void removeLast(int k) {
        while (k>0){
            String toRemove = stack.pop();
            list.remove(toRemove);
            String toRemoveReversed = reverseString(toRemove);
            list.remove(toRemoveReversed);
            k--;
        }
    }
}

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}
