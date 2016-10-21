package ru.noties.avt;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class NotifyDataSetChanged<T> implements OnDataSetChangedListener<T> {

    @Override
    public void onDataSetChanged(ViewTypesAdapter adapter, List<T> oldItems, List<T> newItems) {
        adapter.changeItems(newItems);
        adapter.notifyDataSetChanged();
    }
}
