package com.ama.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.gpstutorial.R;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class ViewImage extends /*Activity*/ActionBarActivity {
	
	RelativeLayout rl = null;
	Bundle bundle = null;
	Intent intent = null;
	RelativeLayout.LayoutParams params = null;
	ImageView iv = null;
	Context context = null;
	Bitmap bitmap = null;
	WallpaperManager wallpaperManager = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		init();
		
		
	}
	
	
	private void init() {
		
		context = ViewImage.this;
		rl = new RelativeLayout(context);
		params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rl.setLayoutParams(params);
		this.setContentView(rl);
		///////////////////////////////////////////////////////////
		
		ActionBar actionBar = this.getSupportActionBar();
		assert actionBar != null;
		actionBar.setDisplayHomeAsUpEnabled(true);
		//actionBar.setIcon(R.drawable.ic_action_back);
		
		/////////////////////////////////////////////////////////
		//bundle = savedInstanceState;
		intent = getIntent();
		bitmap = intent.getParcelableExtra("BitmapImage");
		iv = new ImageView(context);
		iv.setLayoutParams(params);
		iv.setScaleType(ScaleType.CENTER_CROP);
		iv.setImageBitmap(bitmap);
		rl.addView(iv);
		//////////////////////////////////////////////////////////
//		LinearLayout layout = new LinearLayout(context);
//		layout.setGravity(Gravity.RIGHT);
//		layout.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 
//				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
//		final ImageView iv_options = new ImageView(context);
//		iv_options.setScaleType(ScaleType.CENTER_CROP);
//		iv_options.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 
//				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
//		iv_options.setImageResource(R.drawable.options);
//		layout.addView(iv_options);
//		rl.addView(layout);
//		iv_options.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				showPopup(context, v);
//			}
//		});
		
	}
	
	
	@SuppressLint("NewApi")
	private void showPopup(final Context context, View anchor) {
		PopupMenu menu = new PopupMenu(context, anchor);
		menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.setWallPaper:
					setAsWallpaper(context, bitmap);
					break;
				case R.id.share:
					startShare(context, bitmap);
					break;
				case R.id.Saveas:
					saveAs(bitmap);
					break;
				}
				return false;
			}
		});
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.my_popup, menu.getMenu());
		menu.show();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.SetAsWallPaper:
			setAsWallpaper(context, bitmap);
			break;
		case R.id.save:
			saveAs(bitmap);
			break;
		case R.id.share:
			startShare(context, bitmap);
			break;
		case android.R.id.home:
			onBackPressed();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void setAsWallpaper(Context context, Bitmap bitmap) {
//		Bitmap bm = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
//		Canvas canvas = new Canvas(bm);
//		v.draw(canvas);
		try {
			wallpaperManager = WallpaperManager.getInstance(context);
			wallpaperManager.setBitmap(bitmap);
			Toast.makeText(context, "Set successfully", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveAs(Bitmap bitmap) {
		File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		File file2 = new File(file, "My Images");
		FileOutputStream fileOutputStream = null;
		if (!file2.exists()) {
			file2.mkdir();
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
		String date = simpleDateFormat.format(new Date());
		String name = "Img" + date + ".jpg";
		String fileName = file2.getAbsolutePath() + "/" + name;
		File file3 = new File(fileName);
		try {
			fileOutputStream = new FileOutputStream(file3);
//			Bitmap bm = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
//			Canvas canvas = new Canvas(bm);
//			imageView.draw(canvas);
			//bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream); //100-best quality
			bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
			Toast.makeText(getApplicationContext(), "Saved at " + file2.getAbsolutePath(), 
					Toast.LENGTH_LONG).show();
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//refresh(file3);
		
	}
	
	@SuppressLint("SdCardPath")
	private void startShare(Context context, Bitmap bitmap) {
		Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, arrayOutputStream);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator+"ImageDemo.jpg");
        if (file.exists()) {
			file.delete();
			file = new File(Environment.getExternalStorageDirectory() + File.separator+"ImageDemo.jpg");
		}
        try {
			file.createNewFile();
			@SuppressWarnings("resource")
			FileOutputStream fileOutputStream = new FileOutputStream(file);
	        fileOutputStream.write(arrayOutputStream.toByteArray());
	        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/ImageDemo.jpg"));
	        startActivity(Intent.createChooser(intent, "Share Image"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}