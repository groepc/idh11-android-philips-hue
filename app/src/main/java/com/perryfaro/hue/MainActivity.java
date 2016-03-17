package com.perryfaro.hue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // elements
    private Switch groupSwitch;
    private ProgressBar progressBar;

    private String url = "http://145.48.113.85/api/newdeveloper";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        groupSwitch = (Switch) findViewById(R.id.groupSwitch);
        progressBar = (ProgressBar) findViewById(R.id.loadingSpinner);
        progressBar.setIndeterminate(true);

        HueTaskParams firstParams = new HueTaskParams(url, "/groups/1", "", "GET");
        HueTask initView = (HueTask) new HueTask(new AsyncResponse() {
            @Override
            public void processFinish(JSONObject jsonObject) throws JSONException {
                JSONObject action = jsonObject.getJSONObject("action");
                Boolean status = action.getBoolean("on");

                System.out.println("STATUS: " + status);
                System.out.println("STATUS: " + action);

                groupSwitch.setChecked(status);
                groupSwitch.setVisibility(View.VISIBLE);

                progressBar.setVisibility(View.INVISIBLE);
            }
        }).execute(firstParams);




        groupSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(groupSwitch.isShown()) {
                    HueTaskParams params = new HueTaskParams(url, "/groups/1/action", "{\"on\":"+ isChecked +"}", "PUT");
                    HueTask hueTask = (HueTask) new HueTask(new AsyncResponse() {
                        @Override
                        public void processFinish(JSONObject jsonObject) throws JSONException {
                            System.out.println("STATUS IN CHANGE:" + jsonObject);
                        }
                    }).execute(params);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
