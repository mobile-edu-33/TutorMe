package com.mobileedu33.tutorme.ui.viewmodels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.data.models.CurrentUserType;
import com.mobileedu33.tutorme.data.models.UserType;
import com.mobileedu33.tutorme.data.usecases.BaseUseCase.UseCaseListener;
import com.mobileedu33.tutorme.data.usecases.DeleteAssignmentUseCase;
import com.mobileedu33.tutorme.data.usecases.FetchAssingmentsUseCase;
import com.mobileedu33.tutorme.data.usecases.PublishAssignmentUseCase;

import java.io.File;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class AssignmentsActivityViewModel extends BaseViewModel{
    private final PublishAssignmentUseCase publishAssignmentUseCase;
    private final DeleteAssignmentUseCase deleteAssignmentUseCase;
    private final FetchAssingmentsUseCase fetchAssingmentsUseCase;
    private MutableLiveData<Boolean> publishAssignmentLiveData = new MutableLiveData<>();
    private final Realm realm;
    private final MutableLiveData<List<Assignment>> assignmentsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleteAssigmentLiveData = new MutableLiveData<>();


    @ViewModelInject
    public AssignmentsActivityViewModel(PublishAssignmentUseCase publishAssignmentUseCase,
                                        DeleteAssignmentUseCase deleteAssignmentUseCase,
                                        FetchAssingmentsUseCase fetchAssingmentsUseCase) {
        this.publishAssignmentUseCase = publishAssignmentUseCase;
        this.deleteAssignmentUseCase = deleteAssignmentUseCase;
        this.fetchAssingmentsUseCase = fetchAssingmentsUseCase;
        realm = Realm.getDefaultInstance();
    }

    public UserType getUserType() {
        CurrentUserType currentUserType = realm.where(CurrentUserType.class).findFirst();
        assert currentUserType != null;
        return currentUserType.getUserType();
    }

    public LiveData<List<Assignment>> getAssignments() {
        fetchAssingmentsUseCase.fetch();
        RealmResults<Assignment> assignmentRealmResults = realm.where(Assignment.class).findAllAsync();
        assignmentRealmResults.addChangeListener(assignmentsLiveData::postValue);
        assignmentsLiveData.setValue(assignmentRealmResults);
        return assignmentsLiveData;
    }

    public LiveData<Boolean> publishAssignment(Assignment assignment, File attachment, File image) {
        publishAssignmentLiveData = new MutableLiveData<>();
        publishAssignmentUseCase.publish(assignment, attachment, image);
        return publishAssignmentLiveData;
    }

    public LiveData<Boolean> deleteAssignment(Assignment assignment) {
        deleteAssignmentUseCase.delete(assignment.getRefId());
        return deleteAssigmentLiveData;
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

    private UseCaseListener<Void, Void> deleteAssignmentListener = new UseCaseListener<Void, Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            deleteAssigmentLiveData.postValue(true);
        }

        @Override
        public void onError(Void aVoid) {
            deleteAssigmentLiveData.postValue(false);
        }
    };

    @Override
    public void executeOnResume() {
        super.executeOnResume();
        publishAssignmentUseCase.addListener(publishAssignmentListener);
        deleteAssignmentUseCase.addListener(deleteAssignmentListener);
    }

    @Override
    public void executeOnPause() {
        super.executeOnPause();
        publishAssignmentUseCase.removeListener(publishAssignmentListener);
        deleteAssignmentUseCase.removeListener(deleteAssignmentListener);
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
