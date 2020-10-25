package com.mobileedu33.tutorme.ui.viewmodels;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

public abstract class BaseViewModel extends ViewModel implements LifecycleObserver {
    protected final MutableLiveData<String> messagesLiveData = new MutableLiveData<>();

    protected LiveData<String> getMessagesLiveData() {
        return messagesLiveData;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void executeOnCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void executeOnResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void executeOnPause() {

    }

    public abstract void removeLiveDataObservers(LifecycleOwner lifecycleOwner);
}
