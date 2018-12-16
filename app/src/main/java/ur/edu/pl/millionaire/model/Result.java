package ur.edu.pl.millionaire.model;

public class Result {
    private String name;
    private int score;
    private int minutes;
    private int secondsAfterConversion;
    private int seconds;


    public Result(String name, int score, int minutes, int secondsAfterConversion, int seconds) {
        this.name = name;
        this.score = score;
        this.minutes = minutes;
        this.secondsAfterConversion = secondsAfterConversion;
        this.seconds = seconds;
    }

    public Result(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSecondsAfterConversion() {
        return secondsAfterConversion;
    }

    public void setSecondsAfterConversion(int secondsAfterConversion) {
        this.secondsAfterConversion = secondsAfterConversion;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return "Gracz: "+name+"\nKwota: " + score + "\nCzas: "+minutes+":"+secondsAfterConversion;
    }

}
