package ly.generalassemb.project2.shoppingcart.presenter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import ly.generalassemb.project2.R;
import ly.generalassemb.project2.product.interfaces.DataBinder;
import ly.generalassemb.project2.product.model.ProductModel;
import ly.generalassemb.project2.shoppingcart.database.ShoppingCartDataStore;

/**
 * Created by alanjcaceres on 7/14/16.
 */

public class ShoppingCartItemViewHolder extends DataBinder {

    ImageView productImage, subtractButton, addButton;
    TextView productItemName, productItemPrice, itemCount;

    public ShoppingCartItemViewHolder(View itemView) {
        super(itemView);

        productImage = (ImageView)
                itemView.findViewById(R.id.imageview_product_item);

        productItemName = (TextView)
                itemView.findViewById(R.id.textview_product_item_title);

        productItemPrice = (TextView)
                itemView.findViewById(R.id.textview_product_item_price);

        subtractButton = (ImageView)
                itemView.findViewById(R.id.imageview_subtract);

        itemCount = (TextView)
                itemView.findViewById(R.id.textview_item_count);

        addButton = (ImageView)
                itemView.findViewById(R.id.imageview_add);

    }

    @Override
    public void bindData(final Object data) {
        if(data instanceof ProductModel){

            Glide.with(itemView.getContext())
                    .load(((ProductModel) data).getProductImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(productImage);

            productItemName.setText(((ProductModel) data).getProductName());

            productItemPrice.setText(
                    String.format("$%.2f",
                            ((ProductModel) data).getProductPrice()));

            subtractButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShoppingCartDataStore.getInstance()
                            .removeProduct(((ProductModel) data)
                                    .getProductId(), false);
                }
            });

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShoppingCartDataStore.getInstance()
                            .addProduct(((ProductModel) data)
                                    .getProductId());
                }
            });

            if(ShoppingCartDataStore.getInstance()
                    .isInCart(((ProductModel) data).getProductId())) {
                int productCount = ShoppingCartDataStore.getInstance()
                        .getProductCountById(
                                ((ProductModel) data).getProductId());
                itemCount.setText(productCount + "");
            }
        }
    }
}
