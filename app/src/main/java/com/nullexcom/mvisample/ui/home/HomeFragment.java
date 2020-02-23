package com.nullexcom.mvisample.ui.home;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.jakewharton.rxbinding3.widget.TextViewAfterTextChangeEvent;
import com.nullexcom.mvisample.R;
import com.nullexcom.mvisample.models.Movie;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import kotlin.Unit;

public class HomeFragment extends Fragment {

    @BindView(R.id.edtKeyword)
    EditText edtKeyword;

    @BindView(R.id.imgSearch)
    ImageView imgSearch;

    @BindView(R.id.rvResult)
    RecyclerView rvResult;

    @BindView(R.id.layoutLoading)
    View layoutLoading;

    @BindView(R.id.layoutError)
    View layoutError;

    private ResultAdapter adapter;

    private Subject<Boolean> attachObservable = PublishSubject.create();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        adapter = new ResultAdapter(getContext());
        adapter.itemClicks().doOnNext(this::clickOnMovie).subscribe();
        rvResult.setAdapter(adapter);
        rvResult.setLayoutManager(new LinearLayoutManager(getContext()));

        HomeViewModel viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.getState().observe(getViewLifecycleOwner(), this::render);
        HomeViewIntent.bind(this, viewModel);
    }

    @Override
    public void onStart() {
        super.onStart();
        attachObservable.onNext(true);
    }

    Observable<Boolean> doOnStart() {
        return attachObservable;
    }

    Observable<Unit> doSearch() {
        Observable<Unit> clickOnImgSearch = RxView.clicks(imgSearch);
        Observable<Unit> clickOnImeOptions = RxTextView.editorActions(edtKeyword)
                .filter(i -> i == EditorInfo.IME_ACTION_SEARCH)
                .map(i -> Unit.INSTANCE);
        return clickOnImgSearch.mergeWith(clickOnImeOptions);
    }

    @SuppressWarnings("Convert2MethodRef")
    Observable<String> doOnChangedText() {
        return RxTextView.afterTextChangeEvents(edtKeyword)
                .skipInitialValue()
                .map(TextViewAfterTextChangeEvent::getEditable)
                .filter(editable -> editable != null)
                .map(editable -> editable.toString());
    }

    private void render(HomeViewState state) {
        if (state instanceof HomeViewState.LoadingState) {
            renderLoading();
        } else if (state instanceof HomeViewState.ErrorState) {
            renderError();
        } else if (state instanceof HomeViewState.DataState) {
            renderData((HomeViewState.DataState) state);
        }
    }

    private void renderLoading() {
        layoutLoading.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }

    private void renderError() {
        layoutLoading.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
    }

    private void renderData(@NotNull HomeViewState.DataState state) {
        layoutLoading.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
        adapter.refresh(state.getMovies());
    }

    private void clickOnMovie(@NotNull Movie movie) {
        Toast.makeText(requireContext(), movie.getEntryTitle(), Toast.LENGTH_SHORT).show();
    }
}
