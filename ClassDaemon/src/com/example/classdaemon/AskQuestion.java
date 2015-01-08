package com.example.classdaemon;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//用于提问的界面
public class AskQuestion extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_askquestion);

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
		private EditText question;
		private Button askBtn;
		private Handler handler = new Handler() {
				public void handleMessage(Message msg)
				{
					if(msg.what==0x123)
					{
						Toast.makeText(getActivity(), "已成功提交问题", Toast.LENGTH_SHORT);
					}
					else if(msg.what==0x124)
					{
						Toast.makeText(getActivity(), "Sorry,未成功提交问题", Toast.LENGTH_SHORT);
					}
				}
			};
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.askquestion_layout, container,
					false);
			return rootView;
		}
	    public void onActivityCreated(Bundle savedInstanceState) {  
	        super.onActivityCreated(savedInstanceState);  
	        question=(EditText) getView().findViewById(R.id.editText1);
	        askBtn = (Button) getView().findViewById(R.id.button1);
	        askBtn.setOnClickListener(this);
	    }
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(arg0 == askBtn) {
					final String userNameValue = GlobalData.getUserName();
					final String course = GlobalData.getCourse();
					final String questionValue = question.getText().toString();
					new Thread() { 
						public void run() {
							try{
							HttpPost request = new HttpPost("http://182.92.169.74:8000/smartClass/askQuestion/");
							// 先封装一个 JSON 对象
							JSONObject param = new JSONObject();
							param.put("username", userNameValue);
							param.put("course", course);
							param.put("role", "student");
							param.put("question", questionValue);
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
							if("1".equals(result.getString("successedask")))
								message.what = 0x123;
							else
								message.what = 0x124;
							handler.sendMessage(message);
							}
							catch (Exception e) {
							}
						}
				}.start();
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
