package com.example.todolist.ui.home;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.ItemClickListener_edit;
import com.example.todolist.R;
import com.example.todolist.logic.DatabaseHelper;
import com.example.todolist.logic.ItemClickListener;
import com.example.todolist.logic.MemoAdapter;
import com.example.todolist.logic.MemoData;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private List<MemoData> list = new ArrayList<>();
    private MemoAdapter memoAdapter = new MemoAdapter(list);
    private Context context;
    private DatabaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.type);
        final RecyclerView recyclerView = root.findViewById(R.id.rv_home);

        context = root.getContext();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setAdapter(memoAdapter);
        recyclerView.setLayoutManager(layoutManager);
        showMemo(context);

        memoAdapter.setItemClickListener_delete(new ItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                String s = list.get(position).getMain_part();
                list.remove(position);
                memoAdapter.notifyDataSetChanged();
                dbHelper = new DatabaseHelper(context,"BookStory.db",null,1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Book","main=?",new String[] {s});
            }
        });

        memoAdapter.setItemClickListener_edit(new ItemClickListener_edit() {
            @Override
            public void OnClick(View view, int position) {
                //Intent intent = new Intent(context,)
            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    private void showMemo(Context context) {
        list.clear();
        dbHelper = new DatabaseHelper(context,"BookStory.db",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Book",null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                String start = cursor.getString(cursor.getColumnIndex("start"));
                String end = cursor.getString(cursor.getColumnIndex("endtime"));
                String main = cursor.getString(cursor.getColumnIndex("main"));
                list.add(new MemoData(start,end,main));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

}