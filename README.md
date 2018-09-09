# SamLocationAndGeocoding
[![](https://jitpack.io/v/kingsammalik/SamLocationAndGeocoding.svg)](https://jitpack.io/#kingsammalik/SamLocationAndGeocoding)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-SamLocationAndGeocoding-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4381)


This is a simplified library to track the user location and address corresponding to it.

Add it in your root build.gradle at the end of repositories:

    allprojects {
		  repositories {
			  ...
			  maven { url "https://jitpack.io" }
	  	}
	  }
	
 Add the dependency
	 
	 		dependencies {
	        implementation 'com.github.kingsammalik:SamLocationAndGeocoding:2.0.0'
	}


	 

Then use it like this. 
	 
	  SamLocationRequestService samLocationRequestService;
	  
	  
            samLocationRequestService = new SamLocationRequestService(MainActivity.this, new SamLocationRequestService.SamLocationListener() {
                        @Override
                        public void onLocationUpdate(Location location, Address address) {
                            Toast.makeText(MainActivity.this,"mapped",Toast.LENGTH_SHORT).show();
                        }
                    },1000);
		    
or if you would like to enter your custom intervals then use this 

	samLocationRequestService = new SamLocationRequestService(MainActivity.this,1000,2000, new SamLocationRequestService.SamLocationListener() {
                        @Override
                        public void onLocationUpdate(Location location, Address address) {
                            Toast.makeText(MainActivity.this,"mapped",Toast.LENGTH_SHORT).show();
                        }
                    },1000);
		    
1000 is to specify the intervals and 2000 is for fastest intervals.



        @Override
    	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        	super.onActivityResult(requestCode, resultCode, data);
        	if (requestCode == 1000){
            	samLocationRequestService.startLocationUpdates();
        	}
    	}
	
Please do take special attention towards using the same request code in the OnActivityResult which you supplied in the SamLocationRequest constructor.
