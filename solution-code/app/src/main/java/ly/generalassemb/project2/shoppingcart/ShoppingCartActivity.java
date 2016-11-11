package ly.generalassemb.project2.shoppingcart;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ly.generalassemb.project2.R;
import ly.generalassemb.project2.product.database.AppDatabase;
import ly.generalassemb.project2.product.database.contracts.ProductsDatabaseContract;
import ly.generalassemb.project2.product.detail.ProductDetailActivity;
import ly.generalassemb.project2.product.interfaces.RecyclerViewOnItemClickListener;
import ly.generalassemb.project2.shoppingcart.database.ShoppingCartDataStore;
import ly.generalassemb.project2.shoppingcart.presenter.ShoppingCartRecyclerViewAdapter;

/**
 * Created by alanjcaceres on 7/14/16.
 */

public class ShoppingCartActivity extends AppCompatActivity implements RecyclerViewOnItemClickListener {

    private static final String TAG = "ShoppingCartActivity";

    ShoppingCartDataStore mShoppingCartDataStore;
    AppDatabase mDatabase;

    Cursor mProductCursor;

    RecyclerView mRecyclerView;

    TextView subTotalTextView, taxTextView, totalTextView;

    FloatingActionButton fabCheckout;

    ShoppingCartRecyclerViewAdapter adapter;
    double total = 0;
    double cartSubTotal = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_layout);

        subTotalTextView = (TextView) findViewById(R.id.textview_subtotal);
        taxTextView = (TextView) findViewById(R.id.textview_tax_shipping);
        totalTextView = (TextView) findViewById(R.id.textview_total);

        fabCheckout = (FloatingActionButton) findViewById(R.id.fab_checkout);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_shopping_cart_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mDatabase = AppDatabase.getInstance(this);

        mShoppingCartDataStore = ShoppingCartDataStore.getInstance();

        cartSubTotal = mShoppingCartDataStore.getCartSubtotal(this);

        populateCartTotals(cartSubTotal);

        adapter = new ShoppingCartRecyclerViewAdapter();
        adapter.setOnItemClickListener(this);

        List<Long> shoppingCartIds = mShoppingCartDataStore.getShoppingCartProductIds();
        if(!shoppingCartIds.isEmpty()){
            mProductCursor = mDatabase.getProductsById(shoppingCartIds);
            adapter.setCursor(mProductCursor);
            adapter.setOnItemClickListener(this);
        }

        mShoppingCartDataStore.addShoppingCartListener(adapter);
        mShoppingCartDataStore.addShoppingCartListener(new ShoppingCartDataStore.ShoppingCartListener() {
            @Override
            public void onShoppingCartChanged(List<Long> shoppingCartIds) {
                cartSubTotal = mShoppingCartDataStore.getCartSubtotal(ShoppingCartActivity.this);
                populateCartTotals(cartSubTotal);
            }
        });

        mRecyclerView.setAdapter(adapter);

        fabCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mShoppingCartDataStore.isEmpty()){
                    Toast.makeText(view.getContext(),
                            "Your cart is empty!", Toast.LENGTH_LONG)
                            .show();
                }
                else {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void populateCartTotals(double cartSubTotal) {
        Spannable spannedSubTotal = new SpannableString(String.format("Subtotal: %.2f", cartSubTotal));
        spannedSubTotal.setSpan(new StyleSpan(Typeface.BOLD),0,"Subtotal:".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        subTotalTextView.setText(spannedSubTotal);

        double tax = cartSubTotal * 0.08678;

        Spannable spannedTax = new SpannableString(String.format("Tax: %.2f", tax));
        spannedTax.setSpan(new StyleSpan(Typeface.BOLD),0,"Tax:".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        taxTextView.setText(spannedTax);

        total = cartSubTotal + tax;

        Spannable spannedTotal = new SpannableString(String.format("Total: %.2f", total));
        spannedTotal.setSpan(new StyleSpan(Typeface.BOLD),0,"Total:".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        totalTextView.setText(spannedTotal);


    }

    @Override
    public void onClick(View v, int position) {
        mProductCursor.moveToPosition(position);
        int index = mProductCursor.getColumnIndex(
                ProductsDatabaseContract._ID);
        long _id = mProductCursor.getLong(index);

        Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
        intent.putExtra(ProductsDatabaseContract._ID, _id);

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this,
                        v.findViewById(R.id.imageview_product_item)
                        , "product_image");
        startActivity(intent, options.toBundle());
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
