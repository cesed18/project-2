package ly.generalassemb.project2.product.model;

/**
 * Created by alanjcaceres on 7/15/16.
 */
public abstract class BaseProduct {

    protected String mProductName;
    protected double mProductPrice;
    protected String mProductImageUrl;
    protected long mProductId;

    public BaseProduct(){
        mProductName = "A Random Product";
        mProductPrice = 9_999_999.99;
        mProductImageUrl = "http://sp.cm/rodneypikecom/" +
                "files/2014/11/1415314923Bean1000.jpg";
        mProductId = -1;
    }

    public BaseProduct(String productName, double productPrice,
                       String productImageUrl, long productId) {

        mProductName = productName;
        mProductPrice = productPrice;
        mProductImageUrl = productImageUrl;
        mProductId = productId;
    }

    public abstract String getProductName();

    public abstract double getProductPrice();

    public abstract String getProductImageUrl();

    public abstract long getProductId();

}
