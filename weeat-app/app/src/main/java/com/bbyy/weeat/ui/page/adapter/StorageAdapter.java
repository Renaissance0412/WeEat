package com.bbyy.weeat.ui.page.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bbyy.weeat.R;
import com.bbyy.weeat.models.bean.StorageItem;
import com.bbyy.weeat.models.bean.StorageItem;
import com.bbyy.weeat.utils.DefaultInterface;
import com.bbyy.weeat.utils.ViewUtils;
import com.bbyy.weeat.viewModels.StorageViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class StorageAdapter extends ListAdapter<StorageItem, StorageAdapter.StorageViewHolder> {
    private final StorageViewModel viewModel;
    private DefaultInterface.OnListClickListener listener;
    public StorageAdapter(StorageViewModel viewModel, DefaultInterface.OnListClickListener listener) {
        super(new DiffUtil.ItemCallback<StorageItem>() {
            //粗判断id是否一致
            @Override
            public boolean areItemsTheSame(@NonNull StorageItem oldItem, @NonNull StorageItem newItem) {
                return oldItem.getId()==(newItem.getId());
            }
            //判断内容是否一致
            @Override
            public boolean areContentsTheSame(@NonNull StorageItem oldItem, @NonNull StorageItem newItem) {
                //return oldItem.equals(newItem);
                return false;
            }
        });
        this.viewModel=viewModel;
        this.listener=listener;
    }


    @NonNull
    @Override
    public StorageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_storage, parent, false);
        return new StorageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StorageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        StorageItem item=getItem(position);
        String deadline;
        switch (item.getRemainDays()){
            case 1:
                deadline=item.getRemainDays()+" day left";
                break;
            case 0:
                deadline="Expired";
                break;
            default:
                deadline=item.getRemainDays()+" days left";
                break;
        }
        holder.deadline.setText(deadline);
        holder.name.setText(item.getFoodName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.showDialog(view.getContext(), new DefaultInterface.OnDialogConfirmListener() {
                    @Override
                    public void onConfirm(String content, long timestamp) {
                        viewModel.deleteItem(position,item);
                        notifyItemRemoved(position);
                    }
                }, ViewUtils.DIALOG_TYPE.DELETE);
            }
        });
        while(item.getImagePath()==null){}
        Glide.with(holder.item)
                .load(item.getImagePath())
                .transform(new RoundedCorners(40))
                .into(holder.image);
    }

    public static class StorageViewHolder extends RecyclerView.ViewHolder{
        private final TextView name;
        private final TextView deadline;
        private final ImageView image;
        private final View item;
        private final View delete;
        public StorageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.item=itemView;
            this.name=itemView.findViewById(R.id.food_name);
            this.deadline=itemView.findViewById(R.id.deadline);
            this.image=itemView.findViewById(R.id.food_image);
            this.delete=itemView.findViewById(R.id.delete);
        }
    }
}
