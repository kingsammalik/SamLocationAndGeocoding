# SamLocationAndGeocoding
[![](https://jitpack.io/v/kingsammalik/SamLocationAndGeocoding.svg)](https://jitpack.io/#kingsammalik/SamLocationAndGeocoding)


This is a simplified library to track the user location and address corresponding to it.

Add it in your root build.gradle at the end of repositories:

    allprojects {
		  repositories {
			  ...
			  maven { url "https://jitpack.io" }
	  	}
	  }
	
	 Add the dependency
	 
	 compile 'com.github.kingsammalik:SamLocationAndGeocoding:1.1.0'
	 
	 
	 Then use it like this. 
	 
	  
            new SamLocationRequestService(MainActivity.this).executeService(new SamLocationRequestService.SamLocationListener() {
                        @Override
                        public void onLocationUpdate(Location location, Address address) {
                            Toast.makeText(MainActivity.this,"mapped",Toast.LENGTH_SHORT).show();
                        }
                    });
