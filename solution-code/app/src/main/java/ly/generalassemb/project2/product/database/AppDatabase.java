package ly.generalassemb.project2.product.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import ly.generalassemb.project2.R;
import ly.generalassemb.project2.product.database.contracts.OrdersDatabaseContract;
import ly.generalassemb.project2.product.database.contracts.ProductsDatabaseContract;
import ly.generalassemb.project2.product.database.contracts.PurchasedDatabaseContract;

/**
 * James Davis (General Assembly NYC)
 * Created on 4/20/16.
 */

public class AppDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data.db";
    private static AppDatabase instance;

    // ================================================================================ CONSTRUCTORS


    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
        loadAllData(context);
    }

    // ============================================================================== SINGLETON METHOD

    public static AppDatabase getInstance(Context context){
        if(instance == null){
            instance = new AppDatabase(context.getApplicationContext());
        }
        return instance;
    }

    // ============================================================================== HELPER METHODS

    public Cursor getProductById(long productId) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(ProductsDatabaseContract.TABLE_NAME,
                null,
                ProductsDatabaseContract._ID + " = ?",
                new String[]{String.valueOf(productId)},
                null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getProductsById(List<Long> productIdList){
        String[] idList = new String[productIdList.size()];
        for(int i = 0; i < productIdList.size(); i++){
            idList[i] = String.valueOf(productIdList.get(i));
        }

        String selection = String.format("%s = ?", ProductsDatabaseContract._ID);
        StringBuilder builder = new StringBuilder(selection);
        for(int i = 1; i < idList.length; i++){
            builder.append(" OR ");
            builder.append(selection);
        }
        selection = builder.toString();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(ProductsDatabaseContract.TABLE_NAME,
                null,
                selection,
                idList,
                null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAllProducts(){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(ProductsDatabaseContract.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getOrderById(long orderId){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(OrdersDatabaseContract.TABLE_NAME,
                null,
                OrdersDatabaseContract._ID + " = ?",
                new String[]{String.valueOf(orderId)},
                null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAllOrders(){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(
                OrdersDatabaseContract.TABLE_NAME,
                null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getPurchaseById(long purchaseId){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(PurchasedDatabaseContract.TABLE_NAME,
                null,
                PurchasedDatabaseContract._ID + " = ?",
                new String[]{String.valueOf(purchaseId)},
                null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAllPurchases(){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(PurchasedDatabaseContract.TABLE_NAME,
                null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }


    // ============================================================================== DATABASE SETUP

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ProductsDatabaseContract.getCreateTableStatement());
        db.execSQL(PurchasedDatabaseContract.getCreateTableStatement());
        db.execSQL(OrdersDatabaseContract.getCreateTableStatement());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProductsDatabaseContract.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PurchasedDatabaseContract.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OrdersDatabaseContract.TABLE_NAME);
        onCreate(db);
    }

    public void loadAllData(Context context) {
        SQLiteDatabase database = getReadableDatabase();

        boolean hasNoProducts = true;
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM" + ProductsDatabaseContract.TABLE_NAME, null);
        hasNoProducts = cursor != null && cursor.moveToFirst();

        if (hasNoProducts) {

            database.beginTransaction();

            BufferedReader inputStream = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.products_sql)));

            try {
                while (inputStream.ready()) {
                    try {
                        database.execSQL(inputStream.readLine());
                    } catch (Throwable thr) {
                    }
                }

                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            database.setTransactionSuccessful();
            database.endTransaction();
        }
    }
}
