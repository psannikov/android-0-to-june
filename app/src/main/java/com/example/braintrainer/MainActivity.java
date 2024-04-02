package com.example.braintrainer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView textViewTimer;
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewOption0;
    private TextView textViewOption1;
    private TextView textViewOption2;
    private TextView textViewOption3;
    private ArrayList<TextView> options = new ArrayList<>();

    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private int min = 1;
    private int max = 10;
    private int countOfQuestion = 0;
    private int countOfRightAnswer = 0;
    private boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewScore = findViewById(R.id.textViewScore);
        textViewOption0 = findViewById(R.id.textViewOption0);
        textViewOption1 = findViewById(R.id.textViewOption1);
        textViewOption2 = findViewById(R.id.textViewOption2);
        textViewOption3 = findViewById(R.id.textViewOption3);
        options.add(textViewOption0);
        options.add(textViewOption1);
        options.add(textViewOption2);
        options.add(textViewOption3);
        playNext();
        CountDownTimer timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                textViewTimer.setText(getTime(l));
                if (l < 10000) {
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }

            @Override
            public void onFinish() {
                gameOver = true;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = preferences.getInt("max", 0);
                if (countOfRightAnswer >= max) {
                    preferences.edit().putInt("max",countOfRightAnswer).apply();
                }
                Intent intent = new Intent(MainActivity.this,ScoreActivity.class);
                intent.putExtra("countOfRightAnswer", countOfRightAnswer);
                intent.putExtra("countOfQuestion", countOfQuestion);
                startActivity(intent);
            }
        };
        timer.start();
    }
    private void playNext() {
        generateQuestion();
        for (int i = 0; i < options.size(); i++) {
            if (i == rightAnswerPosition) {
                options.get(i).setText(Integer.toString(rightAnswer));
            } else {
                options.get(i).setText(Integer.toString(generateWrongAnswer()));
            }
        }
        String score = String.format("%s / %s", countOfRightAnswer, countOfQuestion);
        textViewScore.setText(score);
    }
    private void generateQuestion() {
        int a = (int) (Math.random()*(max - min + 1) + min);
        int b = (int) (Math.random()*(max - min + 1) + min);
        rightAnswer = a * b;
        question = String.format("%s * %s", a, b);
        textViewQuestion.setText(question);
        rightAnswerPosition = (int) (Math.random()*4);
    }
    private int generateWrongAnswer() {
        int result;
        do {
            int a = (int) (Math.random()*(max - min + 1) + min);
            int b = (int) (Math.random()*(max - min + 1) + min);
            result = a * b;
        } while (result == rightAnswer);
        return result;
    }
    private String getTime (long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
    }
    public void onClickAnswer(View view) {
        if (!gameOver) {
            TextView textView = (TextView) view;
            String answer = textView.getText().toString();
            int chosenAnswer = Integer.parseInt(answer);
            if (chosenAnswer == rightAnswer) {
                countOfRightAnswer++;
                Toast.makeText(this, "Правильный ответ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Неправильный ответ", Toast.LENGTH_SHORT).show();
            }
            countOfQuestion++;
            playNext();
        }
    }
}