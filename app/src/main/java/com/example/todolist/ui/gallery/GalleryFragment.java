package com.example.todolist.ui.gallery;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import com.example.todolist.R;
import com.example.todolist.logic.DatabaseHelper;
import com.example.todolist.logic.ItemClickListener;
import com.example.todolist.logic.MemoAdapter;
import com.example.todolist.logic.MemoData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private List<MemoData> list = new ArrayList<>();
    private MemoAdapter memoAdapter = new MemoAdapter(list);
    private Context context;
    private DatabaseHelper dbHelper;
    private List<Long> deadlineList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.type_1);
        final RecyclerView recyclerView = root.findViewById(R.id.rv_gallery);

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

        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
                long endnum = cursor.getLong(cursor.getColumnIndex("endnum"));
                deadlineList.add(endnum);
            }while (cursor.moveToNext());
        }
        //cursor.close();
        Collections.sort(deadlineList);
        int size = deadlineList.size();
        for (int i=0;i<size;i++) {
            long value = deadlineList.get(i);
            cursor = db.query("Book",new String[]{"start,endtime,startnum,endnum,main"},"endnum=?",new String[]{String.valueOf(value)},null,null,null);
            cursor.moveToFirst();
            String start = cursor.getString(cursor.getColumnIndex("start"));
            String end = cursor.getString(cursor.getColumnIndex("endtime"));
            String main = cursor.getString(cursor.getColumnIndex("main"));
            list.add(new MemoData(start,end,main));
        }
        cursor.close();
    }
}