package ru.noties.vt;

@SuppressWarnings("WeakerAccess")
public interface OnItemClickListener<T, H extends Holder> {

    // we won't be providing the position, as it's error-prone with RecyclerView
    // it's better to just pass an item (because as I understand it's still the most
    // commonly used thing (to just retrieve an element from some data set)

    // maybe, let's also pass our holder (so, if a view of this item is needed,
    // caller can access it via `itemView` call
    void onItemClick(T item, H holder);
}
