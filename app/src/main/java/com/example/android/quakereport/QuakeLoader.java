package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by phartmann on 10/02/2018.
 */

public class QuakeLoader extends AsyncTaskLoader<List<QuakeList>> {

    /** Tag para mensagens de log */
    private static final String LOG_TAG = QuakeLoader.class.getName();
    /** URL da busca */
    private String mUrl;

    /**
     * Constrói um novo {@link QuakeLoader}.
     *
     * @param context O contexto da activity
     * @param url A URL de onde carregaremos dados
     */
    public QuakeLoader( Context context, String url ) {
        super(context);
        mUrl = url;
    }

    /**
     * Está é uma thread de background.
     */
    @Override
    public List <QuakeList> loadInBackground() {
        if (mUrl == null){
            return null;
        }
        Log.e(LOG_TAG, "loadInBackgroud executed");
        // Realiza requisição de rede, decodifica a resposta, e extrai uma lista de earthquakes.
        List<QuakeList> quakeLists = QueryUtils.fetchEarthquakeData(mUrl);
        return quakeLists;
    }

    @Override
    protected void onStartLoading() {

        Log.e(LOG_TAG, "onStartLoarding executed");
        forceLoad();
    }
}
