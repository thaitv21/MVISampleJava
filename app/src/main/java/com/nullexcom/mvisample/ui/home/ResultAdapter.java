package com.nullexcom.mvisample.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding3.view.RxView;
import com.nullexcom.mvisample.R;
import com.nullexcom.mvisample.models.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private Context context;
    private List<Movie> movies = new ArrayList<>();

    private Subject<Movie> onItemClick = PublishSubject.create();

    ResultAdapter(Context context) {
        this.context = context;
    }

    Subject<Movie> itemClicks() {
        return onItemClick;
    }

    void refresh(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void append(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgThumbnail)
        ImageView imgThumbnail;

        @BindView(R.id.tvEntryTitle)
        TextView tvEntryTitle;

        @BindView(R.id.tvOriginalTitle)
        TextView tvOriginalTitle;

        ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int i) {
            Movie movie = movies.get(i);
            Glide.with(context).load(movie.getImgUrl()).into(imgThumbnail);
            tvEntryTitle.setText(movie.getEntryTitle());
            tvOriginalTitle.setText(movie.getOriginalTitle());
            RxView.clicks(itemView).doOnNext(unit -> onItemClick.onNext(movie)).subscribe();
        }
    }
}
