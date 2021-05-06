package com.example.todolist.ui.slideshow;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    private List<MemoData> list = new ArrayList<>();
    private MemoAdapter memoAdapter = new MemoAdapter(list);
    private Context context;
    private DatabaseHelper dbHelper;
    private List<Long> deadlineList = new ArrayList<>();
    private int objectNum = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.type_2);

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

        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    private void showMemo(Context context) {
        list.clear();

        Calendar calendar = Calendar.getInstance();
        int myear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH) + 1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        long num = 100000000*myear+1000000*(mMonth-1)+10000*(mDay+1);

        dbHelper = new DatabaseHelper(context,"BookStory.db",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Book",null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                long startnum = cursor.getLong(cursor.getColumnIndex("startnum"));
                long endnum = cursor.getLong(cursor.getColumnIndex("endnum"));
                if(startnum<num&&endnum>=num) {
                    objectNum++;
                    deadlineList.add(endnum);
                }
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

        final String CHANNEL_ID = "channel_id_1";
        final String CHANNEL_NAME = "channel_name_1";

        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,CHANNEL_ID);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("今日事务")
                .setContentText("你今天有"+objectNum+"件事情要做哟！")
                .setAutoCancel(true);

        mNotificationManager.notify(1, builder.build());
    }

}