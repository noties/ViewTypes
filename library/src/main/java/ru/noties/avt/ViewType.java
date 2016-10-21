package ru.noties.avt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public abstract class ViewType<T, H extends Holder> {

    protected abstract H createView(LayoutInflater inflater, ViewGroup parent);

    // context because anyway there is a lot of need for it in binding state
    protected abstract void bindView(Context context, H holder, T item, List<Object> payloads);

    public long itemId(T item) {
        return RecyclerView.NO_ID;
    }
}
