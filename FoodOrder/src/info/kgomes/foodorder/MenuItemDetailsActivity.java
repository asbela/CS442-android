package info.kgomes.foodorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import info.kgomes.helper.*;

public class MenuItemDetailsActivity extends Activity {	
	String selectedMenuItem;
	String selectedSize;
	Double price;	
	
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
		Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);	
		myIntent.putExtra("selectedMenuItem", selectedMenuItem);
		myIntent.putExtra("size", selectedSize+"");
		myIntent.putExtra("price", "$"+price);
		setResult(RESULT_OK, myIntent);
		finish();
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
