package info.kgomes.helper;

import info.kgomes.foodorder.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MenuItemDetailsAdapter extends ArrayAdapter<MenuItemDetails> {
	
	private Context mContext;
	int layoutResourceId;
	MenuItemDetails[] data = null;

    public MenuItemDetailsAdapter(Context c, int layoutResourceId, MenuItemDetails[] data) {
    	super(c, layoutResourceId, data);
        this.mContext = c;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MenuItemDetailsHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new MenuItemDetailsHolder();
            holder.title = (TextView)row.findViewById(R.id.menuItemTitle);
            holder.description = (TextView)row.findViewById(R.id.menuItemDescription);
           
            row.setTag(holder);
        }
        else
        {
            holder = (MenuItemDetailsHolder)row.getTag();
        }
       
        MenuItemDetails weather = data[position];
        holder.title.setText(weather.title);
        holder.description.setText(weather.description);
       
        return row;
    }
    
    static class MenuItemDetailsHolder
    {
        TextView title;
        TextView description;
    }
}
