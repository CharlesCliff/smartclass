package com.example.classdaemon;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
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

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

public class EditProfile extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editprofile);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements OnClickListener {
		private TextView userName;
		private TextView registeredCourse;
		private EditText newCourse;
		private EditText oldPassWd;
		private EditText newPassWd;
		private EditText allCourse;
		private Button addNewCourseBtn;
		private Button changePassWdBtn;
		private EditText chosedCourse;
		private Button chosedCourseBtn;
		private Handler handler = new Handler() {
				public void handleMessage(Message msg)
				{
					if(msg.what==0x123)
					{//成功增加一门课程后刷新已注册课程
						registeredCourse.setText(msg.obj.toString());
					}
					else if(msg.what==0x124)
					{//未能成功更新课表
						Toast.makeText(getActivity(), "Sorry,未能成功更新课表", Toast.LENGTH_SHORT);
					}
					else if(msg.what==0x125)
					{//成功更新密码
						if("1".equals(msg.obj.toString()))
						{
							Toast.makeText(getActivity(), "成功更新密码", Toast.LENGTH_SHORT);
						}
						else//未能成功更新密码
							Toast.makeText(getActivity(), "Sorry, 未能成功更新密码", Toast.LENGTH_SHORT);
					}
					else if(msg.what==0x126)
					{//成功查询课表
						registeredCourse.setText(msg.obj.toString());	
					}
					else if(msg.what==0x127)
					{//未能成功查询课表
						Toast.makeText(getActivity(), "Sorry,查询课程列表失败", Toast.LENGTH_SHORT);
					}
					else if(msg.what==0x128)
					{
						allCourse.setText(msg.obj.toString());						
					}
				}
			};
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.editprofile_layout, container,
					false);
			return rootView;
		}
	    public void onActivityCreated(Bundle savedInstanceState) {  
	        super.onActivityCreated(savedInstanceState);  
		    userName = (TextView) getView().findViewById(R.id.textView1);  
			registeredCourse = (TextView) getView().findViewById(R.id.textView2);
			newCourse = (EditText) getView().findViewById(R.id.editText1);
			oldPassWd = (EditText) getView().findViewById(R.id.editText2);
			newPassWd = (EditText) getView().findViewById(R.id.editText3);
			addNewCourseBtn = (Button) getView().findViewById(R.id.nepbutton4);
			changePassWdBtn = (Button) getView().findViewById(R.id.button2);
			addNewCourseBtn.setOnClickListener(this);
			changePassWdBtn.setOnClickListener(this);
			userName.setText(GlobalData.getUserName());
			chosedCourse = (EditText) getView().findViewById(R.id.editText4);
			chosedCourseBtn = (Button) getView().findViewById(R.id.button3);
			allCourse = (EditText) getView().findViewById(R.id.editText5);
			
			chosedCourseBtn.setOnClickListener(this);

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
						Log.e("xxxxxxxxxxxxxxxxxxxxxxxxxx", result.getJSONArray("all_course_list").getString(0));
						
						Log.e("Ahahahahahahahahahahahaha",result.getString("all_course_list"));
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
						message.obj = result.getString("my_course_list");						
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
			if(arg0 == addNewCourseBtn) {
					// 添加一门新的课程并且成功之后要再重新更新一遍课程列表
					final String userNameValue = GlobalData.getUserName();
					final String newCourseValue = newCourse.getText().toString();
					new Thread() { 
						public void run() {
							try{

								HttpPost request = new HttpPost(GlobalData.baseUrl+"/smartClass/student/joincourse/");
								if(null != GlobalData.sessionid) {
									request.setHeader("Cookie", "sessionid=" + GlobalData.sessionid);
								}
								List<NameValuePair> paramList = new ArrayList<NameValuePair>();
								BasicNameValuePair param = new BasicNameValuePair("username", userNameValue);
								BasicNameValuePair param2 = new BasicNameValuePair("coursename", newCourseValue);								
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
							
							String retSrc = EntityUtils.toString(httpResponse.getEntity());
							JSONObject result = new JSONObject(retSrc);
							Message message = new Message();
							message.what = 0x126;
							message.obj = result.getString("my_course_list");						
							handler.sendMessage(message);
							
						}
						catch(Exception e){
						}
					}
				}.start();				
			}
			else if(arg0 == changePassWdBtn) {
				final String userNameValue = GlobalData.getUserName();
				new Thread() { 
					public void run() {
						try{
						HttpPost request = new HttpPost(GlobalData.baseUrl+"/smartClass/studentInfo/");
						// 先封装一个 JSON 对象
						JSONObject param = new JSONObject();
						param.put("username", userNameValue);
						param.put("oldpassword", oldPassWd.getText().toString());
						param.put("newpasswd", newPassWd.getText().toString());
						param.put("operation",  "changepasswd");
						// 绑定请求 Entry
						StringEntity se = new StringEntity(param.toString());
						se.setContentType("application/json");
						se.setContentEncoding("UTF-8");
						request.setEntity(se);
						// 发送请求
						HttpResponse httpResponse = new DefaultHttpClient().execute(request);
						// 得到应答的字符串，这也是一个 JSON 格式保存的数据
						String retSrc = EntityUtils.toString(httpResponse.getEntity());
						Log.e("Debug","useless");
						// 生成 JSON 对象
						JSONObject result = new JSONObject(retSrc);
						Message message = new Message();
						message.what = 0x125;
						message.obj = result.getString("successedchangepasswd");
						Log.e("Debug",result.getString("successedchangepasswd"));
						handler.sendMessage(message);
						}
						catch (Exception e) {
						}
					}
			}.start();				
			}
			else if(arg0 == chosedCourseBtn)
			{
				GlobalData.setCourse(chosedCourse.getText().toString());
				Intent bintent = new Intent(getActivity(), com.example.classdaemon.OnlineWorkActivity.class);
				startActivityForResult(bintent, 0);
			}
		}  
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		default:
			break;
		}
	}	
}
