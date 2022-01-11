package NP.lab8.ex1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Song {
    private String title;
    private String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public String getPerformer() {
        return artist;
    }

    @Override
    public String toString() {
        return "Song{title=" + title + ", artist=" + artist + "}";
    }
}

class MP3Player{
    private List<Song> songs;
    private int current;
    private boolean stopped;
    private boolean playing;

    public MP3Player(List<Song> songs) {
        this.songs = new ArrayList<>();
        this.songs.addAll(songs);
        this.current = 0;
        this.stopped = false;
        this.playing = false;
    }

    void pressPlay(){
        System.out.printf("Song %s playing\n", playing ? "is already" : current + " is");
        playing = true;
        stopped = false;
    }

    void pressStop(){
        current = 0;
        if(playing) {
            playing = false;
            stopped = true;
            System.out.println("Song " + current + " is paused");
        } else if(stopped){
            stopped = false;
            System.out.println("Songs are stopped");
        }
        else System.out.println("Songs are already stopped");
    }

    void pressFWD(){
        playing = false;
        stopped = true;
        if(current == songs.size() -1){
            current = 0;
        } else{
            current++;
        }System.out.println("Forward...");
    }

    void pressREW(){
        playing = false;
        stopped = true;
        if(current == 0){
            current = songs.size() - 1;
        } else{
            current--;
        }System.out.println("Reward...");
    }

    void printCurrentSong(){
        System.out.println("Song{title=" + songs.get(current).getTitle() +", artist=" + songs.get(current).getPerformer() + "}");
    }

    @Override
    public String toString() {
        String result = "MP3Player{currentSong = " + current + ", songList = [";
        String songs = this.songs.stream()
                .map(Song::toString)
                .collect(Collectors.joining(", "));
        return result + songs + "]}";
    }
}

public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde
