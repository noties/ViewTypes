package ru.noties.avt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class ViewTypesAdapter<T> extends RecyclerView.Adapter<Holder> {

    public static <T> ViewTypesAdapter.Builder<T> builder(Class<T> base) {
        return new Builder<>(base);
    }

    private final Context mContext;
    private final LayoutInflater mInflater;

    private final ViewTypes mViewTypes;
    private final OnDataSetChangedListener<T> mOnDataSetChangedListener;
    private final OnItemClickListener<Object, Holder> mOnItemClickListener;
    private final Map<Class<?>, OnItemClickListener<?, Holder>> mOnClickListeners;

    private List mItems;

    private ViewTypesAdapter(Context context, Builder<T> builder) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mViewTypes = new ViewTypes(builder.mViewTypeItems);
        mOnDataSetChangedListener = builder.mOnDataSetChangedListener;
        mOnItemClickListener = builder.mOnItemClickListener;
        mOnClickListeners = builder.mOnClickListeners;

        setHasStableIds(builder.mHasStableIds);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int itemViewType) {
        final ViewType viewType = mViewTypes.viewType(itemViewType);
        return viewType.createView(mInflater, parent);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        onBindViewHolder(holder, position, null);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position, List<Object> payloads) {

        final Object item = getItem(position);
        final ViewType viewType = mViewTypes.viewType(item);
        //noinspection unchecked
        viewType.bindView(mContext, holder, item, payloads);

        // now, evaluate on click listeners
        // first check if this viewType has specific one

        final View.OnClickListener listener;
        {
            //noinspection SuspiciousMethodCalls
            final OnItemClickListener specificListener = mOnClickListeners.get(item.getClass());
            if (specificListener != null) {
                // no check if it's NULL one
                if (specificListener == NULL_LISTENER) {
                    // set to NULL
                    listener = null;
                } else {
                    listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //noinspection unchecked
                            specificListener.onItemClick(item, holder);
                        }
                    };
                }
            } else if (mOnItemClickListener != null) {
                listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(item, holder);
                    }
                };
            } else {
                listener = null;
            }
        }
        holder.itemView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        //noinspection unchecked
        return viewType(position).itemId(getItem(position));
    }

    @Override
    public int getItemViewType(int position) {
        return mViewTypes.assignedViewType(getItem(position));
    }

    public ViewTypes viewTypes() {
        return mViewTypes;
    }

    // this is method call that will trigger notification update
    // to actually change items one must call `changeItems` that will not
    // trigger notification (to be used for example by OnDataSetChangedListener)
    // although the list is Nullable itself, if it's not each item must not be null
    public <ITEM extends T> void setItems(@Nullable List<ITEM> items) {
        //noinspection unchecked
        mOnDataSetChangedListener.onDataSetChanged(
                this,
                (List<T>) mItems,
                (List<T>) items
        );
    }

    public <ITEM extends T> void changeItems(List<ITEM> items) {
        mItems = items;
    }


    @Nullable
    public List<T> getItems() {
        //noinspection unchecked
        return mItems;
    }

    public <ITEM extends T> ITEM getItem(int position) {
        //noinspection unchecked
        return (ITEM) mItems.get(position);
    }

    public <ITEM extends T> ITEM getItemAs(int position, Class<ITEM> itemClass) {
        //noinspection unchecked
        return (ITEM) mItems.get(position);
    }

    private ViewType viewType(int position) {
        return mViewTypes.viewType(getItem(position));
    }


    public static class Builder<T> {

        final Set<ViewTypeItem> mViewTypeItems;
        final Map<Class<?>, OnItemClickListener<?, Holder>> mOnClickListeners;

        OnDataSetChangedListener<T> mOnDataSetChangedListener;
        OnItemClickListener<Object, Holder> mOnItemClickListener;

        boolean mHasStableIds;

        boolean mIsBuilt;

        public Builder(Class<T> base) {
            mViewTypeItems = new HashSet<>(2);
            mOnClickListeners = new HashMap<>(2);
        }

        private void add(Class cl, ViewType viewType) {
            if (!mViewTypeItems.add(new ViewTypeItem(cl.hashCode(), viewType))) {
                throw ViewTypesException.newInstance("Adding ViewType for already registered Class: `%s`", cl.getName());
            }
        }

        // so we can register, for example:
        //  * register(String.class, ViewType<String, Holder>)
        //  * register(String.class, ViewType<CharSequence, Holder>)
        public <VIEW_TYPE_TYPE extends T, ACTUAL_TYPE extends VIEW_TYPE_TYPE, HOLDER extends Holder> Builder<T> register(
                @NonNull Class<ACTUAL_TYPE> itemClass,
                @NonNull ViewType<VIEW_TYPE_TYPE, HOLDER> viewType
        ) {
            add(itemClass, viewType);
            return this;
        }

        // if you wish to make an item non-clickable, but still providing the default listener
        // for some other, just call this method with NULL as a listener
        public <VIEW_TYPE_TYPE extends T, ACTUAL_TYPE extends VIEW_TYPE_TYPE, ON_CLICK_TYPE extends T, HOLDER extends Holder> Builder<T> register(
                @NonNull Class<ACTUAL_TYPE> itemClass,
                @NonNull ViewType<VIEW_TYPE_TYPE, HOLDER> viewType,
                @Nullable OnItemClickListener<ON_CLICK_TYPE, HOLDER> click
        ) {

            add(itemClass, viewType);

            if (click == null) {
                //noinspection unchecked
                mOnClickListeners.put(itemClass, NULL_LISTENER);
            } else {
                //noinspection unchecked
                mOnClickListeners.put(itemClass, (OnItemClickListener<?, Holder>) click);
            }

            return this;
        }

        // registers default on click listener
        public <HOLDER extends Holder> Builder<T> registerOnClickListener(OnItemClickListener<T, HOLDER> click) {
            //noinspection unchecked
            mOnItemClickListener = (OnItemClickListener<Object, Holder>) click;
            return this;
        }

        public Builder<T> registerOnDataSetChangedListener(OnDataSetChangedListener<T> onDataSetChangedListener) {
            mOnDataSetChangedListener = onDataSetChangedListener;
            return this;
        }

        public Builder<T> setHasStableIds(boolean hasStableIds) {
            mHasStableIds = hasStableIds;
            return this;
        }

        public ViewTypesAdapter<T> build(@NonNull Context context) throws ViewTypesException {

            // in order to provide consistency (no changes to underlying collections)
            // we allow to build only once
            if (mIsBuilt) {
                throw ViewTypesException.newInstance("This builder has already been built");
            }

            // it doesn't make sense to have zero types
            if (mViewTypeItems.size() == 0) {
                throw ViewTypesException.newInstance("Cannot create ViewTypes with no ViewType's specified. Add at least one.");
            }

            // if nothing was provided we just assume it's simple `notifyDataSetChanged` call
            if (mOnDataSetChangedListener == null) {
                mOnDataSetChangedListener = new NotifyDataSetChanged<>();
            }

            mIsBuilt = true;

            return new ViewTypesAdapter<>(context, this);
        }
    }

    private static final OnItemClickListener NULL_LISTENER = new OnItemClickListener() {
        @Override
        public void onItemClick(Object item, Holder holder) {

        }
    };
}
