package ramasatriafb.dicoding.myanimedb;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import ramasatriafb.dicoding.myanimedb.fragment.FavMovieFragment;
import ramasatriafb.dicoding.myanimedb.fragment.FavTvFragment;
import ramasatriafb.dicoding.myanimedb.fragment.MovieFragment;
import ramasatriafb.dicoding.myanimedb.fragment.TvFragment;
import ramasatriafb.dicoding.myanimedb.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabs;
    private int[] tabIcons = {
            R.drawable.baseline_movie_filter_white_24dp,
            R.drawable.baseline_tv_white_24dp,
            R.drawable.baseline_stars_white_24dp,
            R.drawable.baseline_star_border_white_24dp,
            R.drawable.baseline_search_white_24dp,
            R.drawable.baseline_search_white_24dp,
    };
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
        setUpViewpager(viewPager);
        tabs.setupWithViewPager(viewPager);
        setupTabIcons();


    }

    private void setUpViewpager(ViewPager viewPager) {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.createFragment(new MovieFragment(), getString(R.string.movie));
        sectionsPagerAdapter.createFragment(new TvFragment(), getString(R.string.tv_show));
        sectionsPagerAdapter.createFragment(new FavMovieFragment(), getString(R.string.FavouriteMovies));
        sectionsPagerAdapter.createFragment(new FavTvFragment(), getString(R.string.tv_show));
        viewPager.setAdapter(sectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(this,SettingActivity.class);
            startActivity(mIntent);
        }else if (item.getItemId() == R.id.action_search){
            Intent mIntent = new Intent(this, SearchActivity.class);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupTabIcons() {
        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
        tabs.getTabAt(2).setIcon(tabIcons[2]);
        tabs.getTabAt(3).setIcon(tabIcons[3]);
//        tabs.getTabAt(4).setIcon(tabIcons[4]);
//        tabs.getTabAt(5).setIcon(tabIcons[5]);
    }


}