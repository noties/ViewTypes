package ru.noties.avt;

class ViewTypeItem {

    final int mClassHash;
    final ViewType mViewType;

    ViewTypeItem(int classHash, ViewType viewType) {
        mClassHash = classHash;
        mViewType = viewType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewTypeItem viewTypeItem = (ViewTypeItem) o;

        return mClassHash == viewTypeItem.mClassHash;

    }

    @Override
    public int hashCode() {
        return mClassHash;
    }
}
