package ramasatriafb.dicoding.myanimedb.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.room.Room;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ramasatriafb.dicoding.myanimedb.R;
import ramasatriafb.dicoding.myanimedb.db.AppDatabase;
import ramasatriafb.dicoding.myanimedb.entity.Favourite;

class StackRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    List<Favourite> resultFavMovieList = new ArrayList<>();
    private AppDatabase db;
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342";
    Favourite favourite;
    private final Context mContext;

    public StackRemoteViewFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        db = Room.databaseBuilder(mContext,
                AppDatabase.class, "favouritedb").allowMainThreadQueries().build();

    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();

        resultFavMovieList = Arrays.asList(db.favouriteDAO().selectAllMovies());
Log.d("DB", String.valueOf(resultFavMovieList));
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return resultFavMovieList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        Bitmap bmp = null;
        String poster_url = IMAGE_BASE_URL + resultFavMovieList.get(position).getPoster();
        String movie_title = resultFavMovieList.get(position).getJudul();
        String releaseDate = formatDate("yyyy-MM-dd", "dd MMMM yyyy", resultFavMovieList.get(position).getReleaseDate());

        try {

            bmp = Picasso.with(mContext)
                    .load(poster_url)
                    .get();
            rv.setImageViewBitmap(R.id.imageView, bmp);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        extras.putString(ImageBannerWidget.EXTRA_ITEM, movie_title + "\n" + releaseDate);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public static String formatDate(String fromFormat, String toFormat, String dateToFormat) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat inFormat = new SimpleDateFormat(fromFormat);
        Date date = null;
        try {
            date = inFormat.parse(dateToFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat = new SimpleDateFormat(toFormat);

        return outFormat.format(date);
    }
}
