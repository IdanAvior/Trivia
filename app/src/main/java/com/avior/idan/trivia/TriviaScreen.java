package com.avior.idan.trivia;

import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;

public class TriviaScreen extends AppCompatActivity implements QuestionFragment.QuestionFragmentActivity{

    String correctAnswer;
    final int numberOfQuestions = 10;
    final int pointsPerEasyQuestion = 20;
    final int pointsPerMediumQuestion = 50;
    final int pointsPerHardQuestion = 100;
    int numberOfAnswers;
    int points;
    int pointsPerCorrectAnswer;
    int questionNumber;
    String difficulty;
    String quizType;
    String categoryName;
    String token;
    TextView questionNumberTextView;
    TextView pointsTextView;
    TextView difficultyTextView;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_screen);

        questionNumberTextView = (TextView) findViewById(R.id.question_number);
        pointsTextView = (TextView) findViewById(R.id.points);
        pointsTextView.setText("Score: 0");
        difficultyTextView = (TextView) findViewById(R.id.difficulty);
        difficultyTextView.setText("Easy");
        difficultyTextView.setTextColor(Color.GREEN);
        String rawType = getIntent().getStringExtra("type");
        if (rawType.equals("Multiple Choice")) {
            quizType = "multiple";
            numberOfAnswers = 4;
        }
        else {
            quizType = "boolean";
            numberOfAnswers = 2;
        }
        categoryName = getIntent().getStringExtra("subject");
        client = new AsyncHttpClient();
        startNewGame();
    }

    public void updateActivity(String s)
    {
        if (s == null)
            Toast.makeText(this,"No answer selected", Toast.LENGTH_SHORT).show();
        else {
            if (s.equals(correctAnswer)) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                points+=pointsPerCorrectAnswer;
                pointsTextView.setText("Score: " + points);
                if (points == 3*pointsPerEasyQuestion) {
                    difficulty = "medium";
                    difficultyTextView.setText("Medium");
                    difficultyTextView.setTextColor(Color.YELLOW);
                    pointsPerCorrectAnswer = pointsPerMediumQuestion;
                }
                if (points == 3*pointsPerEasyQuestion + 4*pointsPerMediumQuestion) {
                    difficulty = "hard";
                    difficultyTextView.setText("Hard");
                    difficultyTextView.setTextColor(Color.RED);
                    pointsPerCorrectAnswer = pointsPerHardQuestion;
                }
            }
            else
                Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
            if (questionNumber == numberOfQuestions + 1){
                Intent intent = new Intent(TriviaScreen.this, GameOverScreen.class);
                intent.putExtra("score", points);
                startActivity(intent);
            }
            else
                getNewQuestion();
        }
    }

    public void getNewQuestion(){
        if (token == null)
            retrieveToken();
        final String urlStart = "https://opentdb.com/api.php?amount=1&category=";
        final String urlMiddle = "&difficulty=";
        final String urlEnd = "&type=";
        final String constructedUrl = urlStart + getCategoryNumberByName(categoryName) + urlMiddle + difficulty + urlEnd + quizType;
        questionNumberTextView.setText("Question " + questionNumber++ + "/" + numberOfQuestions);
        client.get(constructedUrl, new JsonHttpResponseHandler(){
           @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
               try{
                   String question = response.getJSONArray("results").getJSONObject(0).getString("question");
                   correctAnswer = response.getJSONArray("results").getJSONObject(0).getString("correct_answer");
                   String[] answers = new String[numberOfAnswers];
                   for (int i = 0; i < numberOfAnswers - 1; i++)
                       answers[i] = response.getJSONArray("results").getJSONObject(0).getJSONArray("incorrect_answers").getString(i);
                   answers[numberOfAnswers - 1] = correctAnswer;
                   Collections.shuffle(Arrays.asList(answers));
                   getSupportFragmentManager().beginTransaction().replace(R.id.question_space, QuestionFragment.newInstance(question, answers)).commit();
               } catch (JSONException e){
                   e.printStackTrace();
               }
           }
        });

    }

    public int getCategoryNumberByName(String name){
        switch(name) {
            case "General Knowledge":
                return 9;
            case "Entertainment: Books":
                return 10;
            case "Entertainment: Film":
                return 11;
            case "Entertainment: Music":
                return 12;
            case "Entertainment: Musicals and Theatres":
                return 13;
            case "Entertainment: Television":
                return 14;
            case "Entertainment: Video Games":
                return 15;
            case "Entertainment: Board Games":
                return 16;
            case "Science and Nature":
                return 17;
            case "Science: Computers":
                return 18;
            case "Science: Mathematics":
                return 19;
            case "Mythology":
                return 20;
            case "Sports":
                return 21;
            case "Geography":
                return 22;
            case "History":
                return 23;
            case "Politics":
                return 24;
            case "Art":
                return 25;
            case "Celebrities":
                return 26;
            case "Animals":
                return 27;
            default:
                return 0;
        }
    }

    public void startNewGame(){
        points = 0;
        pointsPerCorrectAnswer = 20;
        difficulty = "easy";
        questionNumber = 1;
        retrieveToken();
        getNewQuestion();
    }

    public void retrieveToken(){
        client.get("https://opentdb.com/api_token.php?command=request", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try{
                    token = response.getString("token");
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }



}
