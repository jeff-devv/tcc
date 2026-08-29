package com.jefferson.loginactivity.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // ============================================================
    // CONFIGURAÇÃO
    // ============================================================

    public static final String API_KEY = "176041J@p";

    /*
     * Se estiver rodando a API no computador e o Android for
     * um emulador, normalmente use:
     *
     * http://10.0.2.2:8000/
     *
     * Se estiver usando celular físico, coloque o IP do computador:
     *
     * http://192.168.X.X:8000/
     */

    private static final String BASE_URL =
            "http://192.168.15.14:8000/";


    private static Retrofit retrofit;

    private static FacialApi api;


    public static FacialApi getApi() {

        if (api == null) {

            retrofit =
                    new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            )
                            .build();

            api =
                    retrofit.create(
                            FacialApi.class
                    );
        }

        return api;
    }
}