package com.bbyy.architecture.ui.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.kunminx.architecture.ui.page.DataBindingFragment;
import com.kunminx.architecture.ui.scope.ViewModelScope;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public abstract class BaseFragment extends DataBindingFragment {

    private final ViewModelScope mViewModelScope = new ViewModelScope();

    //TODO tip 1: DataBinding 严格模式（详见 DataBindingFragment - - - - - ）：
    // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
    // 通过这方式，彻底解决 View 实例 Null 安全一致性问题，
    // 如此，View 实例 Null 安全性将和基于函数式编程思想的 Jetpack Compose 持平。

    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

    //TODO tip 2: Jetpack 通过 "工厂模式" 实现 ViewModel 作用域可控，
    //目前我们在项目中提供了 Application、Activity、Fragment 三个级别的作用域，
    //值得注意的是，通过不同作用域 Provider 获得 ViewModel 实例非同一个，
    //故若 ViewModel 状态信息保留不符合预期，可从该角度出发排查 是否眼前 ViewModel 实例非目标实例所致。

    //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6257931840

    protected <T extends ViewModel> T getFragmentScopeViewModel(@NonNull Class<T> modelClass) {
        return mViewModelScope.getFragmentScopeViewModel(this, modelClass);
    }

    protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
        return mViewModelScope.getActivityScopeViewModel(mActivity, modelClass);
    }

    protected <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
        return mViewModelScope.getApplicationScopeViewModel(modelClass);
    }

    protected NavController nav() {
        return NavHostFragment.findNavController(this);
    }

    protected void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected void openUrlInBrowser(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    protected Context getApplicationContext() {
        return mActivity.getApplicationContext();
    }

}
