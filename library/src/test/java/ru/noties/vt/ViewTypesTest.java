package ru.noties.vt;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ViewTypesTest {

    @Test
    public void testSingle() {
        final ViewTypes viewTypes = create(Void.class);
        assertEquals(1, viewTypes.viewTypeCount());
        assertEquals(0, viewTypes.assignedViewType(Void.class));
        assertEquals(true, viewTypes.supports(Void.class));
        assertEquals(false, viewTypes.supports(String.class, Double.class));
        assertEquals(false, viewTypes.supports(Void.class, String.class));
    }

    @Test
    public void testMultiple() {
        final ViewTypes viewTypes = create(Integer.class, Long.class, String.class);
        assertEquals(3, viewTypes.viewTypeCount());
        assertEquals(true, viewTypes.supports(Integer.class, Long.class, String.class));
        assertEquals(true, viewTypes.supports(String.class, Integer.class, Long.class));
        assertEquals(false, viewTypes.supports(Short.class, String.class, Integer.class));
        assertEquals(
                true,
                viewTypes.assignedViewType(Integer.class) != viewTypes.assignedViewType(Long.class)
                        && viewTypes.assignedViewType(Long.class) != viewTypes.assignedViewType(String.class)
                        && viewTypes.assignedViewType(Integer.class) != viewTypes.assignedViewType(String.class)
        );
    }

    private static ViewTypes create(Class<?> first, Class<?>... others) {
        final ViewTypes types;
        if (others == null
                || others.length == 0) {
            types = new ViewTypes(Collections.singleton(new ViewTypeItem(first.hashCode(), null)));
        } else {
            final List<ViewTypeItem> list = new ArrayList<>(others.length + 1);
            list.add(new ViewTypeItem(first.hashCode(), null));
            for (Class<?> cl: others) {
                list.add(new ViewTypeItem(cl.hashCode(), null));
            }
            types = new ViewTypes(list);
        }
        return types;
    }
}
