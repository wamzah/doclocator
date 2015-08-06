package com.example.dicdog1;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Landingpage extends ActionBarActivity {
private static DatabaseHandler db;
private static ProgressBar  mProgressBar;
public static List<String> hospital_list;
protected static final int TIMER_RUNTIME = 5000; // in ms —> 5s
@SuppressWarnings("unused")
private static ParseObject doctordata;
private static TextView text;
protected boolean mbActive;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionbar=getSupportActionBar();
		actionbar.hide();
		setContentView(R.layout.activity_landingpage);
		if(!isNetworkAvailable())
		{
			Toast.makeText(getApplicationContext(),"Network Problem.Please Check Your Internet Connection & try again", Toast.LENGTH_LONG).show();
		  	showSettingsAlert("Internet");		  	
		
		}
		if(isNetworkAvailable())
		{ 
			Parse.enableLocalDatastore(this);
	        Parse.initialize(this,"bWBg10fbvRRs1E6DLjSTQNVoctDfp5UU7oZXNaNx","22qCsWfIKoHxXRjaIe0afLuINwqCGj0HgSPomDym" );
	       
			
			db=new DatabaseHandler(this);
			text=(TextView) findViewById(R.id.textView1);
		     mProgressBar = (ProgressBar)findViewById(R.id.progress);
		     //	a complete method for loading and opening the page
		     final Thread timerThread = new Thread() {
		               @Override
		               public void run() {
		            	 doWork();
		                   mbActive = true;
		                   try {
		                       int waited = 0;
		                       while(mbActive && (waited < TIMER_RUNTIME)) {
		                           sleep(200);
		                           if(mbActive) {
		                               waited += 200;
		                               updateProgress(waited);
		                           }
		                       }
		               } catch(InterruptedException e) {
		                   // do nothing
		               } finally {
		                   onContinue();
		               }
		             }
		          };
		          timerThread.start();
	        }
	}
		
	       
	        public void updateProgress(final int timePassed) {
	            if(null != mProgressBar) {
	                // Ignore rounding error here
	                final int progress = mProgressBar.getMax() * timePassed / TIMER_RUNTIME;
	                mProgressBar.setProgress(progress);
	            }
	        }


	     public void onContinue() {
	          // perform any final actions here
	    	 Intent i=new Intent(Landingpage.this,DashboardActivity.class);
	    	
	    	 startActivity(i);
	    	 finish();
	        }
	     //end of that method
	

	     private boolean isNetworkAvailable() {
	 	    ConnectivityManager connectivityManager 
	 	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	 	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	 	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	 	}
	//loading database data
	public void doWork()
	{
		hospital_list=new ArrayList<String>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
		query.whereExists("Hospital");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> la,
					com.parse.ParseException e) {
				// TODO Auto-generated method stub
				 if(la!=null){
					 
					 for(int i=0;i<la.size();i++)
						{
						 String s=la.get(i).getString("Hospital");
						 if(!(hospital_list.contains(s)))
						 {
							 hospital_list.add(s);
							 //Toast.makeText(getApplicationContext(), hospital_list.get(0), Toast.LENGTH_LONG).show();
							 
						 }};}
                  else{//handle the error}
                	  Toast.makeText(getApplicationContext(),"Network Problem.Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                	  showSettingsAlert("Internet");
                            }
				
			}
		});
		
	
        
	}
	 public void showSettingsAlert(String provider) {
		
		 AlertDialog.Builder alertDialog = new AlertDialog.Builder(Landingpage.this);
		 alertDialog.setTitle(provider + " Settings");
		 alertDialog.setMessage(provider + " is not enabled! Want to go to settings menu?");
		 alertDialog.setInverseBackgroundForced(true);	
		 alertDialog.setPositiveButton("Settings",
		 new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int which) {
		 Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
		 Landingpage.this.startActivity(intent);	
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
}
