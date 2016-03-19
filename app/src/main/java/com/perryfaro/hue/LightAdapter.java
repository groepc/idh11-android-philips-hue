package com.perryfaro.hue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by perryfaro on 19-03-16.
 */
public class LightAdapter extends BaseAdapter {

    // lamp elements
    private Switch lampSwitch;
    private SeekBar lampHueBar;
    private SeekBar lampSaturationBar;
    Context context;
    String[] data;
    private String url;
    private static LayoutInflater inflater = null;

    public LightAdapter(Context context, String[] data, String url) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        this.url = url;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_light_view, null);

        lampSwitch = (Switch) vi.findViewById(R.id.lampSwitch);
        lampHueBar = (SeekBar) vi.findViewById(R.id.lampHueBar);
        lampSaturationBar = (SeekBar) vi.findViewById(R.id.lampSaturationBar);

        HueTaskParams initLampParams = new HueTaskParams(url, "/lights/" + data[position], "", "GET");
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
            }
        });
        initLampView.execute(initLampParams);
        TextView text = (TextView) vi.findViewById(R.id.lampText);
        text.setText("Lamp " + data[position]);

        setLampEventListeners(data[position]);
        return vi;
    }

    public void setLampEventListeners(final String id) {
        lampSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HueTaskParams params = new HueTaskParams(url, "/lights/" + id +"/state", "{\"on\":" + isChecked + "}", "PUT");
                HueTask hueTask = (HueTask) new HueTask().execute(params);
            }
        });

        lampHueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                HueTaskParams params = new HueTaskParams(url, "/lights/" + id +"/state", "{\"hue\":" + progress + "}", "PUT");
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
                HueTaskParams params = new HueTaskParams(url, "/lights/" + id + "/state", "{\"sat\":" + progress + "}", "PUT");
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



}
