package com.example.dicdog1;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SearchingByTime extends ActionBarActivity {
	private static Button btnSelectDate,btnSelectTime;
    private static Button button_search;
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID=1;
    private static Spinner hospitalspinner;
    private static Spinner specialityspinner;
    private static Spinner genderspinner;
    private static String searchGender;
    private static String searchHospital;
    private static String searchJob;
    private static String check;
    private static String aTime;
    private static Intent intenttime;
    private static List<String> jobspec;
    private static String s1;
    private static String s2;
    private static String s3;
    private static String s4ss;
    private static String dayOfTheWeek;
    public static List<String> namelist;
    public static List<String> joblist;
    private static List<String> array_list; 


        // variables to save user selected date and time
    public  int year,month,day,hour,minute;  
// declare  the variables to Show/Set the date and time when Time and  Date Picker Dialog first appears
    private int mYear, mMonth, mDay,mHour,mMinute; 
    
    // constructor
    
    public SearchingByTime()
    {
                // Assign current Date and Time Values to Variables
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
    }        
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        namelist.removeAll(namelist);
        joblist.removeAll(joblist);
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//hide action bar
		ActionBar actionBar=getSupportActionBar();
		actionBar.hide();
	    setContentView(R.layout.activity_searching_by_time);
	   
		check="";
		aTime="Initialized";
		dayOfTheWeek="Initialized";
		namelist=new ArrayList<String>();
		joblist=new ArrayList<String>();
		jobspec=new ArrayList<String>();
		jobspec.add("Null");
		
		
		//hospital spinner
		//adding hospital spinner
  		hospitalspinner = (Spinner) findViewById(R.id.spinnerHospital2);
  		List<String> hospspec=new ArrayList<String>();
  		hospspec=Landingpage.hospital_list;
      
  		//array adapter for adding string
  		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,R.layout.spinner3_item_text,hospspec);
  	    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  	    //calling nothingclass for setting the default value on spinner
  	    hospitalspinner.setAdapter( new NothingSelectedSpinnerAdapter(adapter3, R.layout.contact_spinner3_row_nothing_selected,this));
  	    hospitalspinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String value11 = (String) hospitalspinner.getSelectedItem();
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
			
		});
		
  	//adding first job spinner
		specialityspinner = (Spinner) findViewById(R.id.spinnerSpeciality2);
		//array adapter for adding string
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_text, jobspec);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				//calling nothingclass for setting the default value on spinner
				specialityspinner.setAdapter( new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected,this));
		specialityspinner.setOnTouchListener(new OnTouchListener() {
			
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
				else if(hospitalspinner.getSelectedItem()!=null)
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
	    
	  //adding second gender spinner
  		genderspinner = (Spinner) findViewById(R.id.genderSpinner);
  		String genderspec[]={"male","female"};
  		//array adapter for adding string
  		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.spinner2_item_text,genderspec);
  	    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  	    //calling nothingclass for setting the default value on spinner
  	    genderspinner.setAdapter( new NothingSelectedSpinnerAdapter(adapter2, R.layout.contact_spinner2_row_nothing_selected,this));
	    
  	    //date and time picker process
  	    
  	    
  	    // get the references of buttons
        btnSelectDate=(Button)findViewById(R.id.buttondate1);
        btnSelectTime=(Button)findViewById(R.id.buttontime);
        button_search=(Button)findViewById(R.id.Button01111);
        Button button1=(Button)findViewById(R.id.buttonHome);
		button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(SearchingByTime.this,DashboardActivity.class);
				startActivity(i);
			}
		});
        
        
        // Set ClickListener on btnSelectDate 
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View v) {
                // Show the DatePickerDialog
                 showDialog(DATE_DIALOG_ID);
            }
        });
        
        
        // Set ClickListener on btnSelectTime
        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View v) {
                // Show the TimePickerDialog
                 showDialog(TIME_DIALOG_ID);
            }
        });
        
        
        //search button
		button_search.setOnClickListener(new View.OnClickListener() {
		            
		            public void onClick(View v) {
		                //method calling
		            	button_performed1();
		    }
		});
}
	
	

	
	//parsing method
	public void method_done(String s)
	{
		if(s=="end")
		{
		//array adapter for adding string
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_text, jobspec);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//calling nothingclass for setting the default value on spinner
		specialityspinner.setAdapter( new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected,this));
		//Toast.makeText(getApplicationContext(), hospital_list.get(0), Toast.LENGTH_LONG).show();
		}
		else
		{
			jobspec.add(s);
			
		}					
	}
	// Register  DatePickerDialog listener //a complete dialog for selecting date and time
    private DatePickerDialog.OnDateSetListener mDateSetListener =
                           new DatePickerDialog.OnDateSetListener() {
                       // the callback received when the user "sets" the Date in the DatePickerDialog
                               public void onDateSet(DatePicker view, int yearSelected,
                                                     int monthOfYear, int dayOfMonth) {
                            	   monthOfYear=monthOfYear+1;
                            	   String s=new StringBuilder().append(dayOfMonth).append('/').append('0')
                                           .append(monthOfYear).append("/").append(yearSelected).toString();
                            	   //Toast.makeText(getApplicationContext(),s ,Toast.LENGTH_LONG).show();
                            	   SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            	   Date date = null;
                            	   try {
                            	    date = formatter.parse(s);//catch exception
                            	   } catch (ParseException e) {
                            	    
                            	   } catch (java.text.ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
                            	   dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date);//Thursday
                            	 
                                  
                               }
                           };

      // Register  TimePickerDialog listener                 
                           private TimePickerDialog.OnTimeSetListener mTimeSetListener =
                               new TimePickerDialog.OnTimeSetListener() {
                        // the callback received when the user "sets" the TimePickerDialog in the dialog
                                   public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                                       hour = hourOfDay;
                                       minute = min;
                                       Date d=new Date();
                                       String delegate = "hh:mm aaa"; 
                                       SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                                       updateTime(hour,minute);
                                       
                                     }
                               };


   // Method automatically gets Called when you call showDialog()  method
                           @Override
                           protected Dialog onCreateDialog(int id) {
                               switch (id) {
                               case DATE_DIALOG_ID:
                        // create a new DatePickerDialog with values you want to show 
                                   return new DatePickerDialog(this,
                                               mDateSetListener,
                                               mYear, mMonth, mDay);
                       // create a new TimePickerDialog with values you want to show 
                               case TIME_DIALOG_ID:
                                   return new TimePickerDialog(this,
                                           mTimeSetListener, mHour, mMinute, false);
                              
                               }
                               return null;
                           }
                           private static String utilTime(int value) {
                               
                               if (value < 10)
                                   return "0" + String.valueOf(value);
                               else
                                   return String.valueOf(value);
                           }
                            
                           // Used to convert 24hr format to 12hr format with AM/PM values
                           private void updateTime(int hours, int mins) {
                                
                               String timeSet = "";
                               if (hours > 12) {
                                   hours -= 12;
                                   timeSet = "PM";
                               } else if (hours == 0) {
                                   hours += 12;
                                   timeSet = "AM";
                               } else if (hours == 12)
                                   timeSet = "PM";
                               else
                                   timeSet = "AM";
                        
                                
                               String minutes = "";
                               if (mins < 10)
                                   minutes = "0" + mins;
                               else
                                   minutes = String.valueOf(mins);
                        
                               // Append in a StringBuilder
                                aTime = new StringBuilder().append(hours).append(':')
                                       .append(minutes).append(" ").append(timeSet).toString();
                          
                           }
                           
                       	public void button_performed1()
                    	{
                       		Toast.makeText(getApplicationContext(), "Finding Doctors", Toast.LENGTH_SHORT).show();
                       		if(genderspinner.getSelectedItem()==null || specialityspinner.getSelectedItem()==null ||
                       				hospitalspinner.getSelectedItem()==null || aTime.equals("Initialized")
                       				|| dayOfTheWeek.equals("Initialized"))
                    		{
                    			Toast.makeText(getApplicationContext(), "Select All categories ", Toast.LENGTH_LONG).show();
                    		}
                    		else
                    		{
                       		s4ss="";
                    		array_list=new ArrayList<String>();
                    		searchGender=(String)genderspinner.getSelectedItem();
                    		searchJob = (String) specialityspinner.getSelectedItem();
                    		searchHospital = (String)hospitalspinner.getSelectedItem();
                    		 s2=aTime;
        					 dayOfTheWeek=dayOfTheWeek.substring(0,3);
                          	String value;
                    		ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
                    		query.whereEqualTo("Gender", searchGender);
                    		query.whereEqualTo("Job", searchJob);
                    		query.whereEqualTo("Hospital", searchHospital);
                    		query.findInBackground(new FindCallback<ParseObject>() {
                    			@Override
                    			public void done(List<ParseObject> la,
                    					com.parse.ParseException e) {
                    				// TODO Auto-generated method stub
                    				 for(int i=0;i<la.size()+1;i++)
                    				 {
                    					 if(i==la.size())
                						 {
                    						 if(la.size()==0)
                    						 {
                    							 
                    							 Toast.makeText(getApplicationContext(), "No doctor of these characteristic found ", Toast.LENGTH_LONG).show();
                    							
                    							 break;
                    						 }
                    						 else
                    						 {
                    							String s0="end";
                								String s2="end";
                								String s="end";
                								check2(s0,s);                								
                							//	Toast.makeText(getApplicationContext(), "hello1 ", Toast.LENGTH_LONG).show();
                								break;
                    						 }
                						 }
                    					 s1=la.get(i).getString("Scehdule");
                    					 if(s1==null)
                    					 {
                    						 
                    						// Toast.makeText(getApplicationContext(),"There is no doctor available in this "
                    						 //		+ "time period in this hospital", Toast.LENGTH_LONG).show();
                    						 continue;
                    					 }
                    					 
                    					// Toast.makeText(getApplicationContext(), "schedule "+ s1, Toast.LENGTH_LONG).show();
                    					 for(int b=0;b<s1.length();b++)
                    					 {                    						 
                    						 if(String.valueOf(s1.charAt(b)).equals("/"))
                    						 {                    							 
                    								//Toast.makeText(getApplicationContext(), "day \n"+ dayOfTheWeek+"z\n"
                      								//+s4ss.substring(0, 3)+"z", Toast.LENGTH_LONG).show();
                    							 if(dayOfTheWeek.equals(s4ss.substring(0, 3)))
                             					{
                    								 //timeEntered is the schedule string
                    								String timeEntered= s4ss.substring(4,s4ss.length()-1);
                                					int index=timeEntered.indexOf('-');                      								
                    								String startTime=timeEntered.substring(0,index);
                    								String endTime=timeEntered.substring(index+1 , timeEntered.length());
                    								
                    								//It is the time that user enters
                    								int index1=aTime.indexOf(':');
                    								double start=Double.parseDouble(aTime.substring(0,index1));
                    								double minute=Double.parseDouble(aTime.substring(index1+1,index1+3));
                    								start+=(minute*(0.01));          //converting from string to double          								                    								
                    								
                    								//Now calculating start and endtime from Db values
                    								int index2=startTime.indexOf(':');
                    								double startdb=Double.parseDouble(startTime.substring(0,index2));
                    								double minutedb=Double.parseDouble(startTime.substring(index2+1,index2+3));
                    								startdb+=(minutedb*(0.01));
                    								
                    								index2=endTime.indexOf(':');
                    								double enddb=Double.parseDouble(endTime.substring(0,index2));
                    								double m=Double.parseDouble(endTime.substring(index2+1,index2+3));
                    								enddb+=(m*(0.01));
                    								
                    								
                    								
                    								//Form i.e AM or PM
                    								String enteredForm= aTime.substring(aTime.length()-2);                    								
                    								String startForm= startTime.substring(startTime.length()-2);
                    								String endForm= endTime.substring(endTime.length()-2);
                       								if(enteredForm.equals(startForm))
                    								{
                    									if(start<startdb )
                    									{
                    										if(enddb<startdb && start<=enddb && endForm.equals(startForm))
                    										{
                    											String s1=la.get(i).getString("Name");
                            									String s2=la.get(i).getString("Job");
                            									check2(s1,s2);
                    										}
                    										
                    									}
                    									else{
                    									//In case if the doctors starting and ending time formats are same i.e PM or AM
                    									//timings 9PM - 11PM and time entered 10PM
                    									if(enteredForm.equals(endForm) && start<=enddb )
                    									{
                    										String s1=la.get(i).getString("Name");
                        									String s2=la.get(i).getString("Job");
                        									check2(s1,s2);
                    										
                    									}
                    									
                    									//timings 9PM - 1PM and time entered 10PM
                    									else if(enteredForm.equals(endForm) && start>=enddb )
                    									{
                    										String s1=la.get(i).getString("Name");
                        									String s2=la.get(i).getString("Job");
                        									check2(s1,s2);
                    										
                    									}
                    									//In case if the doctors starting and ending time formats are diffent i.e end time is AM and user entered PM
                    									//timings 9PM - 2AM and time entered 10PM
                    									else if(!(enteredForm.equals(endForm)) && start>=enddb )
                    									{
                    										if(start>=12.0)
                    										{
                    											
                    										}
                    										else{
                    										//Toast.makeText(getApplicationContext(), "enter   "+ s4ss+"\ntime    "+timeEntered
                            							    //	+"\ntime entered    "+aTime, Toast.LENGTH_LONG).show();
                    										String s1=la.get(i).getString("Name");
                        									String s2=la.get(i).getString("Job");
                        									check2(s1,s2);
                    										}
                    										
                    									}
                    									                    	
                    									//In case if the doctors starting and ending time formats are diffent i.e end time is AM and user entered PM
                    									//timings 9PM - 11AM and time entered 1PM
                    									else if(!(enteredForm.equals(endForm)) && start<=enddb )
                    									{                    										
                    										check2("None","none");
                    									}    
                    									}
                    								}
                    								else if(!(enteredForm.equals(startForm)))
                    								{
                    									//In case if the doctors starting and ending time formats are same i.e PM or AM
                    									//timings 9PM - 2AM and time entered 1AM
                    									if(enteredForm.equals(endForm) )
                    									{
                    										if(start<=enddb || start>=12.0)
                    										{
	                    										String s1=la.get(i).getString("Name");
	                        									String s2=la.get(i).getString("Job");
	                        									check2(s1,s2);                    									
                    										}
                    									}
                    									
                    									//timings 9PM - 2AM and time entered 3AM
                    									else if(enteredForm.equals(endForm) && start>=enddb )
                    									{                    										
                    										check2("None","none");
                    									}
                    									//In case if the doctors starting and ending time formats are diffent i.e end time is AM and user entered PM
                    									//timings 9PM - 2AM and time entered 10PM
                    									else if(!(enteredForm.equals(endForm)) && start>=enddb )
                    									{
                    										
                    										String s1=la.get(i).getString("Name");
                        									String s2=la.get(i).getString("Job");
                        									check2(s1,s2);                    										
                    									}                    									                    	
                    									//In case if the doctors starting and ending time formats are diffent i.e end time is AM and user entered PM
                    									//timings 9PM - 11AM and time entered 1PM
                    									else if(!(enteredForm.equals(endForm)) && start<=enddb )
                    									{                    										
                    										String s1=la.get(i).getString("Name");
                        									String s2=la.get(i).getString("Job");
                        									check2(s1,s2);
                    									}                    								
                    								}                             				                             					
                             					}
                    							 s4ss="";
                    						 }
                    						 else
                    						 {
                    							 s4ss=s4ss+s1.charAt(b);
                    						 }
                    					 }
                    					
                    					 
                    				 }                    			
                    			}
                    		});
                    		}
                    		
                    	}
                       	public void check2(String s45,String s)
                    	{   
                       		
	                    		if(s45.equals("end"))
	                    		{
	                    			if(!namelist.isEmpty())
	                    			{
		                    			
		                    			intenttime=new Intent(SearchingByTime.this,DoctorsList2.class);
		                    			
		                    			startActivity(intenttime);
	                    			}
	                    			else
	                    			{
	                    				
	                    				Toast.makeText(getApplicationContext(),"The requested specialist doctor not available in this time period in this hospital", Toast.LENGTH_LONG).show();
	                    			}
	                    			//setListAdapter(new DoctorListAdapter(this, namelist,joblist));
	                    		}
	                    		else if(s45.equals("None"))
	                    		{
	                    			
	                    		}
	                    		else
	                    		{
	                    			namelist.add(s45);
	                    			joblist.add("\n\n" + s);
	                    		}
                       		
                    	}                       



}