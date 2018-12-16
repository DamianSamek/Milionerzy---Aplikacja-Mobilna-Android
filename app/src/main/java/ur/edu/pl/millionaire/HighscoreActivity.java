package ur.edu.pl.millionaire;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ur.edu.pl.millionaire.model.Result;

public class HighscoreActivity extends Activity {


    private ListView highscore;
    ArrayList<Result> highscores;
    ArrayList<String> highscoresStrings;

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_highscore);
     highscore = (ListView)findViewById(R.id.listView);
    highscores = new ArrayList<>();
    highscoresStrings = new ArrayList<>();

        retrieveHighscoreList();
        adapter = new ArrayAdapter<String>(this, R.layout.highscore_list_row, highscoresStrings);

    }

    public void retrieveHighscoreList() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("highscore");
        final Query myTopScoresQuery = databaseReference.orderByChild("score").limitToLast(10);
        myTopScoresQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    Result result = new Result();
                    result.setName(childDataSnapshot.child("name").getValue().toString());
                    result.setScore(Integer.valueOf(childDataSnapshot.child("score").getValue().toString()));
                    result.setMinutes(Integer.valueOf(childDataSnapshot.child("minutes").getValue().toString()));
                    result.setSeconds(Integer.valueOf(childDataSnapshot.child("seconds").getValue().toString()));
                    result.setSecondsAfterConversion(Integer.valueOf(childDataSnapshot.child("secondsAfterConversion").getValue().toString()));
                    highscores.add(result);



                    }

                highscores.sort(new Comparator<Result>() {
                    @Override
                    public int compare(Result result1, Result result2)
                    {
                        if(result1.getScore()==result2.getScore()){
                        if(result1.getSeconds()==result2.getSeconds())
                            return 0;
                        else if(result1.getSeconds()> result2.getSeconds())
                            return -1;
                        else
                            return 1;
                    }
                        else return 0;
                    }

                });





                    for(int i=9; i>=0; i--)
                    {
                        highscoresStrings.add(highscoresStrings.size()+1+". "+highscores.get(i).toString());
                    }






                highscore.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                }

                }
        );
    }


    }

