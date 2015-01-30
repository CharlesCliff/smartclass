package com.example.classdaemon;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SelectTeammate extends Activity implements OnClickListener {
	private ScrollView sv;
	private LinearLayout ll;
	private TextView tv;
	private Button hi;
	private int i;
	private boolean[] chosed = new boolean[GlobalData.stulist.size()];
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		sv = new ScrollView(this);
		ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		tv = new TextView(this);
		
		tv.setText("请选择队友");
		
		ll.addView(tv);
		
		hi = new Button(this);
		hi.setText("确认队友信息");
		hi.setOnClickListener(this);
		hi.getBackground().setAlpha(32);
		ll.addView(hi);
		
		for(i = 0; i <GlobalData.stulist.size(); i++)
			chosed[i] = false;
		for(i = 0; i < GlobalData.stulist.size(); i++) {
			CheckBox cb = new CheckBox(this);
			cb.setText(GlobalData.stulist.get(i).toString());
			Log.e("Debuginfo1", i+"");
/*			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					Log.e("Debuginfo", i+"");
				}
			});*/
			cb.setId(GlobalData.id_constant2+i);
			ll.addView(cb);
			Log.e("Debuginfo3", i+"");
		}		
		
		sv.addView(ll);
		this.setContentView(sv);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(hi == arg0) {
			GlobalData.teammate.clear();
			for(i = 0; i<GlobalData.stulist.size(); i++) {
				CheckBox cb = (CheckBox)findViewById(GlobalData.id_constant2+i);
				if (cb.isChecked()) {
					chosed[i] = true;
				}
				else {
					chosed[i] = false;
				}
				Log.e("Debuginfo", i+" checked="+cb.isChecked());
			}
			for(i = 0; i<GlobalData.stulist.size(); i++) {
				if(chosed[i]) {
					GlobalData.teammate.add(GlobalData.stulist.get(i).toString());
				}
			}
			Log.e("GlobalData.teammate", GlobalData.teammate.toString());
			Intent bintent = new Intent(com.example.classdaemon.SelectTeammate.this, com.example.classdaemon.SelectProject.class);
			startActivityForResult(bintent, 0);		
		}
	}
}
