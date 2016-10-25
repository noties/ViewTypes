package ru.noties.vt;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ViewTypes {

    private final int[] mClasses;
    private final ViewType[] mViewTypes;

    ViewTypes(@NonNull Collection<ViewTypeItem> items) {
        final List<ViewTypeItem> viewTypeItems = new ArrayList<>(items);
        final int size = viewTypeItems.size();
        Collections.sort(viewTypeItems, new ViewTypeItemComparator());
        final int[] classes = new int[size];
        final ViewType[] viewTypes = new ViewType[size];
        ViewTypeItem viewTypeItem;
        for (int i = 0; i < size; i++) {
            viewTypeItem = viewTypeItems.get(i);
            classes[i] = viewTypeItem.mClassHash;
            viewTypes[i] = viewTypeItem.mViewType;
        }
        mClasses = classes;
        mViewTypes = viewTypes;
    }

    public boolean supportsDataSet(List objects) {

        if (objects == null
                || objects.size() == 0) {
            // empty list is ok
            return true;
        }

        try {
            // todo, i think we need a simple way to store previously checked types
            // so we don't iterate over all collection checking EACH object
            // for example in case
            for (Object o: objects) {
                assignedViewType(o);
            }
            return true;
        } catch (ViewTypesException e) {
            return false;
        }
    }

    public boolean supports(Class<?> first, Class<?>... others) {

        if (first == null) {
            return false;
        }

        try {
            // as this method throws if we are not registered
            // no need to check for `< 0`
            assignedViewType(first);

            final int othersLength = others != null ? others.length : 0;
            if (othersLength > 0) {
                for (int i = 0; i < othersLength; i++) {
                    assignedViewType(others[i]);
                }
            }
            return true;
        } catch (ViewTypesException e) {
            return false;
        }
    }

    public int viewTypeCount() {
        return mClasses.length;
    }

    public ViewType viewType(@NonNull Object item) throws ViewTypesException {
        return mViewTypes[assignedViewType(item)];
    }

    public ViewType viewType(int assignedViewType) throws ViewTypesException {
        if (assignedViewType < 0 || assignedViewType >= mViewTypes.length) {
            throw ViewTypesException.newInstance("AssignedViewType `%d` is not within bounds of this ViewTypes", assignedViewType);
        }
        return mViewTypes[assignedViewType];
    }

    public int assignedViewType(@NonNull Object item) throws ViewTypesException {
        return assignedViewType(item.getClass());
    }

    public int assignedViewType(@NonNull Class<?> cl) throws ViewTypesException {
        final int index = Arrays.binarySearch(mClasses, cl.hashCode());
        if (index < 0) {
            throw ViewTypesException.newInstance("Class `%s` is not registered with this ViewTypes", cl.getName());
        }

        return index;
    }

    private static class ViewTypeItemComparator implements Comparator<ViewTypeItem> {

        ViewTypeItemComparator() {}

        @Override
        public int compare(ViewTypeItem lhs, ViewTypeItem rhs) {
            return compare(lhs.mClassHash, rhs.mClassHash);
        }

        private static int compare(int lhs, int rhs) {
            return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
        }
    }
}
