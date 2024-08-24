package com.bbyy.weeat.ui.page.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.ContentInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bbyy.weeat.R;
import com.bbyy.weeat.models.bean.TalkItem;
import com.bbyy.weeat.models.bean.event.GenerateEvent;
import com.bbyy.weeat.utils.ViewUtils;
import com.bbyy.weeat.viewModels.TalkViewModel;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class TalkAdapter extends ListAdapter<TalkItem,TalkAdapter.TalkViewHolder> {
    private static MediaPlayer mediaPlayer;
    private final TalkViewModel viewModel;
    private static WeakReference<Context> context;
    public TalkAdapter(TalkViewModel viewModel) {
        super(new DiffUtil.ItemCallback<TalkItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull TalkItem oldItem, @NonNull TalkItem newItem) {
                return oldItem.getIndex()==newItem.getIndex();
            }

            @Override
            public boolean areContentsTheSame(@NonNull TalkItem oldItem, @NonNull TalkItem newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.viewModel=viewModel;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @NonNull
    @Override
    public TalkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_talk, parent, false);
        context=new WeakReference<>(parent.getContext());
        return new TalkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TalkViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindItem(getItem(position));
        Log.d("test"," bind "+position);
    }

    public static class TalkViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        ImageView image;
        LinearLayout bubble;
        CardView cardView;
        Button button;
        View voice;
        Boolean isPlaying;
        AnimationDrawable animationDrawable;
        int position;
        @SuppressLint("UseCompatLoadingForDrawables")
        public TalkViewHolder(@NonNull View itemView) {
            super(itemView);
            position=getAbsoluteAdapterPosition();
            text=itemView.findViewById(R.id.text);
            image=itemView.findViewById(R.id.image);
            bubble=itemView.findViewById(R.id.talk_bubble);
            cardView=itemView.findViewById(R.id.cardView);
            button=itemView.findViewById(R.id.button);
            voice=itemView.findViewById(R.id.voice);
            animationDrawable=(AnimationDrawable) itemView.getResources().getDrawable(R.drawable.animation_voice);
            isPlaying=false;
        }

        public void bindItem(TalkItem item){
            Log.d("test"," bind "+item.isFromMe());
            if (!item.isFromMe()) {
                bubble.setGravity(Gravity.START);
                cardView.setCardBackgroundColor(Color.WHITE);
            }else{
                bubble.setGravity(Gravity.END);
                cardView.setCardBackgroundColor(Color.parseColor("#fdf0cc"));
            }
            if(item.isKeyText()){
                cardView.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new GenerateEvent(true));
                    }
                });
            }
            if(item.getText()!=null){
                text.setText(item.getText());
            }else{
                text.setVisibility(View.GONE);
            }
            if(item.getBitmap()!=null){
                image.setImageBitmap(item.getBitmap());
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewUtils.showImageDialog(view.getContext(),item.getBitmap());
                    }
                });
            }
            if(item.getMediaPath()!=null){
                voice.setVisibility(View.VISIBLE);
                voice.setBackground(animationDrawable);
                voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isPlaying=!isPlaying;
                        playAudio(item.getMediaPath(),isPlaying,animationDrawable);
                    }
                });
            }
        }
    }

    private static void playAudio(String filePath, Boolean isPlaying, AnimationDrawable anim){
        if (mediaPlayer!= null) {
            mediaPlayer.release();
        }
        if(!isPlaying){
            anim.stop();
            return;
        }
        anim.start();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                anim.stop();
            }
        });
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}