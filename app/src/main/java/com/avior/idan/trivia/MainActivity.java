package com.avior.idan.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner subjectSpinner = (Spinner) findViewById(R.id.subject_spinner);
        ArrayAdapter<CharSequence> subjectAdapter = ArrayAdapter.createFromResource(this,
                R.array.subjects_array, android.R.layout.simple_spinner_item);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);

        final Spinner typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.quiz_types_array, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        Button proceedButton = (Button) findViewById(R.id.proceed_button);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TriviaScreen.class);
                intent.putExtra("subject", subjectSpinner.getSelectedItem().toString());
                intent.putExtra("type", typeSpinner.getSelectedItem().toString());
                startActivity(intent);
            }
        });

    }
}
