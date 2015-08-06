package com.example.dicdog1;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SearchMaps extends ActionBarActivity {

private static List<Doctor> doctorList;
private static String searchJob;
//private static String searchGender;
private static String speciality;
//private static Spinner genderspinner;
private static Button button2;
private static Button button1;
private static String check;
private static AutoCompleteTextView specialityName;
List<String> jobspec;
List<String> names;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_search_maps);
						
		check="";
		jobspec=new ArrayList<String>();
		names=new ArrayList<String>();			
		jobspec=Landingpage.hospital_list;				 		
		//initializing doctorlist,intent
		doctorList=new ArrayList<Doctor>();		
		
		//search button
		button2=(Button)findViewById(R.id.buttonsearchdoc);		
		button2.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				button_performed();
			}
		});
		
	
	  //adding second gender spinner
	  		//genderspinner = (Spinner) findViewById(R.id.Spinner02);
	  		String genderspec[]={"male","female"};
	  		//array adapter for adding string
	  		//ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.spinner2_item_text,genderspec);
	  	    //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	    //calling nothingclass for setting the default value on spinner
	  	    //genderspinner.setAdapter( new NothingSelectedSpinnerAdapter(adapter2, R.layout.contact_spinner2_row_nothing_selected,this));
			specialityName = (AutoCompleteTextView) findViewById(R.id.specialityName);
			specialityName.setOnClickListener(new View.OnClickListener() {			  			
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//String value22 = (String) genderspinner.getSelectedItem();
							ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
							//query.whereEqualTo("Gender",value22 );							
							query.findInBackground(new FindCallback<ParseObject>() {						
								@Override
								public void done(List<ParseObject> la,
										com.parse.ParseException e) {
									// TODO Auto-generated method stub
									 if(la!=null)
									 {								 
										 for(int i=0;i<la.size();i++)
										 {
											 String s=la.get(i).getString("Job");									 
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
							specialityName.showDropDown();
						}
						});
	  	    //setting home button. it directs to dashboard
	  	    button1=(Button)findViewById(R.id.buttonHome);
			button1.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i=new Intent(SearchMaps.this,DashboardActivity.class);					
					startActivity(i);
					finish();
				}
			});		
	}	
	public void method_done(String s)
	{
		if(s.equals("end"))
		{
		  	// Get the string array
		  	//String[] countries = getResources().getStringArray(R.array.countries_array);
		  	  		  	  		  	  		  	  		  	  
	  		  // 	Create the adapter and set it to the AutoCompleteTextView 
  		  ArrayAdapter<String> nameadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
  		 specialityName.setAdapter(nameadapter);
		  	  	  	    	  	    	  	    	  	
		}
		else
		{
			//Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
			if(!names.contains(s))
			{
				names.add(s);
			}
			
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
		
			Toast.makeText(getApplicationContext(), "Locating Hospital on Maps", Toast.LENGTH_LONG).show();
		
			//searchGender=(String)genderspinner.getSelectedItem();			
			speciality = specialityName.getText().toString();			
	      	
			ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
			//query.whereEqualTo("Gender", searchGender);
			query.whereEqualTo("Job", speciality);			
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
							 Toast.makeText(getApplicationContext(), "No doctor of these characteristics found", Toast.LENGTH_LONG).show();
						 }
						 else
						 {						
							 Intent intent2=new Intent(SearchMaps.this,MapsActivity.class);
							 //intent2.putExtra("gender", searchGender);
							 intent2.putExtra("job", speciality);
						     startActivity(intent2);								 
						 }
						
	                  }
	                  else{//handle the error}
	                	  Toast.makeText(getApplicationContext(), "There is no doctor of this category ", Toast.LENGTH_LONG).show();
	                       }
					
				}
			});					
	}		
}

