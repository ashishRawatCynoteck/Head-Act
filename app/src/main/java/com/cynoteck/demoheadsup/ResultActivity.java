package com.cynoteck.demoheadsup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends FragmentActivity {
    TextView correctStringCount;
    TextView skippedStringCount;
    String[] skippedStrings;
    ListView corretList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle b = getIntent().getExtras();
        corretList = findViewById(R.id.correctLV);
        Intent intent = getIntent();
        correctStringCount = (TextView) findViewById(R.id.number_of_correct_strings);
        skippedStringCount = (TextView) findViewById(R.id.number_of_skipped_strings);
        correctStringCount.setText(String.valueOf(b.getInt("CorrectArrSize")));
        skippedStringCount.setText(String.valueOf(b.getInt("SkippedArrSize")));
        skippedStrings = b.getStringArray("SkippedStrings");
        ArrayList<String> correctStrings = (ArrayList<String>) getIntent().getSerializableExtra("CorrectStrings");
        Log.e("moviews", String.valueOf(correctStrings));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.correct_list_name, R.id.movieNameTV, correctStrings);
        corretList.setAdapter(adapter);


    }
}
