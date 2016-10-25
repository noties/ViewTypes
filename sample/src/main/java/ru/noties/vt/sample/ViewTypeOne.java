package ru.noties.vt.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ru.noties.vt.ViewType;

/**
 * Created by Dimitry Ivanov on 24.09.2016.
 */
public class ViewTypeOne extends ViewType<MainActivity.One, TextViewHolder> {

    @Override
    public TextViewHolder createView(LayoutInflater inflater, ViewGroup parent) {
        return new TextViewHolder(inflater.inflate(R.layout.adapter_item_one, parent, false));
    }

    @Override
    public void bindView(Context context, TextViewHolder holder, MainActivity.One item, List<Object> payloads) {
        holder.text.setText(item.getClass().getName() + ": " + item.oneValue);
    }

    @Override
    public long itemId(MainActivity.One item) {
        return item.oneValue.hashCode();
    }
}
