package com.bbyy.weeat.ui.page.adapter;


import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bbyy.weeat.R;
import com.bbyy.weeat.models.bean.TodoItem;
import com.bbyy.weeat.utils.ViewUtils;
import com.bbyy.weeat.viewModels.TodoViewModel;

public class TodoAdapter extends ListAdapter<TodoItem, TodoAdapter.TodoViewHolder> {
    private final TodoViewModel viewModel;
    public TodoAdapter(TodoViewModel viewModel) {
        super(new DiffUtil.ItemCallback<TodoItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull TodoItem oldItem, @NonNull TodoItem newItem) {
                return oldItem.getId().equals(newItem.getId());
            }
            @Override
            public boolean areContentsTheSame(@NonNull TodoItem oldItem, @NonNull TodoItem newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.viewModel=viewModel;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.itemView.setTag(R.id.item_for_view_holder,getItem(position));
        holder.todo.setText(getItem(position).getTodo());
        if(getItem(position).getFinished()==0){
            holder.checkBox.setChecked(false);
            holder.todo.setPaintFlags(holder.todo.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.checkBox.setChecked(true);
            holder.todo.setPaintFlags(holder.todo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            TodoItem new_item=(TodoItem) holder.itemView.getTag(R.id.item_for_view_holder);
            new_item.setFinished(isChecked?1:0);
            viewModel.updateItem(holder.getAdapterPosition(),new_item);
            Log.d("add","position:"+holder.getAdapterPosition()+" finished:"+new_item.getFinished());
            if (isChecked) {
                holder.todo.setPaintFlags(holder.todo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.todo.setPaintFlags(holder.todo.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });
        holder.todo.setOnLongClickListener(v -> {
            ViewUtils.showEditDialog(holder.todo.getContext(), (content, timestamp) -> {
                TodoItem new_item = (TodoItem) holder.itemView.getTag(R.id.item_for_view_holder);
                new_item.setTodo(content);
                viewModel.updateItem(holder.getAdapterPosition(), new_item);
                holder.todo.setText(content);
            }, ViewUtils.DIALOG_TYPE.TODO,0);
            return false;
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.deleteItem(position,(TodoItem) holder.itemView.getTag(R.id.item_for_view_holder));
                //这里一定要手动调用，否则position不会更新，会引起错误
                notifyDataSetChanged();
            }
        });
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder{
        private final CheckBox checkBox;
        private final TextView todo;
        private final View delete;
        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkBox);
            todo=itemView.findViewById(R.id.todo);
            delete=itemView.findViewById(R.id.delete);
        }
    }

    public void removeItem(int position){
        // 通知适配器进行局部刷新
        notifyItemRemoved(position);
    }
}
