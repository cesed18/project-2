package ly.generalassemb.project2.product.list.presenter;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
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
 * Created by alanjcaceres on 7/15/16.
 */
public class ProductCardViewHolder extends DataBinder {

    ImageView productItemImage, moreActionsImage;
    TextView productItemTitle, productItemPrice;

    public ProductCardViewHolder(View itemView) {
        super(itemView);

        productItemImage = (ImageView) itemView
                .findViewById(R.id.imageview_product_item);

        moreActionsImage = (ImageView) itemView
                .findViewById(R.id.imageview_more_actions);

        productItemTitle = (TextView) itemView
                .findViewById(R.id.textview_product_item_title);

        productItemPrice = (TextView) itemView
                .findViewById(R.id.textview_product_item_price);

    }

    @Override
    public void bindData(final Object data) {
        if(data instanceof ProductModel){
            Glide.with(itemView.getContext())
                    .load(((ProductModel) data).getProductImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(productItemImage);


            moreActionsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    PopupMenu menu = new PopupMenu(v.getContext(), v);
                    menu.inflate(R.menu.product_action_menu);
                    MenuItem item = menu.getMenu().findItem(R.id.menu_action_remove);
                    item.setVisible(ShoppingCartDataStore.getInstance()
                            .isInCart(((ProductModel) data).getProductId()));

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.menu_action_add_to_cart:
                                    ShoppingCartDataStore.getInstance()
                                            .addProduct(((ProductModel) data).getProductId());
                                    Snackbar.make(v,
                                            "Added to Cart", Snackbar.LENGTH_LONG).show();
                                    return true;
                                case R.id.menu_action_remove:
                                    ShoppingCartDataStore.getInstance()
                                            .removeProduct(((ProductModel) data).getProductId(), true);
                                    if(!ShoppingCartDataStore.getInstance()
                                            .isInCart(((ProductModel) data).getProductId())) {
                                        Snackbar.make(v,
                                                "Removed from Cart", Snackbar.LENGTH_LONG).show();
                                    }
                                    return true;
                            }

                            return false;
                        }
                    });
                    menu.show();
                }
            });

            productItemTitle.setText(((ProductModel) data).getProductName());

            productItemPrice.setText(
                    String.format("$%.2f",
                            ((ProductModel) data).getProductPrice()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}

