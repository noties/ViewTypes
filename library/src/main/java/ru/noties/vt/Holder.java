package ru.noties.vt;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

@SuppressWarnings("WeakerAccess")
public class Holder extends RecyclerView.ViewHolder {

    public Holder(View itemView) {
        super(itemView);
    }

    public <V extends View> V findView(@IdRes int id) {
        //noinspection unchecked
        return (V) itemView.findViewById(id);
    }
}
