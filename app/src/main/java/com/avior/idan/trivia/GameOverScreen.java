package com.avior.idan.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverScreen extends AppCompatActivity {

    Button returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);

        TextView score = (TextView) findViewById(R.id.score);
        score.setText("Your score: " + getIntent().getIntExtra("score", 0));
        returnButton = (Button) findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });
    }

    public void restartGame(){
        Intent intent = new Intent(GameOverScreen.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        restartGame();
    }

}
