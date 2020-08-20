package com.cynoteck.HeadAct;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

public class ResultActivity extends FragmentActivity {
    TextView correctStringCount;
    TextView skippedStringCount;
    String[] skippedStrings;
    ListView corretList;
    View doneView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        doneView = findViewById(R.id.doneView);
        Bundle b = getIntent().getExtras();
        corretList = findViewById(R.id.correctLV);
        correctStringCount = findViewById(R.id.number_of_correct_strings);
        correctStringCount.setText(String.valueOf(b.getInt("CorrectArrSize")));
        ArrayList<String> correctStrings = (ArrayList<String>) getIntent().getSerializableExtra("CorrectStrings");
        Log.e("moviews", String.valueOf(correctStrings));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.correct_list_name, R.id.movieNameTV, correctStrings);
        corretList.setDivider(null);
        corretList.setAdapter(adapter);

        doneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveIntent = new Intent(ResultActivity.this,MainActivity.class);
                startActivity(moveIntent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent moveIntent = new Intent(ResultActivity.this,MainActivity.class);
        startActivity(moveIntent);
    }
}
