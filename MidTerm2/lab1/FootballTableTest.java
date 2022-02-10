package NP.MidTerm2.lab1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here


class Team implements Comparable<Team>{
    String name;
    int wins;
    int draws;
    int losts;
    int goals;
    int awayGoals;
    public Team(String name){
        this.name = name;
        this.draws = 0;
        this.wins = 0;
        this.losts = 0;
        this.goals = 0;
        this.awayGoals = 0;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public void setLosts(int losts) {
        this.losts = losts;
    }
    public int getPoints(){
        return this.wins * 3 + this.draws;
    }
    public int matches(){
        return wins + draws + losts;
    }
    public int getGoals(){
        return this.goals;
    }
    public int goalsdifference(){
        return this.getGoals() - this.awayGoals;
    }
    @Override
    public int compareTo(Team o) {
        return Comparator.comparingInt(Team::getPoints).thenComparing(Team::goalsdifference).reversed().thenComparing(Team::getName).compare(this, o);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return wins == team.wins&&draws == team.draws && losts == team.losts && goals == team.goals && name.equals(team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosts() {
        return losts;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public void setAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }


}
class FootballTable{
    Map<String, Team> teams;

    public FootballTable(){
        teams = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homegoals, int awayGoals){
        String t1 = homeTeam;
        String t2 = awayTeam;
        teams.putIfAbsent(homeTeam, new Team(homeTeam));
        teams.putIfAbsent(awayTeam, new Team(awayTeam));

        if(homegoals > awayGoals){
            teams.get(t1).setWins(teams.get(t1).getWins() + 1);
            teams.get(t2).setLosts(teams.get(t2).getLosts() + 1);
        }else if(homegoals < awayGoals){
            teams.get(t1).setLosts(teams.get(t1).getLosts() + 1);
            teams.get(t2).setWins(teams.get(t2).getWins() + 1);
        }else{
            teams.get(t1).setDraws(teams.get(t1).getDraws() + 1);
            teams.get(t2).setDraws(teams.get(t2).getDraws() + 1);
        }

        teams.get(t1).setGoals(teams.get(t1).getGoals() + homegoals);
        teams.get(t2).setGoals(teams.get(t2).getGoals() + awayGoals);
        teams.get(t1).setAwayGoals(teams.get(t1).getAwayGoals() + awayGoals);
        teams.get(t2).setAwayGoals(teams.get(t2).getAwayGoals() + homegoals);
    }
    public void printTable(){
        int c = 1;

        List<Team> sorted_teams = teams.values().stream()
                .sorted()
                .collect(Collectors.toList());
        for(Team t: sorted_teams){
            System.out.printf("%2d. %-15s%5d%5d%5d%5d%5d\n",c, t.getName(), t.matches(), t.getWins(), t.getDraws(), t.getLosts(), t.getPoints());
            c++;
        }
    }
}
