package com.rajesh.android.mapsv2.draganddrop;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
public class PageOne extends Activity{
	
	
	TextView textView1;
	Button button1;
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.pageone);
	textView1=(TextView)findViewById(R.id.textView1);
	button1=(Button)findViewById(R.id.button1);
	
	button1.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Intent map=new Intent(PageOne.this,MainActivity.class);
			startActivityForResult(map, 20002);
		}
	});
	
	
}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	
	switch(requestCode) {
    case 20002:
          if (resultCode == RESULT_OK) {
              String foundAddress = data.getStringExtra("foundAddress");            
              textView1.setText(""+foundAddress);
              break;
          }
          }
}
}
