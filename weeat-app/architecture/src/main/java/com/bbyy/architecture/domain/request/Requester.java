package com.bbyy.architecture.domain.request;

import androidx.lifecycle.ViewModel;

/**
 * "领域层组件" 中应当只关注数据的生成，而不关注数据的使用，
 * 改变 UI 状态的逻辑代码，只应在表现层页面中编写、在 Observer 回调中响应数据的变化，
 *
 * Activity {
 *  onCreate(){
 *   vm.livedata.observe { result->
 *     if(result.show)
 *       panel.visible(VISIBLE)
 *     else
 *       panel.visible(GONE)
 *     tvTitle.setText(result.title)
 *     tvContent.setText(result.content)
 *   }
 * }
 *
 * Activity {
 *  onCreate(){
 *   request.observe {result ->
 *     is Event ? -> execute one time
 *     is State ? -> BehaviorSubject setValue and notify
 *   }
 * }
 */
public class Requester extends ViewModel {
}
