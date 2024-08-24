package com.bbyy.weeat.ui.page.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bbyy.weeat.R;
import com.bbyy.weeat.databinding.FragmentTodoBinding;
import com.bbyy.weeat.utils.ViewUtils;
import com.bbyy.weeat.viewModels.TodoListViewModel;

public class TodoListFragment extends Fragment {

    private TodoListViewModel viewModel;
    private FragmentTodoBinding binding;
    private TodoListAdapter adapter;
    protected static FragmentManager fragmentManager;

    public static TodoListFragment newInstance() {
        return new TodoListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_todo,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        //获取fragment管理器
        fragmentManager= getChildFragmentManager();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel=new ViewModelProvider(requireActivity()).get(TodoListViewModel.class);
        binding.setListNameData(viewModel);
        adapter= new TodoListAdapter();
        binding.timeline.setAdapter(adapter);
        binding.button.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        binding.button.setOnClickListener(v -> {
            Log.d("test","click");
            ViewUtils.showEditDialog(requireActivity(), (content, timestamp) -> viewModel.addList(content), ViewUtils.DIALOG_TYPE.LIST_NAME,0);
        });
        viewModel.getNameList().observe(getViewLifecycleOwner(), strings -> {
            adapter.submitList(strings);
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    TodoListAdapter.TodoListViewHolder holder=(TodoListAdapter.TodoListViewHolder)viewHolder;
                    TextView view= viewHolder.itemView.findViewById(R.id.list_name);
                    int position = viewHolder.getAdapterPosition();
                    viewModel.deleteList(viewHolder.getAdapterPosition(),view.getText().toString());
                    //这里一定要手动调用，否则position不会更新，会引起错误
                    adapter.notifyItemRemoved(position);
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(binding.timeline);
    }

    public class TodoListAdapter extends ListAdapter<String, TodoListAdapter.TodoListViewHolder> {
        public TodoListAdapter() {
            super(new DiffUtil.ItemCallback<String>() {
                @Override
                public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }
                @Override
                public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return false;
                }
            });
        }

        public class TodoListViewHolder extends RecyclerView.ViewHolder{
            private final TextView listName;
            private final FragmentContainerView containerView;
            public TodoListViewHolder(@NonNull View itemView) {
                super(itemView);
                listName=itemView.findViewById(R.id.list_name);
                containerView=itemView.findViewById(R.id.todoContainer);
            }

            public void bind(int position){
                listName.setText(getItem(position));
                listName.setOnClickListener(v -> {
                    //跳转到相应清单
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    Bundle bundle=new Bundle();
                    TodoFragment todoFragment = new TodoFragment();
                    bundle.putString("name",getItem(position));
                    todoFragment.setArguments(bundle);
                    // 判断是否已经显示 TodoFragment，如果是则移除，否则添加
                    Fragment currentFragment = fragmentManager.findFragmentById(R.id.todoContainer);
                    if (currentFragment instanceof TodoFragment) {
                        transaction.remove(currentFragment);
                    } else {
                        transaction.add(R.id.todoContainer, todoFragment);
                    }
                    transaction.commit();
                });
            }
        }
        @NonNull
        @Override
        public TodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //适配item的布局
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_list, parent, false);
            return new TodoListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TodoListViewHolder holder, int position) {
            //view进行适配，包括创建监听等等
            //holder.bind(position);
            holder.listName.setText(getItem(position));
            holder.listName.setOnClickListener(v -> {
                //跳转到相应清单
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Bundle bundle=new Bundle();
                TodoFragment todoFragment = new TodoFragment();
                bundle.putString("name",getItem(position));
                todoFragment.setArguments(bundle);
                // 判断是否已经显示 TodoFragment，如果是则移除，否则添加
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.todoContainer);
                if (currentFragment instanceof TodoFragment) {
                    transaction.remove(currentFragment);
                } else {
                    transaction.add(R.id.todoContainer, todoFragment);
                }
                transaction.commit();
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}