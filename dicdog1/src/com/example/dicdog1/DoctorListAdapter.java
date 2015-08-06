package com.example.dicdog1;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DoctorListAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final List<String> name;
	private final List<String> values;
	private final String gender;
	
 
	public DoctorListAdapter(Context context,List<String> names, List<String> values) {
		super(context, R.layout.activity_doctors_list, names);
		
		this.context = context;
		this.name=names;
		this.values = values;
		String s="male";
		this.gender=s;
	}	
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_doctors_list, parent, false);
		TextView nameView = (TextView) rowView.findViewById(R.id.name);
		TextView textView = (TextView) rowView.findViewById(R.id.label);	
		final ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);		
		nameView.setText("Dr. "+name.get(position));
		textView.setText(values.get(position)); 
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorsTable");
		query.whereEqualTo("Name", name.get(position));
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> la,
					com.parse.ParseException e) {
				// TODO Auto-generated method stub
				 if(la!=null){
					 //Toast.makeText(getApplicationContext(),"Name: "+la.get(0).getString("Name"), Toast.LENGTH_LONG).show();					
						ParseFile fileObject = (ParseFile) la.get(0).get("Images");
						fileObject.getDataInBackground(new GetDataCallback() {									
									@Override
									public void done(byte[] arg0,
											ParseException arg1) {
										// TODO Auto-generated method stub
										if (arg1 == null) {			
											Bitmap bmp = BitmapFactory.decodeByteArray(arg0, 0,arg0.length);
                                            BitmapDrawable b=new BitmapDrawable(bmp);
											imageView.setBackgroundDrawable(b);    																					
										}
										
									}
						
					
                         });}
                  else{ }				
			}
		});			
		return rowView;
	}
}