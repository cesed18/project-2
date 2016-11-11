package ly.generalassemb.project2.product.database.contracts;

import android.provider.BaseColumns;

/**
 * James Davis (General Assembly NYC)
 * Created on 4/20/16.
 */
public class OrdersDatabaseContract implements BaseColumns {
    public static final String TABLE_NAME = "orders";
    public static final String DATE_COLUMN = "date";
    public static final String TOTAL_PRICE_COLUMN = "total_price";
    public static final String ESTIMATED_DELIVERY_COLUMN = "estimated_delivery";

    public static String getCreateTableStatement() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER NOT NULL PRIMARY KEY, " +
                DATE_COLUMN + " TEXT NOT NULL, " +
                TOTAL_PRICE_COLUMN + " REAL NOT NULL, " +
                ESTIMATED_DELIVERY_COLUMN + " TEXT NOT NULL)";
    }
}
