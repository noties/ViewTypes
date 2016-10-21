package ru.noties.vt;

import android.support.annotation.IdRes;
import android.view.View;

public class HolderSingle<V extends View> extends Holder {

    public final V view;

    public HolderSingle(View itemView) {
        super(itemView);
        //noinspection unchecked
        view = (V) itemView;
    }

    public HolderSingle(View itemView, @IdRes int id) {
        super(itemView);
        view = findView(id);
    }
}
