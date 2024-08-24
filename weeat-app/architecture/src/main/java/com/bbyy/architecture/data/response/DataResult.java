package com.bbyy.architecture.data.response;


/**
 * 专用于数据层返回结果至 domain 层或 ViewModel，原因如下：
 * <p>
 * liveData 专用于页面开发、解决生命周期安全问题，
 * 有时数据并非通过 liveData 分发给页面，也可是通过其他方式通知非页面组件，
 * 这时 repo 方法中内定通过 liveData 分发便不合适，不如一开始就规定不在数据层通过 liveData 返回结果。
 */
public class DataResult<T> {

    private final T mEntity;
    private final ResponseStatus mResponseStatus;

    public DataResult(T entity, ResponseStatus responseStatus) {
        mEntity = entity;
        mResponseStatus = responseStatus;
    }

    public DataResult(T entity) {
        mEntity = entity;
        mResponseStatus = new ResponseStatus();
    }

    public T getResult() {
        return mEntity;
    }

    public ResponseStatus getResponseStatus() {
        return mResponseStatus;
    }

    public interface Result<T> {
        void onResult(DataResult<T> dataResult);
    }
}

