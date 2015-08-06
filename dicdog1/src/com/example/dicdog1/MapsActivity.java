package com.example.dicdog1;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MapsActivity extends FragmentActivity implements OnMarkerClickListener {
	private static double longitude;
	private static double latitude;
	private float distance;	
	private static CheckBox check;
	private static boolean manual;
	AppLocationService appLocationService;	
	//List<Doctor> doctorList = new ArrayList<Doctor>();
	private static List LatLngList;     
	private static List<String> nameList;
	private static List<String> hospitalList;
	private static List<String> specialityList;
	private static List<String> genderList;
   private GoogleMap googleMap;   
	private static String value;
   
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
     // ActionBar actionBar = getSupportActionBar();
		//actionBar.hide();
      /*requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
      setContentView(R.layout.activity_maps);
      LatLngList =new ArrayList<LatLng>();
      nameList=new ArrayList<String>();
      hospitalList=new ArrayList<String>();
      specialityList=new ArrayList<String>();
      genderList=new ArrayList<String>();
		value="";
		Intent getintent=getIntent();
		String speciality=getintent.getExtras().getString("job");
		//String gender=getintent.getExtras().getString("gender");				
		ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
		//query.whereEqualTo("Gender", gender);
		query.whereEqualTo("Job", speciality);		
		query.findInBackground(new FindCallback<ParseObject>() {
		@Override
		public void done(List<ParseObject> la,
				com.parse.ParseException e) {
			
			// TODO Auto-generated method stub
			 if(la!=null)
			 {					 
				 for(int i=0;i<la.size()+1;i++)
					{
						 if(i==la.size())
						 {
							 if(i==0)
							 {
								 Toast.makeText(getApplicationContext(), "No doctor of these characteristic found ", Toast.LENGTH_LONG).show();
							 }
							 else
							 {							    							
								//Toast.makeText(getApplicationContext(), "hello1 ", Toast.LENGTH_LONG).show();
								break;
							 }
						 }
						 else
						 {
					 //Toast.makeText(getApplicationContext(),la.get(i).getString("Job"), Toast.LENGTH_LONG).show();
						 String s=la.get(i).getString("Job");
						 String s1=la.get(i).getString("Name");
						 String s2=la.get(i).getString("Gender");
						 String hosp=la.get(i).getString("Hospital");
						 ParseGeoPoint userLocation = (ParseGeoPoint) la.get(i).get("Location");						 						
						 LatLng lat=new LatLng(userLocation.getLatitude(),userLocation.getLongitude());
						 //Toast.makeText(getApplicationContext(), "Long:   "+userLocation.getLongitude()+"\nLat:  "+userLocation.getLatitude() ,Toast.LENGTH_LONG).show();
			        	 LatLngList.add(lat);        	 
			        	 nameList.add(s1);
			        	 hospitalList.add(hosp);
			        	 genderList.add(s2);
			        	 specialityList.add(s);
						// check(s1,s,s2);						 						 
						// value+="\nName :  " +s1+"   Job :   "+s+"   Hosp :   "+hosp;
					// Toast.makeText(getApplicationContext(),"Hello " + s, Toast.LENGTH_LONG).show();
			//		     genderList1.add(s);		
						 }
					 };
					 //TextView text=(TextView) findViewById(R.id.textView1);												
					 //text.setText(value+"\n\n End");
					 appLocationService = new AppLocationService(MapsActivity.this);      
				      distance=5;				      
				      try {
				         if (googleMap == null) {
				        	 googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
				         }
				         googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				         googleMap.setOnMarkerClickListener(MapsActivity.this);
				         //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				         boolean verify=true;            
				         Location gpsLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
				         if (gpsLocation != null) 
							{
								latitude = gpsLocation.getLatitude();
								longitude = gpsLocation.getLongitude();
								/*Toast.makeText(getApplicationContext(),
								"Mobile Location (GPS): \nLatitude: " + latitude 
								+ "\nLongitude: " + longitude ,
								Toast.LENGTH_LONG).show();		*/		
							}				
							else
							{
								Location gpsLoc = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
								if (gpsLoc != null) 
								{
									latitude = gpsLoc.getLatitude();
									longitude = gpsLoc.getLongitude();
								    /*Toast.makeText(getApplicationContext(),
									"Mobile Location (GPS): \nLatitude: " + latitude
									+ "\nLongitude: " + longitude,
									Toast.LENGTH_LONG).show();*/	
								}	
								else
								{
									verify=false;			
								}
							}
								if(verify==false)
									showSettingsAlert("GPS");  
								
					 		Toast.makeText(getApplicationContext(),"Click on the Marker for doctorslist",Toast.LENGTH_LONG).show();	
				 		        
				     
				         //Toast.makeText(this, "The doctor nearby is " + doctorList.get(0).getname() +"   ", Toast.LENGTH_LONG).show();
						UiSettings sets=googleMap.getUiSettings();
						sets.setCompassEnabled(true);
						sets.setMyLocationButtonEnabled(true);
						sets.setZoomControlsEnabled(true);
						
						googleMap.setMyLocationEnabled(true);
				         //LatLng myLoc=new LatLng(latitude,longitude);         
				         /*Marker currLoc = googleMap.addMarker(new MarkerOptions().
				    	         position(myLoc).title("My Location").
				    	         icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				    	  */
				    	 
				         CameraUpdate center=
				        	        CameraUpdateFactory.newLatLng(new LatLng(latitude,
				        	                                                 longitude));
				        	    CameraUpdate zoom=CameraUpdateFactory.zoomTo(12);
				        	    googleMap.moveCamera(center);
				        	    googleMap.animateCamera(zoom);                           
				         //LatLng point=(com.google.android.gms.maps.model.LatLng) LatLngList.get(0);
				         //Toast.makeText(getApplicationContext(), "OutLoop \nLong:   "+point.longitude+"\nLat:  "+point.latitude ,Toast.LENGTH_LONG).show();         
				         //Toast.makeText(getApplicationContext(), "Size of array"+LatLngList.size() ,Toast.LENGTH_LONG).show();
				         for(int i=0;i<LatLngList.size();i++)
					      {
				        	 
				        	// Toast.makeText(getApplicationContext(), "Inside loop \nLong:   "+point.longitude+"\nLat:  "+point.latitude ,Toast.LENGTH_LONG).show();         
				        	 BitmapDescriptor bit;        	
				          	 //float color= (jobList.indexOf(doctorList.get(i).getjob())+1)*30;
				          	 //bit=BitmapDescriptorFactory.defaultMarker(color);   
				        	 //String snippet="Phone: "+ doctorList.get(i).getphone()+" \nGender: "+"\n "+        			 
				    		 //doctorList.get(i).getgender();
				        	 LatLng point=(com.google.android.gms.maps.model.LatLng) LatLngList.get(i);
				        	 Marker TP = googleMap.addMarker(new MarkerOptions().
				        	         position(point).title(nameList.get(i)).snippet(hospitalList.get(i)+"/"+specialityList.get(i)+"/"
				        	         +genderList.get(i)) );
				        	        		 //.icon(BitmapDescriptorFactory.fromResource(R.drawable.dentist)  ));        	 
					      }	        		                      
				         //Marker TP2 = googleMap.addMarker(new MarkerOptions().
				         //position(TutorialsPoint2).title("Ali"));
				      }
				      catch (Exception exc) {
				         exc.printStackTrace();
				      }
					//Toast.makeText(getApplicationContext(),"Hello " + genderList1.get(0), Toast.LENGTH_LONG).show();									
             }
            else{//handle the error}
          	  Toast.makeText(getApplicationContext(), "No doctors of these characteristics found", Toast.LENGTH_LONG).show();
                      }
			 
			
		}
	});
				            
   }
   public void showSettingsAlert(String provider) {
 		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
 		alertDialog.setTitle(provider + " SETTINGS");
 		alertDialog.setMessage(provider + " is not enabled! Want to go to settings menu?");
 		alertDialog.setPositiveButton("Settings",
 		new DialogInterface.OnClickListener() {
 			public void onClick(DialogInterface dialog, int which) {
 				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
 				MapsActivity.this.startActivity(intent);
 			}});

 		alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
 			public void onClick(DialogInterface dialog, int which) {
 				dialog.cancel();
 			}});
 		alertDialog.show();
 	}
	@Override
	public boolean onMarkerClick(Marker arg0) {
		String info=arg0.getSnippet();
		//Toast.makeText(getApplicationContext(), "Start : "+info+"\nsnip:  "+arg0.getSnippet(), Toast.LENGTH_LONG).show();
		int index1=info.indexOf('/');	
		String hospit=info.substring(0,index1);		
		info=info.substring(index1+1);
		index1=info.indexOf('/');
		String spec=info.substring(0,index1);
		//Toast.makeText(getApplicationContext(), "2nd : "+info, Toast.LENGTH_LONG).show();
		info=info.substring(index1+1);		
		String gend=info;
		//Toast.makeText(getApplicationContext(), "3rd : "+info, Toast.LENGTH_LONG).show();
		//Toast.makeText(getApplicationContext(), hospit + "\n"+spec+"\n"+gend, Toast.LENGTH_LONG).show();
		int count=0;
		for(int i=0;i<hospitalList.size();i++)
		{
			if(hospitalList.get(i).equals(hospit))
			{
				count++;
			}
		}
		//Toast.makeText(getApplicationContext(), "Count:   "+count, Toast.LENGTH_LONG).show();
		if(count==1)
		{
			// TODO Auto-generated method stub
			Intent i=new Intent(MapsActivity.this, SelectedDoctor.class);
			i.putExtra("doctor", arg0.getTitle());
			Toast.makeText(getApplicationContext(), "Hospital: "+ hospit, Toast.LENGTH_LONG).show();
			startActivity(i);		
		}
		else
		{
			Intent intent=new Intent(MapsActivity.this,DoctorsList.class);
			intent.putExtra("speciality", spec);
	      	intent.putExtra("gender", gend);
	      	intent.putExtra("hospital", hospit);
	      	intent.putExtra("name","None");
	      	Toast.makeText(getApplicationContext(), "Hospital: "+ hospit, Toast.LENGTH_LONG).show();
	      	startActivity(intent);			
		}			
		return true;
	}      
	
}
