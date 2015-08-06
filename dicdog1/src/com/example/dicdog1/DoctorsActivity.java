package com.example.dicdog1;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class DoctorsActivity extends ActionBarActivity {
private static Button button1;
private static List<Doctor> doctorList;
private static String searchJob;
private static String searchGender;
private static String searchHospital;
private static Spinner spinnerDoctor;
private static Spinner spinnerHospital;
private static Spinner spinnerGender;
private static Button button2;
private static Intent i;

private static ParseObject doctordata;
private static Intent intent;
private static String check;
public static List<String> jobspec; 
private static ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getSupportActionBar();
				actionBar.hide();
		setContentView(R.layout.activity_doctors);
		if(!isNetworkAvailable())
		{
			Toast.makeText(getApplicationContext(),"Network Problem.Please Check Your Internet Connection & try again", Toast.LENGTH_LONG).show();
		  	showSettingsAlert("Internet");		  	
			}
		if(isNetworkAvailable())
		{ 
			spinnerDoctor=(Spinner)findViewById(R.id.Spinner02);
			jobspec=new ArrayList<String>();
			jobspec.add("Null");
			check="";

	  //adding second gender spinner
	  		spinnerGender = (Spinner) findViewById(R.id.Spinner01);
	  		String genderspec[]={"male","female"};
	  		
	  //array adapter for adding string
	  		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.spinner2_item_text,genderspec);
	  	    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	    //calling nothingclass for setting the default value on spinner
	  	  spinnerGender.setAdapter( new NothingSelectedSpinnerAdapter(adapter2, R.layout.contact_spinner2_row_nothing_selected,this));
       
	   //adding hospital spinner
	  	  	spinnerHospital = (Spinner) findViewById(R.id.spinnerHosp);
	  		List<String> hospspec=new ArrayList<String>();
	  		hospspec=Landingpage.hospital_list;
	      
	  	//array adapter for adding string
			adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_text,jobspec);
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    //calling nothingclass for setting the default value on spinner
		    spinnerDoctor.setAdapter( new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected,this));
		   
	  		
	  	//array adapter for adding string
	  		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,R.layout.spinner3_item_text,hospspec);
	  	    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	    
	  	 //calling nothingclass for setting the default value on spinner
	  	    spinnerHospital.setAdapter( new NothingSelectedSpinnerAdapter(adapter3, R.layout.contact_spinner3_row_nothing_selected,this));
	  	    
	  	  	intent=new Intent(this,DoctorsList.class);
	  	  	
			button2=(Button)findViewById(R.id.button1);
			button2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					button_performed1();
				}
			});
			
	  	    
	  	    //setting home button. it directs to dashboard
	  	  button1=(Button)findViewById(R.id.buttonHome);
			button1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					i=new Intent(DoctorsActivity.this,DashboardActivity.class);
					startActivity(i);
				}
			});
			
			spinnerDoctor.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if(jobspec.get(0).equals("Null"))
					{
						final Toast toast = Toast.makeText(getApplicationContext(),"Please Select Hospital First",Toast.LENGTH_SHORT);
						toast.show();
					Handler handler = new Handler();
			        handler.postDelayed(new Runnable() {
			           @Override
			           public void run() {
			               toast.cancel(); 
			           }
			    }, 500);
					return true;
					}
					else if(spinnerHospital.getSelectedItem()!=null)
					{
						
						final Toast toast = Toast.makeText(getApplicationContext(),"Loading data",Toast.LENGTH_SHORT);
						toast.show();
					Handler handler = new Handler();
			        handler.postDelayed(new Runnable() {
			           @Override
			           public void run() {
			               toast.cancel(); 
			           }
			    }, 500);
					return false;
					}
					else
						return false;
				
				}
			});
		
			
			spinnerHospital.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					
					String value11 = (String) spinnerHospital.getSelectedItem();
					ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
					query.whereEqualTo("Hospital", value11);
					query.findInBackground(new FindCallback<ParseObject>() {
						
						@Override
						public void done(List<ParseObject> la,
								com.parse.ParseException e) {
							// TODO Auto-generated method stub
							 if(la!=null){
								 
								 for(int i=0;i<la.size();i++)
									{
									 String s=la.get(i).getString("Job");
									 if(i==la.size()-1)
									 {
										 check="end";
										 jobspec.remove("Null");
										 method_done(check);
										 break;
										 }
									 else if(!(jobspec.contains(s)))
									 {
										 method_done(s); 
									 }
									 };}
			                  else{
			                            }							
						}
					});					
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
				
			});}}
		
				public void method_done(String s)
				{
					if(s.equals("end"))
					{
						
						//array adapter for adding string
					adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_text, jobspec);
					    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					    //calling nothingclass for setting the default value on spinner
					    spinnerDoctor.setAdapter( new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected,this));
					   //Toast.makeText(getApplicationContext(), hospital_list.get(0), Toast.LENGTH_LONG).show();*/
					}
					else
					{
						
						jobspec.add(s);						
					}					
				}							
	
				
	//button for extracting data from database and showing on listview
		public void onBackPressed() {
	        Intent intent = new Intent(this, DashboardActivity.class);
	        startActivity(intent);
	        finish();
	
	} 
	
	public void button_performed1()
	{		
		Toast.makeText(getApplicationContext(), "Finding Doctors", Toast.LENGTH_SHORT).show();
		if(spinnerGender.getSelectedItem()==null || spinnerDoctor.getSelectedItem()==null ||
				spinnerHospital.getSelectedItem()==null)
		{
			Toast.makeText(getApplicationContext(), "Select All categories ", Toast.LENGTH_LONG).show();
		}
		else
		{
		searchGender=(String)spinnerGender.getSelectedItem();
		searchJob = (String) spinnerDoctor.getSelectedItem();
		searchHospital = (String) spinnerHospital.getSelectedItem();	
		ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
		query.whereEqualTo("Gender", searchGender);
		query.whereEqualTo("Job", searchJob);
		query.whereEqualTo("Hospital", searchHospital);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> la,
					com.parse.ParseException e) {
				// TODO Auto-generated method stub
				 if(la!=null){
					 
					 if(la.size()==0)
					 {
						 Toast.makeText(getApplicationContext(), "No doctor of these characteristic found ", Toast.LENGTH_SHORT).show();
					 }
					 else
					 {
					    intent.putExtra("speciality", searchJob);
				      	intent.putExtra("gender", searchGender);
				      	intent.putExtra("hospital", searchHospital);
				      	intent.putExtra("name","None");
				      	
				      	startActivity(intent);
				      	 
					 }
					
                 }
                  else{//handle the error}
                	  Toast.makeText(getApplicationContext(), "No doctors of these characteristics found", Toast.LENGTH_LONG).show();
                      }
				
			}
		});
		}
	} 
	public void showSettingsAlert(String provider) {
		
		 AlertDialog.Builder alertDialog = new AlertDialog.Builder(DoctorsActivity.this);
		 alertDialog.setTitle(provider + " Settings");
		 alertDialog.setMessage(provider + " is not enabled! Want to go to settings menu?");
		 alertDialog.setInverseBackgroundForced(true);	
		 alertDialog.setPositiveButton("Settings",
		 new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int which) {
		 Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
		 DoctorsActivity.this.startActivity(intent);	
		 finish();
		 }});	
		 alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int which) {
		 //dialog.cancel();
		 Intent intent = new Intent(Intent.ACTION_MAIN);
		     intent.addCategory(Intent.CATEGORY_HOME);
		     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		     startActivity(intent);
		     finish();
		 }});
		 alertDialog.show();
		 
		}   
	 private boolean isNetworkAvailable() {
	 	    ConnectivityManager connectivityManager 
	 	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	 	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	 	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	 	}
     
	} 


		
	
	

