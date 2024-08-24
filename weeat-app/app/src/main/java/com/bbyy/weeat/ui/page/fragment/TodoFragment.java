package com.bbyy.weeat.ui.page.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bbyy.weeat.R;
import com.bbyy.weeat.databinding.FragmentTodoChildBinding;
import com.bbyy.weeat.models.bean.TodoItem;
import com.bbyy.weeat.databinding.FragmentTodoBinding;
import com.bbyy.weeat.ui.page.adapter.TodoAdapter;
import com.bbyy.weeat.utils.DefaultInterface;
import com.bbyy.weeat.utils.ViewUtils;
import com.bbyy.weeat.viewModels.TodoViewModel;

public class TodoFragment extends Fragment {

    private TodoViewModel viewModel;
    private FragmentTodoChildBinding binding;
    private TodoAdapter adapter;

    public static TodoFragment newInstance() {
        return new TodoFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_todo_child,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //在fragment中获取Activity中的布局控件
        viewModel=new ViewModelProvider(this).get(TodoViewModel.class);
        if(getArguments()!=null){
            viewModel.setName(getArguments().getString("name"));
        }else{
            viewModel.setName("default");
        }
        viewModel.updateList();
        binding.setData(viewModel);
        adapter=new TodoAdapter(viewModel);
        viewModel.getTodoList().observe(getViewLifecycleOwner(), todoItems -> {
            adapter.submitList(todoItems);
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.showEditDialog(requireActivity(), new DefaultInterface.OnDialogConfirmListener() {
                    @Override
                    public void onConfirm(String content, long timestamp) {
                        viewModel.addItem(new TodoItem(String.valueOf(System.currentTimeMillis()), viewModel.getName(), content));
                        adapter.notifyItemInserted(viewModel.getTodoList().getValue().size());
                    }
                }, ViewUtils.DIALOG_TYPE.TODO, 0);

            }
        });
        //注意，将adapter最后set
        binding.timeline.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}