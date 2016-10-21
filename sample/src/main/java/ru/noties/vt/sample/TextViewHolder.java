package ru.noties.vt.sample;

import android.support.annotation.IdRes;
import android.view.View;
import android.widget.TextView;

import ru.noties.vt.Holder;

/**
 * Created by Dimitry Ivanov on 24.09.2016.
 */
public class TextViewHolder extends Holder {

    public final TextView text;

    public TextViewHolder(View itemView) {
        this(itemView, 0);
    }

    public TextViewHolder(View itemView, @IdRes int textViewId) {
        super(itemView);
        if (textViewId == 0) {
            text = (TextView) itemView;
        } else {
            text = findView(textViewId);
        }
    }
}
