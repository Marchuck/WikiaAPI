package pl.marchuck.wikiaapi;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, SearchFragment.newInstance(), SearchFragment.TAG)
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (hideSuggestions()) Log.d(TAG, "onBackPressed: ");
        else super.onBackPressed();
    }

    @Nullable
    SearchFragment getSearchFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SearchFragment.TAG);
        if (fragment instanceof SearchFragment) return (SearchFragment) fragment;
        else return null;
    }

    private boolean hideSuggestions() {
        SearchFragment searchFragment = getSearchFragment();
        return searchFragment != null && searchFragment.hideSuggestions();
    }
}
