package ru.noties.avt.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.noties.avt.HolderSingle;
import ru.noties.avt.ViewType;

public class ViewTypeTwo extends ViewType<MainActivity.Two, HolderSingle<TextView>> {

    @Override
    protected HolderSingle<TextView> createView(LayoutInflater inflater, ViewGroup parent) {
        return new HolderSingle<TextView>(inflater.inflate(R.layout.adapter_item_two, parent, false));
    }

    @Override
    protected void bindView(Context context, HolderSingle<TextView> holder, MainActivity.Two item, List<Object> payloads) {
        holder.view.setText(item.value);
    }

    @Override
    public long itemId(MainActivity.Two item) {
        return item.value.hashCode();
    }
}
