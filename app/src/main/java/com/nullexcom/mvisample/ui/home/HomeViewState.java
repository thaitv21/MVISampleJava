package com.nullexcom.mvisample.ui.home;

import com.nullexcom.mvisample.models.Movie;

import java.util.List;

class HomeViewState {

    private String keyword = "";

    void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    String getKeyword() {
        return keyword;
    }

    HomeViewState cloneFrom(HomeViewState state) {
        this.keyword = state.getKeyword();
        return this;
    }

    static class LoadingState extends HomeViewState {

    }

    static class ErrorState extends HomeViewState {

    }

    static class DataState extends HomeViewState {
        private List<Movie> movies;

        DataState(List<Movie> movies) {
            this.movies = movies;
        }

        List<Movie> getMovies() {
            return movies;
        }
    }
}
