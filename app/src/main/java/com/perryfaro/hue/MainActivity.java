package com.perryfaro.hue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    // starting elements
    private ImageView overlayImage;
    private ProgressBar progressBar;

    // group elements
    private Switch groupSwitch;
    private SeekBar groupHueBar;
    private SeekBar groupSaturationBar;

    private ListView listViewLight;

    private String url = "http://192.168.1.68:8080/api/newdeveloper";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewLight = (ListView) findViewById(R.id.listView);


        HueTaskParams initLightsParams = new HueTaskParams(url, "/lights", "", "GET");
        HueTask initLightsView = (HueTask) new HueTask();
        initLightsView.setDelegate(new AsyncResponse() {
            @Override
            public void processFinish(JSONObject jsonObject) throws JSONException {
                System.out.println("listview");

                String[] stringArray = new String[jsonObject.length()];
                Iterator<String> iter = jsonObject.keys();
                Integer  counter =0;
                while (iter.hasNext()) {
                    String key = iter.next();
                    stringArray[counter]= key;
                    counter++;
                }

                System.out.println("listview");

                System.out.print(stringArray);

                listViewLight.setAdapter(new LightAdapter(getBaseContext(), stringArray, url));
            }
        });
        initLightsView.execute(initLightsParams);


        // find starting elements
        overlayImage = (ImageView) findViewById(R.id.overlayImage);
        progressBar = (ProgressBar) findViewById(R.id.loadingSpinner);

        // find group elements
        groupSwitch = (Switch) findViewById(R.id.groupSwitch);
        groupHueBar = (SeekBar) findViewById(R.id.groupHueBar);
        groupSaturationBar = (SeekBar) findViewById(R.id.groupSaturationBar);


        // fist initialize group status
        HueTaskParams initGroupParams = new HueTaskParams(url, "/groups/1", "", "GET");
        HueTask initGroupView = (HueTask) new HueTask();
        initGroupView.setDelegate(new AsyncResponse() {
            @Override
            public void processFinish(JSONObject jsonObject) throws JSONException {
                JSONObject action = jsonObject.getJSONObject("action");

                Boolean groupStatus = action.getBoolean("on");
                Integer groupHue = action.getInt("hue");
                Integer groupSaturation = action.getInt("sat");

                groupSwitch.setChecked(groupStatus);
                groupHueBar.setProgress(groupHue);
                groupSaturationBar.setProgress(groupSaturation);

                hideProgressOverlay();
                setGroupEventListeners();
            }
        });
        initGroupView.execute(initGroupParams);

    }

    public void hideProgressOverlay() {
        overlayImage.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    public void setGroupEventListeners() {
        groupSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HueTaskParams params = new HueTaskParams(url, "/groups/1/action", "{\"on\":"+ isChecked +"}", "PUT");
                HueTask hueTask = (HueTask) new HueTask().execute(params);
            }
        });

        groupHueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                HueTaskParams params = new HueTaskParams(url, "/groups/1/action", "{\"hue\":" + progress + "}", "PUT");
                HueTask hueTask = new HueTask();
                hueTask.execute(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        groupSaturationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                HueTaskParams params = new HueTaskParams(url, "/groups/1/action", "{\"sat\":" + progress + "}", "PUT");
                HueTask hueTask = new HueTask();
                hueTask.execute(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
