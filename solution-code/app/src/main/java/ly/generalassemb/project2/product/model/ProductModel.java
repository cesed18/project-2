package ly.generalassemb.project2.product.model;

/**
 * Created by alanjcaceres on 7/13/16.
 */

public class ProductModel extends BaseProduct{

    public ProductModel(){
        super();
    }

    public ProductModel(String productName, double productPrice,
                        String productImageUrl, long productId) {
        super(productName, productPrice, productImageUrl, productId);
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
}
