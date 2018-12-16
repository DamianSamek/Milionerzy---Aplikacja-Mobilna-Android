package ur.edu.pl.millionaire;

import java.util.ArrayList;
import java.util.Random;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import ur.edu.pl.millionaire.model.Question;
import ur.edu.pl.millionaire.model.Result;

public class PlayActivity extends Activity {

    final Context context = this;
    private EditText result;
    AlertDialog.Builder alertDialogBuilder;

    private DatabaseReference database;
    Button menuItemPhone, menuItem50, menuItemAudience, menuItemEnd,optionA, optionB,optionC,optionD,header;
    TextView question, money;
    String name;

    int level, elapsedSeconds, elapsedMinutes, secondsAfterConversion,score,lifelinesAvailable=3,fifty1,fifty2,
            correct,audienceAnswer,phoneAnswer,phoneStatus=0,audienceStatus=0,status50=0;
    ArrayList<Question> questions;
    Random random;
    long tStart, tEnd;
    String[] scores;
    boolean bContinue=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_play);
        tStart = System.currentTimeMillis();
        playTheGame();
        attachViewsToElements();
        level=0;


        if(bContinue==false){
            phoneStatus = 0;
            status50 = 0;
            audienceStatus = 0;
        }

        playTheGame.run();

    }

    public void playTheGame() {
        questions = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("questions");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 0; i < 36; i++) {
                    Question question = new Question();
                    question.setOptionA(dataSnapshot.child(String.valueOf(i)).child("optionA").getValue().toString());
                    question.setOptionB(dataSnapshot.child(String.valueOf(i)).child("optionB").getValue().toString());
                    question.setOptionC(dataSnapshot.child(String.valueOf(i)).child("optionC").getValue().toString());
                    question.setOptionD(dataSnapshot.child(String.valueOf(i)).child("optionD").getValue().toString());
                    question.setQuestion(dataSnapshot.child(String.valueOf(i)).child("question").getValue().toString());
                    question.setCorrect(dataSnapshot.child(String.valueOf(i)).child("correct").getValue().toString());
                     questions.add(question);

                     }
                     readTheQuestion(level);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

                }
        );
    }

    private void readTheQuestion(int level) {
        int min = level * 3;
        int max = level * 3 + 2;

        random = new Random();
        int questionIndex = random.nextInt((max - min) + 1) + min;

        Question questionToRead = questions.get(questionIndex);
        optionA.setText("A: "+questionToRead.getOptionA());
        optionB.setText("B: "+questionToRead.getOptionB());
        optionC.setText("C: "+questionToRead.getOptionC());
        optionD.setText("D: "+questionToRead.getOptionD());
        question.setText(questionToRead.getQuestion());
        money.setText(getResources().getStringArray(R.array.tablica_kwot)[level]+" zł");

        ArrayList<Integer> answers = new ArrayList<>();
        for (int i=1; i<=4; i++)
        {
            answers.add(i);
        }

        if(questionToRead.getOptionA().equals(questionToRead.getCorrect())) {correct =1; fifty1=2; fifty2=3;}
                else if(questionToRead.getOptionB().equals(questionToRead.getCorrect())) {correct =2; fifty1=1; fifty2=4;}
                else if(questionToRead.getOptionC().equals(questionToRead.getCorrect())) {correct =3; fifty1=2; fifty2=4;}
                else {correct=4; fifty1=1; fifty2=3;}

        phoneAnswer=correct;
        audienceAnswer=correct;
 }



    // Słuchacze
    View.OnClickListener handleroptionA = new View.OnClickListener() {
        public void onClick(View v) {
            optionA.setBackgroundResource(R.drawable.button_opcion_selected);
            optionA.postDelayed(checkoptionA, 500);
        }
    };

    View.OnClickListener handleroptionB = new View.OnClickListener() {
        public void onClick(View v) {
            optionB.setBackgroundResource(R.drawable.button_opcion_selected);
            optionB.postDelayed(checkoptionB, 500);
        }
    };

    View.OnClickListener handleroptionC = new View.OnClickListener() {
        public void onClick(View v) {
            optionC.setBackgroundResource(R.drawable.button_opcion_selected);
            optionC.postDelayed(checkoptionC, 500);
        }
    };

    View.OnClickListener handleroptionD = new View.OnClickListener() {
        public void onClick(View v) {
            optionD.setBackgroundResource(R.drawable.button_opcion_selected);
            optionD.postDelayed(checkoptionD, 500);
        }
    };

    View.OnClickListener handlermenuItemPhone = new View.OnClickListener() {

        public void onClick(View v) {


            menuItemPhone.setBackgroundResource(R.drawable.icon_phone_used);
            menuItemPhone.setEnabled(false);

                phoneStatus=level;
                lifelinePhone();

            lifelinesAvailable--;
            if (lifelinesAvailable ==0) Toast.makeText(getApplicationContext(), R.string.NoHelp, Toast.LENGTH_LONG).show();
        }
    };


    View.OnClickListener handlermenuItem50 = new View.OnClickListener() {
        public void onClick(View v) {

            menuItem50.setBackgroundResource(R.drawable.icon_50_used);
            menuItem50.setEnabled(false);

                status50=level;
                lifeline50();

            lifelinesAvailable--;
            if (lifelinesAvailable ==0) Toast.makeText(getApplicationContext(), R.string.NoHelp, Toast.LENGTH_LONG).show();
        }
    };


    View.OnClickListener handlermenuItemAudience = new View.OnClickListener() {
        public void onClick(View v) {

            menuItemAudience.setBackgroundResource(R.drawable.icon_audience_used);
            menuItemAudience.setEnabled(false);

                audienceStatus=level;
                lifelineAudience();



            lifelinesAvailable--;
            if (lifelinesAvailable ==0) Toast.makeText(getApplicationContext(), R.string.NoHelp, Toast.LENGTH_LONG).show();
        }
    };

    View.OnClickListener handlermenuItemEnd = new View.OnClickListener() {
        public void onClick(View v) {
            endGame(true);
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Czy chcesz zakończyć?")
                    .setTitle("Wyjście")
                    .setCancelable(false)

                    .setNegativeButton(R.string.backAndNotSave,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    level=0;
                                    bContinue=false;

                                    finish();

                                }
                            })
                    .setNeutralButton(R.string.backAndContinue,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void lifeline50()
    {
        switch(fifty1){
            case 1:optionA.setVisibility(Button.INVISIBLE);optionA.setClickable(false);break;
            case 2:optionB.setVisibility(Button.INVISIBLE);optionB.setClickable(false);break;
            case 3:optionC.setVisibility(Button.INVISIBLE);optionC.setClickable(false);break;
            case 4:optionD.setVisibility(Button.INVISIBLE);optionD.setClickable(false);break;
        }
        switch(fifty2){
            case 1:optionA.setVisibility(Button.INVISIBLE);optionA.setClickable(false);break;
            case 2:optionB.setVisibility(Button.INVISIBLE);optionB.setClickable(false);break;
            case 3:optionC.setVisibility(Button.INVISIBLE);optionC.setClickable(false);break;
            case 4:optionD.setVisibility(Button.INVISIBLE);optionD.setClickable(false);break;
        }
    }

    private void lifelinePhone()
    {
        switch(phoneAnswer){
            case 1:optionA.setBackgroundResource(R.drawable.button_opcion_selected);break;
            case 2:optionB.setBackgroundResource(R.drawable.button_opcion_selected);break;
            case 3:optionC.setBackgroundResource(R.drawable.button_opcion_selected);break;
            case 4:optionD.setBackgroundResource(R.drawable.button_opcion_selected);break;
        }
    }

    private void lifelineAudience()
    {
        switch(audienceAnswer){
            case 1:optionA.setBackgroundResource(R.drawable.button_opcion_selected);break;
            case 2:optionB.setBackgroundResource(R.drawable.button_opcion_selected);break;
            case 3:optionC.setBackgroundResource(R.drawable.button_opcion_selected);break;
            case 4:optionD.setBackgroundResource(R.drawable.button_opcion_selected);break;
        }
    }

    private void showIncorrectAnswer(int selectedAnswer, int correct)
    {
        switch(selectedAnswer)
        {
            case 1: optionA.setBackgroundResource(R.drawable.button_opcion_wrong); break;
            case 2: optionB.setBackgroundResource(R.drawable.button_opcion_wrong); break;
            case 3: optionC.setBackgroundResource(R.drawable.button_opcion_wrong); break;
            case 4: optionD.setBackgroundResource(R.drawable.button_opcion_wrong); break;
            default:break;
        }

        switch(correct)
        {
            case 1: optionA.setBackgroundResource(R.drawable.button_opcion_correct); break;
            case 2: optionB.setBackgroundResource(R.drawable.button_opcion_correct); break;
            case 3: optionC.setBackgroundResource(R.drawable.button_opcion_correct); break;
            case 4: optionD.setBackgroundResource(R.drawable.button_opcion_correct); break;
            default:break;
        }
    }

    Runnable checkoptionA= new Runnable() {
        @Override
        public void run() {
            if(correct == 1)
            {
                optionA.setBackgroundResource(R.drawable.button_opcion_correct);
                level++;
                if(level == 12)
                {

                    optionA.postDelayed(runnableEndGame, 1000);
                }
                else {

                    optionA.postDelayed(clearButtons, 1000);
                    optionA.postDelayed(readNextQuestion, 1000);
                }
            }else{
                showIncorrectAnswer(1, correct);
                optionA.postDelayed(runnableEndGame, 1000);
            }
        }
    };

    Runnable checkoptionB= new Runnable() {
        @Override
        public void run() {
            if(correct == 2)
           {
               optionB.setBackgroundResource(R.drawable.button_opcion_correct);
               level++;
                if(level == 12)
                {

                    optionB.postDelayed(runnableEndGame, 1000);
                }
                else
                {
                    optionB.postDelayed(clearButtons, 1000);
                    optionB.postDelayed(readNextQuestion, 1000);
                }

            }
            else
            {
                showIncorrectAnswer(2, correct);
                optionB.postDelayed(runnableEndGame, 1000);
            }
        }
    };

    Runnable checkoptionC= new Runnable() {
        @Override
        public void run() {
            if(correct == 3)
            {

                optionC.setBackgroundResource(R.drawable.button_opcion_correct);
                level++;
                if(level == 12)
                {

                    optionC.postDelayed(runnableEndGame, 1000);
                }
               else
                {

                    optionC.postDelayed(clearButtons, 1000);
                    optionC.postDelayed(readNextQuestion, 1000);
               }

            }

            else
            {
                showIncorrectAnswer(3, correct);
               optionC.postDelayed(runnableEndGame, 1000);
           }
        }
    };

    Runnable checkoptionD= new Runnable() {
        @Override
        public void run() {
            if(correct == 4)
            {

                optionD.setBackgroundResource(R.drawable.button_opcion_correct);
                level++;
                if(level == 12)
                {

                    optionD.postDelayed(runnableEndGame, 1000);
                }
                else
                {

                    optionD.postDelayed(clearButtons, 1000);
                    optionD.postDelayed(readNextQuestion, 1000);
                }

            }
            else
           {
                showIncorrectAnswer(4, correct);
                optionD.postDelayed(runnableEndGame, 1000);
           }
        }
    };


        Runnable clearButtons = new Runnable() {
            @Override
            public void run() {
                //Przywrócenie funkcjonalności po użyciu koła ratunkowego w poprzednim pytaniu
                optionA.setVisibility(Button.VISIBLE); optionA.setClickable(true);
                optionB.setVisibility(Button.VISIBLE); optionB.setClickable(true);
                optionC.setVisibility(Button.VISIBLE); optionC.setClickable(true);
                optionD.setVisibility(Button.VISIBLE); optionD.setClickable(true);


                optionA.setBackgroundResource(R.drawable.button_opcion);
                optionB.setBackgroundResource(R.drawable.button_opcion);
                optionC.setBackgroundResource(R.drawable.button_opcion);
                optionD.setBackgroundResource(R.drawable.button_opcion);
            }
        };



    private void endGame(boolean giveUp) {
        calculateWinningAmount(giveUp);
        calculateTime();
        putValuesToSharedPreferences();
        nameDialog();
    }

    private void calculateWinningAmount(boolean giveUp) {
        //Jeśli gracz się poddaje, wygrywa kwotę z poprzedniego pytania
        //Jeśli gracz się nie poddaje, wygrywa kwotę z progu gwarantowanego
        String[] amounts=getResources().getStringArray(R.array.tablica_kwot);
        String aux;
        score=0;
        if(level-1>0){
            aux=amounts[level-1];
            score=Integer.valueOf(aux);
        }
        else {
            if(level==0) score=0;
            if(level==1) score=500;
        }

        if(!giveUp){
            if(level<2)score=0;
            else if(level>=2 && level<7)score=Integer.valueOf(amounts[1]);
            else if(level>=7 && level<12)score=Integer.valueOf(amounts[6]);
            else if(level==12)score=Integer.valueOf(amounts[11]);
        }
    }

    private void calculateTime(){
        tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        elapsedSeconds = (int)(tDelta / 1000.0);
        elapsedMinutes = elapsedSeconds/60;
        secondsAfterConversion = elapsedSeconds-elapsedMinutes*60;
    }

    private void putValuesToSharedPreferences() {
        SharedPreferences myScore = getSharedPreferences("MyScore",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myScore.edit();
        editor.putInt("score",score);
        editor.putInt("minutes",elapsedMinutes);
        editor.putInt("seconds",secondsAfterConversion);
        editor.commit();
    }

    private void nameDialog() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);

        alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = promptsView
                .findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                result.setText(userInput.getText());
                                name = userInput.getText().toString();
                                saveToDB();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void saveToDB(){
        database = FirebaseDatabase.getInstance().getReference("highscore");
        database.push().setValue(new Result(name,score,elapsedMinutes,secondsAfterConversion,elapsedSeconds));
        startActivity(new Intent(PlayActivity.this, ScoreActivity.class));
        finish();
    }

    private void attachViewsToElements() {
        scores = getResources().getStringArray(R.array.tablica_kwot);
        question= findViewById(R.id.textViewQuestion);
        money= findViewById(R.id.textViewMoney);
        header= findViewById(R.id.buttonHeaderPlay);
        optionA= findViewById(R.id.buttonoptionA);
        optionA.setOnClickListener(handleroptionA);
        optionB= findViewById(R.id.buttonoptionB);
        optionB.setOnClickListener(handleroptionB);
        optionC= findViewById(R.id.buttonoptionC);
        optionC.setOnClickListener(handleroptionC);
        optionD= findViewById(R.id.buttonoptionD);
        optionD.setOnClickListener(handleroptionD);
        menuItemPhone= findViewById(R.id.menuItemPhone);
        menuItemPhone.setOnClickListener(handlermenuItemPhone);
        menuItem50= findViewById(R.id.menuItem50);
        menuItem50.setOnClickListener(handlermenuItem50);
        menuItemAudience= findViewById(R.id.menuItemAudience);
        menuItemAudience.setOnClickListener(handlermenuItemAudience);
        menuItemEnd= findViewById(R.id.menuItemEnd);
        menuItemEnd.setOnClickListener(handlermenuItemEnd);

        result = (EditText) findViewById(R.id.editTextResult);
    }

    Runnable playTheGame= new Runnable()
    {
        @Override
        public void run(){

            playTheGame();
        }

    };

    Runnable readNextQuestion = new Runnable() {
        public void run() {
            readTheQuestion(level);
        }
    };

    //
    Runnable runnableEndGame= new Runnable() {
        @Override
        public void run() {
            endGame(false);
        }
    };

}