package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        textViewResult = findViewById(R.id.textViewResault);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("countOfRightAnswer")) {
            int countOfRightAnswer = intent.getIntExtra("countOfRightAnswer", 0);
            int countOfQuestion = intent.getIntExtra("countOfQuestion", 0);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int max = preferences.getInt("max", 0);
            String result = String.format("Ваш результат: \n" +
                            "Правильных ответов %s \n" +
                            "Из %s вопросов\n" +
                            "Максимальный результат %s",
                    countOfRightAnswer,
                    countOfQuestion,
                    max);
            textViewResult.setText(result);
        }
    }

    public void onClickStartNewGame(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}