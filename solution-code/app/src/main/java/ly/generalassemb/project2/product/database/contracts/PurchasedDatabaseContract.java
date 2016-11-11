package ly.generalassemb.project2.product.database.contracts;

import android.provider.BaseColumns;

/**
 * James Davis (General Assembly NYC)
 * Created on 4/20/16.
 */
public class PurchasedDatabaseContract implements BaseColumns {
    public static final String TABLE_NAME = "purchased_products";
    public static final String ORDER_ID_COLUMN = "order_id";
    public static final String PRODUCT_ID_COLUMN = "product_id";

    public static String getCreateTableStatement() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY NOT NULL, " +
                ORDER_ID_COLUMN + " INTEGER NOT NULL REFERENCES orders (" + _ID + "), " +
                PRODUCT_ID_COLUMN + " INTEGER NOT NULL REFERENCES products (" + ProductsDatabaseContract._ID + "), " +
                "FOREIGN KEY (" + ORDER_ID_COLUMN + ") REFERENCES orders (" + OrdersDatabaseContract._ID + "))";
    }
}
