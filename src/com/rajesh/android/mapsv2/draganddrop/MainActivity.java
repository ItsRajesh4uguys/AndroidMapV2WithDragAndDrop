package com.rajesh.android.mapsv2.draganddrop;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements  OnInfoWindowClickListener, OnMarkerDragListener {
 
  private GoogleMap map=null;
  TextView rescueText;
	Button button1;
	private String StateName;
	private String CityName;
	private String CountryName;
	private boolean gps_enabled;
	private boolean network_enabled;
	private Location location;
	ImageView ImageViewMapCurrentLocation;
	private ProgressDialog progDailog;
	 String foundAddress = "";
  @SuppressWarnings("unused")
  
  
  public class GetAddress extends AsyncTask<Void, Void, Void> {
		double latitude;
		double longitude;
		
		public GetAddress(double latitude,double longitude)
		{
			this.latitude=latitude;
			this.longitude=longitude;
			
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			//progDailog.dismiss();
			if (progDailog!=null) {
				if (progDailog.isShowing()) {
					progDailog.dismiss();
				}
			}
			rescueText.setText(foundAddress);
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			FormattedAddress tel=new FormattedAddress();
			foundAddress= tel.getAddress(String.valueOf(latitude), String.valueOf(longitude));
			//read("http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&sensor=false");
			
			return null;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			rescueText.setText("");
			foundAddress="";
		}
	}
	
  
@Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
   
    {
      setContentView(R.layout.activity_main);
      
      rescueText = (TextView)findViewById(R.id.rescueText);  
      rescueText.setSelected(true);
      rescueText.setEllipsize(TruncateAt.MARQUEE);
      rescueText.setSingleLine(true);
      button1=(Button)findViewById(R.id.button1);
      ImageViewMapCurrentLocation=(ImageView)findViewById(R.id.ImageViewMapCurrentLocation);
      
      ImageViewMapCurrentLocation.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			GetCurrentLocation();
			
		}
	});
      
    SupportMapFragment mapFrag=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
      
      mapFrag.setRetainInstance(true);   
      

      map=mapFrag.getMap();

      if (savedInstanceState == null) {     
    	  double templat=0;
    	  double templong=0;
    	  
    try
    {
    	

    	    SharedPreferences loggedstate = MainActivity.this.getSharedPreferences("GoogleMapLastLocation", 0);	      
    	    String lastlatti=  loggedstate.getString("LastLattitue", "");   
    		String lastlong=   loggedstate.getString("LastLongitude", "");   
	       
    	    templat=Double.valueOf(lastlatti);
    	    templong=Double.valueOf(lastlong);
	// System.out.println(" LLLLLLLAAAAAAAA "+ templat ); 
	// System.out.println(" LLLLLLLLLLLLOOOOOOOOO "+ templong);
    }
    catch(NumberFormatException e)
    {
    	e.printStackTrace();
    }
if(((templat==0)==false ) && (templong==0)==false )
{
	System.out.println(" LAST KNown Location ");
	 LatLng latLng = new LatLng(templat, templong);
     CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 5);
     map.animateCamera(cameraUpdate);     
   
     
     addMarker(map, templat, templong,1, 1); 
     
    
     
    progDailog = ProgressDialog.show(MainActivity.this,
			 "Fetching Address",
			 "Please wait...", true);
			 new GetAddress(templat,templong).execute();
     
    
}
else
{
	GetCurrentLocation();
}
      map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
     // map.setOnInfoWindowClickListener(this);
      map.setOnMarkerDragListener(this);
    }
    
    
        
        button1.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			    Intent returnIntent = new Intent();
    			    if(CountryName==(null))
    			    {
    			    	CountryName="";
    			    }
    			    if(CityName==(null))
    			    {
    			    	CityName="";
    			    }
    			    if(StateName==(null))
    			    {
    			    	StateName="";
    			    }
    		        /*returnIntent.putExtra("CountryName",CountryName.trim());
    		        returnIntent.putExtra("CityName",CityName.trim());
    		        returnIntent.putExtra("StateName",StateName.trim());*/
    			    
    			    returnIntent.putExtra("foundAddress",foundAddress.trim());    		       		        
    		        SharedPreferences loggedstate = MainActivity.this.getSharedPreferences("GoogleMapLastLocation", 0);
    		        SharedPreferences.Editor loggedEditor = loggedstate.edit();    		        
    		        loggedEditor.putString("LastLattitue", String.valueOf(Mylattitude));   
    		        loggedEditor.putString("LastLongitude", String.valueOf(MyLongitude));    
    		        loggedEditor.commit();
    		        
    		        
    		      setResult(RESULT_OK,returnIntent);       
    		      finish();
    		}
    	});
        
        
  }

  }

 
  @Override
  public void onInfoWindowClick(Marker marker) {
    Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
  }

  @Override
  public void onMarkerDragStart(Marker marker) {
    //LatLng position=marker.getPosition();

   // Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f",
                                                //    position.latitude,
                                                 //   position.longitude));
  }

  @Override
  public void onMarkerDrag(Marker marker) {
    //LatLng position=marker.getPosition();

   // Log.d(getClass().getSimpleName(),
        //  String.format("Dragging to %f:%f", position.latitude,
                        //position.longitude));
  }

  @Override
  public void onMarkerDragEnd(Marker marker) {
    LatLng position=marker.getPosition();

    //Log.d(getClass().getSimpleName(), String.format("Dragged to %f:%f",
                                                 //   position.latitude,
                                                 //   position.longitude));
    
  /*  CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 5);
    map.animateCamera(cameraUpdate);     
    map.setMyLocationEnabled(true);*/
    
    
     Mylattitude= position.latitude;
     MyLongitude=position.longitude;
  //   System.out.println(" MYYYYYYYYYLAAAAAAAA "+  position.latitude);
     //System.out.println(" MYYYYYYYYYLOOOOOOOO "+  position.longitude);
                    
     progDailog = ProgressDialog.show(MainActivity.this,
			 "Fetching Address",
		 "Please wait...", true);
			 new GetAddress(position.latitude,position.longitude).execute();
     addMarker(map, position.latitude, position.longitude,1, 1); 
   
  }

  


  private void addMarker(GoogleMap map, double lat, double lon,
                         int title, int snippet) {
	  
	 
	  System.out.println(" MAAAAAAAAAA LAT " + lat);
	  System.out.println(" MAAAAAAAAAA Long " + lon);
	  map.clear();
	 
	  
	//  Bitmap.Config conf = Bitmap.Config.ARGB_8888; 
	//  Bitmap bmp = Bitmap.createBitmap(200, 50, conf); 
	//  Canvas canvas = new Canvas(bmp);
     
    //  Paint color = new Paint();
    //  color.setTextSize(14);
    //  color.setColor(Color.BLACK);
	//  canvas.drawText("Drag to Move Pin", 0, 50, color); // paint defines the text color, stroke width, size
	//color.setTextAlign(Align.CENTER);
	  Marker marker =  map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                                      .title("Drag to Move Pin")                                      
                                     .snippet(""+round(lat,3)+","+round(lon,3))	
                                     .anchor(0.5f, 1) 
                                     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                     .draggable(true));
	  
	  map.setInfoWindowAdapter(new InfoWindowAdapter() {
		  
	         @Override
	         public View getInfoWindow(Marker arg0) {

	             return null;
	         }

	         @Override
	         public View getInfoContents(Marker marker) {
	             View myContentsView = getLayoutInflater().inflate(R.layout.popup, null);
	              TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
	                 tvTitle.setText(marker.getTitle());
	                 TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
	                 tvSnippet.setText(marker.getSnippet());
	             return myContentsView;
	         }
	     });
	  
	  marker.showInfoWindow();
	  
	 
	  
     
  }
  
  public class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location location) {
			if (location != null) {
				//double lat = location.getLatitude();
				//double lon = location.getLongitude();
			}
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}
  double Mylattitude;
  double MyLongitude;
		  
		 
		  
		  void GetCurrentLocation( )
		  {

		        try
				{
				LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				LocationListener locListener = new MyLocationListener();
				
				
				 try{gps_enabled=locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
				 
			       try{network_enabled=locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}

			    
			        if(gps_enabled){
			        	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
			    
			        }
			        
			        
			        if(gps_enabled){
			        	location=locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);	    
			        }
			        
			        //else 
			        if(network_enabled && location==null){
			        	locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
			        }
				
			        if(network_enabled && location==null)	{
		            	location=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);  
		            
			        }
				
			    if(location==null)
			    {
			    	ShowNoLocationAlert(MainActivity.this);
			    }
			    else
			    {
				if (location != null) {		
				
				locManager.removeUpdates(locListener);
		        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 5);
		        map.animateCamera(cameraUpdate);     
				}
		        addMarker(map, location.getLatitude(), location.getLongitude(),1, 1); 
		        progDailog = ProgressDialog.show(MainActivity.this,
		   			 "Fetching Address",
		   		 "Please wait...", true);
		   			 new GetAddress(location.getLatitude(),location.getLongitude()).execute();
			    }
		      }
		        catch(Exception e)
		        {
		        	e.printStackTrace();
		        }

		  }
		 
		  
		  public static double round(double value, int places) {
			    if (places < 0) throw new IllegalArgumentException();

			    long factor = (long) Math.pow(10, places);
			    value = value * factor;
			    long tmp = Math.round(value);
			    return (double) tmp / factor;
			}
		  
		  public static void ShowNoLocationAlert(final Context con)
			{ 
				 new AlertDialog.Builder(con)
					.setMessage(
							"Please enable the location service and try again.")
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									/*startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);*/
									
									Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									con.startActivity(intent);
								}
							})
							.setCancelable(false)
							.show();
			}
			
			
}
