package com.mobileedu33.tutorme.data.usecases;

import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class BaseUseCase<SUCCESS_RESULT, ERROR_RESULT> {
    private final BackgroundThreadPoster backgroundThreadPoster;
    private final UiThreadPoster uiThreadPoster;
    private Set<UseCaseListener<SUCCESS_RESULT, ERROR_RESULT>> listeners = new HashSet<>();
    private AtomicBoolean isBusy = new AtomicBoolean(false);

    public BaseUseCase(BackgroundThreadPoster backgroundThreadPoster, UiThreadPoster uiThreadPoster) {
        this.backgroundThreadPoster = backgroundThreadPoster;
        this.uiThreadPoster = uiThreadPoster;
    }

    private void setNotBusy() {
        boolean isSuccessful = isBusy.compareAndSet(true, false);
        if(!isSuccessful) throw new IllegalStateException("Usecase should be busy at this point");
    }

    private void setIsBusy() {
        boolean isSuccessful = isBusy.compareAndSet(false, true);
        if(!isSuccessful)
            throw new IllegalStateException("Usecase should not be busy at this point");
    }

    protected void executeInBackground(Runnable runnable) {
        if (!isBusy.get()) {
            setIsBusy();
            // if there is no ongoing operation run the operation
            backgroundThreadPoster.post(runnable);
        }
    }


    public void addListener(UseCaseListener<SUCCESS_RESULT, ERROR_RESULT> listener) {
        listeners.add(listener);
    }

    public void removeListener(UseCaseListener<SUCCESS_RESULT, ERROR_RESULT> listener) {
        listeners.remove(listener);
    }

    protected void notifySuccess(SUCCESS_RESULT result) {
//        Update on the UI thread
        uiThreadPoster.post(() -> {
            for (UseCaseListener<SUCCESS_RESULT, ERROR_RESULT> listener : listeners) {
                listener.onSuccess(result);
            }
        });
        setNotBusy();
    }

    protected void notifyError(ERROR_RESULT errorResult) {
        //        Update on the UI thread
        uiThreadPoster.post(() -> {
            for (UseCaseListener<SUCCESS_RESULT, ERROR_RESULT> listener : listeners) {
                listener.onError(errorResult);
            }
        });
        setNotBusy();
    }

    public interface UseCaseListener<SUCCESS_RESULT, ERROR_RESULT> {
        void onSuccess(SUCCESS_RESULT result);

        void onError(ERROR_RESULT result);
    }
}
