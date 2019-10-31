package com.example.go_snow_glencoe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.android.volley.VolleyLog.d;

public class MainActivity extends AppCompatActivity {
    //Below declares the Textviews created in activity_main
    TextView t1_temp, t2_city, t3_summary, t4_date, t5_upper_snow, t6_lower_snow, t7_new_snow, t8_last_snow,
                t9_conditions;
    //Below declares the Button views created in activity_main
    private Button directions_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declare/initiate the textviews
        t1_temp = findViewById(R.id.temp);
        t2_city = findViewById(R.id.city);
        t3_summary = findViewById(R.id.summary);
        t4_date = findViewById(R.id.date);
        t5_upper_snow = findViewById(R.id.upper_snow);
        t6_lower_snow = findViewById(R.id.lower_snow);
        t7_new_snow = findViewById(R.id.new_snow);
        t8_last_snow = findViewById(R.id.last_snow);
        t9_conditions = findViewById(R.id.conditions);

        // these call the methods below when the page/app is loaded up
        find_weather();
        find_snow();

        //to get date, create an instance of Calendar class, then getInstance() of Calendar
        Calendar calendar = Calendar.getInstance();
        //to get String of current date
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        //after calendar returns the date below we ask it to set the String of text to the textView with id: date
        t4_date.setText(currentDate);

        //initiate the direction button in MainActivity
        final Button directions_button = findViewById(R.id.directions_button);

        directions_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDirectionsActivity(); //this is calling the method below and will go run the code in that method
                //when the Get Directions button is clicked
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(this, DirectionsActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3:
                Toast.makeText(this, "Item 3 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.subitem1:
                Toast.makeText(this, " Sub Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.subitem2:
                Toast.makeText(this, " Sub Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void find_snow() {
            String url = "https://api.weatherunlocked.com/api/snowreport/1398?app_id=7d008ca4&app_key=f2fcfd587f47046f1f04f48cb68a00a3";

            JsonObjectRequest snow = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String upper = response.getString("uppersnow_cm");
                        String lower = response.getString("lowersnow_cm");
                        String new_snow = response.getString("newsnow_cm");
                        String last_snow = response.getString("lastsnow");
                        String conditions = response.getString("conditions");
                        String name = response.getString("resortname");

                        t2_city.setText(name);
                        t5_upper_snow.setText(upper);
                        t6_lower_snow.setText(lower);
                        t7_new_snow.setText(new_snow);
                        t8_last_snow.setText(last_snow);
                        t9_conditions.setText(conditions);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            );
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(snow);
    }

    private void find_weather() {
            String url = "https://api.darksky.net/forecast/27a87fa552b2a741f881b3ee639b994a/56.6325,-4.8279";



            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject main_object = response.getJSONObject("currently");
//                        temperature = main_object.getString("temperature")
//                            JSONArray array = response.getJSONArray("weather");
                        String temp = String.valueOf(main_object.getDouble("temperature"));
//                        String city = response.getString("timezone");
                        String summary = main_object.getString("summary");

                        t1_temp.setText(temp);
//                        t2_city.setText(city);
                        t3_summary.setText(summary);

                        double temp_int = Double.parseDouble(temp);
                        double centi = (temp_int - 32) / 1.8000;
                        centi = Math.round(centi);
                        int i = (int)centi;
                        t1_temp.setText(String.valueOf(i));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            );
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jor);
        }

        public void openDirectionsActivity() {
            Intent intent = new Intent(this, DirectionsActivity.class);
            startActivity(intent);
        }

        }







