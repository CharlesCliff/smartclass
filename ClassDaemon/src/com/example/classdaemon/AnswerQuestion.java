package com.example.classdaemon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class AnswerQuestion extends Activity implements OnClickListener {
	private ScrollView sv;
	private LinearLayout ll;
	private RadioGroup group;
	private TextView tv;
	private Button hi;
	private String answer = new String();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sv = new ScrollView(this);
		ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		
		for(int i = 0; i < GlobalData.getTestNum(); i++) {
			tv = new TextView(this);
			tv.setText("题目"+i);
			group = new RadioGroup(this);
			group.setOrientation(RadioGroup.HORIZONTAL);
			
			RadioButton radio1 = new RadioButton(this);
			radio1.setText("A");
			radio1.setId(1000*i+1);
			group.addView(radio1);
			RadioButton radio2 = new RadioButton(this);
			radio2.setText("B");
			radio2.setId(1000*i+2);
			group.addView(radio2);
			RadioButton radio3 = new RadioButton(this);
			radio3.setText("C");
			radio3.setId(1000*i+3);
			group.addView(radio3);
			RadioButton radio4 = new RadioButton(this);
			radio4.setText("D");
			radio4.setId(1000*i+4);
			group.addView(radio4);
			group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup arg0, int arg1) {
					// TODO Auto-generated method stub
					if((arg1%1000)==1) {
					}
					else if((arg1%1000)==2) {
						
					}
					else if(arg1%1000==3) {
						
					}
					else if(arg1%1000==4) {
						
					}
					
				}
			});
			
			ll.addView(tv);
			ll.addView(group);			
		}
		
		hi = new Button(this);
		hi.setOnClickListener(this);
		hi.setText("提交答案");
		
		ll.addView(hi);
		
		sv.addView(ll);
		
		this.setContentView(sv);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0 == hi) {
			// 提交答案
		}
	}
}
