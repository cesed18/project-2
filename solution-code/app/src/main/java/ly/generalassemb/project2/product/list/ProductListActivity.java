package ly.generalassemb.project2.product.list;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ly.generalassemb.project2.R;
import ly.generalassemb.project2.product.database.AppDatabase;
import ly.generalassemb.project2.product.database.contracts.ProductsDatabaseContract;
import ly.generalassemb.project2.product.detail.ProductDetailActivity;
import ly.generalassemb.project2.product.interfaces.RecyclerViewOnItemClickListener;
import ly.generalassemb.project2.product.list.presenter.ProductRecyclerViewAdapter;
import ly.generalassemb.project2.product.search.SearchResultsActivity;
import ly.generalassemb.project2.shoppingcart.ShoppingCartActivity;
import ly.generalassemb.project2.shoppingcart.database.ShoppingCartDataStore;

public class ProductListActivity extends AppCompatActivity implements RecyclerViewOnItemClickListener {

    protected AppDatabase mDatabase;

    protected Toolbar mToolbar;
    protected RecyclerView mRecyclerView;
    protected FloatingActionButton mFab;

    protected ProductRecyclerViewAdapter mAdapter;

    protected Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        mDatabase = AppDatabase.getInstance(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_product_list);

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.VERTICAL, false));
        }

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFab = (FloatingActionButton) findViewById(R.id.shoppingCartButton);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shoppingCartIntent = new Intent(v.getContext(),
                        ShoppingCartActivity.class);
                startActivityForResult(shoppingCartIntent, 123);


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCursor = mDatabase.getAllProducts();
        mAdapter = new ProductRecyclerViewAdapter(mCursor, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mRecyclerView.setAdapter(mAdapter);
    }

    //RecyclerViewOnItemClickListener
    @Override
    public void onClick(View v, int position) {
        mCursor.moveToPosition(position);
        int index = mCursor.getColumnIndex(
                ProductsDatabaseContract._ID);
        long _id = mCursor.getLong(index);

        Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
        intent.putExtra(ProductsDatabaseContract._ID, _id);

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this,
                        v.findViewById(R.id.imageview_product_item)
                        , "product_image");
        startActivity(intent, options.toBundle());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);

        ComponentName componentName = new ComponentName(this, SearchResultsActivity.class);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case 123:
                    ShoppingCartDataStore.getInstance().clearCart();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setTitle("Order Complete")
                            .setMessage("Thank you for your order!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    builder.show();

                    break;
            }
        }
    }
}
