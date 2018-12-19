package io.brookmg.soccerethiopiaapi;

/**
 * Created by BrookMG on 12/19/2018 in io.brookmg.soccerethiopiaapi
 * inside the project SoccerEthiopia .
 */
public class RankItem {

    private int rank; //Rank of the football team
    private String teamIcon; //Icon of the team
    private String teamName; //Name of the team
    private int points; //Team point

    public RankItem(int rank, String teamIcon, String teamName, int points) {
        this.rank = rank;
        this.teamIcon = teamIcon;
        this.teamName = teamName;
        this.points = points;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getTeamIcon() {
        return teamIcon;
    }

    public void setTeamIcon(String teamIcon) {
        this.teamIcon = teamIcon;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
