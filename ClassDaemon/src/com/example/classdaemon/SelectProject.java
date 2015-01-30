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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SelectProject extends Activity implements OnClickListener {
	private Spinner sp1;
	private Spinner sp2;
	private Spinner sp3;
	private Button hi;
	private List<String> questionlist = new ArrayList<String>();
	private List<String> questionlist2 = new ArrayList<String>();
	private String helpInfo = "";
	private TextView tv4;
	private TextView tv5;
	private TextView tv6;
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==GlobalData.success) {
				ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getBaseContext(), R.layout.array_item, questionlist);
				sp1.setAdapter(adapter1);
				sp2.setAdapter(adapter1);
				sp3.setAdapter(adapter1);
				
				sp1.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Log.e("item selected", arg0.getItemAtPosition(arg2).toString());
						GlobalData.project1 = questionlist.get(arg2).toString();
						tv4.setText(questionlist2.get(arg2).toString());
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						Log.e("item selected", arg0.getItemAtPosition(0).toString());	
						GlobalData.project1 = questionlist.get(0).toString();
						tv4.setText(questionlist2.get(0).toString());
					}
				});
				sp2.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Log.e("item selected", arg0.getItemAtPosition(arg2).toString());
						GlobalData.project2 = questionlist.get(arg2).toString();
						tv5.setText(questionlist2.get(arg2).toString());
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						Log.e("item selected", arg0.getItemAtPosition(0).toString());			
						GlobalData.project2 = questionlist.get(0).toString();
						tv5.setText(questionlist2.get(0).toString());
					}
				});
				sp3.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Log.e("item selected", arg0.getItemAtPosition(arg2).toString());
						GlobalData.project3 = questionlist.get(arg2).toString();
						tv6.setText(questionlist2.get(arg2).toString());
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						Log.e("item selected", arg0.getItemAtPosition(0).toString());		
						GlobalData.project3 = questionlist.get(0).toString();
						tv6.setText(questionlist2.get(0).toString());
					}
				});
			}
			else {
				Log.e("handler error", "...........");
			}
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectproject);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Toast.makeText(getApplicationContext(), "onCreate is called", Toast.LENGTH_LONG).show();
		
		questionlist.clear();
		questionlist2.clear();
		
		sp1 = (Spinner) findViewById(R.id.fuckspinner1);
		sp2 = (Spinner) findViewById(R.id.spinner2);
		sp3 = (Spinner) findViewById(R.id.spinner3);
		hi = (Button) findViewById(R.id.spbutton2);
		tv4 = (TextView) findViewById(R.id.textView4);
		tv5 = (TextView) findViewById(R.id.textView5);
		tv6 = (TextView) findViewById(R.id.textView6);
				
		hi.setOnClickListener(this);
		
		new Thread() {
			public void run() {
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost request = new HttpPost(GlobalData.baseUrl + "/smartClass/student/course/project/question/");
					List<NameValuePair> paramList = new ArrayList<NameValuePair>();
					BasicNameValuePair param = new BasicNameValuePair("projectname", GlobalData.getProject());
					BasicNameValuePair param2 = new BasicNameValuePair("coursename", GlobalData.getCourse());
					paramList.add(param);
					paramList.add(param2);
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
					
					String retSrc = EntityUtils.toString(httpResponse.getEntity());
					
					JSONObject result = new JSONObject(retSrc);
					Message message = new Message();
					if("1".equals(result.getString("result"))) {
						message.what = GlobalData.success;
						Log.e("message.message", result.getString("message"));
						Log.e("result", result.toString());
						Log.e("questionlist", result.getString("questionlist"));
						GlobalData.maxGroupSize = 0;
						for(int i = 0; i < result.getJSONArray("questionlist").length(); i++) {
							Log.e("JSONArray", result.getJSONArray("questionlist").getString(i));
							questionlist.add(result.getJSONArray("questionlist").getJSONArray(i).getString(0));
							questionlist2.add("最大人数:"+result.getJSONArray("questionlist").getJSONArray(i).getInt(1));
							if(GlobalData.maxGroupSize < result.getJSONArray("questionlist").getJSONArray(i).getInt(1)) {
								GlobalData.maxGroupSize = result.getJSONArray("questionlist").getJSONArray(i).getInt(1);
							}
						}
					handler.sendMessage(message);
					}
				}
				catch(Exception e) {
					
				}
			}
		}.start();
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		if(hi == arg0) {
			// 提交组队信息
			Log.e("Debuginfo", "点击了提交组队信息按钮");
			if(GlobalData.project1.equals(GlobalData.project2) || GlobalData.project1.equals(GlobalData.project2) || 
					GlobalData.project2.equals(GlobalData.project3)) {
					Toast.makeText(getApplicationContext(), "请不要选择重复的题目", Toast.LENGTH_LONG).show();
					return;
			}
			new Thread() {
				public void run() {
					try {
						HttpClient httpClient = new DefaultHttpClient();
						HttpPost request = new HttpPost(GlobalData.baseUrl + "/smartClass/student/course/project/groupinfo/");
						if(GlobalData.sessionid != null) {
							request.setHeader("Cookie", "sessionid=" + GlobalData.sessionid);
						}
						JSONObject param = new JSONObject();
						param.put("coursename", GlobalData.getCourse());
						param.put("projectname", GlobalData.getProject());
						JSONArray jsonArray1 = new JSONArray();
						jsonArray1.put(0, GlobalData.project1);
						jsonArray1.put(1, GlobalData.project2);
						jsonArray1.put(2, GlobalData.project3);
						JSONArray jsonArray2 = new JSONArray();
						for(int i = 0; i < GlobalData.teammate.size(); i++) {
							jsonArray2.put(i, GlobalData.teammate.get(i));
						}
						param.put("questionlist", jsonArray1);
						param.put("memberlist", jsonArray2);
						
						Log.e("提交的组队信息", param.toString());
						helpInfo += param.toString();
						StringEntity se = new StringEntity(param.toString(),"UTF-8");
						se.setContentType("application/json");
						se.setContentEncoding("UTF-8");
						request.setEntity(se);
//						request.setHeader("Content-Type", "application/json;charset=utf-8");
						Log.e("...", se.toString());
						
						HttpClient client = new DefaultHttpClient();
						HttpResponse httpResponse =  client.execute(request);
						
						CookieStore mCookieStore = ((DefaultHttpClient)client).getCookieStore();
						List<Cookie> cookies = mCookieStore.getCookies();
						for(int i = 0; i < cookies.size(); i++) {
							if("sessionid".equals(cookies.get(i).getName())) {
								GlobalData.sessionid = cookies.get(i).getValue();
							}
						}
						String retSrc = EntityUtils.toString(httpResponse.getEntity());
						JSONObject result = new JSONObject(retSrc);
						Log.e("message.message", result.getString("message"));
//						Toast.makeText(getBaseContext(), result.getString("message"), Toast.LENGTH_LONG);
						helpInfo += result.getString("message");
												
					}
					catch(Exception e){
					}
				}
			}.start();
			
			try {
				Thread.currentThread().sleep(500);
			}
			catch(Exception e) {
			}
			Toast.makeText(getApplicationContext(), helpInfo, Toast.LENGTH_LONG).show();
			Intent bintent = new Intent(this, com.example.classdaemon.OnlineWorkActivity.class);
			startActivityForResult(bintent, 0);	
		}
	}
	
	public void onResume() {
		super.onResume();
		Toast.makeText(getApplicationContext(), "onResume is called", Toast.LENGTH_LONG).show();
	}
}
