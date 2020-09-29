package com.mcq.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.mcq.trivia.data.AnswerListAsyncResponse;
import com.mcq.trivia.data.QuestionBank;
import com.mcq.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Question> questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                Log.d("Inside", "processFinished " + questionArrayList);

            }
        });

        //Log.d("Main", "onCreate: " + questionList);



    }
}