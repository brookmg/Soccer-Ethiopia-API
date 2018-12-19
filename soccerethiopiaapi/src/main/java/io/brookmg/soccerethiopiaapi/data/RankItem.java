package io.brookmg.soccerethiopiaapi.data;

/**
 * Created by BrookMG on 12/19/2018 in io.brookmg.soccerethiopiaapi
 * inside the project SoccerEthiopia .
 */
public class RankItem {

    private int rank; //Rank of the football team
    private String teamIcon; //Icon of the team
    private String teamName; //Name of the team
    private int points; //Team point
    private int playedGames; //The number of games the team played
    private int wonGames; //The number of games the team has won
    private int drawGames; //The number of games the team neither won nor lost
    private int lostGames; //The number of games the team has lost

    public RankItem(int rank, String teamIcon, String teamName, int points, int playedGames, int wonGames, int drawGames, int lostGames) {
        this.rank = rank;
        this.teamIcon = teamIcon;
        this.teamName = teamName;
        this.points = points;
        this.playedGames = playedGames;
        this.wonGames = wonGames;
        this.drawGames = drawGames;
        this.lostGames = lostGames;
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


    public int getPlayedGames() {
        return playedGames;
    }

    public void setPlayedGames(int playedGames) {
        this.playedGames = playedGames;
    }

    public int getWonGames() {
        return wonGames;
    }

    public void setWonGames(int wonGames) {
        this.wonGames = wonGames;
    }

    public int getDrawGames() {
        return drawGames;
    }

    public void setDrawGames(int drawGames) {
        this.drawGames = drawGames;
    }

    public int getLostGames() {
        return lostGames;
    }

    public void setLostGames(int lostGames) {
        this.lostGames = lostGames;
    }

}
