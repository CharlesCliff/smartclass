package com.example.classdaemon;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
//ÓÐÍøÂçÄ£Ê½
public class OnlineWorkActivity extends ActionBarActivity {
	static private String userName;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_onlinework);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		userName = GlobalData.getUserName();
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
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
		private Button signUpBtn;
		private Button answerQuestionBtn;
		private Button askQuestionBtn;
		private Button groupBtn;
		private TextView userId;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.onlineworkactivity_layout, container,
					false);
			return rootView;
		}
	    public void onActivityCreated(Bundle savedInstanceState) {  
	        super.onActivityCreated(savedInstanceState);  
	    signUpBtn = (Button) getView().findViewById(R.id.nepbutton4);  
		signUpBtn.setOnClickListener(this);
		answerQuestionBtn = (Button) getView().findViewById(R.id.button2);
		answerQuestionBtn.setOnClickListener(this);
		askQuestionBtn = (Button) getView().findViewById(R.id.button3);
		askQuestionBtn.setOnClickListener(this);
		groupBtn = (Button) getView().findViewById(R.id.button4);
		groupBtn.setOnClickListener(this);
		userId = (TextView) getView().findViewById(R.id.textView1);
		userId.setText(userName);
	    }

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(arg0 == signUpBtn) {
				
			}
			else if(arg0 == answerQuestionBtn) {
				Intent bintent = new Intent(getActivity(), com.example.classdaemon.TestList.class);
				startActivityForResult(bintent, 0);
			}
			else if(arg0 == askQuestionBtn) {
				Intent bintent = new Intent(getActivity(), com.example.classdaemon.AskQuestion.class);
				startActivityForResult(bintent, 0);				
			}
			else if(arg0 == groupBtn) {
				Intent bintent = new Intent(getActivity(), com.example.classdaemon.Group.class);
				startActivityForResult(bintent, 0);				
			}
		}
	}	
	
}
