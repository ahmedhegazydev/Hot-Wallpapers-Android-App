package com.ama.main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.gpstutorial.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


@TargetApi(23)
public class Animals extends Fragment {
	
	TextView tv = null;
	ListView lv = null;
	Context ctxt = null;
	boolean boo = true;
	int[] ids = {R.drawable.lion0, R.drawable.lion1, R.drawable.lion2};
	Canvas canvas = null;
	ImageView iv = null;
	ScrollView sv = null;
    private int PICK_IMAGE_REQUEST = 1;
	LinearLayout layout2 = null;
	AlertDialog alertDialog  = null;
	AlertDialog.Builder builder = null;
	String myJson = null;
	ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.animals, container, false);
		RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.ll_animals);
		this.sv = (ScrollView) layout.findViewById(R.id.sv_anim);
		layout2 = (LinearLayout) sv.findViewById(R.id.child_anim);
		this.ctxt = view.getContext();
		
		addFewImages(ids);
		observeTree(ctxt);
		//initAds(this.ctxt, layout);
		
		return view;
	}
	
//	private void initAds(Context ctxt2, RelativeLayout layout) {
//		AdView adView = (AdView) layout.findViewById(R.id.adView);
//		AdRequest adRequest = new AdRequest.Builder()
//				//.addTestDevice("A9CE07A2C959A9C174254B84F5139414")
//				.build();
//		adView.loadAd(adRequest);
//				
//		
//	}

	ProgressBar progressBar = null;
	@SuppressLint("NewApi")
	private void observeTree(final Context context) {
		this.sv.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() {
			@Override
			public void onScrollChanged() {
				View view = (View) sv.getChildAt(sv.getChildCount() - 1);
			    int diff = (view.getBottom() - (sv.getHeight() + sv.getScrollY()));
			    // if diff is zero, then the bottom has been reached
			    if (diff == 0) {
			    	if (!isNetworkAvailable(context)) {
			    		builder = new AlertDialog.Builder(context)
						.setMessage("For more images, Connect to internet")
						.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {dialog.cancel();								
							}
						})
						.setIcon(R.drawable.warning)
						;
//			    		if( alertDialog != null && alertDialog.isShowing() ) return;
//						else {
//							alertDialog = builder.create();
//							alertDialog.setCanceledOnTouchOutside(false);
//							alertDialog.show();
//						}
			    		
			    		if (!layout2.getChildAt(layout2.getChildCount() - 1).equals(progressBar)) {
				    		layout2.addView(progressBar);
						}
					}else {
						if (boo) {
				    		if (layout2.getChildAt(layout2.getChildCount() - 1).equals(progressBar)) {
					    		layout2.removeView(layout2.getChildAt(layout2.getChildCount()-1));
							}
							getData("http://ahmedmohamedali.pe.hu/get_image2.php?table_name=animals", layout2.getContext());
							boo = false;
							
						}
					}
			    }
				
			}
		});
	}

	public boolean isNetworkAvailable(final Context context) {
	    final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
	    return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
	}
	
	private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    
	
	public void getData(final String scriptUrl, final Context ctxt){
        class GetDataJSON extends AsyncTask<String, Void, String>{
 
        	ProgressDialog progressDialog = null;
        	
        	@Override
        	protected void onProgressUpdate(Void... values) {
        		super.onProgressUpdate(values);
        	}
        	
        	@Override
        	protected void onPreExecute() {
        		super.onPreExecute();
        		
        		//Try this, a alternate way to show Dialog
        		//progressDialog = ProgressDialog.show(ctxt, "", "Fetching book oversight");
        		
        		progressDialog = new ProgressDialog(ctxt);
        		progressDialog.setTitle("Retrieving From Database");
        		progressDialog.setMessage("Please wait ...");
        		progressDialog.setCancelable(false);
        		progressDialog.setCanceledOnTouchOutside(false);
        		progressDialog.show();
        		
        		
        	}
        	
            @Override
            protected String doInBackground(String... params) {
            	DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                //DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(scriptUrl);
                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");
                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }
 
            @Override
            protected void onPostExecute(String result){
                myJson = result;
                if (progressDialog != null || progressDialog.isShowing()) {
					progressDialog.hide();
					progressDialog.dismiss();
					try {
						JSONArray array = new JSONArray(myJson);
						for (int i = 0; i < array.length(); i++) {
							JSONObject jsonObject = array.getJSONObject(i);
							String string = jsonObject.getString("img_data");
							//Toast.makeText(ctxt, string, Toast.LENGTH_SHORT).show();
							byte[] rawImage = Base64.decode(string, Base64.DEFAULT);
                            Bitmap bmp = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length); 
                           Animals.this.bitmaps.add(bmp);
                           
						}
						setBitmapsToIvs(bitmaps);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					
				}
                
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
    
    
	private void setBitmapsToIvs(ArrayList<Bitmap> bitmaps) {
		for (int i = 0; i < bitmaps.size(); i++) {
			 ImageView imageView = new ImageView(ctxt);
	         LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	         p.setMargins(0, 0, 0, 2);
	         imageView.setLayoutParams(p);
	         imageView.setScaleType(ScaleType.CENTER_CROP);
	         imageView.setImageBitmap(bitmaps.get(i));
	         imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//showPopup(ctxt, v);
						Intent i = new Intent(ctxt, ViewImage.class);
						bitmap = ((BitmapDrawable)((ImageView)v).getDrawable()).getBitmap();
						i.putExtra("BitmapImage", bitmap);
						startActivity(i);
					}
				});
	         Animals.this.layout2.addView(imageView);
		}
	}
	
    Bitmap bitmap = null;
	private void addFewImages(int[] ids) {
		for (int i = 0; i < ids.length; i++) {
			ImageView imageView = new ImageView(ctxt);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            p.setMargins(0, 0, 0, 2);
            imageView.setLayoutParams(p);
			imageView.setImageResource(ids[i]);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//showPopup(ctxt, v);
					Intent i = new Intent(ctxt, ViewImage.class);
					bitmap = ((BitmapDrawable)((ImageView)v).getDrawable()).getBitmap();
					i.putExtra("BitmapImage", bitmap);
					startActivity(i);
				}
			});
			layout2.addView(imageView);
		}
		if (!isNetworkAvailable(ctxt)) {
			progressBar = new ProgressBar(ctxt, null, android.R.attr.progressBarStyleSmall);
			progressBar.setTag("progressBar");
			layout2.addView(progressBar);
		}
		
	}
	
	
	
	
}
