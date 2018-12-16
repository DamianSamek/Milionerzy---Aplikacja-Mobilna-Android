package ur.edu.pl.millionaire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class ScoreActivity extends Activity {

    private int score,minutes,seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scores);
        TextView scoreText = (TextView) findViewById(R.id.scoreTextView);

        SharedPreferences myScore = this.getSharedPreferences("MyScore",Context.MODE_PRIVATE);
        score = myScore.getInt("score",0);
        minutes = myScore.getInt("minutes",0);
        seconds = myScore.getInt("seconds",0);

        if(seconds==0)
        scoreText.setText("Wygrałeś "+score+" złotych! \n Czas: "+minutes+":00");
        else
            if(seconds<10) scoreText.setText("Wygrałeś "+score+" złotych! \n Czas: "+minutes+":0"+seconds);
        else scoreText.setText("Wygrałeś "+score+" złotych! \n Czas: "+minutes+":"+seconds);

        scoreText.postDelayed(highscore,3000);


}

Runnable highscore = new Runnable() {
    @Override
    public void run() {
        startActivity(new Intent(ScoreActivity.this, HighscoreActivity.class));
        finish();
    }
};

}
