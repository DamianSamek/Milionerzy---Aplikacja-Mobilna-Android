package ur.edu.pl.millionaire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button playButton, scoreButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        playButton = (Button)findViewById(R.id.buttonPlay);
        scoreButton = (Button)findViewById(R.id.buttonScores);

        playButton.setOnClickListener(playButtonHandler);
        scoreButton.setOnClickListener(scoreButtonHandler);
    }

    View.OnClickListener playButtonHandler = new View.OnClickListener() {
        public void onClick(View v) {
            playButton.setBackgroundResource(R.drawable.button_opcion_selected);
            playButton.postDelayed(swapSelectedPlay, 500);
            startActivity(new Intent(MainActivity.this, PlayActivity.class));
        }
    };

    View.OnClickListener scoreButtonHandler = new View.OnClickListener() {
        public void onClick(View v) {
            scoreButton.setBackgroundResource(R.drawable.button_opcion_selected);
            startActivity(new Intent(MainActivity.this, HighscoreActivity.class));
            scoreButton.postDelayed(swapSelectedScores, 500);
        }
    };

    Runnable swapSelectedPlay = new Runnable() {
        @Override
        public void run() {
            playButton.setBackgroundResource(R.drawable.button_opcion);
        }
    };

    Runnable swapSelectedScores= new Runnable() {
        @Override
        public void run() {
            scoreButton.setBackgroundResource(R.drawable.button_opcion);
        }
    };
}
