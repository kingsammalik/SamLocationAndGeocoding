package com.entire.sammalik.samlocationandgeocoding;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class SamLocationRequestService extends LocationCallback  {

    private LocationRequest mLocationRequest;
    private Context context;
    private static long INTERVAL = 1000;
    private static long FASTEST_INTERVAL = 1000;
    private int REQUEST_CODE;

    private Address geolocation;

    private SamLocationListener samLocationListener;


    public SamLocationRequestService(final Context context,final SamLocationListener samLocationListener,int REQUEST_CODE) {
        this.context = context;
        this.samLocationListener = samLocationListener;
        this.REQUEST_CODE = REQUEST_CODE;
        askForPermission();

    }

    public SamLocationRequestService(Context context, long interval, long fastest_interval,final SamLocationListener samLocationListener,int REQUEST_CODE) {
        this.context = context;
        INTERVAL=interval;
        FASTEST_INTERVAL=fastest_interval;
        this.samLocationListener = samLocationListener;
        this.REQUEST_CODE = REQUEST_CODE;

        askForPermission();

    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        ((Activity) context).startActivityForResult(intent, 101);
    }

    private void askForPermission(){
        Dexter.withActivity((Activity) context)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        setGoogleClient();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            // navigate user to app settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    
    


    public interface SamLocationListener {

        public void onLocationUpdate(Location location, Address address);

    }



    private void setGoogleClient(){

            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(30 * 1000);
            mLocationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);

            //**************************
            builder.setAlwaysShow(true); //this is the key ingredient
            //**************************

            Task<LocationSettingsResponse> result =
                    LocationServices.getSettingsClient(context).checkLocationSettings( builder.build());

            result.addOnFailureListener((Activity) context, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    int statusCode = ((ApiException) e).getStatusCode();
                    if (statusCode
                            == LocationSettingsStatusCodes
                            .RESOLUTION_REQUIRED) {
                        // Location settings are not satisfied, but this can
                        // be fixed by showing the user a dialog
                        try {
                            // Show the dialog by calling
                            // startResolutionForResult(), and check the
                            // result in onActivityResult()
                            ResolvableApiException resolvable =
                                    (ResolvableApiException) e;
                            resolvable.startResolutionForResult
                                    ((Activity) context,
                                            REQUEST_CODE);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error
                        }
                    }
                }
            });

            result.addOnSuccessListener((Activity) context, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    startLocationUpdates();
                }
            });


    }









    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(
                 mLocationRequest,this, null);
        Log.d("LocationRequestService", "Location update started ..............: ");
    }



    private void stopLocationUpdates() {
        LocationServices.getFusedLocationProviderClient(context).removeLocationUpdates(
                 this);
        Log.d("LocationRequestService", "Location update stopped .......................");
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);
        int temp = locationResult.getLocations().size();
        --temp;
        Log.e("Lat", " " + String.valueOf(locationResult.getLocations().get(temp).getLatitude()));
        Log.e("Long", " " + String.valueOf(locationResult.getLocations().get(temp).getLongitude()));
            stopLocationUpdates();
            LocationAddress locationAddress = new LocationAddress();

            geolocation = locationAddress.getAddressFromLocation(locationResult.getLocations().get(temp).getLatitude(),locationResult.getLocations().get(temp).getLongitude(),
                    context);
            Log.e("geolocation", " " + geolocation);


        samLocationListener.onLocationUpdate(locationResult.getLocations().get(temp),geolocation);

    }

    @Override
    public void onLocationAvailability(LocationAvailability locationAvailability) {
        super.onLocationAvailability(locationAvailability);
        Log.e("tag","isavail "+locationAvailability.isLocationAvailable());
    }
}
