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
import org.apache.http.util.EntityUtils;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewEditProfile extends Activity implements OnClickListener  {
	private TextView userName;
	private Button changePasswd;
	private Button selectCourse;
	private Button addCourse;
	private Button deleteCourse;
	private Spinner unselectedCourse;
	private Spinner myCourse;
	private Spinner myCourse2;
	private List<String> allCourselist = new ArrayList<String>();
	private List<String> myCourselist = new ArrayList<String>();
	private List<String> unselectedCourselist = new ArrayList<String>();
	private String selectedCourse;
	private String courseToAdd;
	private String courseToDelete;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg)
		{
			if(msg.what==0x124)
			{//未能成功更新课表
				Toast.makeText(getBaseContext(), "Sorry,未能成功更新课表", Toast.LENGTH_SHORT);
			}
			else if(msg.what==0x125)
			{//成功更新密码
			}
			else if(msg.what==0x126)
			{//成功查询课表
				ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getBaseContext(), R.layout.selected_course_item, myCourselist);
				myCourse.setAdapter(adapter1);
				
				ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getBaseContext(), R.layout.todelete_course_item, myCourselist);
				myCourse2.setAdapter(adapter3);

				unselectedCourselist.clear();
				for(int i = 0; i < allCourselist.size(); i++) {
					if(myCourselist.indexOf(allCourselist.get(i))==-1) {
						unselectedCourselist.add(allCourselist.get(i));
					}
				}				
				
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getBaseContext(), R.layout.unselected_course_item, unselectedCourselist);
				unselectedCourse.setAdapter(adapter2);				
			}
			else if(msg.what==0x127)
			{//未能成功查询课表
				Toast.makeText(getBaseContext(), "Sorry,查询课程列表失败", Toast.LENGTH_SHORT);
			}
			else if(msg.what==0x128)
			{
				unselectedCourselist.clear();
				Log.e("classdaemon.neweditprofile", "location1"+allCourselist.toString());
				Log.e("classdaemon.neweditprofile", "location1"+allCourselist.toString());

				for(int i = 0; i < allCourselist.size(); i++) {
					if(myCourselist.indexOf(allCourselist.get(i))==-1) {
						unselectedCourselist.add(allCourselist.get(i));
					}
				}
				Log.e("classdaemon.neweditprofile", ""+unselectedCourselist.size());
				ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getBaseContext(), R.layout.unselected_course_item, unselectedCourselist);
				unselectedCourse.setAdapter(adapter1);
			}
		}
	};	
	
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.neweditprofile);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		allCourselist.clear();
		myCourselist.clear();
		
		userName = (TextView) findViewById(R.id.neptextView1);
		userName.setText(GlobalData.getUserName());
		changePasswd = (Button) findViewById(R.id.nepbutton2);
		selectCourse = (Button) findViewById(R.id.nepbutton1);
		addCourse = (Button) findViewById(R.id.nepbutton4);
		deleteCourse = (Button) findViewById(R.id.fuckbutton);
		unselectedCourse = (Spinner) findViewById(R.id.nepspinner2);
		myCourse = (Spinner) findViewById(R.id.nepspinner1);
		myCourse2 = (Spinner) findViewById(R.id.nepspinner3);
		
		changePasswd.setOnClickListener(this);
		selectCourse.setOnClickListener(this);
		addCourse.setOnClickListener(this);
		deleteCourse.setOnClickListener(this);
		
		unselectedCourse.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				courseToAdd = unselectedCourselist.get(arg2).toString();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				if(unselectedCourselist.size() >= 0) {
					courseToAdd = unselectedCourselist.get(0).toString();
				}
			}
		});
		
		myCourse.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				selectedCourse = myCourselist.get(arg2).toString();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				selectedCourse = myCourselist.get(0).toString();
			}
		});
		
		myCourse2.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				courseToDelete = myCourselist.get(arg2).toString();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				courseToDelete = myCourselist.get(0).toString();
			}
		});

		new Thread(){
			public void run() {
				try{
					
					HttpPost request = new HttpPost(GlobalData.baseUrl+"/smartClass/student/allcourse/");
					//request.addHeader("Cookie", GlobalData.cookie);
					if(null!=GlobalData.sessionid)
					{
						request.setHeader("Cookie", "sessionid"+"="+GlobalData.sessionid);
						Log.e("session look up",GlobalData.sessionid);
					}
					List<NameValuePair> paramList = new ArrayList<NameValuePair>();
					BasicNameValuePair param = new BasicNameValuePair("username", GlobalData.getUserName());
					
					
					paramList.add(param);
					
					request.setEntity(new UrlEncodedFormEntity(paramList,HTTP.UTF_8));						
					HttpClient httpClient = new DefaultHttpClient();
					
					HttpResponse httpResponse = httpClient.execute(request);
					
					CookieStore mCookieStore = ((DefaultHttpClient)httpClient).getCookieStore();
					List<Cookie> cookies = mCookieStore.getCookies();
					for(int i=0; i<cookies.size(); i++)
					{
						if("sessionid".equals(cookies.get(i).getName()))
						{
							GlobalData.sessionid = cookies.get(i).getValue();
							Log.w("let;s see the sessionid ",GlobalData.sessionid);
							break;
						}
					}
					
					String retSrc = EntityUtils.toString(httpResponse.getEntity());
					JSONObject result = new JSONObject(retSrc);
					Message message = new Message();
					message.what = 0x128;
					Log.e("classdaemon.neweditprofile", result.getJSONArray("all_course_list").getString(0));
					
					allCourselist.clear();
					Log.e("classdaemon.neweditprofile",result.getString("all_course_list"));
					for(int i = 0; i < result.getJSONArray("all_course_list").length(); i++) {
						allCourselist.add(result.getJSONArray("all_course_list").getString(i));
					}
					message.obj = result.getString("all_course_list");
					handler.sendMessage(message);
				}
				catch(Exception e){
				}
			}
		}.start();			
		
		new Thread(){
			public void run() {
				try{
					HttpPost request = new HttpPost(GlobalData.baseUrl+"/smartClass/student/mycourse/");
					List<NameValuePair> paramList = new ArrayList<NameValuePair>();
					BasicNameValuePair param = new BasicNameValuePair("username", GlobalData.getUserName());				
					paramList.add(param);
					request.setEntity(new UrlEncodedFormEntity(paramList,HTTP.UTF_8));		
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
							break;
						}
					}
					
					String retSrc = EntityUtils.toString(httpResponse.getEntity());
					JSONObject result = new JSONObject(retSrc);
					Message message = new Message();
					message.what = 0x126;
					myCourselist.clear();
					message.obj = result.getString("my_course_list");		
					for(int i = 0; i < result.getJSONArray("my_course_list").length(); i++) {
						myCourselist.add(result.getJSONArray("my_course_list").getString(i));
					}
					handler.sendMessage(message);
					
				}
				catch(Exception e){
				}
			}
		}.start();
			
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(selectCourse == arg0) {
			GlobalData.setCourse(selectedCourse);
			Intent bintent = new Intent(getBaseContext(), com.example.classdaemon.OnlineWorkActivity.class);
			startActivityForResult(bintent, 0);
		}
		else if(addCourse == arg0) {
			if(courseToAdd==null) {
				Toast.makeText(getApplicationContext(), "没有课程可以添加", Toast.LENGTH_LONG).show();
				Log.e("classdaemon.neweditprofile", ""+courseToAdd);
				return;
			}
			// 添加一门新的课程并且成功之后要再重新更新一遍课程列表
			final String userNameValue = GlobalData.getUserName();
			new Thread() { 
				public void run() {
					try{

						HttpPost request = new HttpPost(GlobalData.baseUrl+"/smartClass/student/joincourse/");
						if(null != GlobalData.sessionid) {
							request.setHeader("Cookie", "sessionid=" + GlobalData.sessionid);
						}
						List<NameValuePair> paramList = new ArrayList<NameValuePair>();
						BasicNameValuePair param = new BasicNameValuePair("username", userNameValue);
						BasicNameValuePair param2 = new BasicNameValuePair("coursename", courseToAdd);								
						paramList.add(param);
						paramList.add(param2);
						request.setEntity(new UrlEncodedFormEntity(paramList,HTTP.UTF_8));	
						
					// 发送请求
					HttpClient client = new DefaultHttpClient();
					HttpResponse httpResponse = client.execute(request);
					CookieStore mCookieStore = ((DefaultHttpClient)client).getCookieStore();
					List<Cookie> cookies = mCookieStore.getCookies();
					for(int i = 0; i < cookies.size(); i++) {
						if("sessionid".equals(cookies.get(i).getName())) {
							GlobalData.sessionid = cookies.get(i).getValue();
							break;
						}
					}

					// 得到应答的字符串，这也是一个 JSON 格式保存的数据
					String retSrc = EntityUtils.toString(httpResponse.getEntity());
					Log.e("Debug","useless");
					// 生成 JSON 对象
					JSONObject result = new JSONObject(retSrc);
				}
				catch (Exception e)
				{
				}
			}
		}.start();
		
		try {
			Thread.currentThread().sleep(500);
		}
		catch(Exception e) {
		}
		
		new Thread(){
			public void run() {
				try{
					HttpPost request = new HttpPost(GlobalData.baseUrl+"/smartClass/student/mycourse/");
					List<NameValuePair> paramList = new ArrayList<NameValuePair>();
					BasicNameValuePair param = new BasicNameValuePair("username", GlobalData.getUserName());				
					paramList.add(param);
					request.setEntity(new UrlEncodedFormEntity(paramList,HTTP.UTF_8));		
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
							break;
						}
					}
					
					myCourselist.clear();
					String retSrc = EntityUtils.toString(httpResponse.getEntity());
					JSONObject result = new JSONObject(retSrc);
					Message message = new Message();
					message.what = 0x126;
					message.obj = result.getString("my_course_list");		
					for(int i = 0; i < result.getJSONArray("my_course_list").length(); i++) {
						myCourselist.add(result.getJSONArray("my_course_list").getString(i));
					}
					handler.sendMessage(message);
					
				}
				catch(Exception e){
				}
			}
		}.start();						
		}
		else if(deleteCourse == arg0) {
			
		}
		else if(changePasswd == arg0) {
		}
		
	}

}
