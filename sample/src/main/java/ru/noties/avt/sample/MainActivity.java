package ru.noties.avt.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.noties.avt.DiffUtilDataSetChanged;
import ru.noties.avt.Holder;
import ru.noties.avt.HolderSingle;
import ru.noties.avt.NotifyDataSetChanged;
import ru.noties.avt.OnItemClickListener;
import ru.noties.avt.ViewTypesAdapter;
import ru.noties.debug.Debug;
import ru.noties.debug.out.AndroidLogDebugOutput;

public class MainActivity extends AppCompatActivity {

    static {
        Debug.init(new AndroidLogDebugOutput(true));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewTypesAdapter<One> adapter = ViewTypesAdapter.builder(One.class)
                .register(One.class, new ViewTypeOne(), null) // will be not clickable
                .register(Two.class, new ViewTypeTwo(), new OnItemClickListener<Two, HolderSingle<TextView>>() {
                    @Override
                    public void onItemClick(Two item, HolderSingle<TextView> holder) {

                    }
                })
                .register(Three.class, new ViewTypeOne())
                .setHasStableIds(true)
//                .registerOnDataSetChangedListener(new DiffUtilDataSetChanged<One>(true))
                .registerOnDataSetChangedListener(new NotifyDataSetChanged<One>())
                .registerOnClickListener(new OnItemClickListener<One, Holder>() {
                    @Override
                    public void onItemClick(One item, Holder holder) {
                        Debug.i("class: `%s`, value: %s", item.getClass().getSimpleName(), item.value);
                    }
                })
                .build(this);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final List<One> list;
                {
                    final List current = adapter.getItems();
                    if (current == null
                            || current.size() == 0) {
                        // generate new
                        list = new ArrayList<>();
                        for (int i = 0; i < 10; i++) {
                            list.add(new One("Item " + i));
                            list.add(new Two("Item " + i * 2));
                            list.add(new Three("Item " + i * 3));
                        }
                    } else {
                        //noinspection unchecked
                        list = new ArrayList<>(current);
                        Collections.shuffle(list);
                    }
                }
                adapter.setItems(list);
                handler.postDelayed(this, 2500L);
            }
        }, 2500L);
    }

    class One {
        String value;
        One(String value) {
            this.value = value;
        }
    }

    class Two extends One {
        Two(String value) {
            super(value);
        }
    }

    class Three extends Two {

        Three(String value) {
            super(value);
        }
    }
}
