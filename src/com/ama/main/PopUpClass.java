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
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class PopUpClass extends Activity{
	
	WallpaperManager wallPaperManager = null;
	Bitmap bitmap = null;
	File file3 = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_window);
		
	    DisplayMetrics displayMetrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	    int width = displayMetrics.widthPixels, height = displayMetrics.heightPixels;
	    getWindow().setLayout((int)(width * .7), (int)(height * .6));
		///////////////////////////////////////
	    Intent intent = getIntent();
	    bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
	    findViewById(R.id.setAsWalpaper).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Bitmap bm = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
//				Canvas canvas = new Canvas(bm);
//				v.draw(canvas);
				try {
					wallPaperManager = WallpaperManager.getInstance(PopUpClass.this);
					wallPaperManager.setBitmap(bitmap);
					Toast.makeText(getApplicationContext(), "Set successfully", Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	    ////////////////////////////////////////////////////////
	    findViewById(R.id.saveas).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				save(bitmap);
			}
		});
	    /////////////////////////////////////////////////
	    findViewById(R.id.share).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//save(bitmap);
				///shareBitmap(getApplicationContext(), bitmap, file3.getName());
				//Toast.makeText(getApplicationContext(), file3.getName(), Toast.LENGTH_SHORT).show();
				startShare(getApplicationContext(), bitmap);
			}
		});
	    
	    
	}
	
	private void save(Bitmap bitmap) {
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
		file3 = new File(fileName);
		try {
			fileOutputStream = new FileOutputStream(file3);
//			Bitmap bm = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
//			Canvas canvas = new Canvas(bm);
//			imageView.draw(canvas);
			//bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream); //100-best quality
			bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
			Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//refresh(file3);
		
	}
	
	
	private void refresh(File f) {
		Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		i.setData(Uri.fromFile(f));
		this.sendBroadcast(i);
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
	
	private void shareBitmap (Context context, Bitmap bitmap,String fileName) {
	    try {
//	        File file = new File(context.getCacheDir(), fileName + ".jpg");
//	        FileOutputStream fOut = new FileOutputStream(file);
//	        bitmap.compress(CompressFormat.PNG, 100, fOut);
//	        fOut.flush();
//	        fOut.close();
//	        file.setReadable(true, false);
	        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
	        //intent.setType("*/*");
	        //startActivity(intent);
	        //startActivity(Intent.createChooser(intent , "Share Via"));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	

}
