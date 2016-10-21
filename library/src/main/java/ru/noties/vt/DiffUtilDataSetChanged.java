package ru.noties.vt;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class DiffUtilDataSetChanged<T> implements OnDataSetChangedListener<T> {

    public static abstract class ItemsChecker<T> {

        public abstract boolean areItemsTheSame(T oldItem, T newItem);
        public abstract boolean areContentsTheSame(T oldItem, T newItem);

        @Nullable
        public Object getChangePayload(T oldItem, T newItem) {
            return null;
        }
    }

    public static class SimpleItemsChecker<T> extends ItemsChecker<T> {

        @Override
        public boolean areItemsTheSame(T oldItem, T newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(T oldItem, T newItem) {
            return (oldItem == null && newItem == null)
                    || (oldItem != null && newItem != null && oldItem.equals(newItem));
        }
    }

    private final ItemsChecker mItemsChecker;
    private final boolean mDetectMoves;

    public DiffUtilDataSetChanged() {
        this(new SimpleItemsChecker<T>(), false);
    }

    public DiffUtilDataSetChanged(boolean detectMoves) {
        this(new SimpleItemsChecker<T>(), detectMoves);
    }

    public DiffUtilDataSetChanged(ItemsChecker<T> itemsChecker) {
        this(itemsChecker, true);
    }

    public DiffUtilDataSetChanged(ItemsChecker<T> itemsChecker, boolean detectMoves) {
        mItemsChecker = itemsChecker;
        mDetectMoves = detectMoves;
    }

    @Override
    public void onDataSetChanged(ViewTypesAdapter adapter, final List<T> oldItems, final List<T> newItems) {
        final int oldSize = oldItems != null ? oldItems.size() : 0;
        final int newSize = newItems != null ? newItems.size() : 0;
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldSize;
            }

            @Override
            public int getNewListSize() {
                return newSize;
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                //noinspection ConstantConditions,unchecked
                return mItemsChecker.areItemsTheSame(
                        oldItems.get(oldItemPosition),
                        newItems.get(newItemPosition)
                );
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                //noinspection ConstantConditions,unchecked
                return mItemsChecker.areContentsTheSame(
                        oldItems.get(oldItemPosition),
                        newItems.get(newItemPosition)
                );
            }

            @Nullable
            @Override
            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                //noinspection ConstantConditions,unchecked
                return mItemsChecker.getChangePayload(
                        oldItems.get(oldItemPosition),
                        newItems.get(newItemPosition)
                );
            }
        }, mDetectMoves);
        adapter.changeItems(newItems);
        result.dispatchUpdatesTo(adapter);
    }
}
