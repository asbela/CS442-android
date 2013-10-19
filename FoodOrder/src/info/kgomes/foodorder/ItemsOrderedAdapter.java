package info.kgomes.foodorder;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import info.kgomes.foodorder.db.*;

public class ItemsOrderedAdapter extends ArrayAdapter<ItemOrdered> {
	Context context; 
    int layoutResourceId;    
    List<ItemOrdered> data = null;
    
    public ItemsOrderedAdapter(Context context, int layoutResourceId, List<ItemOrdered> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemOrderedHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ItemOrderedHolder();
            holder.item_name = (TextView) row.findViewById(R.id.item_name);
            holder.price = (TextView) row.findViewById(R.id.price);
            holder.order_time = (TextView) row.findViewById(R.id.order_time); 
            holder.contact_name = (TextView) row.findViewById(R.id.contact_name);
            holder.contact_number = (TextView) row.findViewById(R.id.contact_number);
            row.setTag(holder);
        }
        else
        {
            holder = (ItemOrderedHolder)row.getTag();
        }
        
        ItemOrdered item = data.get(position);
        holder.item_name.setText(item.getItem_name());
        holder.price.setText("$"+item.getPrice());
        holder.order_time.setText(item.getOrder_time());
        holder.contact_name.setText(item.getContact_name());
        holder.contact_number.setText(item.getContact_number());
        
        return row;
    }
    
    static class ItemOrderedHolder
    {
        TextView item_name;
        TextView price;
        TextView order_time;
        TextView contact_name;
        TextView contact_number;
    }
}
