package com.example.classdaemon;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//有网络模式
public class RegisterActivity extends ActionBarActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
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
		private EditText userName;
		private EditText passWd;
		private EditText type;
		private Button registerBtn;
		private String result;
		private Handler handler = new Handler() {
			public void handleMessage(Message msg)
			{
				if(msg.what==0x124)
				{
					result = msg.obj.toString();
					Log.e("Debug2", msg.obj.toString());
					if("1".equals(result)) {
						Intent bintent = new Intent(getActivity(), com.example.classdaemon.EditProfile.class);
						Bundle data = new Bundle();
						data.putString("userName", userName.getText().toString());
						bintent.putExtras(data);
						Log.e("Debug3","......");
						GlobalData.setUserName(userName.getText().toString());
						startActivityForResult(bintent, 0);
						Log.e("Debug4", "......");
					}
					else
					{
						Toast.makeText(getActivity(), "Sorry,注册失败。该用户名已经存在", Toast.LENGTH_SHORT).show();
					}
				}
			}			
		};
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.registeractivity_layout, container,
					false);
			return rootView;
		}
	    public void onActivityCreated(Bundle savedInstanceState) {  
	        super.onActivityCreated(savedInstanceState);  
	        userName=(EditText)getView().findViewById(R.id.editText1);
	        passWd=(EditText)getView().findViewById(R.id.editText2);
	        type=(EditText)getView().findViewById(R.id.editText3);
	        registerBtn=(Button)getView().findViewById(R.id.button1);
	        registerBtn.setOnClickListener(this);
	    }
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(arg0 == registerBtn) {
					final String userNameValue = userName.getText().toString();
					final String passWdValue = passWd.getText().toString();
					final String typeValue = type.getText().toString();
					new Thread() { 
						public void run() {
							try{
/*
 * 							HttpPost request = new HttpPost("http://182.92.169.74:8000/smartClass/register/");
							// 先封装一个 JSON 对象
							JSONObject param = new JSONObject();
							param.put("username", userNameValue);
							param.put("password", passWdValue);
							param.put("role", "student");
							// 绑定请求 Entry
							StringEntity se = new StringEntity(param.toString());
							se.setContentType("application/json");
							se.setContentEncoding("UTF-8");
							request.setEntity(se);
							// 发送请求	
*/
							HttpClient httpClient = new DefaultHttpClient();
							HttpPost request = new HttpPost("http://182.92.169.74:8000/smartClass/student/register/");
							request.addHeader("Cookie", GlobalData.cookie);
							List<NameValuePair> paramList = new ArrayList<NameValuePair>();
							BasicNameValuePair param = new BasicNameValuePair("username", userNameValue);
							BasicNameValuePair param2 = new BasicNameValuePair("password", passWdValue);
							
							paramList.add(param);
							paramList.add(param2);
							
							request.setEntity(new UrlEncodedFormEntity(paramList,HTTP.UTF_8));
							
								
							HttpResponse httpResponse = new DefaultHttpClient().execute(request);
							Header header = httpResponse.getFirstHeader("Set-Cookie");
							if(header!=null)
								GlobalData.cookie = header.getValue();
							// 得到应答的字符串，这也是一个 JSON 格式保存的数据
							String retSrc = EntityUtils.toString(httpResponse.getEntity());
							Log.e("Debug","useless");
							Log.e("Debug", userNameValue);
							Log.e("Debug", passWdValue);
							Log.e("Debug", typeValue);
							// 生成 JSON 对象
							JSONObject result = new JSONObject(retSrc);
							Message message = new Message();
							message.what = 0x124;
							message.obj = result.getString("result");
							Log.e("Debug",result.getString("result"));
							handler.sendMessage(message);
							}
							catch (Exception e) {
							}
						}
				}.start();
			}
		}  
	}		
	
}
