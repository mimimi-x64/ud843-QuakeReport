/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<QuakeList>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final int QUAKE_LOADER_ID = 77;
    private QuakeListAdapter mAdapter;
    private ProgressBar loading;
    private TextView noResults;
    private TextView noInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        loading = (ProgressBar) findViewById(R.id.loading);
        noResults = (TextView) findViewById(android.R.id.empty);
        noResults.setVisibility(View.GONE);
        noInternet = (TextView) findViewById(R.id.no_internet);
        noInternet.setVisibility(View.GONE);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mAdapter = new QuakeListAdapter(this, new ArrayList <QuakeList>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick( AdapterView <?> adapterView, View view, int i, long l ) {
                QuakeList currentQuakeList = mAdapter.getItem(i);
                openWebPage(currentQuakeList.getUrl());
            }
        });

        // Obtém uma referência ao LoaderManager, a fim de interagir com loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Inicializa o loader. Passa um ID constante int definido acima e passa nulo para
        // o bundle. Passa esta activity para o parâmetro LoaderCallbacks (que é válido
        // porque esta activity implementa a interface LoaderCallbacks).


        if (mAdapter.isEmpty()){
            noResults.setVisibility(View.VISIBLE);
            earthquakeListView.setEmptyView(noResults);
        }

        if (isConnected){
            loaderManager.initLoader(QUAKE_LOADER_ID, null, this);
            Log.e(LOG_TAG, "After loader manager");
        } else {
            noResults.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            noInternet.setText("Verifique sua internet");
            noInternet.setVisibility(View.VISIBLE);
        }
    }

    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public Loader<List <QuakeList>> onCreateLoader( int i, Bundle bundle ) {
        // Criar um novo loader para a dada URL
        Log.e(LOG_TAG, "onCreateLoader Executed");
        return new QuakeLoader(this, QueryUtils.USGS_REQUEST_URL);

    }

    @Override
    public void onLoadFinished( Loader <List <QuakeList>> loader, List <QuakeList> quakeLists ) {
        // Limpa o adapter de dados de earthquake anteriores
        mAdapter.clear();
        Log.e(LOG_TAG, "onLoadFinished executed");

        // Se há uma lista válida de {@link Earthquake}s, então os adiciona ao data set do adapter.
        // Isto ativará a atualização da ListView.
        if (quakeLists != null && !quakeLists.isEmpty()) {
            mAdapter.addAll(quakeLists);
        }
    }

    @Override
    public void onLoaderReset( Loader <List <QuakeList>> loader ) {
        loading.setVisibility(View.GONE);
        noResults.setVisibility(View.VISIBLE);
        noResults.setText("Sem Resultados");
        Log.e(LOG_TAG, "onLoadReset executed");
        mAdapter.clear();
    }
}