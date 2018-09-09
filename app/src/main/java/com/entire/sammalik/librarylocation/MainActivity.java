package com.entire.sammalik.librarylocation;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.entire.sammalik.samlocationandgeocoding.SamLocationRequestService;

public class MainActivity extends AppCompatActivity  {

    private SamLocationRequestService samLocationRequestService;
    private int REQUEST_CODE=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.check)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    samLocationRequestService = new SamLocationRequestService(MainActivity.this, new SamLocationRequestService.SamLocationListener() {
                        @Override
                        public void onLocationUpdate(Location location, Address address) {
                            Toast.makeText(MainActivity.this,"mapped",Toast.LENGTH_SHORT).show();
                        }
                    },REQUEST_CODE);

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            samLocationRequestService.startLocationUpdates();
        }
    }



}
