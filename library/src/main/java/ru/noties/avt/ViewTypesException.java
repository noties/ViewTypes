package ru.noties.avt;

class ViewTypesException extends IllegalStateException {

    static ViewTypesException newInstance(String msg, Object... args) {
        return new ViewTypesException(String.format(msg, args));
    }

    private ViewTypesException(String s) {
        super(s);
    }
}
