package com.nullexcom.mvisample.usecase;

import com.nullexcom.mvisample.models.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class SearchUseCase {
    public Observable<List<Movie>> enqueue(String keyword) {
        return Observable.defer(() -> {
            List<Movie> movies = new ArrayList<>();
            try {
                Document document = Jsoup.connect("http://xemphimplus.net/wp-content/themes/halimmovies/halim-ajax.php")
                        .data("action", "halim_ajax_live_search")
                        .data("search", keyword)
                        .post();
                for (Element element : document.select(".exact_result")) {
                    String entryTitle = element.select(".label").text();
                    String originalTitle = element.select(".enName").text();
                    String url = element.select("a").attr("href");
                    String imgUrl = element.select("img").attr("src");
                    String date = element.select(".date").text();
                    Movie movie = new Movie(entryTitle, originalTitle, url, imgUrl, "");
                    movie.setDate(date);
                    movies.add(movie);
                }
                System.out.println(movies);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Observable.just(movies);
        });
    }
}
