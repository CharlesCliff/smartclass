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
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
//登录界面
public class MainActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
		private Button loginBtn;
		private Button registerBtn;
		private EditText userName;
		private EditText passWd;
		private String result;
		private Handler handler = new Handler() {
				public void handleMessage(Message msg)
				{
					if(msg.what==GlobalData.success)
					{
						result = msg.obj.toString();
						if("1".equals(result)) {
							Intent bintent = new Intent(getActivity(), com.example.classdaemon.EditProfile.class);
							Bundle data = new Bundle();
							data.putString("userName", userName.getText().toString());
							bintent.putExtras(data);
							GlobalData.setUserName(userName.getText().toString());
							startActivityForResult(bintent, 0);
						}
					}
				}
			};
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	    public void onActivityCreated(Bundle savedInstanceState) {  
	        super.onActivityCreated(savedInstanceState);  
	    loginBtn = (Button) getView().findViewById(R.id.button2);  
		loginBtn.setOnClickListener(this);
		registerBtn = (Button) getView().findViewById(R.id.button1);
		registerBtn.setOnClickListener(this);
		userName = (EditText) getView().findViewById(R.id.editText1);
		passWd = (EditText) getView().findViewById(R.id.editText2);
	    }
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(arg0 == loginBtn) {
					final String userNameValue = userName.getText().toString();
					final String passWdValue = passWd.getText().toString();
					new Thread() { 
						public void run() {
							try{
								HttpClient httpClient = new DefaultHttpClient();
								HttpPost request = new HttpPost(GlobalData.baseUrl+"/smartClass/student/login/");
								List<NameValuePair> paramList = new ArrayList<NameValuePair>();
								BasicNameValuePair param = new BasicNameValuePair("username", userNameValue);
								BasicNameValuePair param2 = new BasicNameValuePair("password", passWdValue);
								
								paramList.add(param);
								paramList.add(param2);
								
								request.setEntity(new UrlEncodedFormEntity(paramList,HTTP.UTF_8));
								if(GlobalData.sessionid!=null) {
									request.setHeader("Cookie", "sessionid=" + GlobalData.sessionid);
								}
							
							HttpClient client = new DefaultHttpClient();
							// 发送请求
							HttpResponse httpResponse = client.execute(request);
							// 得到应答的字符串，这也是一个 JSON 格式保存的数据
							
							CookieStore mCookieStore = ((DefaultHttpClient)client).getCookieStore();
							List<Cookie> cookies = mCookieStore.getCookies();
							for(int i = 0; i < cookies.size(); i++) {
								if("sessionid".equals(cookies.get(i).getName())) {
									GlobalData.sessionid = cookies.get(i).getValue();
								}
							}
							
							String retSrc = EntityUtils.toString(httpResponse.getEntity());
							// 生成 JSON 对象
							JSONObject result = new JSONObject(retSrc);
							Message message = new Message();
							message.what = GlobalData.success;
							message.obj = result.getString("result");
							handler.sendMessage(message);
							}
							catch (Exception e) {
							}
						}
				}.start();
			}
			else if(arg0 == registerBtn) {
				Intent bintent = new Intent(getActivity(), com.example.classdaemon.RegisterActivity.class);
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
