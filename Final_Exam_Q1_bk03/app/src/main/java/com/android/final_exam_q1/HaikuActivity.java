package com.android.final_exam_q1;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HaikuActivity extends AppCompatActivity {

    private TextView haiku;

    // establish syllable arrays for word #1
    private ArrayList<String> one_syllable = new ArrayList<String>();
    private ArrayList<String> two_syllable = new ArrayList<String>();
    private ArrayList<String> three_syllable = new ArrayList<String>();
    private ArrayList<String> four_syllable = new ArrayList<String>();
    private ArrayList<String> five_syllable = new ArrayList<String>();
    private ArrayList<String> six_syllable = new ArrayList<String>();
    private ArrayList<String> seven_syllable = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haiku);

        // Get the data
        Bundle b = getIntent().getExtras();

        // receive syllable data
        ArrayList<String> two_syllable=b.getStringArrayList("two_syllable");
        ArrayList<String> three_syllable=getIntent().getExtras().getStringArrayList("three_syllable");
        ArrayList<String> four_syllable=getIntent().getExtras().getStringArrayList("four_syllable");

        haiku = (TextView)findViewById(R.id.haiku_text);

        Button genHaiku = (Button) findViewById(R.id.haiku);
        genHaiku.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Log.d("Gen Haiku", "Generate Haiku");
                generateHaiku();

            }
        });

        generateHaiku();
    }


    // display the haiku on the screen
    private void generateHaiku() {

        boolean[] syllable_exists = {false,false,false,false,false,false,false};

        if (one_syllable.size() > 0) syllable_exists[0] = true;
        if (two_syllable.size() > 0) syllable_exists[1] = true;
        if (three_syllable.size() > 0) syllable_exists[2] = true;
        if (four_syllable.size() > 0) syllable_exists[3] = true;
        if (five_syllable.size() > 0) syllable_exists[4] = true;
        if (six_syllable.size() > 0) syllable_exists[5] = true;
        if (seven_syllable.size() > 0) syllable_exists[6] = true;

    }

}
