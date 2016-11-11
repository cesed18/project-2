package ly.generalassemb.project2.product.list.presenter;

import android.content.res.Configuration;
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

/**
 * Created by alanjcaceres on 7/13/16.
 */

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<DataBinder<ProductModel>> {

    private Cursor mCursor;
    private List<ProductModel> mInternalData = new ArrayList<>();
    private RecyclerViewOnItemClickListener mListener;

    public ProductRecyclerViewAdapter(){}

    public ProductRecyclerViewAdapter(Cursor cursor,
                                      RecyclerViewOnItemClickListener listener){
        mListener = listener;
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
                mInternalData.add(new ProductModel(title, price, imageUrl, id));
                mCursor.moveToNext();
            }
        }
    }

    @Override
    public DataBinder onCreateViewHolder(ViewGroup parent, int viewType) {
        int orientation = parent.getResources().getConfiguration().orientation;
        View view;
        switch(orientation){
            default:
            case Configuration.ORIENTATION_PORTRAIT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_item_row, parent, false);
                return new ProductRowViewHolder(view);
            case Configuration.ORIENTATION_LANDSCAPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_item_card, parent, false);
                return new ProductCardViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(DataBinder holder, final int position) {
        ProductModel model = mInternalData.get(position);
        holder.bindData(model);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInternalData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener){
        mListener = listener;
    }

    public void setCursor(Cursor cursor){
        mInternalData.clear();
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
                mInternalData.add(new ProductModel(title, price, imageUrl, id));
                mCursor.moveToNext();
            }
        notifyDataSetChanged();
        }
    }
}
