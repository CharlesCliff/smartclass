package com.example.classdaemon;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
	private char[] as = new char[GlobalData.getTestNum()];
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		sv = new ScrollView(this);
		ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		
		for(int i = 0; i < GlobalData.getTestNum(); i++) {
			tv = new TextView(this);
			tv.setText("题目"+i);
			group = new RadioGroup(this);
			group.setOrientation(RadioGroup.HORIZONTAL);
			
			int id_c1 = GlobalData.id_constant1;
			
			RadioButton radio1 = new RadioButton(this);
			radio1.setText("A");
			radio1.setId(id_c1*i+1);
			group.addView(radio1);
			RadioButton radio2 = new RadioButton(this);
			radio2.setText("B");
			radio2.setId(id_c1*i+2);
			group.addView(radio2);
			RadioButton radio3 = new RadioButton(this);
			radio3.setText("C");
			radio3.setId(id_c1*i+3);
			group.addView(radio3);
			RadioButton radio4 = new RadioButton(this);
			radio4.setText("D");
			radio4.setId(id_c1*i+4);
			group.addView(radio4);
			group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				int id_c1 = GlobalData.id_constant1;
				@Override
				public void onCheckedChanged(RadioGroup arg0, int arg1) {
					// TODO Auto-generated method stub
					if((arg1%id_c1)==1) {
						Log.e("Debug info", "第"+arg1/id_c1+"题的"+"A clicked");
						as[arg1/id_c1] = 'A';
					}
					else if((arg1%id_c1)==2) {
						Log.e("Debug info", "第"+arg1/id_c1+"题的"+"B clicked");
						as[arg1/id_c1] = 'B';
					}
					else if(arg1%id_c1==3) {
						Log.e("Debug info", "第"+arg1/id_c1+"题的"+"C clicked");
						as[arg1/id_c1] = 'C' ;
					}
					else if(arg1%id_c1==4) {
						Log.e("Debug info", "第"+arg1/id_c1+"题的"+"D clicked");
						as[arg1/id_c1] = 'D';
					}
					
				}
			});
			
			ll.addView(tv);
			ll.addView(group);			
		}
		
		hi = new Button(this);
		hi.setOnClickListener(this);
		hi.setText("提交答案");
		hi.getBackground().setAlpha(32);
		
		ll.addView(hi);
		
		sv.addView(ll);
		
		this.setContentView(sv);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0 == hi) {
			// 提交答案
			Log.e("Debug info", "hi button clicked");
			new Thread() {
				public void run() {
					try {
						HttpClient httpClient = new DefaultHttpClient();
						HttpPost request = new HttpPost(GlobalData.baseUrl + "/smartClass/student/course/test/answer/");
						List<NameValuePair> paramList = new ArrayList<NameValuePair>();
						BasicNameValuePair param = new BasicNameValuePair("coursename", GlobalData.getCourse());
						BasicNameValuePair param1 = new BasicNameValuePair("testname", GlobalData.getTest());
						paramList.add(param);
						paramList.add(param1);
						for(int j=0; j<GlobalData.getTestNum(); j++) {
							BasicNameValuePair tmp = new BasicNameValuePair("answer"+(j+1), as[j]+"");
							paramList.add(tmp);
						}
						request.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
						
						if(null != GlobalData.sessionid) {
							request.setHeader("Cookie", "sessionid=" + GlobalData.sessionid);
						}
						
						HttpClient client = new DefaultHttpClient();
						HttpResponse httpResponse = client.execute(request);
					
						CookieStore mCookieStore = ((DefaultHttpClient)client).getCookieStore();
						List<Cookie> cookies = mCookieStore.getCookies();
						for(int i = 0; i < cookies.size(); i++) {
							if("sessionid".equals(cookies.get(i).getName())) {
								GlobalData.sessionid = cookies.get(i).getValue();
							}
						}
					}
					catch(Exception e) {
					}
				}
			}.start();
			try {
				Thread.currentThread().sleep(500);
			}
			catch(Exception e) {
			}
			Intent bintent = new Intent(com.example.classdaemon.AnswerQuestion.this, com.example.classdaemon.TestList.class);
			startActivityForResult(bintent, 0);		
		}
	}
}
