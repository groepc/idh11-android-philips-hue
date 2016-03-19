package com.perryfaro.hue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // starting elements
    private ImageView overlayImage;
    private ProgressBar progressBar;

    // group elements
    private Switch groupSwitch;
    private SeekBar groupHueBar;
    private SeekBar groupSaturationBar;

    // lamp elements
    private Switch lampSwitch;
    private SeekBar lampHueBar;
    private SeekBar lampSaturationBar;

    private String url = "http://192.168.178.17/api/newdeveloper";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // find starting elements
        overlayImage = (ImageView) findViewById(R.id.overlayImage);
        progressBar = (ProgressBar) findViewById(R.id.loadingSpinner);

        // find group elements
        groupSwitch = (Switch) findViewById(R.id.groupSwitch);
        groupHueBar = (SeekBar) findViewById(R.id.groupHueBar);
        groupSaturationBar = (SeekBar) findViewById(R.id.groupSaturationBar);

        // find lamp elements
        lampSwitch = (Switch) findViewById(R.id.lampSwitch);
        lampHueBar = (SeekBar) findViewById(R.id.lampHueBar);
        lampSaturationBar = (SeekBar) findViewById(R.id.lampSaturationBar);

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

        HueTaskParams initLampParams = new HueTaskParams(url, "/lights/3", "", "GET");
        HueTask initLampView = (HueTask) new HueTask();
        initLampView.setDelegate(new AsyncResponse() {
            @Override
            public void processFinish(JSONObject jsonObject) throws JSONException {
                JSONObject state = jsonObject.getJSONObject("state");



                Boolean lampStatus = state.getBoolean("on");
                Integer lampHue = state.getInt("hue");
                Integer lampSaturation = state.getInt("sat");

                lampSwitch.setChecked(lampStatus);
                lampHueBar.setProgress(lampHue);
                lampSaturationBar.setProgress(lampSaturation);

                hideProgressOverlay();
                setLampEventListeners();
            }
        });
        initLampView.execute(initLampParams);

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

    public void setLampEventListeners() {
        lampSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HueTaskParams params = new HueTaskParams(url, "/lights/3/state", "{\"on\":"+ isChecked +"}", "PUT");
                HueTask hueTask = (HueTask) new HueTask().execute(params);
            }
        });

        lampHueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                HueTaskParams params = new HueTaskParams(url, "/lights/3/state", "{\"hue\":" + progress + "}", "PUT");
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

        lampSaturationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                HueTaskParams params = new HueTaskParams(url, "/lights/3/state", "{\"sat\":" + progress + "}", "PUT");
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
