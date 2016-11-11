package ly.generalassemb.project2.shoppingcart.database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ly.generalassemb.project2.product.database.AppDatabase;
import ly.generalassemb.project2.product.database.contracts.ProductsDatabaseContract;

/**
 * James Davis (General Assembly NYC)
 * Created on 4/21/16.
 */
public class ShoppingCartDataStore {
    private static ShoppingCartDataStore ourInstance;

    Map<Long, Integer> mProducts;
    List<ShoppingCartListener> mListeners;

    public interface ShoppingCartListener{
        void onShoppingCartChanged(List<Long> shoppingCartIds);
    }

    public static ShoppingCartDataStore getInstance() {
        if(ourInstance == null){
            ourInstance = new ShoppingCartDataStore();
        }
        return ourInstance;
    }

    private ShoppingCartDataStore() {
        mProducts = new HashMap<>();
        mListeners = new ArrayList<>();
    }

    public void addShoppingCartListener(ShoppingCartListener listener){
        mListeners.add(listener);
    }

    private void notifyListeners(){
        for(int i = 0; i < mListeners.size(); i++){
            mListeners.get(i).onShoppingCartChanged(getShoppingCartProductIds());
        }
    }

    public void addProduct(long productId){
        if (!mProducts.containsKey(productId)){
            mProducts.put(productId, 1);
        }
        else{
            incrementProductCount(productId);
        }
        notifyListeners();
    }

    private void incrementProductCount(long productId){
        int count = mProducts.get(productId) + 1;
        mProducts.put(productId, count);
    }

    public void removeProduct(long productId, boolean removeAll){
        if(!mProducts.containsKey(productId)){return;}
        else if(mProducts.get(productId) > 1 && !removeAll) {
            decrementProductCount(productId);
        }
        else {
            mProducts.remove(productId);
        }
        notifyListeners();
    }

    private void decrementProductCount(long productId){
        int count = mProducts.get(productId) - 1;
        if(count <= 0){
            mProducts.remove(productId);
        }
        else {
            mProducts.put(productId, count);
        }
        notifyListeners();
    }

    public List<Long> getShoppingCartProductIds(){

        if(mProducts.keySet().isEmpty()){
            return new ArrayList<>();
        }
        else {
            return new ArrayList<>(mProducts.keySet());
        }
    }

    public int getProductCountById(long productId){
        return mProducts.get(productId);
    }

    public void clearCart(){
        mProducts.clear();
    }

    public boolean isEmpty(){
        return mProducts.isEmpty();
    }

    public boolean isInCart(long productId){
        return mProducts.containsKey(productId);
    }

    public double getCartSubtotal(Context context){

        int subtotal = 0;

        AppDatabase database = AppDatabase.getInstance(context);

        List<Long> ids = getShoppingCartProductIds();

        Cursor cursor = database.getProductsById(ids);

        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                int priceColIndex = cursor.getColumnIndex(
                        ProductsDatabaseContract.PRICE_COLUMN);

                for(int i = 0; i < ids.size(); i++) {
                    int idCol = cursor.getColumnIndex(ProductsDatabaseContract._ID);
                    long id = cursor.getLong(idCol);
                    if(ids.get(i).equals(id)){
                        subtotal += cursor.getDouble(priceColIndex) * mProducts.get(ids.get(i));
                        break;
                    }
                    else {
                        subtotal += cursor.getDouble(priceColIndex);
                    }
                }
                cursor.moveToNext();
            }
            cursor.close();

        }
        return subtotal;
    }
}
