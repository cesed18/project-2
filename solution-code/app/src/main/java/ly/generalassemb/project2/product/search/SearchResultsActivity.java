package ly.generalassemb.project2.product.search;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import ly.generalassemb.project2.product.database.contracts.ProductsDatabaseContract;
import ly.generalassemb.project2.product.list.ProductListActivity;

/**
 * Created by alanjcaceres on 7/18/16.
 */

public class SearchResultsActivity extends ProductListActivity {

    @Override
    protected void onStart() {
        super.onStart();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            SQLiteDatabase db = mDatabase.getReadableDatabase();

            Cursor productNameCursor = db.query(
                    ProductsDatabaseContract.TABLE_NAME,
                    null,
                    ProductsDatabaseContract.PRODUCT_NAME_COLUMN + " LIKE ?",
                    new String[]{"%" + query + "%"}, null, null, null);

            Cursor productDescriptionCursor = db.query(
                    ProductsDatabaseContract.TABLE_NAME,
                    null,
                    ProductsDatabaseContract.DESCRIPTION_COLUMN + " LIKE ?",
                    new String[]{"%" + query + "%"}, null, null, null);

            Cursor productPriceCursor = db.query(
                    ProductsDatabaseContract.TABLE_NAME,
                    null,
                    ProductsDatabaseContract.PRICE_COLUMN + "=?",
                    new String[]{query}, null, null, null);

            if (productNameCursor.moveToFirst()) {
                mAdapter.setCursor(productNameCursor);
            }
            if (productDescriptionCursor.moveToFirst()) {
                mAdapter.setCursor(productDescriptionCursor);
            }
            if (productPriceCursor.moveToFirst()) {
                mAdapter.setCursor(productPriceCursor);
            }
            //else do nothing
        }
        else{
            mCursor = mDatabase.getAllProducts();
            mAdapter.setCursor(mCursor);
        }
    }

    static class MyFragment extends Fragment {

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            getArguments().getString("selected_planet");
        }
    }

}
