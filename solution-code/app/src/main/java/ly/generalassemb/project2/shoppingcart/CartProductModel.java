package ly.generalassemb.project2.shoppingcart;

import ly.generalassemb.project2.product.model.BaseProduct;

/**
 * Created by alanjcaceres on 7/15/16.
 */

public class CartProductModel extends BaseProduct {

    int mProductCount = 0;

    public CartProductModel(){
        super();
        mProductCount = 1;
    }

    public CartProductModel(String productName, double productPrice,
                            String productImageUrl, long productId) {
        super(productName, productPrice, productImageUrl, productId);
        mProductCount = 1;
    }

    @Override
    public String getProductName() {
        return mProductName;
    }

    @Override
    public double getProductPrice() {
        return mProductPrice;
    }

    @Override
    public String getProductImageUrl() {
        return mProductImageUrl;
    }

    @Override
    public long getProductId(){
        return mProductId;
    }

    public int getProductCount(){
        return mProductCount;
    }
}
