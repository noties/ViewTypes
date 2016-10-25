package ru.noties.vt.sample;

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

import ru.noties.vt.Holder;
import ru.noties.vt.HolderSingle;
import ru.noties.vt.NotifyDataSetChanged;
import ru.noties.vt.OnItemClickListener;
import ru.noties.vt.ViewTypesAdapter;
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
                        Debug.i("class: `%s`, value: %s", item.getClass().getSimpleName(), item.oneValue);
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
                            list.add(new Two("Item " + i * 2, i));
                            list.add(new Three("Item " + i * 3, i, 2.F * i));
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

        String oneValue;

        One(String oneValue) {
            this.oneValue = oneValue;
        }
    }

    class Two extends One {

        int twoValue;

        Two(String value, int twoValue) {
            super(value);
            this.twoValue = twoValue;
        }
    }

    class Three extends Two {

        float threeValue;

        Three(String value, int twoValue, float threeValue) {
            super(value, twoValue);
            this.threeValue = threeValue;
        }
    }
}
