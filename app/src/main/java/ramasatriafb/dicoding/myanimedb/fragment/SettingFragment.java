package ramasatriafb.dicoding.myanimedb.fragment;

;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ramasatriafb.dicoding.myanimedb.R;
import ramasatriafb.dicoding.myanimedb.api.ApiClient;
import ramasatriafb.dicoding.myanimedb.api.ApiInterface;
import ramasatriafb.dicoding.myanimedb.data.Movie;
import ramasatriafb.dicoding.myanimedb.data.ResultMovie;
import ramasatriafb.dicoding.myanimedb.reminder.DailyAlarm;
import ramasatriafb.dicoding.myanimedb.reminder.MovieNotif;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    String Bahasa;
    String DaylyAlarm;
    String MovieNotif;
    Preference bahasaPreference;
    SwitchPreference switchdaylyPreference;
    SwitchPreference switchnotifPreference;


    private DailyAlarm dailyAlarmReceiver = new DailyAlarm();
    private MovieNotif movieNotif = new MovieNotif();

    private List<ResultMovie> nowPlayMovies = new ArrayList<>();

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        getActivity();
        init();
    }

    private void init(){
        Bahasa = getResources().getString(R.string.changeLanguage);
        DaylyAlarm = getResources().getString(R.string.dailyAlarm);
        MovieNotif = getResources().getString(R.string.notifTodayMovie);

        bahasaPreference = (Preference) findPreference(Bahasa);
        bahasaPreference.setOnPreferenceClickListener(this);
        switchdaylyPreference = (SwitchPreference) findPreference(DaylyAlarm);
        switchdaylyPreference.setOnPreferenceChangeListener(this);
        switchnotifPreference = (SwitchPreference) findPreference(MovieNotif);
        switchnotifPreference.setOnPreferenceChangeListener(this);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        boolean isSet = (boolean) newValue;

        if (key.equals(DaylyAlarm)) {
            if (isSet) {
                dailyAlarmReceiver.setRepeatingAlarm(getActivity());
            } else {
                dailyAlarmReceiver.cancelAlarm(getActivity());
            }
        } else if(key.equals((MovieNotif))){
            if (isSet) {
                getTodayRelease();
            } else {
                movieNotif.cancelAlarm(getActivity());
            }
        }

        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(Bahasa)) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }
        return true;
    }

    public void getTodayRelease() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        final String currentDate = sdf.format(date);
        final String currentDate2 = sdf.format(date);
        Log.d("MASUK", "AYE");
        ApiInterface api = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<Movie> call = api.getMovieRelease(currentDate,currentDate2);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    List<ResultMovie> todayMovie = new ArrayList<>();
                    nowPlayMovies = response.body().getResults();
                    for (int i = 0; i < nowPlayMovies.size(); i++) {

                        ResultMovie movie = nowPlayMovies.get(i);
                        Date movieDate = dateFormatter(movie.getReleaseDate());
                        Log.d("TESS", "onResponse: " + movieDate);

                        todayMovie.add(movie);
                        Log.d("TES", "onResponse: " + todayMovie.size());
                    }
                    movieNotif.setRepeatingAlarm(getActivity(), todayMovie);
                } else {
                    Toast.makeText(getActivity(), R.string.not_found_movie, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.not_found_movie, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private Date dateFormatter(String movieDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(movieDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }
}