package info.kgomes.foodorder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.protocol.ResponseServer;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import info.kgomes.helper.*;

public class MenuItemDetailsActivity extends Activity {	
	private static final String TAG = "download";
	String selectedMenuItem = null;
	String selectedSize = null;
	Double price = null;	
	private long myDownloadReference;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    int layout = R.layout.activity_menuitem1;
	    Bundle extras = getIntent().getExtras();	    
	    if(extras != null) {
	    	if(extras.getString("menu_activity_to_display") != null) {
	    		layout = Integer.parseInt(extras.getString("menu_activity_to_display"));
	    	}
	    	if(extras.getString("menu_item_selected") != null) {
	    		selectedMenuItem = extras.getString("menu_item_selected");
	    	}
	    }
	    setContentView(layout);
	    
	    /*
	    MenuItemDetails[] data = {
	    	new MenuItemDetails("a", "abc")
	    };
	    MenuItemDetailsAdapter adapter = new MenuItemDetailsAdapter(this, R.layout.menuitemdetails_row, data); 
	    ListView menuDetailsView = (ListView) findViewById(R.id.menuDetailsView);
	    menuDetailsView.setAdapter(adapter);
	    */
	    
	    
	}

	public void onMenuItemCancel(View view) {
		Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        setResult(RESULT_CANCELED, myIntent);
        finish();
	}
	
	public void onMenuItemOrder(View view) {
		if(selectedMenuItem != null) {
			Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);	
			myIntent.putExtra("selectedMenuItem", selectedMenuItem);
			myIntent.putExtra("size", selectedSize+"");
			myIntent.putExtra("price", "$"+price);
			myIntent.putExtra("price_double", ""+price);
			setResult(RESULT_OK, myIntent);
			finish();
		} else {
			Toast.makeText(MenuItemDetailsActivity.this, "Please select size.", Toast.LENGTH_SHORT);
		}
	}

	public void onMenuItemDownload(View view) {
		String myFeed = getString(R.string.my_feed);
		String message = "";
		
		try {
			String serviceString = Context.DOWNLOAD_SERVICE;
			DownloadManager dm = (DownloadManager) getSystemService(serviceString);
			Uri uri = Uri.parse(myFeed);
			DownloadManager.Request request = new Request(uri);
			request.setTitle("SushiMenu");
			request.setDescription("Downloading Sushi Menu");
			request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SushiMenu.pdf");
			 
			long reference = dm.enqueue(request);
			myDownloadReference = reference;
			
			message = "Initiating download..";
		} catch(Exception e) {
			Log.d(TAG, "Other Exception. "+e.getMessage());
			message = "Other Exception. "+e.getMessage();			
		}
		
		Toast.makeText(MenuItemDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
	}
	
	public void onSizeClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();
	    if(checked) {
	    	switch(view.getId()) {
	    	case R.id.menuItem1_small:
	    	case R.id.menuItem2_small:
	    	case R.id.menuItem3_small:
	    	case R.id.menuItem4_small:
	    		selectedSize = "small";
	    		price = 20.00;
	    		break;
	    	case R.id.menuItem1_medium:
	    	case R.id.menuItem2_medium:
	    	case R.id.menuItem3_medium:
	    	case R.id.menuItem4_medium:
	    		selectedSize = "medium";
	    		price = 30.00;
	    		break;
	    	case R.id.menuItem1_large:
	    	case R.id.menuItem2_large:
	    	case R.id.menuItem3_large:
	    	case R.id.menuItem4_large:
	    		selectedSize = "large";
	    		price = 37.00;
	    		break;
	    	}
	    }
	}	
}
