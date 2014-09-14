package com.testdroid.sample.android;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.testdroid.sample.android.adapters.DevicePropertyExpandableArrayAdapter;
import com.testdroid.sample.android.models.DeviceProperty;
import com.testdroid.sample.android.models.DevicePropertyGroup;

import java.util.ArrayList;

public class DI_DeviceInfo extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = DI_DeviceInfo.class.getName().toString();

    // UI Widgets
    private static ListView lv_deviceInfo;
    private static ExpandableListView elv_deviceInfo;

    private static ArrayList<DevicePropertyGroup> propertyGroupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.di_layout);

        propertyGroupList = new ArrayList<DevicePropertyGroup>();

        elv_deviceInfo = (ExpandableListView) findViewById(R.id.di_elv_deviceInfo);

        collectPropertiesBuild();
        collectPropertiesBattery();
        collectPropertiesHardware();
        collectPropertiesSensors();
        collectPropertiesMeasurements();

        DevicePropertyExpandableArrayAdapter expandableArrayAdapter = new DevicePropertyExpandableArrayAdapter(getApplicationContext(), propertyGroupList);
        elv_deviceInfo.setAdapter(expandableArrayAdapter);

        for (int i = 0; i < propertyGroupList.size(); i++) {
            elv_deviceInfo.expandGroup(i, false);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
        }
    }

    private void collectPropertiesMeasurements() {

        ArrayList<DeviceProperty> propertyGroup = new ArrayList<DeviceProperty>();

        propertyGroup.add(new DeviceProperty("Radio", "TODO"));

        propertyGroupList.add(new DevicePropertyGroup("Measurements", propertyGroup));
    }

    private void collectPropertiesBuild() {

        ArrayList<DeviceProperty> propertyGroup = new ArrayList<DeviceProperty>();

        propertyGroup.add(new DeviceProperty("Manufacturer", Build.MANUFACTURER));
        propertyGroup.add(new DeviceProperty("Model", Build.MODEL));
        propertyGroup.add(new DeviceProperty("Device", Build.DEVICE));
        propertyGroup.add(new DeviceProperty("Product", Build.PRODUCT));
        propertyGroup.add(new DeviceProperty("SDK", "" + Build.VERSION.SDK_INT));
        propertyGroup.add(new DeviceProperty("Release", Build.VERSION.RELEASE));
        propertyGroup.add(new DeviceProperty("Serial", Build.SERIAL));
        propertyGroup.add(new DeviceProperty("IMEI", ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId()));

        propertyGroupList.add(new DevicePropertyGroup("Build", propertyGroup));

    }

    private void collectPropertiesHardware() {

        ArrayList<DeviceProperty> propertyGroup = new ArrayList<DeviceProperty>();

        // GPS
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        propertyGroup.add(new DeviceProperty("GPS", "" + isGps));

        propertyGroup.add(new DeviceProperty("WiFi", "TODO"));
        propertyGroup.add(new DeviceProperty("Camera", "TODO"));
        propertyGroup.add(new DeviceProperty("Front camera", "TODO"));
        propertyGroup.add(new DeviceProperty("SDCard", "TODO"));

        propertyGroupList.add(new DevicePropertyGroup("Hardware", propertyGroup));

    }

    private void collectPropertiesSensors() {

        ArrayList<DeviceProperty> propertyGroup = new ArrayList<DeviceProperty>();

        propertyGroup.add(new DeviceProperty("SensorA", "TODO"));
        propertyGroup.add(new DeviceProperty("SensorB", "TODO"));
        propertyGroup.add(new DeviceProperty("SensorC", "TODO"));
        propertyGroup.add(new DeviceProperty("SensorD", "TODO"));
        propertyGroup.add(new DeviceProperty("SensorE", "TODO"));

        propertyGroupList.add(new DevicePropertyGroup("Sensors", propertyGroup));

    }

    private void collectPropertiesBattery() {

        ArrayList<DeviceProperty> propertyGroup = new ArrayList<DeviceProperty>();

        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int health = batteryIntent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int plugged = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        int temperature = batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        int voltage = batteryIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        String technology = batteryIntent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);

        // level
        int levelFloat;
        if (level == -1 || scale == -1) {
            levelFloat = 50;
        } else {
            levelFloat = (int) (((float) level / (float) scale) * 100.0f);
        }
        propertyGroup.add(new DeviceProperty("Level", "" + levelFloat));

        // status
        String statusString = null;
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                statusString = "Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                statusString = "Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                statusString = "Full";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                statusString = "Not charging";
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                statusString = "Unknown";
                break;
            default:
                statusString = "Unknown";
                break;
        }
        propertyGroup.add(new DeviceProperty("Status", statusString));

        // plugged
        String pluggedString = null;
        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                pluggedString = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                pluggedString = "USB";
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                pluggedString = "Wireless";
                break;
            default:
                pluggedString = "Unplugged";
                break;
        }
        propertyGroup.add(new DeviceProperty("Plugged", pluggedString));

        // voltage
        propertyGroup.add(new DeviceProperty("Voltage", "" + (float) voltage / 1000.0));

        // temperature
        propertyGroup.add(new DeviceProperty("Temperature", "" + (float) temperature / 10.0));

        // health
        String healthString = null;
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_COLD:
                healthString = "Cold";
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthString = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthString = "Good";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthString = "Over voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthString = "Overheat";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                healthString = "Unspecified failure";
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                healthString = "Unknown";
                break;
            default:
                healthString = "Unknown";
                break;
        }
        propertyGroup.add(new DeviceProperty("Health", healthString));

        // technology
        propertyGroup.add(new DeviceProperty("Technology", technology));

        propertyGroupList.add(new DevicePropertyGroup("Battery", propertyGroup));

    }

    private void printPropertyList(ArrayList<DevicePropertyGroup> propertyGroupList) {

        for (DevicePropertyGroup propertyGroup : propertyGroupList) {
            String groupName = propertyGroup.getName();
            for (DeviceProperty deviceProperty : propertyGroup.getPropertyList()) {
                Log.d(TAG, String.format("Group: %s | Property: %s, Value: %s", groupName, deviceProperty.getName(), deviceProperty.getValue()));
            }
        }

    }

}
