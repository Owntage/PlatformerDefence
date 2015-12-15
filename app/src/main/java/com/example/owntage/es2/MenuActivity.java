package com.example.owntage.es2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owntage on 11/28/2015.
 */
public class MenuActivity extends Activity {
    private final static int N = 20;
    private final static String TAG = MenuActivity.class.getSimpleName();

    private TextView textView;
    private final List<String> lst = new ArrayList<String>();
    private RecyclerView recyclerView;
    private SimpleRecycleAdapter adapter;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.menu_layout);

        textView = (TextView) findViewById(R.id.txt);

        for (int i = 0; i < N; i++) {
            lst.add("Level" + i);
        }
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleRecycleAdapter(this, lst);
        recyclerView.setAdapter(adapter);
        /*

        level1.setOnClickListener(new MyOnClickListener(1));
        level2.setOnClickListener(new MyOnClickListener(2));*/

    }

    private class MyOnClickListener implements View.OnClickListener {
        int levelNumber;

        public MyOnClickListener(int levelNumber) {
            this.levelNumber = levelNumber;
        }

        public void onClick(View v) {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            intent.putExtra("level_number", levelNumber);
            startActivity(intent);
        }
    }

    private class SimpleRecycleAdapter extends RecyclerView.Adapter<SimpleRecycleAdapter.ViewHolder> {
        private final List<String> items;
        private final LayoutInflater li;

        private SimpleRecycleAdapter(Context context, List<String> items) {
            li = LayoutInflater.from(context);
            this.items = items;
            setHasStableIds(true);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(li.inflate(R.layout.item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String str = items.get(position);
            holder.firstLine.setText(str);
            holder.firstLine.setOnClickListener(new MyOnClickListener(position));
        }

        @Override
        public long getItemId(int position) {
            return items.get(position).hashCode();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final Button firstLine;
            //final TextView secondLine;

            public ViewHolder(View itemView) {
                super(itemView);

                firstLine = (Button) itemView.findViewById(R.id.fist_line);
                //secondLine = (TextView)itemView.findViewById(R.id.second_line);
            }
        }
    }
}
