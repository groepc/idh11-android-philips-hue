package com.perryfaro.hue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Switch groupSwitch;
    String url = "http://192.168.2.7:8080/api/newdeveloper";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        HueTaskParams firstParams = new HueTaskParams(url, "/groups/1", "", "GET");
        HueTask firstHueTask = new HueTask();
        firstHueTask.execute(firstParams);

        System.out.println(firstHueTask);

        groupSwitch = (Switch) findViewById(R.id.groupSwitch);
        groupSwitch.setChecked(true);
        groupSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HueTaskParams params = new HueTaskParams(url, "/groups/1/action", "{\"on\":true}", "PUT");
                    HueTask hueTask = new HueTask();
                    hueTask.execute(params);
                } else {
                    HueTaskParams params = new HueTaskParams(url, "/groups/1/action", "{\"on\":false}", "PUT");
                    HueTask hueTask = new HueTask();
                    hueTask.execute(params);
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
