package info.kgomes.foodorder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;  
import android.provider.ContactsContract.CommonDataKinds.Email;  
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import info.kgomes.foodorder.db.*;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {	
	protected static final String TAG = "Main Activity Download Receiver";

	ArrayList<ItemOrdered> itemsOrderedAL = new ArrayList<ItemOrdered>();
	int REQUEST_CODE_MENUITEM_SIZE = 1;
	int REQUEST_CODE_CONTACT_PICKER = 2;
	private ItemsOrderedDAO datasource;
	int selectedMenuItemId;
	int itemsOrdered = 0;
	StringBuilder sb = new StringBuilder();
	ItemOrdered orderItemToSetContact = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_main);		
		
		//SQLite
		datasource = new ItemsOrderedDAO(this);
	    datasource.open();	    
	    if(savedInstanceState == null) {
			datasource.deleteAllItemsOrdered();
    	} else {
    		itemsOrderedAL = (ArrayList<ItemOrdered>) datasource.getAllItemsOrdered();
    		itemsOrdered = itemsOrderedAL.size();
    	}
	    displayOrderedItemsFromSQLite();

	    ListView itemsOrderedView = (ListView) findViewById(R.id.items_ordered_listview);	    	    	    
	    itemsOrderedView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {	        	
	        	if(position < itemsOrdered) {
	        		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);  
	        		startActivityForResult(contactPickerIntent, REQUEST_CODE_CONTACT_PICKER);
	        		ItemOrdered item = itemsOrderedAL.get(position);
	        		orderItemToSetContact = item;	        		
	        	} else {
	        		orderItemToSetContact = null;
	        	}
	        }
	    });
	    
	    //Linkify
		final TextView emailRestaurantTextView = (TextView)findViewById(R.id.emailRestaurant);		
		Linkify.addLinks(emailRestaurantTextView, Linkify.EMAIL_ADDRESSES);
		
		//Download Manager
		final DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);	    
	    IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);	        
	    BroadcastReceiver receiver = new BroadcastReceiver() {
	      @Override
	      public void onReceive(Context context, Intent intent) {
	    	  String action = intent.getAction();
	    	  if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
	    		  long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
	    		  Query myDownloadQuery = new Query();
		          myDownloadQuery.setFilterById(reference);
		          Cursor myDownload = downloadManager.query(myDownloadQuery);
		          if (myDownload.moveToFirst()) {
		        	  int fileNameIdx = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
		        	  int fileUriIdx = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
		        	  
		        	  String fileName = myDownload.getString(fileNameIdx);
		        	  String fileUri = myDownload.getString(fileUriIdx);
		        	  
		        	  // TODO Do something with the file.
		        	  //Log.d(TAG, fileName + " : " + fileUri);
		        	  Toast.makeText(MainActivity.this, "Download Completed! " + fileName + " : " + fileUri, Toast.LENGTH_LONG).show();
			      }
	    	  }
	      }
	    };	        
	    registerReceiver(receiver, filter);
	}
	
	public void displayOrderedItemsFromSQLite() {
		try {
			//datasource.open();
			List<ItemOrdered> values = datasource.getAllItemsOrdered();
			ArrayList<String> itemsList = new ArrayList<String>();
			int i = 0;
			for (i = 0; i < values.size(); ++i) {
				ItemOrdered a = (ItemOrdered) values.get(i);
				if(!a.getOrder_time().isEmpty() && !a.getContact_number().isEmpty()) {
					itemsList.add(a.getId()+". "+a.getItem_name()+" - $"+a.getPrice()+" ["+a.getOrder_time()+"] Contact: "+a.getContact_name()+", "+a.getContact_number());
				} else {
					itemsList.add(a.getId()+". "+a.getItem_name()+" - $"+a.getPrice()+" ["+a.getOrder_time()+"] Set Contact!");
				}
			}		
			itemsOrdered = i;
			if(itemsList.size() == 0) {
				itemsList.add("No items ordered!");
			} else {
				itemsList.add(i+" orders placed");
			}
			
			ListView itemsOrderedView = (ListView) findViewById(R.id.items_ordered_listview);
			
			//ItemsOrderedAdapter adapter = new ItemsOrderedAdapter(this, R.layout.listview_itemordered_row, values);
			StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, itemsList);						
			itemsOrderedView.setAdapter(adapter);
		} catch(Exception e) {
			Toast.makeText(MainActivity.this, "Unable to load data from SQL.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {		
		super.onActivityResult(requestCode, resultCode, intent);
	    String itemName = "", orderTime, contactName = "", contactNumber = "";
	    Double price = 0.00;
	    	    
	    //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
	    Calendar cal = Calendar.getInstance();	    
	    orderTime = dateFormat.format(cal.getTime());
	    
	    if (resultCode == RESULT_OK) {
	    	if(requestCode == REQUEST_CODE_MENUITEM_SIZE) {
	    		if(intent.getStringExtra("selectedMenuItem") != null) {
	    			itemName = intent.getStringExtra("selectedMenuItem").toString();
	    			sb.append("\n"+intent.getStringExtra("selectedMenuItem").toString());
	    		}
	    		if(intent.getStringExtra("size") != null) {
	    			sb.append(" - "+intent.getStringExtra("size").toString());
	    		}
	    		if(intent.getStringExtra("price") != null) {
	    			price = Double.parseDouble(intent.getStringExtra("price_double").toString());
	    			sb.append(" - "+intent.getStringExtra("price").toString());
	    		}
	    	 
	    		//TextView menuOrdered = (TextView) findViewById(R.id.menuOrdered);
	    		//menuOrdered.setText(sb.toString());
	    		ItemOrdered item = datasource.createItemOrdered_Rec(itemName, price, orderTime, contactName, contactNumber);
	    		itemsOrderedAL.add(item);
	    		
	    		displayOrderedItemsFromSQLite();
	    	} else if(requestCode == REQUEST_CODE_CONTACT_PICKER) {
	    		if(orderItemToSetContact != null) {
	    			Uri result = intent.getData();
	    			String id = result.getLastPathSegment();
	    			Cursor cursor = getContentResolver().query(CommonDataKinds.Phone.CONTENT_URI, null, CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{id}, null);  
	    			
	    			if(cursor.moveToFirst()) {
	    				int phoneIdx = cursor.getColumnIndex(CommonDataKinds.Phone.DATA);	    				
	    				String nick = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	    				String phone = orderItemToSetContact.getContact_number();
	    				if(phoneIdx != -1) {
	    					phone = cursor.getString(phoneIdx);
	    				}
	    				Toast.makeText(MainActivity.this, "Set contact as ["+nick+", "+phone+"] for order of "+orderItemToSetContact.getItem_name(), Toast.LENGTH_LONG).show();
	    				orderItemToSetContact.setContact_name(nick);
	    				orderItemToSetContact.setContact_number(phone);
	    				datasource.updateItemOrdered(orderItemToSetContact);
	    				
	    				displayOrderedItemsFromSQLite();
	    			} else {
	    				Toast.makeText(MainActivity.this, "Unable to fetch Name and Phone for selected contact.", Toast.LENGTH_LONG).show();
	    			}
	    		}
	    	}
	    }
	}
	
	public void onSushiMenuItemClicked(View view) {
	    boolean checked = ((RadioButton) view).isChecked();
	    if(checked) {
	    	selectedMenuItemId = view.getId();
	    	
	    	String selectedMenuItemName = "";
	    	Intent myIntent = new Intent(getApplicationContext(), MenuItemDetailsActivity.class);	    	
	    	switch(view.getId()) {
	    	case R.id.menu_item1:
	    		myIntent.putExtra("menu_activity_to_display", R.layout.activity_menuitem1+"");
	    		selectedMenuItemName = getString(R.string.menu_item1);
	    		break;
	    	case R.id.menu_item2:
	    		myIntent.putExtra("menu_activity_to_display", R.layout.activity_menuitem2+"");
	    		selectedMenuItemName = getString(R.string.menu_item2);
	    		break;
	    	case R.id.menu_item3:
	    		myIntent.putExtra("menu_activity_to_display", R.layout.activity_menuitem3+"");
	    		selectedMenuItemName = getString(R.string.menu_item3);
	    		break;
	    	case R.id.menu_item4:
	    		myIntent.putExtra("menu_activity_to_display", R.layout.activity_menuitem4+"");
	    		selectedMenuItemName = getString(R.string.menu_item4);
	    		break;
	    	}	
	    	myIntent.putExtra("menu_item_selected", selectedMenuItemName);
	    	startActivityForResult(myIntent, REQUEST_CODE_MENUITEM_SIZE);
	    } else {
	    	Toast.makeText(MainActivity.this, "No menu item checked.", Toast.LENGTH_SHORT).show();
	    }
	}
	
	public void onReset(View view) {
		if(itemsOrdered > 0) {
			//sb = new StringBuilder();
			//TextView menuOrdered = (TextView) findViewById(R.id.menuOrdered);
			//menuOrdered.setText("");
			RadioButton rb = (RadioButton) findViewById(selectedMenuItemId);
    		rb.setChecked(false);
    	
    		itemsOrdered = 0;    	
    		itemsOrderedAL = new ArrayList<ItemOrdered>();
    		datasource.deleteAllItemsOrdered();
    		displayOrderedItemsFromSQLite();
		}
	}
	
	public void onCheckout(View view) {
		if(itemsOrdered > 0) {
			Toast.makeText(MainActivity.this, "Order has been sent!", Toast.LENGTH_SHORT).show();
			//sb = new StringBuilder();
			//TextView menuOrdered = (TextView) findViewById(R.id.menuOrdered);
			//menuOrdered.setText("");
			RadioButton rb = (RadioButton) findViewById(selectedMenuItemId);
			rb.setChecked(false);
    	
			itemsOrdered = 0;
			itemsOrderedAL = new ArrayList<ItemOrdered>();
			datasource.deleteAllItemsOrdered();
			displayOrderedItemsFromSQLite();
		}
	}
	
	public void visitWebsite(View view) {
		Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
	    startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		//datasource.close();
		super.onPause();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  savedInstanceState.putBoolean("ClearOrderedItems", false);	  
	  super.onSaveInstanceState(savedInstanceState);
	}
	
	/*
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(savedInstanceState.getBoolean("ClearOrderedItems")) {
			datasource.deleteAllItemsOrdered();
    	}
	}	
	*/
	
	private class StableArrayAdapter extends ArrayAdapter<String> {

	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }

	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }
	  }	
}
