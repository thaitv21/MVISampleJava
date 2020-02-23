package com.nullexcom.mvisample.ui.home;

class HomeViewIntent {

    private HomeFragment homeFragment;
    private HomeViewModel homeViewModel;

    static void bind(HomeFragment fragment, HomeViewModel viewModel) {
        HomeViewIntent intent = new HomeViewIntent(fragment, viewModel);
        intent.bindIntents();
    }

    private HomeViewIntent(HomeFragment homeFragment, HomeViewModel homeViewModel) {
        this.homeFragment = homeFragment;
        this.homeViewModel = homeViewModel;
    }

    private void bindIntents() {
        homeFragment.doOnStart().doOnNext(b -> homeViewModel.loadPage()).subscribe();

        homeFragment.doOnChangedText().doOnNext(text -> homeViewModel.changeKeyword(text)).subscribe();

        homeFragment.doSearch().doOnNext(unit -> homeViewModel.search()).subscribe();
    }
}
