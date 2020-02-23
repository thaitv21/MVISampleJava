package com.nullexcom.mvisample.di;

import com.nullexcom.mvisample.usecase.RecommendUseCase;
import com.nullexcom.mvisample.usecase.SearchUseCase;

import dagger.Module;
import dagger.Provides;

@Module
class UseCaseModule {

    @Provides
    SearchUseCase searchUseCase() {
        return new SearchUseCase();
    }

    @Provides
    RecommendUseCase recommendUseCase() {
        return new RecommendUseCase();
    }

}
