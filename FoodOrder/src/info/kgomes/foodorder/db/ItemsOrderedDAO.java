package info.kgomes.foodorder.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ItemsOrderedDAO {
	// Database fields
	  private SQLiteDatabase database;
	  private ItemsOrderedTable dbHelper;
	  private String[] allColumns = { ItemsOrderedTable.COLUMN_ID,
			  ItemsOrderedTable.COLUMN_ITEMNAME, ItemsOrderedTable.COLUMN_PRICE, 
			  ItemsOrderedTable.COLUMN_ORDERTIME, ItemsOrderedTable.COLUMN_CONTACTNAME,
			  ItemsOrderedTable.COLUMN_CONTACTNUMBER};

	  public ItemsOrderedDAO(Context context) {
	    dbHelper = new ItemsOrderedTable(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public ItemOrdered createItemOrdered_Rec(String itemName, Double price, String orderTime, String contactName, String contactNumber) {
	    ContentValues values = new ContentValues();
	    values.put(ItemsOrderedTable.COLUMN_ITEMNAME, itemName);
	    values.put(ItemsOrderedTable.COLUMN_PRICE, price);
	    values.put(ItemsOrderedTable.COLUMN_ORDERTIME, orderTime);
	    values.put(ItemsOrderedTable.COLUMN_CONTACTNAME, contactName);
	    values.put(ItemsOrderedTable.COLUMN_CONTACTNUMBER, contactNumber);
	    
	    long insertId = database.insert(ItemsOrderedTable.TABLE_ITEMS_ORDERED, null, values);
	    Cursor cursor = database.query(ItemsOrderedTable.TABLE_ITEMS_ORDERED, allColumns, ItemsOrderedTable.COLUMN_ID + " = " + insertId, null, null, null, null);
	    cursor.moveToFirst();
	    ItemOrdered item = cursorToItemOrdered(cursor);
	    cursor.close();
	    return item;
	  }

	  public void updateItemOrdered(ItemOrdered item) {
		  long id = item.getId();
		  ContentValues values = new ContentValues();
		  values.put(ItemsOrderedTable.COLUMN_ITEMNAME, item.getItem_name());
		  values.put(ItemsOrderedTable.COLUMN_PRICE, item.getPrice());
		  values.put(ItemsOrderedTable.COLUMN_ORDERTIME, item.getOrder_time());
		  values.put(ItemsOrderedTable.COLUMN_CONTACTNAME, item.getContact_name());
		  values.put(ItemsOrderedTable.COLUMN_CONTACTNUMBER, item.getContact_number());
		    
		  database.update(ItemsOrderedTable.TABLE_ITEMS_ORDERED, values, ItemsOrderedTable.COLUMN_ID + " = " + id, null);
	  }
	  
	  public void deleteItemOrdered(ItemOrdered item) {
	    long id = item.getId();
	    System.out.println("Item with id: " + id + " deleted.");
	    database.delete(ItemsOrderedTable.TABLE_ITEMS_ORDERED, ItemsOrderedTable.COLUMN_ID + " = " + id, null);
	  }
	  
	  public void deleteAllItemsOrdered() {
		  database.delete(ItemsOrderedTable.TABLE_ITEMS_ORDERED, null, null);
	  }

	  public List<ItemOrdered> getAllItemsOrdered() {
	    List<ItemOrdered> items = new ArrayList<ItemOrdered>();

	    Cursor cursor = database.query(ItemsOrderedTable.TABLE_ITEMS_ORDERED, allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      ItemOrdered item = cursorToItemOrdered(cursor);
	      items.add(item);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return items;
	  }

	  private ItemOrdered cursorToItemOrdered(Cursor cursor) {
		ItemOrdered item = new ItemOrdered();
	    item.setId(cursor.getLong(0));
	    item.setItem_name(cursor.getString(1));
	    item.setPrice(cursor.getDouble(2));
	    item.setOrder_time(cursor.getString(3));
	    item.setContact_name(cursor.getString(4));
	    item.setContact_number(cursor.getString(5));
	    return item;
	  }
}
