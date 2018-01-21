package com.vav.cn.util;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.vav.cn.R;
import com.vav.cn.config.Config;
import com.vav.cn.listener.DialogFragmentSingleButtonListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Handrata Samsul on 1/25/2016.
 */
public class LocationHelper {
    public static final double DEFAULT_LAT = 3.103;
    public static final double DEFAULT_LONG = 101.60;
    private static LocationHelper instance = new LocationHelper();
    private final String CLASS_TAG = LocationHelper.this.getClass().getSimpleName();
    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerNetwork);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerGps);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    private Context context;
    private boolean isFirstRequest = true;

    public static LocationHelper getInstance() {
        return instance;
    }

    public boolean getLocation(Context context, LocationResult result) {
        this.context = context;
        locationResult = result;
        if (context == null) {
            locationResult.onFailCallback("Context null");
            return false;
        }
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        handleLocationPermission();
        //don't start listeners if no provider is enabled
//		if(!gps_enabled && !network_enabled)
//			return false;

        return true;
    }

    private void processLocationAfterGranted() {
        if (gps_enabled)
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
        if (network_enabled)
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        timer1 = new Timer();
        timer1.schedule(new GetLastLocation(), 1000);
    }

    private void handleLocationPermission() {
        if (isFirstRequest && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            isFirstRequest = false;
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                publishDefaultLocation();
            } else
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        Config.REQUEST_PERMISSION_LOCATION);
        } else if (!isFirstRequest && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            publishDefaultLocation();
        } else {
            processLocationAfterGranted();
        }
    }

    //call this onPause / onDestroy
    public void cancelTimer() {
        if (timer1 != null) {
            timer1.cancel();
        }
        if (lm != null) {
            lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerNetwork);
        }
    }

    private void publishDefaultLocation() {
        Location location = new Location("");
        location.setLatitude(DEFAULT_LAT);
        location.setLongitude(DEFAULT_LONG);
        locationResult.gotLocation(location);
    }

    public void requestUserPermissionTurnGPS(final Activity activity) {
        DialogHelper.getInstance().showDialogBasicCustomFragment(
                activity,
                "",
                activity.getString(R.string.location_gps_turn_on_title),
                activity.getString(R.string.dialog_yes_btn),
                false,
                new DialogFragmentSingleButtonListener() {
                    @Override
                    public void onBtnClick(DialogFragment dialogFragment) {
                        dialogFragment.dismiss();
                        BackgroundIndicatorUtils.getInstance().setIsFromBackground(false);
                        publishDefaultLocation();
                    }
                });
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);

        public abstract void onFailCallback(String message);

        public abstract void onRequestTurnGPS();
    }

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerNetwork);

            Location net_loc = null, gps_loc = null;
            if (gps_enabled)
                gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (network_enabled)
                net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            //if there are both values use the latest one
            if (gps_loc != null && net_loc != null) {
                if (gps_loc.getTime() > net_loc.getTime())
                    locationResult.gotLocation(gps_loc);
                else
                    locationResult.gotLocation(net_loc);
                return;
            }

            if (gps_loc != null) {
                locationResult.gotLocation(gps_loc);
                return;
            }
            if (net_loc != null) {
                locationResult.gotLocation(net_loc);
                return;
            }
            if (!BackgroundIndicatorUtils.getInstance().isFromBackground()) {
                publishDefaultLocation();
            } else {
                locationResult.onRequestTurnGPS();
            }
        }
    }
}
