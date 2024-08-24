package com.bbyy.weeat.ui.page.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bbyy.weeat.R;
import com.bbyy.weeat.utils.DefaultInterface;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    private final List<String> tags;
    private final DefaultInterface.OnListClickListener listener;
    private int current_position;

    /*
    * 适配器构造函数，用于初始化tags数据
    * */
    public TagAdapter(List<String> tags, DefaultInterface.OnListClickListener listener, int position) {
        this.tags=tags;
        this.listener=listener;
        current_position=position;
    }

    /*
    * 创建viewholder
    * */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        return new ViewHolder(view);
    }

    /*
    * 绑定viewholder中的数据
    * */
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String text=tags.get(position);
        holder.tag.setText(text);
        if(position==current_position){
            holder.tag_view.setBackgroundResource(R.drawable.tag_selected_bg);
            holder.tag.setTextColor(Color.WHITE);
        }
        holder.tag_view.setOnClickListener(v -> {
            current_position=position;
            listener.onClick(position);
        });
    }

    /*
    * 获取数据数量
    * */
    @Override
    public int getItemCount() {
        return tags.size();
    }

    /*
    * 创建viewholder内部类
    * */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tag;
        final View tag_view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tag=itemView.findViewById(R.id.tag);
            tag_view=itemView.findViewById(R.id.tag_view);
        }
    }
}
