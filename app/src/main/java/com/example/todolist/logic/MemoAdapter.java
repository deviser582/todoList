package com.example.todolist.logic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.ItemClickListener_edit;
import com.example.todolist.R;
import com.example.todolist.databinding.MemoItemBinding;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.memoViewHolder> {
    private List<MemoData> list;

    public MemoAdapter(List<MemoData> list){
        this.list = list;
    }

    private ItemClickListener itemClickListener;
    private ItemClickListener_edit itemClickListener_edit;

    class memoViewHolder extends RecyclerView.ViewHolder{
        MemoItemBinding memoItemBinding;

        public memoViewHolder(@NonNull View itemView){
            super(itemView);
            memoItemBinding = DataBindingUtil.bind(itemView);

            memoItemBinding.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null) {
                        itemClickListener.OnClick(v,getPosition());
                    }
                }
            });

            memoItemBinding.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener_edit != null) {
                        itemClickListener_edit.OnClick(v,getPosition());
                    }
                }
            });
        }
    }

    @Override
    public memoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new memoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_item,null));
    }

    @Override
    public void onBindViewHolder(@NonNull memoViewHolder holder,int position){
        MemoData memoData = list.get(position);
        holder.memoItemBinding.startTime.setText(memoData.getStart_time());
        holder.memoItemBinding.deadline.setText(memoData.getDeadline());
        holder.memoItemBinding.mainPart.setText(memoData.getMain_part());
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    public void setItemClickListener_delete(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemClickListener_edit(ItemClickListener_edit itemClickListener_edit) {
        this.itemClickListener_edit = itemClickListener_edit;
    }
}
