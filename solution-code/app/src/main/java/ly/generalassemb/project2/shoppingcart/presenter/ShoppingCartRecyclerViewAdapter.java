package ly.generalassemb.project2.shoppingcart.presenter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ly.generalassemb.project2.R;
import ly.generalassemb.project2.product.database.contracts.ProductsDatabaseContract;
import ly.generalassemb.project2.product.interfaces.DataBinder;
import ly.generalassemb.project2.product.interfaces.RecyclerViewOnItemClickListener;
import ly.generalassemb.project2.product.model.ProductModel;
import ly.generalassemb.project2.shoppingcart.database.ShoppingCartDataStore;

/**
 * Created by alanjcaceres on 7/14/16.
 */

public class ShoppingCartRecyclerViewAdapter
        extends RecyclerView.Adapter<DataBinder<ProductModel>>
        implements ShoppingCartDataStore.ShoppingCartListener{

    Cursor mCursor;
    List<ProductModel> mProductList = new ArrayList<>();
    RecyclerViewOnItemClickListener mListener;

    public ShoppingCartRecyclerViewAdapter(){}

    public ShoppingCartRecyclerViewAdapter(Cursor cursor, RecyclerViewOnItemClickListener listener){
        mCursor = cursor;
        mListener = listener;

        if(mCursor.moveToFirst()){
            while(!mCursor.isAfterLast()){
                int titleColIndex = mCursor.getColumnIndex(
                        ProductsDatabaseContract.PRODUCT_NAME_COLUMN);
                int priceColIndex = mCursor.getColumnIndex(
                        ProductsDatabaseContract.PRICE_COLUMN);
                int imageColIndex = mCursor.getColumnIndex(
                        ProductsDatabaseContract.PICTURE_URL_COLUMN);
                int idColIndex = mCursor.getColumnIndex(
                        ProductsDatabaseContract._ID);

                String title = mCursor.getString(titleColIndex);
                double price = mCursor.getDouble(priceColIndex);
                String imageUrl = mCursor.getString(imageColIndex);
                long id = mCursor.getLong(idColIndex);
                mProductList.add(new ProductModel(title, price, imageUrl, id));
                mCursor.moveToNext();
            }
        }
    }

    @Override
    public DataBinder<ProductModel> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        if(mProductList.isEmpty()){
            v = inflater.from(parent.getContext())
                    .inflate(R.layout.shopping_cart_empty, parent, false);
            return new EmptyCartViewHolder(v);
        }
        else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shopping_cart_item, parent, false);
            return new ShoppingCartItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(DataBinder<ProductModel> holder, final int position) {
        if(mProductList.isEmpty()){
            //Don't do anything
        }
        else {
            ProductModel model = mProductList.get(position);
            holder.bindData(model);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClick(view, position);
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return mProductList.isEmpty() ? 1 : mProductList.size();
    }

    @Override
    public void onShoppingCartChanged(List<Long> shoppingCartProductId) {
        if(!shoppingCartProductId.isEmpty()){
            List<ProductModel> tempList = new ArrayList<>(mProductList);
            for (int i = 0; i < tempList.size(); i++) {
                if(!shoppingCartProductId.contains(tempList.get(i).getProductId())){
                    mProductList.remove(i);
                    notifyItemRemoved(i);
                }
            }
        }
        else if(shoppingCartProductId.isEmpty()){
            int itemCount = getItemCount();
            mProductList.clear();
            notifyItemRangeRemoved(0, itemCount);
        }


    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener){
        mListener = listener;
    }

    public void setCursor(Cursor cursor){
        mCursor = cursor;
        if(mCursor.moveToFirst()){
            while(!mCursor.isAfterLast()){
                int titleColIndex = mCursor.getColumnIndex(
                        ProductsDatabaseContract.PRODUCT_NAME_COLUMN);
                int priceColIndex = mCursor.getColumnIndex(
                        ProductsDatabaseContract.PRICE_COLUMN);
                int imageColIndex = mCursor.getColumnIndex(
                        ProductsDatabaseContract.PICTURE_URL_COLUMN);
                int idColIndex = mCursor.getColumnIndex(
                        ProductsDatabaseContract._ID);

                String title = mCursor.getString(titleColIndex);
                double price = mCursor.getDouble(priceColIndex);
                String imageUrl = mCursor.getString(imageColIndex);
                long id = mCursor.getLong(idColIndex);
                mProductList.add(new ProductModel(title, price, imageUrl, id));
                mCursor.moveToNext();
            }
        }
    }
}
