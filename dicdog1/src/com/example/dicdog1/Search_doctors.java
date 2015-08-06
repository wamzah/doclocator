package com.example.dicdog1;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Search_doctors extends ActionBarActivity {
	
private static Button button1;
private static List<Doctor> doctorList;
private static String searchJob;
private static String searchGender;
private static String searchName;
private static Spinner spinnerHospital;
private static Spinner genderspinner;
private static Button button2;
private static Intent intent;
private static String check;
private static String searchHospital;
private static AutoCompleteTextView doctorName;

List<String> jobspec;
List<String> names;
public static Intent intent2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
				actionBar.hide();
		setContentView(R.layout.activity_search_doctors);
		check="";
		jobspec=new ArrayList<String>();
		names=new ArrayList<String>();			
		jobspec=Landingpage.hospital_list;		
		intent2=new Intent(Search_doctors.this,SelectedDoctor.class);
		//initializing doctorlist,intent
		doctorList=new ArrayList<Doctor>();
		intent=new Intent(Search_doctors.this,DoctorsList.class);
		doctorName = (AutoCompleteTextView) findViewById(R.id.doctorNameautoComplete);
		doctorName.setOnClickListener(new View.OnClickListener() {	 	
			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
			doctorName.showDropDown();
			}
			});
			
			
		//search button
		button2=(Button)findViewById(R.id.buttonsearchdoc);		
		button2.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				button_performed();
			}
		});

		spinnerHospital=(Spinner)findViewById(R.id.spinner_hospital);
		//array adapter for adding string
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_text, jobspec);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    //calling nothingclass for setting the default value on spinner
	    spinnerHospital.setAdapter( new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner3_row_nothing_selected,this));
	
	  //adding second gender spinner
	  		genderspinner = (Spinner) findViewById(R.id.Spinner02);
	  		String genderspec[]={"male","female"};
	  		//array adapter for adding string
	  		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.spinner2_item_text,genderspec);
	  	    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	    //calling nothingclass for setting the default value on spinner
	  	    genderspinner.setAdapter( new NothingSelectedSpinnerAdapter(adapter2, R.layout.contact_spinner2_row_nothing_selected,this));
       
	  	    
	  	  spinnerHospital.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String value11 = (String) spinnerHospital.getSelectedItem();
					String value22 = (String) genderspinner.getSelectedItem();
					ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");				
					query.whereEqualTo("Hospital", value11);
					query.setLimit(1000);
					query.findInBackground(new FindCallback<ParseObject>() {						
						@Override
						public void done(List<ParseObject> la,
								com.parse.ParseException e) {
							// TODO Auto-generated method stub
							 if(la!=null)
							 {								 
								 for(int i=0;i<la.size();i++)
								 {
									 String s=la.get(i).getString("Name");									 
									 if(i==la.size()-1)
									 {
										 check="end";
										 method_done(check);
										 break;
									 }
									 else if(!(names.contains(s)))
									 {
										 method_done(s); 
									 }
								  };
							  }
			                  else
			                  {//handle the error}
			                	  Toast.makeText(getApplicationContext(), "There is no doctor of this category ", Toast.LENGTH_LONG).show();
			                  }						
						}
					});					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
				
			});
	  	     
	    
	  	  
	  	    //setting home button. it directs to dashboard
	  	    button1=(Button)findViewById(R.id.buttonHome);
			button1.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i=new Intent(Search_doctors.this,DashboardActivity.class);					
					startActivity(i);
					finish();
				}
			});
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.doctors, menu);
		return true;
	}	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void method_done(String s)
	{
		if(s.equals("end"))
		{ 		  	  		  	  		  	  		  	  
	  		  // 	Create the adapter and set it to the AutoCompleteTextView 
  		  ArrayAdapter<String> nameadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
  		 doctorName.setAdapter(nameadapter);
		  	  	  	    	  	    	  	    	  	
		}
		else
		{
		names.add(s);
			
		}					
   }

	
	public void onBackPressed() {
                    Intent intent = new Intent(this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
        
	} 
	
	
	//button for extracting data from database and showing on listview
	public void button_performed()
	{
		Toast.makeText(getApplicationContext(), "Finding Doctors", Toast.LENGTH_SHORT).show();
		if(genderspinner.getSelectedItem()==null || spinnerHospital.getSelectedItem()==null )
		{
			Toast.makeText(getApplicationContext(), "Select All categories", Toast.LENGTH_LONG).show();
		}
		else
		{
			searchGender=(String)genderspinner.getSelectedItem();
			searchHospital = (String) spinnerHospital.getSelectedItem();
			searchName = doctorName.getText().toString();			
	      	
			ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
			query.whereEqualTo("Gender", searchGender);
			query.whereEqualTo("Hospital", searchHospital);
			query.whereEqualTo("Name", searchName);
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> la,
						com.parse.ParseException e) {
					// TODO Auto-generated method stub
					 if(la!=null)
					 {
						 //startActivity(intent);
						 if(la.size()==0)
						 {
							
							 Toast.makeText(getApplicationContext(), "No doctor of these characteristics found in this hospital ", Toast.LENGTH_LONG).show();
						 }
						 else
						 { 
							 intent2.putExtra("doctor", searchName);
							 startActivity(intent2);
						    
						 }
						
	                  }
	                  else{//handle the error
	                	 
	                	  Toast.makeText(getApplicationContext(), "There is no doctor of this category ", Toast.LENGTH_LONG).show();
	                       }
					
				}
			});
			
		}
	}
	
		
     }

