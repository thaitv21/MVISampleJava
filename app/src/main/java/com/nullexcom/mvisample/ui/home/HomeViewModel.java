package com.nullexcom.mvisample.ui.home;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nullexcom.mvisample.di.DaggerUseCaseComponent;
import com.nullexcom.mvisample.usecase.RecommendUseCase;
import com.nullexcom.mvisample.usecase.SearchUseCase;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {

    @Inject
    SearchUseCase searchUseCase;

    @Inject
    RecommendUseCase recommendUseCase;

    public HomeViewModel() {
        DaggerUseCaseComponent.create().inject(this);
    }

    private MutableLiveData<HomeViewState> state = new MutableLiveData<>();

    LiveData<HomeViewState> getState() {
        return state;
    }

    void loadPage() {
        recommendUseCase.enqueue()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> state.setValue(new HomeViewState.LoadingState()))
                .doOnNext(movies -> state.setValue(new HomeViewState.DataState(movies)))
                .subscribe();
        state.setValue(new HomeViewState.LoadingState());
    }

    void changeKeyword(String keyword) {
        HomeViewState currentState = this.state.getValue();
        if (currentState != null) {
            currentState.setKeyword(keyword);
        }
    }

    void search() {
        HomeViewState currentState = this.state.getValue();
        if (currentState == null) {
            return;
        }
        String keyword = currentState.getKeyword();
        if (TextUtils.isEmpty(keyword)) {
            return;
        }
        searchUseCase.enqueue(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> state.setValue(new HomeViewState.LoadingState().cloneFrom(currentState)))
                .doOnNext(movies -> state.setValue(new HomeViewState.DataState(movies).cloneFrom(currentState)))
                .subscribe();
    }
}
