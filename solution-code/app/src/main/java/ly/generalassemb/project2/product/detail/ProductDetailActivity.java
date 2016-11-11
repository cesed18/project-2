package ly.generalassemb.project2.product.detail;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;

import ly.generalassemb.project2.R;
import ly.generalassemb.project2.product.database.AppDatabase;
import ly.generalassemb.project2.product.database.contracts.ProductsDatabaseContract;
import ly.generalassemb.project2.shoppingcart.ShoppingCartActivity;
import ly.generalassemb.project2.shoppingcart.database.ShoppingCartDataStore;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView mImageView;
    private TextView mNameTextView;
    private TextView mDescriptionTextView;
    private Button mBuyButton;
    private FloatingActionButton fabCheckout;

    private long mProductId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_detail);

        mImageView = (ImageView) findViewById(R.id.product_detail_imageView);
        mNameTextView = (TextView) findViewById(R.id.product_detail_name);
        mDescriptionTextView = (TextView) findViewById(R.id.product_detail_description);
        mBuyButton = (Button) findViewById(R.id.buyButton);

        fabCheckout = (FloatingActionButton) findViewById(R.id.fab_checkout);
        fabCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ShoppingCartActivity.class);
                startActivity(intent);
            }
        });

        Intent callingIntent = getIntent();
        mProductId = callingIntent.getLongExtra(ProductsDatabaseContract._ID, -1);

        if (mProductId < 0){
            Toast.makeText(ProductDetailActivity.this, "Something went wrong! :(", Toast.LENGTH_SHORT).show();
            finish();
        }

        AppDatabase database = AppDatabase.getInstance(this);
        Cursor cursor = database.getProductById(mProductId);

        Glide.with(this).load(getStringFromCursor(cursor, ProductsDatabaseContract.PICTURE_URL_COLUMN))
                .into(mImageView);

        mNameTextView.setText(getStringFromCursor(cursor, ProductsDatabaseContract.PRODUCT_NAME_COLUMN));
        mDescriptionTextView.setText(getStringFromCursor(cursor, ProductsDatabaseContract.DESCRIPTION_COLUMN));

        NumberFormat format = NumberFormat.getCurrencyInstance();
        mBuyButton.setText(format.format(cursor.getInt(cursor.getColumnIndex(ProductsDatabaseContract.PRICE_COLUMN))));

        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCartDataStore.getInstance().addProduct(mProductId);
                Snackbar.make(findViewById(R.id.product_detail_imageView), "Added to cart!",
                        Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShoppingCartDataStore.getInstance().removeProduct(mProductId, false);
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .show();
            }
        });
        cursor.close();
    }

    private String getStringFromCursor(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
}
