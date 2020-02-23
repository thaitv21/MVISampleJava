package com.nullexcom.mvisample.di;

import com.nullexcom.mvisample.ui.home.HomeViewModel;

import dagger.Component;

@Component(modules = UseCaseModule.class)
public interface UseCaseComponent {
    void inject(HomeViewModel viewModel);
}
