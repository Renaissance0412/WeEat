package com.bbyy.weeat.ui.page.fragment;

import static android.app.Activity.RESULT_OK;
import static com.bbyy.weeat.models.config.Const.REQUEST_IMAGE_CAPTURE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bbyy.weeat.R;
import com.bbyy.weeat.databinding.FragmentTalkBinding;
import com.bbyy.weeat.models.api.OkHttpExample;
import com.bbyy.weeat.models.bean.TalkItem;
import com.bbyy.weeat.models.bean.event.GenerateEvent;
import com.bbyy.weeat.models.bean.response.Recipe;
import com.bbyy.weeat.models.config.Const;
import com.bbyy.weeat.ui.page.adapter.TalkAdapter;
import com.bbyy.weeat.utils.ViewUtils;
import com.bbyy.weeat.viewModels.TalkViewModel;
import com.bbyy.weeat.viewModels.TodoListViewModel;
import com.bbyy.weeat.viewModels.TodoViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class TalkFragment extends Fragment {
    private TalkViewModel mViewModel;
    private TodoListViewModel todoListViewModel;
    private FragmentTalkBinding binding;
    private TalkAdapter adapter;
    private Handler handler;
    private LinearLayoutManager layoutManager;
    private MediaRecorder mediaRecorder;
    private AnimationDrawable animationDrawable;

    public static TalkFragment newInstance() {
        return new TalkFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_talk,container,false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler=new Handler();
        if(mViewModel==null)
            initViewModel();
        todoListViewModel=new ViewModelProvider(requireActivity()).get(TodoListViewModel.class);
        binding.setData(mViewModel);    //得先init再setData
        binding.talk.setAdapter(adapter);   //set之前监听livedata？
        Log.d("test"," now size "+mViewModel.getDataList().getValue().size());
        layoutManager=new LinearLayoutManager(requireActivity());
        animationDrawable=(AnimationDrawable) getResources().getDrawable(R.drawable.animation_voice);
        binding.talk.setLayoutManager(layoutManager);
        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 这里需要获取拍照成功回传的图片
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, 1);
            }
        });
        binding.enter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.send.setVisibility(View.VISIBLE);
                } else {
                    binding.send.setVisibility(View.GONE);
                }
            }
        });
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.enter.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(requireActivity(), "Empty message", Toast.LENGTH_SHORT).show();
                } else {
                    mViewModel.addItem(new TalkItem(mViewModel.getDataListSize(),text,null,true));
                    sendMessage(text);
                }
            }
        });
        binding.root.setOnTouchListener(clearFocus);
        binding.talk.setOnTouchListener(clearFocus);
        binding.record.setOnTouchListener(recordListener);
        checkPermission();
    }

    @Subscribe
    public void onEvent(GenerateEvent event){
        ViewUtils.showEditDialog(requireActivity(), (content, timestamp) -> {}, ViewUtils.DIALOG_TYPE.LIST_NAME,0);
    }

    public View.OnTouchListener clearFocus=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (binding.enter.isFocused()) {
                    Rect outRect = new Rect();  //输入控件的rect
                    binding.enterBox.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {  //点击了输入控件之外的地方
                        binding.enter.clearFocus(); //清空焦点
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(binding.enter.getWindowToken(), 0);
                    }
                }
            }
            return false;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            TalkItem item = new TalkItem(mViewModel.getDataListSize(),null,(Bitmap) data.getExtras().get("data"),true);
            mViewModel.addItem(item);
            File folder = new File(Const.IMAGE_PATH);
            File file = new File(folder, System.currentTimeMillis()+"");
            String imagePath=file.getAbsolutePath();
            Glide.with(this)
                    .asBitmap()
                    .load((Bitmap) data.getExtras().get("data"))
                    .transform(new RoundedCorners(40))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                // 使用 JPEG 格式压缩，质量为 60
                                resource.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                                //fos.flush();
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            new Thread(postImageRunnable(imagePath)).start();
        }
    }

    public void addVoice(int index){
        String path=Const.MEDIA_CACHE_PATH + index+".aac";
        TalkItem item=new TalkItem(mViewModel.getDataListSize(),null,null,true);
        item.setMediaPath(path);
        new Thread(postAudioRunnable(item)).start();
    }


    /**
     * 简单的权限申请逻辑
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(requireActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), permissions, 200);
                    return;
                }
            }
        }
    }

    private void startRecordAudio(int index) {
        //文件夹一定要先创建，不然报错的bug信息中是找不到这里的
        File audioFile = new File(Const.MEDIA_CACHE_PATH);
        String path=Const.MEDIA_CACHE_PATH + index+".aac";
        if (!audioFile.exists()) {
            audioFile.mkdirs();
        } else if (!audioFile.isDirectory()) {
            audioFile.delete();
            audioFile.mkdirs();
        }
        File file = new File(path);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置麦克风
            /*
             * 设置保存输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//设置音频文件编码格式
            mediaRecorder.setOutputFile(path);
        }
        try {
            mediaRecorder.prepare();  //start之前要先prepare
            mediaRecorder.start();
        } catch (IllegalStateException el){
            el.printStackTrace();
        } catch (RuntimeException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void stopRecordAudio() {
        //有的5.0机型上MediaRecorder.stop会报错，这里建议抓取一下异常
        if(mediaRecorder !=null){
            try {
                mediaRecorder.stop();//停止录音
                mediaRecorder.release();//释放资源
                mediaRecorder =null;
            }catch (Exception exception){
                mediaRecorder.reset();//重置
                mediaRecorder.release();//释放资源
                mediaRecorder =null;
            }
        }
    }

    public View.OnTouchListener recordListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    binding.listening.setVisibility(View.VISIBLE);
                    binding.listening.setBackground(animationDrawable);
                    animationDrawable.start();
                    startRecordAudio(mViewModel.getDataListSize());
                    break;
                case MotionEvent.ACTION_UP:
                    binding.listening.setVisibility(View.GONE);
                    animationDrawable.stop();
                    stopRecordAudio();
                    addVoice(mViewModel.getDataListSize());
                    break;
                default:
                    break;
            }
            return true;    //这里一定要消费掉事件
        }
    };

    public Runnable postTextRunnable(String text,StringBuilder content,TalkItem item){
        return (new Runnable() {
            @Override
            public void run() {
                OkHttpExample.postRagChatSync(text, Long.toString(mViewModel.getContext_id()), new OkHttpExample.onLineListener() {
                    @Override
                    public void onNewLine(String line) {
                        Log.d("api", " text new line: " + line);
                        content.append(line);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                item.setText(content.toString());
                                mViewModel.updateItem(mViewModel.getDataListSize() - 1, item);
                                adapter.notifyItemChanged(mViewModel.getDataListSize() - 1);//这里要手动notify，submitlist没用
                            }
                        });
                    }
                }, new OkHttpExample.onRecipeListener() {
                    @Override
                    public void onNewList(List<Recipe> list) {
                        todoListViewModel.addGptList(list);
                    }
                });
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("api"," text new line: "+content.toString());
                        item.setText(content.toString());
                        mViewModel.updateItem(mViewModel.getDataListSize()-1,item);
                        adapter.notifyItemChanged(mViewModel.getDataListSize()-1);//这里要手动notify，submitlist没用
                    }
                });
            }
        });
    }



    public Runnable postAudioRunnable(TalkItem item){
        return (new Runnable() {
            @Override
            public void run() {
                OkHttpExample.postAsrSync(item,new OkHttpExample.onLineListener() {
                    @Override
                    public void onNewLine(String line) {
                        Log.d("api",line);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                item.setText(line);
                                mViewModel.addItem(item);
                                mViewModel.updateItem(mViewModel.getDataListSize()-1,item);
                                //这里要手动notify，submitlist没用
                                adapter.notifyDataSetChanged();
                                sendMessage(line);
                            }
                        });
                    }
                });
            }
        });
    }

    public Runnable postImageRunnable(String imagePath){
        return (new Runnable() {
            @Override
            public void run() {
                OkHttpExample.postImageSync(imagePath,new OkHttpExample.onLineListener() {
                    @Override
                    public void onNewLine(String line) {
                        Log.d("api",line);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                sendMessage(line);
                            }
                        });
                    }
                });
            }
        });
    }

    public void initViewModel(){
        mViewModel=new ViewModelProvider(requireActivity()).get(TalkViewModel.class);
        adapter=new TalkAdapter(mViewModel);
        mViewModel.getDataList().observe(getViewLifecycleOwner(), new Observer<List<TalkItem>>() {
            @Override
            public void onChanged(List<TalkItem> talkItems) {
                if(talkItems.size()>0){
                    mViewModel.getIsEmpty().setValue(false);
                }
                adapter.submitList(talkItems);
            }
        });
    }

    public void sendMessage(String text){
        binding.enter.setText(null);
        TalkItem item=new TalkItem(mViewModel.getDataListSize(),"",null,false);
        mViewModel.addItem(item);
        sentPureText(text,item);
        layoutManager.scrollToPosition(mViewModel.getDataListSize()-1);
    }

    public void sentPureText(String text,TalkItem item){
        StringBuilder content=new StringBuilder();
        new Thread(postTextRunnable(text,content,item)).start();
    }
}