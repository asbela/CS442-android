package info.kgomes.foodorder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ItemsOrderedTable extends SQLiteOpenHelper {
	public static final String TABLE_ITEMS_ORDERED = "items_ordered";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_ITEMNAME = "item_name";
	  public static final String COLUMN_PRICE = "price";
	  public static final String COLUMN_ORDERTIME = "order_time";
	  public static final String COLUMN_CONTACTNAME = "contact_name";
	  public static final String COLUMN_CONTACTNUMBER = "contact_number";

	  private static final String DATABASE_NAME = "foodorder.db";
	  private static final int DATABASE_VERSION = 1;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_ITEMS_ORDERED + "(" + COLUMN_ID
	      + " integer primary key autoincrement, " + COLUMN_ITEMNAME
	      + " text not null, " + COLUMN_PRICE
	      + " real not null, " + COLUMN_ORDERTIME
	      + " text not null, " + COLUMN_CONTACTNAME
	      + " text not null, " + COLUMN_CONTACTNUMBER
	      + " text not null);";

	  public ItemsOrderedTable(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(ItemsOrderedTable.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS_ORDERED);
	    onCreate(db);
	  }
}
