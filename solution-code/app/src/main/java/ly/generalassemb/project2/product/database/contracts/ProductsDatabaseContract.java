package ly.generalassemb.project2.product.database.contracts;

import android.provider.BaseColumns;

/**
 * James Davis (General Assembly NYC)
 * Created on 4/20/16.
 */
public class ProductsDatabaseContract implements BaseColumns {
    public static final String TABLE_NAME = "products";
    public static final String PRODUCT_NAME_COLUMN = "name";
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String PRICE_COLUMN = "price";
    public static final String PICTURE_URL_COLUMN = "picture_url";

    public static String getCreateTableStatement(){
        return "CREATE TABLE " + TABLE_NAME +" ("
                + _ID + " INTEGER NOT NULL PRIMARY KEY, "
                + PRODUCT_NAME_COLUMN + " TEXT NOT NULL, "
                + DESCRIPTION_COLUMN + " TEXT NOT NULL, "
                + PRICE_COLUMN + " REAL NOT NULL, "
                + PICTURE_URL_COLUMN + " TEXT)";
    }
}
