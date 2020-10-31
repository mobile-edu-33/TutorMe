package com.mobileedu33.tutorme.ui.viewmodels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.data.usecases.BaseUseCase.UseCaseListener;
import com.mobileedu33.tutorme.data.usecases.FetchAssingmentsUseCase;
import com.mobileedu33.tutorme.data.usecases.PublishAssignmentUseCase;

import java.io.File;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class AssignmentsActivityViewModel extends BaseViewModel{
    private final PublishAssignmentUseCase publishAssignmentUseCase;
    private final FetchAssingmentsUseCase fetchAssingmentsUseCase;
    private final MutableLiveData<Boolean> publishAssignmentLiveData = new MutableLiveData<>();
    private final Realm realm;
    private final MutableLiveData<List<Assignment>> assignmentsLiveData = new MutableLiveData<>();

    @ViewModelInject
    public AssignmentsActivityViewModel(PublishAssignmentUseCase publishAssignmentUseCase,
                                        FetchAssingmentsUseCase fetchAssingmentsUseCase) {
        this.publishAssignmentUseCase = publishAssignmentUseCase;
        this.fetchAssingmentsUseCase = fetchAssingmentsUseCase;
        realm = Realm.getDefaultInstance();
    }

    public LiveData<List<Assignment>> getAssignments() {
        fetchAssingmentsUseCase.fetch();
        RealmResults<Assignment> assignmentRealmResults = realm.where(Assignment.class).findAllAsync();
        assignmentRealmResults.addChangeListener(assignmentsLiveData::postValue);
        assignmentsLiveData.setValue(assignmentRealmResults);
        return assignmentsLiveData;
    }

    public LiveData<Boolean> publishAssignment(Assignment assignment, File attachment, File image) {
        publishAssignmentUseCase.publish(assignment, attachment, image);
        return publishAssignmentLiveData;
    }

    private UseCaseListener<Void, String> publishAssignmentListener = new UseCaseListener<Void, String>() {
        @Override
        public void onSuccess(Void aVoid) {
            messagesLiveData.postValue("Assignment successfully published.");
            publishAssignmentLiveData.postValue(true);
        }

        @Override
        public void onError(String message) {
            messagesLiveData.postValue(message);
            publishAssignmentLiveData.postValue(false);
        }
    };

    @Override
    public void executeOnResume() {
        super.executeOnResume();
        publishAssignmentUseCase.addListener(publishAssignmentListener);
    }

    @Override
    public void executeOnPause() {
        super.executeOnPause();
        publishAssignmentUseCase.removeListener(publishAssignmentListener);
    }

    @Override
    protected void onCleared() {
        realm.close();
        super.onCleared();
    }

    @Override
    public void removeLiveDataObservers(LifecycleOwner lifecycleOwner) {
        messagesLiveData.removeObservers(lifecycleOwner);
        assignmentsLiveData.removeObservers(lifecycleOwner);
    }
}
