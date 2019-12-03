package com.android.final_exam_q1;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText word1;
    private EditText word2;
    private EditText word3;
    private TextView haiku;
    private Button submit;
    private String output;
    private String reqUrl = "http://rhymebrain.com/talk?";    // call rhymebrain
    private static final String REQUEST_METHOD_GET = "GET";
    private static final String TAG_HTTP_URL_CONNECTION = "HTTP_URL_CONNECTION";
    private static final int MAX_RESULTS = 50;
    //private Bundle b = new Bundle();

    // establish syllable arrays
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
        setContentView(R.layout.activity_main);

        // connect XML to code
        word1 = (EditText) findViewById(R.id.word1);
        word2 = (EditText) findViewById(R.id.word2);
        word3 = (EditText) findViewById(R.id.word3);
        haiku = (TextView) findViewById(R.id.haiku_text);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                processWords();

            }
        });

        Button genHaiku = (Button) findViewById(R.id.haiku);
        genHaiku.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Log.d("Gen Haiku", "Generate Haiku");
                makeHaiku();
            }
        });

    }

    private void processWords() {
        String word01, word02, word03;

        // get user input fro each of the words
        word01 = word1.getText().toString();
        startSendHttpRequestThread(word01);

        word02 = word2.getText().toString();
        startSendHttpRequestThread(word02);

        word03 = word3.getText().toString();
        startSendHttpRequestThread(word03);

        // use the words to make a Haiku
        makeHaiku();

    }

    /* Start a thread to send http request to web server use HttpURLConnection object. */
    private void startSendHttpRequestThread(final String word)
    {

        Thread sendHttpRequestThread = new Thread()
        {
            @Override
            public void run() {
                // using Panel 7.2.21 as a model- The Movie API

                //make empty URL and connection
                URL url;
                HttpURLConnection ur1Connection = null; //HttpsURLConnection aiso avaitab1e
                try {

                    //String service = reqUrl;    // call rhymebrain
                    String service = "https://rhymebrain.com/talk?";    // call rhymebrain
                    //String parm = "getRhymes&word=" + word;
                    //String queryString = URLEncoder.encode(parm, "UTF-8");
                    String queryString = "getRhymes&word=" + word + "&maxResults=" + String.valueOf(MAX_RESULTS);
                    //try to process url and connect to it
                    url = new  URL( service +  "function=" + queryString);
                    Log.d("which URL: ", String.valueOf(url));
                    ur1Connection = (HttpURLConnection)url.openConnection();
                    ur1Connection.setRequestMethod("GET");

                    // Set connection timeout and read timeout value.
                    ur1Connection.setConnectTimeout(70000);
                    ur1Connection.setReadTimeout(70000);

                    //create an input stream and stream reader from the connection
                    InputStream inputStream = ur1Connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    //get some data from the stream
                    int data = inputStreamReader.read();
                    //string for collecting all output
                    output = "";
                    //if the stream is not empty
                    while(data != -1) {
                        //turn what we read into a char and print it
                        char current = (char) data;
                        output += current;
                        data = inputStreamReader.read();

                        //Log.d("Network", output);
                    }
                    Log.d("Network", output);
                    parseJSON(output);
                    int i =0;
                }catch (Exception e) {
                    Log.d( "Network", e.toString());
                }finally {
                    if (ur1Connection != null) {
                        ur1Connection.disconnect();
                        ur1Connection = null;
                    }
                }
            }

        };
        // Start the child thread to request web page.
        sendHttpRequestThread.start();
    }

    private void parseJSON(String rhymeJSON) {
        try {
            String word;
            String numOfSyllables;

            // process JSON rhyming word list
            JSONArray jsonArray = new JSONArray(rhymeJSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject wordListObject = jsonArray.getJSONObject(i);
                word = wordListObject.getString("word");
                numOfSyllables = wordListObject.getString("syllables");

                // sift the JSON by syllable count
                switch (numOfSyllables) {
                    case "1":
                        one_syllable.add(word);
                        break;
                    case "2":
                        two_syllable.add(word);
                        break;
                    case "3":
                        three_syllable.add(word);
                        break;
                    case "4":
                        four_syllable.add(word);
                        break;
                    case "5":
                        five_syllable.add(word);
                        break;
                    case "6":
                        six_syllable.add(word);
                        break;
                    case "7":
                        seven_syllable.add(word);
                        break;
                    default:
                        // throw it away
                        break;
                }
            }

            // tuck them into bundles
            /*// send word1's data off to generate a Haiku
            b.putStringArrayList("one_syllable", one_syllable);
            b.putStringArrayList("two_syllable", two_syllable);
            b.putStringArrayList("three_syllable", three_syllable);
            b.putStringArrayList("four_syllable", four_syllable);
            b.putStringArrayList("five_syllable", five_syllable);
            b.putStringArrayList("six_syllable", six_syllable);
*/
        } catch (JSONException e) {
            Log.d("MainActivity", e.toString());
            int i =0;
        }
    }

    private void makeHaiku() {

        // which syllable count has words
        Random random = new Random();
        boolean[] hasWords = {false,false,false,false,false,false,false};
        char[][] fiveSylablePatterns =  {{2,3},{3,2},{1,4},{4,1}};
        char[][] sevenSylablePatterns =  {{3,4},{4,3},{2,5},{5,2}};
        int wordIdx1 = 0;
        int wordIdx2 = 0;
        int wordIdx3 = 0;
        int wordIdx4 = 0;
        int wordIdx5 = 0;
        int wordIdx6 = 0;
        int wordIdx7 = 0;


        /*Bundle b = new Bundle();
        Intent passData = new Intent(getApplicationContext(), HaikuActivity.class);

        // send word1's data off to generate a Haiku
        b.putStringArrayList("one_syllable", one_syllable);
        b.putStringArrayList("two_syllable", two_syllable);
        b.putStringArrayList("three_syllable", three_syllable);
        b.putStringArrayList("four_syllable", four_syllable);
        b.putStringArrayList("five_syllable", five_syllable);
        b.putStringArrayList("six_syllable", six_syllable);
*/
        // check if arrays are populated
        if (one_syllable != null) {
            if (one_syllable.size() > 0) {
                hasWords[0] = true;
                wordIdx1=random.nextInt(one_syllable.size()-1);
            }
        }

        if (two_syllable != null) {
            if (two_syllable.size() > 0) {
                hasWords[1] = true;
                wordIdx2=random.nextInt(two_syllable.size()-1);
            }
        }

        if (three_syllable != null) {
            if (three_syllable.size() > 0) {
                hasWords[2] = true;
                wordIdx3=random.nextInt(three_syllable.size()-1);
            }
        }

        if (four_syllable != null) {
            if (four_syllable.size() > 0) {
                hasWords[3] = true;
                wordIdx4=random.nextInt(four_syllable.size()-1);
            }
        }

        if (five_syllable != null) {
            if (five_syllable.size() > 0) {
                hasWords[4] = true;
                wordIdx5=random.nextInt(five_syllable.size()-1);
            }
        }

        if (six_syllable != null) {
            if (six_syllable.size() > 0) {
                hasWords[5] = true;
                wordIdx6=random.nextInt(six_syllable.size()-1);
            }
        }

        if (seven_syllable != null) {
            if (seven_syllable.size() > 0) {
                hasWords[6] = true;
                wordIdx7=random.nextInt(seven_syllable.size()-1);
            }
        }

        int j = 0;

        // just in case the service is unavailable
        one_syllable.add("one");
        one_syllable.add("two");
        one_syllable.add("go");
        one_syllable.add("though");
        one_syllable.add("dough");
        wordIdx1=random.nextInt(one_syllable.size()-1);

        two_syllable.add("chateaux");
        two_syllable.add("although");
        two_syllable.add("overgrow");
        two_syllable.add("idle");
        two_syllable.add("primal");
        wordIdx2=random.nextInt(two_syllable.size()-1);


        three_syllable.add("buffalo");
        three_syllable.add("overflow");
        three_syllable.add("overthrow");
        three_syllable.add("sanity");
        three_syllable.add("balcony");
        wordIdx3=random.nextInt(three_syllable.size()-1);

        four_syllable.add("portfolio");
        four_syllable.add("presidio");
        four_syllable.add("moustachio");
        four_syllable.add("catastrophe");
        four_syllable.add("totality");
        wordIdx4=random.nextInt(four_syllable.size()-1);

        five_syllable.add("impresario");
        five_syllable.add("archipelago");
        five_syllable.add("pianissimo");
        five_syllable.add("generality");
        five_syllable.add("circularity");
        wordIdx5=random.nextInt(five_syllable.size()-1);

        six_syllable.add("colonialism");
        six_syllable.add("materialism");
        six_syllable.add("emotionalism");
        six_syllable.add("congeniality");
        six_syllable.add("irrationality");
        wordIdx6=random.nextInt(six_syllable.size()-1);

        seven_syllable.add("colonialism");
        seven_syllable.add("Arteriosclerosis");
        seven_syllable.add("Artificiality");
        seven_syllable.add("Autobiographical");
        seven_syllable.add("Editorializing");
        wordIdx7=random.nextInt(seven_syllable.size()-1);


        /*// generate the next index for each syllable
        int wordIdx1=random.nextInt(one_syllable.size()-1);
        int wordIdx2=random.nextInt(two_syllable.size()-1);
        int wordIdx3=random.nextInt(three_syllable.size()-1);
        int wordIdx4=random.nextInt(four_syllable.size()-1);
        int wordIdx5=random.nextInt(five_syllable.size()-1);
        int wordIdx6=random.nextInt(six_syllable.size()-1);
        int wordIdx7=random.nextInt(seven_syllable.size()-1);*/

        // update the activity_main layout - add a TextView
        // update the activity_main layout add Generate Haiku button
        // add this inot the TextView
        // set up to choose random patterns
        String haiku_debug = one_syllable.get(wordIdx1)+ " " + two_syllable.get(wordIdx2) + ",\n";
        haiku_debug = haiku_debug + three_syllable.get(wordIdx3) + " " + four_syllable.get(wordIdx4) + " " + five_syllable.get(wordIdx5)  + ",\n";
        haiku_debug = haiku_debug + six_syllable.get(wordIdx6) + " " + seven_syllable.get(wordIdx7) + ".";

        haiku.setText(haiku_debug.toString());



        int i = 0;


       /* passData.putExtras(b);
        startActivity(passData);*/

    }
}