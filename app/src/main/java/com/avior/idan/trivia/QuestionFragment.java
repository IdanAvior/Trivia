package com.avior.idan.trivia;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class QuestionFragment extends Fragment {

    public static QuestionFragment newInstance(String question, String[] answers) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString("question", question);
        args.putStringArray("answers", answers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        TextView question =(TextView) view.findViewById(R.id.question);
        question.setText(Html.fromHtml(getArguments().getString("question")));
        question.setTextSize(25);
        final String[] answers = getArguments().getStringArray("answers");
        final RadioGroup radioGroup = new RadioGroup(getContext());

        if (answers.length > 2) {
            for (int i = 0; i < answers.length; i++) {
                RadioButton radioButton = new RadioButton(getContext());
                radioButton.setId(i + 1);
                radioButton.setText(Html.fromHtml(answers[i]));
                radioButton.setTextSize(20);
                radioGroup.addView(radioButton);
            }
        }
        else{
            RadioButton trueRadioButton = new RadioButton(getContext());
            RadioButton falseRadioButton = new RadioButton(getContext());
            trueRadioButton.setId(0+1);
            falseRadioButton.setId(0+2);
            trueRadioButton.setText("True");
            falseRadioButton.setText("False");
            trueRadioButton.setTextSize(20);
            falseRadioButton.setTextSize(20);
            radioGroup.addView(trueRadioButton);
            radioGroup.addView(falseRadioButton);
        }
        ViewGroup viewGroup = (ViewGroup) view;
        viewGroup.addView(radioGroup);
        Button button = new Button(getContext());
        button.setText("Submit");
        button.setTextSize(20);
        button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        button.setTextColor(getResources().getColor(R.color.colorPrimary));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = null;
                for (int i = 0; i < answers.length; i++) {
                    RadioButton button = (RadioButton) radioGroup.getChildAt(i);
                    if (button.isChecked()) {
                        Spanned htmlText = Html.fromHtml(button.getText().toString());
                        answer = htmlText.toString();
                    }
                }
                ((TriviaScreen)getActivity()).updateActivity(answer);
            }
        });
        viewGroup.addView(button);
        return view;
    }

    public static interface QuestionFragmentActivity{
        public void updateActivity(String s);
    }

}
