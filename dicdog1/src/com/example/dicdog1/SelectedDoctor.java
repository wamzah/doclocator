package com.example.dicdog1;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SelectedDoctor extends ActionBarActivity {

	private static ImageButton icon;
	private static Button call;
	//private static Button Schedule;
	private static TextView main;
	private static TextView name;
	private static TextView spec;
	private static TextView gender;
	private static TextView hospital;
	private static TextView timings;
	private static TextView experience;
	private static EditText edit1;
	private static Button button1;
	private static ImageView callimage;
	private static Doctor doc;
	private static String schedule;
	private static String mainSchedule;
	private static Button buttonMaps;
	private static Button close;
	private static String nameOfDoctor;
	public static Button buttonnext;
	private static double longitude;
	private static double latitude;
	private float distance;	
	private static String selectedValue;
	private static CheckBox check;
	private static boolean manual;
	AppLocationService appLocationService;	
	//List<Doctor> doctorList = new ArrayList<Doctor>();
	private static List LatLngList;     
	private static List<String> nameList;
	private static List<String> hospitalList;	
   private static GoogleMap googleMap; 
   private static SupportMapFragment mMapFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar=getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_selected_doctor);	
		
	
		mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapFrag));
		LatLngList =new ArrayList<LatLng>();
		nameList=new ArrayList<String>();
	    hospitalList=new ArrayList<String>();
		nameOfDoctor="";		
		callimage=(ImageView)findViewById(R.id.imageView2);
		callimage.setClickable(true);
		callimage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String number="tel:";
				if(hospital.getText().equals("MAROOF International Hospital"))
				{
					number += "051 111 644 911";				
				}
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
			    startActivity(intent);
			}
		});
		
		name=(TextView)findViewById(R.id.textView1);
		spec=(TextView)findViewById(R.id.textView3);		
		gender=(TextView)findViewById(R.id.TextView02);
		hospital=(TextView)findViewById(R.id.textView8);		
		experience=(TextView)findViewById(R.id.textView9);
		icon = (ImageButton)findViewById(R.id.ImageButton);
		icon.setClickable(false);		
		
		button1=(Button)findViewById(R.id.homebitton);
		button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(SelectedDoctor.this,DashboardActivity.class);
				startActivity(i);
			}
		});
		Intent intent=getIntent();
	    selectedValue=intent.getExtras().getString("doctor");
		nameOfDoctor=selectedValue;
		//Toast.makeText(getApplicationContext(), "Intent Name :  "+selectedValue, Toast.LENGTH_LONG).show();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");		
		query.whereEqualTo("Name", selectedValue);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> la,
					com.parse.ParseException e) {
				// TODO Auto-generated method stub
				 if(la!=null){
					 
					 nameOfDoctor=la.get(0).getString("Name");
					 name.setText("Dr. " + nameOfDoctor);
						spec.setText(la.get(0).getString("Job"));
						gender.setText(la.get(0).getString("Gender"));
						hospital.setText(la.get(0).getString("Hospital"));
						mainSchedule=la.get(0).getString("Scehdule");
						
						experience.setMovementMethod(new ScrollingMovementMethod());
						experience.setText(la.get(0).getString("Qualifications"));
						ParseFile fileObject = (ParseFile) la.get(0).get("Images");
						fileObject.getDataInBackground(new GetDataCallback() {									
									@Override
									public void done(byte[] arg0,
											ParseException arg1) {
										// TODO Auto-generated method stub
										if (arg1 == null) {
											
											Bitmap bmp = BitmapFactory.decodeByteArray(arg0, 0,arg0.length);
                                            BitmapDrawable b=new BitmapDrawable(getResources(),bmp);
											icon.setBackgroundDrawable(b);
    										
											
										}
										
									}
						
					
                         });						
						TextView time1=(TextView) findViewById(R.id.time1); 
						TextView time2=(TextView) findViewById(R.id.time2);
						TextView time3=(TextView) findViewById(R.id.time3);
						TextView time4=(TextView) findViewById(R.id.time4);
						TextView time5=(TextView) findViewById(R.id.time5);
						TextView time6=(TextView) findViewById(R.id.time6);
						TextView time7=(TextView) findViewById(R.id.time7);
						
						//Setting up schedule table
						String sched="";
						schedule=mainSchedule;				
					
						while(true)
						{				
							if(schedule==null || schedule.equals("end"))
								break;									
							if(schedule.contains("/"))
							{
								int index=schedule.indexOf("/");
								String temp="",end="";
								temp+=schedule.substring(0, 3);
								end+=schedule.substring(4, index-1);
								if(temp.equals("Mon"))
								{
									time1.setText(end);
								}
								else if(temp.equals("Tue"))
								{
									time2.setText(end);
								}
								else if(temp.equals("Wed"))
								{
									time3.setText(end);
								}
								else if(temp.equals("Thu"))
								{
									time4.setText(end);
								}
								else if(temp.equals("Fri"))
								{
									time5.setText(end);
								}
								else if(temp.equals("Sat"))
								{
									time6.setText(end);
								}
								else if(temp.equals("Sun"))
								{
									time7.setText(end);
								}
								sched+=temp;
								sched+="            "+end;
								
								if((schedule=schedule.substring(index+1)).equals("/"))
									break;							
								
							}
							else
								break;
						}						
						
				 }
                  else{       	  
                            }
				
			}
		});
		appLocationService = new AppLocationService(SelectedDoctor.this);      
	      distance=5;
	      close=(Button) findViewById(R.id.close);
	      mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapFrag));		      					 					 	
		  close.setVisibility(View.GONE);
	      getSupportFragmentManager().beginTransaction().hide(mMapFragment).commit();
	     
		Intent getintent=getIntent();
		String speciality=(String) spec.getText();
		String gend= (String) gender.getText();
		query = ParseQuery.getQuery("DoctorsTable");	
		query.whereEqualTo("Name", nameOfDoctor);
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
								break;							
						 }
						 else
						 {
						 String s=la.get(i).getString("Job");
						 String s1=la.get(i).getString("Name");
						 String s2=la.get(i).getString("Gender");
						 String hosp=la.get(i).getString("Hospital");
						 ParseGeoPoint userLocation = (ParseGeoPoint) la.get(i).get("Location");						 						
						 LatLng lat=new LatLng(userLocation.getLatitude(),userLocation.getLongitude());
						 //Toast.makeText(getApplicationContext(), "Long adding:   "+userLocation.getLongitude()+"\nLat:  "+userLocation.getLatitude() ,Toast.LENGTH_LONG).show();
			        	 LatLngList.add(lat);        	 
			        	 nameList.add(s1);
			        	 hospitalList.add(hosp);
						
						 }
					 };
					
					 try {				         				       				
				    	  googleMap = mMapFragment.getMap();
				    	  
				          googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				         
				         boolean verify=true;            
				         Location gpsLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
				         if (gpsLocation != null) 
							{
								latitude = gpsLocation.getLatitude();
								longitude = gpsLocation.getLongitude();
								
							}				
							else {
								Location gpsLoc = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
								if (gpsLoc != null) 
								{
									latitude = gpsLoc.getLatitude();
									longitude = gpsLoc.getLongitude();
								   
								}	
								else
								{
									verify=false;			
								}
							}
								
						UiSettings sets=googleMap.getUiSettings();
						sets.setCompassEnabled(true);
						sets.setMyLocationButtonEnabled(true);
						sets.setZoomControlsEnabled(true);
						
						
				    	  
						LatLng point=(com.google.android.gms.maps.model.LatLng) LatLngList.get(0);
				         CameraUpdate center=
				        	        CameraUpdateFactory.newLatLng(new LatLng(point.latitude,
				        	                                                 point.longitude));
				        	    CameraUpdate zoom=CameraUpdateFactory.zoomTo(12);
				        	    googleMap.moveCamera(center);
				        	    googleMap.animateCamera(zoom);                           
				         
				        
				         for(int i=0;i<LatLngList.size();i++)
					      {
				        	 point=(com.google.android.gms.maps.model.LatLng) LatLngList.get(i);
				        	 Marker TP = googleMap.addMarker(new MarkerOptions().
				        	         position(point).title(nameList.get(i)).snippet(hospitalList.get(i)));
				        	        		   	 
					      }	        		                      
				        
				      }
				      catch (Exception exc) {
				         exc.printStackTrace();
				      }
			 }
	            else{//handle the error}
	          	  Toast.makeText(getApplicationContext(), "No doctors of these characteristics found", Toast.LENGTH_LONG).show();
	                      }
				 
				
			}
		});

		
	      
	      buttonMaps=(Button) findViewById(R.id.buttonMaps);
	 	  buttonMaps.setOnClickListener(new View.OnClickListener() {			
	 			@Override
	 			public void onClick(View v) {
	 				// TODO Auto-generated method stub
	 				
	 				close.setVisibility(View.VISIBLE);
	 				getSupportFragmentManager().beginTransaction().show(mMapFragment).commit();
	 				
	 			}
	 		});
	 	   	
	 		buttonnext=(Button)findViewById(R.id.buttonnext);
	 		buttonnext.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
	 	   close.setOnClickListener(new View.OnClickListener() {			
	 			@Override
	 			public void onClick(View v) {
	 				// TODO Auto-generated method stub
	 				
	 				mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapFrag));	 				
	 							 			
	 				close.setVisibility(View.GONE);
	 				getSupportFragmentManager().beginTransaction().hide(mMapFragment).commit();
	 				
	 			}
	 		});   				      
	      
	}
	  public void showSettingsAlert(String provider) {
	 		AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectedDoctor.this);
	 		alertDialog.setTitle(provider + " SETTINGS");
	 		alertDialog.setMessage(provider + " is not enabled! Want to go to settings menu?");
	 		alertDialog.setPositiveButton("Settings",
	 		new DialogInterface.OnClickListener() {
	 			public void onClick(DialogInterface dialog, int which) {
	 				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	 				SelectedDoctor.this.startActivity(intent);
	 			}});

	 		alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
	 			public void onClick(DialogInterface dialog, int which) {
	 				dialog.cancel();
	 			}});
	 		alertDialog.show();
	 	}
		
		
	
	public void onBackPressed() {
             finish();

	} 
}
