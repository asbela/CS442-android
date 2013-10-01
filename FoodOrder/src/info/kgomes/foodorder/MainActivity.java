package info.kgomes.foodorder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	int selectedMenuItemId;
	
	StringBuilder sb = new StringBuilder();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_main);
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

	    
	    if (resultCode == RESULT_OK) {
	    	if(intent.getStringExtra("selectedMenuItem") != null) {
	    		sb.append("\n"+intent.getStringExtra("selectedMenuItem").toString());
	    	}
	    	if(intent.getStringExtra("size") != null) {
	    		sb.append(" - "+intent.getStringExtra("size").toString());
	    	}
	    	if(intent.getStringExtra("price") != null) {
	    		sb.append(" - "+intent.getStringExtra("price").toString());
	    	}
	    	
	    	TextView menuOrdered = (TextView) findViewById(R.id.menuOrdered);
	    	menuOrdered.setText(sb.toString());
		    
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
	    	startActivityForResult(myIntent, 1);
	    } else {
	    	Toast.makeText(MainActivity.this, "No menu item checked.", Toast.LENGTH_SHORT).show();
	    }
	}
	
	public void onReset(View view) {
		sb = new StringBuilder();
		TextView menuOrdered = (TextView) findViewById(R.id.menuOrdered);
    	menuOrdered.setText("");
    	RadioButton rb = (RadioButton) findViewById(selectedMenuItemId);
    	rb.setChecked(false);
	}
	
	public void onCheckout(View view) {
		Toast.makeText(MainActivity.this, "Order has been sent!", Toast.LENGTH_SHORT).show();
		sb = new StringBuilder();
		TextView menuOrdered = (TextView) findViewById(R.id.menuOrdered);
    	menuOrdered.setText("");
    	RadioButton rb = (RadioButton) findViewById(selectedMenuItemId);
    	rb.setChecked(false);
	}
}
