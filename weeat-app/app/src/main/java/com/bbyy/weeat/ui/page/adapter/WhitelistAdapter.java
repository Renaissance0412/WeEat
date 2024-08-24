package com.bbyy.weeat.ui.page.adapter;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bbyy.weeat.R;
import com.bbyy.weeat.models.bean.WhitelistItem;

import java.util.List;

public class WhitelistAdapter extends RecyclerView.Adapter<WhitelistAdapter.TextViewHolder> {
    private final List<WhitelistItem> app_names;

    public WhitelistAdapter(List<WhitelistItem> app_names) {
        this.app_names=app_names;
    }

    @NonNull
    @Override
    public WhitelistAdapter.TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WhitelistAdapter.TextViewHolder holder, int position) {
        WhitelistItem item=app_names.get(position);
        holder.textView.setText(item.getAppName());
        holder.imageView.setImageDrawable(item.getIcon());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> item.setSelected(isChecked));
    }

    @Override
    public int getItemCount() {
        return app_names.size();
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        final ImageView imageView;
        final CheckBox checkBox;
        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.app_name);
            imageView=itemView.findViewById(R.id.app_icon);
            checkBox=itemView.findViewById(R.id.check);
        }
    }

    public List<WhitelistItem> getSelectedList(){
        return app_names;
    }
}
