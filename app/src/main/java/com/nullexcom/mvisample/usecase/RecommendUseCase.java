package com.nullexcom.mvisample.usecase;

import com.nullexcom.mvisample.models.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class RecommendUseCase {
    public Observable<List<Movie>> enqueue() {
        return Observable.defer(() -> {
            List<Movie> movies = new ArrayList<>();
            Document document = Jsoup.connect("http://xemphimplus.net/").get();
            for (Element element : document.select("#halim-carousel-widget-3xx").select("div > article")) {
                String imgUrl = element.select("figure").select("img").attr("data-src");
                String status = element.select(".status").text();
                String entryTitle = element.select(".entry-title").text();
                String originalTitle = element.select(".original_title").text();
                String url = element.select(".halim-thumb").attr("href");
                movies.add(new Movie(entryTitle, originalTitle, url, imgUrl, status));
            }
            return Observable.just(movies);
        });
    }
}
