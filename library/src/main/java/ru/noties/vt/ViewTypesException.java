package ru.noties.vt;

class ViewTypesException extends IllegalStateException {

    static ViewTypesException newInstance(String msg, Object... args) {
        return new ViewTypesException(String.format(msg, args));
    }

    private ViewTypesException(String s) {
        super(s);
    }
}
