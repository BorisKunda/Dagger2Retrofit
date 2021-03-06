package com.codepath.daggerexample;

import com.codepath.daggerexample.models.Repository;
import com.codepath.daggerexample.network.interfaces.GitHubApiInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    Retrofit mRetrofit;

    @Inject
    GitHubApiInterface mGitHubApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Call<ArrayList<Repository>> call = mGitHubApiInterface.getRepository("codepath");

                call.enqueue(new Callback<ArrayList<Repository>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Repository>> call, Response<ArrayList<Repository>> response) {
                        if (response.isSuccessful()) {

                            ArrayList<Repository> repositoryArrayList = response.body();

                            for (Repository repo : repositoryArrayList) {
                                Log.d("BK-DEBUG", repo.getFullName());
                            }

                            Snackbar.make(view, "Data retrieved", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else {
                            Log.i("ERROR", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Repository>> call, Throwable t) {

                    }

                });
            }

        });

        ((MyApp) getApplication()).getGitHubComponent().inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
