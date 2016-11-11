package ly.generalassemb.project2.product.interfaces;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by alanjcaceres on 7/13/16.
 */

public abstract class DataBinder<T> extends RecyclerView.ViewHolder{

    public DataBinder(View itemView) {
        super(itemView);
    }

    public abstract <T> void bindData(T data);
}
