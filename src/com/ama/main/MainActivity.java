package com.ama.main;

import java.util.ArrayList;

import com.example.gpstutorial.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TabHost;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity/*TabActivity*/implements 
ViewPager.OnPageChangeListener{
	
	//animals_images babies_images
	
	ActionBar actionBar = null;
	TabHost th = null;
	String indicators[] = {"Animals", "Babies", "Birds", "BirthDays", "Cartoons", 
	"Desktop", "Fishes", "Flowers", "Funny", "Horror", "Islamic", "Love", 
	"Movies Personalities", "Nature"
	};
	@SuppressWarnings("rawtypes")
//	Class classes[] = {Animals.class, Birds.class, Love.class, 
//			Horror.class, Moving.class, Flowers.class, Islamic.class, Nature.class
//			, Desktop.class};
	ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	ViewPager viewPager = null;
	FragmentPageAdapter fragmentPageAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
//		this.th = this.getTabHost();
		checkWifi();
		init();
	}
	
	

	@Override
	protected void onResume() {
		super.onResume();
		
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		
	}


	private void init() {
		
		this.actionBar = this.getActionBar();
		this.viewPager = (ViewPager) this.findViewById(R.id.pager);
		this.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (int i = 0; i < indicators.length; i++) {
			//this.actionBar.addTab(actionBar.newTab().setText(indicators[i]).setTabListener(this));
			this.actionBar.addTab(actionBar.newTab().setText(indicators[i]).setTabListener(new TabListener() {
				
				@Override
				public void onTabUnselected(Tab tab, FragmentTransaction ft) {
					
				}
				@Override
				public void onTabSelected(Tab tab, FragmentTransaction ft) {
					viewPager.setCurrentItem(tab.getPosition());
				}
				@Override
				public void onTabReselected(Tab tab, FragmentTransaction ft) {
					
				}
			}));
		}
//		this.actionBar.addTab(actionBar.newTab().setText("Animals").setTabListener(this));
//		this.actionBar.addTab(actionBar.newTab().setText("Flowers").setTabListener(this));
//		
		fragments.add(new Animals());
		fragments.add(new Babies());
		fragments.add(new Birds());
		fragments.add(new BirthDay());
		fragments.add(new Cartoon());
		fragments.add(new Desktop());
		fragments.add(new Fishes());
		fragments.add(new Flowers());
		fragments.add(new Funny());
		fragments.add(new Horror());
		fragments.add(new Islamic());
		fragments.add(new Love());
		fragments.add(new MoviesPersonalities());
		fragments.add(new Nature());
		
		this.fragmentPageAdapter  = new FragmentPageAdapter(this.getSupportFragmentManager(), this.fragments);
		
		this.viewPager.setAdapter(fragmentPageAdapter);
		this.viewPager.setOnPageChangeListener(this);
		
		
	}
	

	///////////////////////////////////////////

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}


	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}


	@Override
	public void onPageSelected(int arg0) {
		this.actionBar.setSelectedNavigationItem(arg0);
	}


	
	
	
	
//	private void init() {
//		for (int i = 0; i < indicators.length; i++) {
//			TabSpec spec = th.newTabSpec(indicators[i]);
//			spec.setIndicator(indicators[i]);
//			spec.setContent(new Intent(MainActivity.this, classes[i]));
//			this.th.addTab(spec);
//		}
//	}

	
	class GridViewAdapter extends BaseAdapter{
		
//		public GridViewAdapter(Context context, ) {
//			// TODO Auto-generated constructor stub
//		}
		
		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return null;
		}
	}
	
	
	public boolean isNetworkAvailable(final Context context) {
	    final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
	    return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
	}
	
	private void checkWifi() {
		final WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		if (!wifi.isWifiEnabled()){
			new AlertDialog.Builder(MainActivity.this)
			.setTitle("Error")
			.setMessage("Wifi is disabled, Do u want to enable it?")
			.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					//startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
					wifi.setWifiEnabled(true);//Turn off Wifi
					
				}
			})
			.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
			.show();
			
		}else {
			checkInternetConn();
		}

	}
	
	private void checkInternetConn() {
		if (!isNetworkAvailable(MainActivity.this)) {
			new AlertDialog.Builder(MainActivity.this)
			.setNeutralButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
			.setTitle("Error")
			.setMessage("No internet connection")
			.show();
		}
	}
	
}
