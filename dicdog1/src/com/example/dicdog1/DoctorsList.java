package com.example.dicdog1;

import java.util.ArrayList;
import java.util.List;

import android.R.color;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class DoctorsList extends ListActivity {

public static List<String> jobList;
public static List<String> nameList;
private static List<String> genderList;
private static List<String> genderList1;
private static String name;
public static int position1;
//private static String s1;
private static String selectedValue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View view = this.getWindow().getDecorView();
		 view.setBackgroundDrawable( getResources().getDrawable(R.drawable.textv2) );
		//setContentView(R.layout.activity_selected_doctor_copy);
		jobList =new ArrayList<String>();
		nameList =new ArrayList<String>();
		genderList =new ArrayList<String>(); 
		//genderList1 =new ArrayList<String>();
		

		List<Doctor> doctorList =new ArrayList<Doctor>();
		Intent intent=getIntent();
		String speciality=intent.getExtras().getString("speciality");
 		String gender=intent.getExtras().getString("gender");
 		String hosp=intent.getExtras().getString("hospital");
 		name=intent.getExtras().getString("name");
 		if(name.equals("None"))
 		{
 		//////////////////////////////
	 		ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
			query.whereEqualTo("Gender", gender);
			query.whereEqualTo("Job", speciality);
			query.whereEqualTo("Hospital", hosp);
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
							    String s0="end";
								String s2="end";
								String s="end";
								check(s0,s,s2);
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
						 check(s1,s,s2);
					// Toast.makeText(getApplicationContext(),"Hello " + s, Toast.LENGTH_LONG).show();
			//		     genderList1.add(s);		
						 }
						 };
						
						//Toast.makeText(getApplicationContext(),"Hello " + genderList1.get(0), Toast.LENGTH_LONG).show();
					
                         }
                  else{//handle the error}
                	  Toast.makeText(getApplicationContext(), "No doctors of these characteristics found", Toast.LENGTH_LONG).show();
                            }
				
			}
		});
 		}
 		else
 		{
 			
 			Intent intent2=new Intent(DoctorsList.this,SelectedDoctor.class);
			intent2.putExtra("doctor", name);
		    startActivity(intent2);	
		     
/* 			ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");			
			query.whereEqualTo("Name", name);
			query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> la,
					com.parse.ParseException e) {
				// TODO Auto-generated method stub
				 if(la!=null){
					 Intent intent=new Intent(DoctorsList.this,SelectedDoctor.class);
					 intent.putExtra("doctor", name);
				     startActivity(intent);					 				
                         }
                  else{//handle the error}
                            }
				
			}			
		});*/
		     
 		}
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {

		//get selected items
		
		//String selectedValue = (String) getListAdapter().getItem(position);
		

		//get selected items
		//String selectedValue = (String) getListAdapter().getItem(position);
		position=position-1;
		position1=position;
		selectedValue = (String) getListAdapter().getItem(position);		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
		query.whereEqualTo("Name", selectedValue);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> la,
					com.parse.ParseException e) {
				// TODO Auto-generated method stub
				 if(la!=null){
					 Intent intent=new Intent(DoctorsList.this,SelectedDoctor.class);
					 intent.putExtra("doctor", selectedValue);
					 intent.putExtra("locat", position1);
				     startActivity(intent);
                         }
                  else{Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
                            }
				
			}
		});
		
		
	}
	public void check(String s1,String s,String s2)
	{
		 
		if(s1.equals("end"))
		{
			//Toast.makeText(getApplicationContext(),"Ending", Toast.LENGTH_LONG).show();
			ListView lv = getListView();
			LayoutInflater inflater = getLayoutInflater();
			View header = inflater.inflate(R.layout.activity_selected_doctor_copy, lv, false);
			lv.addHeaderView(header, null, false);
			setListAdapter(new DoctorListAdapter(this, nameList,jobList));
			
			
		}
		else
		{
			//Toast.makeText(getApplicationContext(),"Hello YESH"  + s2, Toast.LENGTH_LONG).show();
			nameList.add(s1);
			jobList.add("\n\n" + s);
			genderList.add(s2);
			//Toast.makeText(getApplicationContext(), "helloeyeyyy ", Toast.LENGTH_LONG).show();
		}
	}
	
	public void onBackPressed() {
     //   Intent intent = new Intent(this, DashboardActivity.class);
       // startActivity(intent);
        finish();

} 
	
}
