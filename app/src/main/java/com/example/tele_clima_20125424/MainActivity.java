
package com.example.tele_clima_20125424;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tele_clima_20125424.NavigationActivity;
import com.example.tele_clima_20125424.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String PING_HOST = "www.google.com";
    private static final int PING_PORT = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnIngresar = findViewById(R.id.botonIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InternetCheck(new InternetCheck.Consumer() {
                    @Override
                    public void accept(Boolean internet) {
                        if (internet) {
                            // Redirigir a la actividad Navigation
                            Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                            startActivity(intent);
                        } else {
                            showNoInternetDialog();
                        }
                    }
                }).execute();
            }
        });
    }

    private void showNoInternetDialog() {
        new MaterialAlertDialogBuilder(this)
                .setMessage("No se pudo establecer una conexión con internet")
                .setCancelable(false)
                .setNegativeButton("Configuración", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public static class InternetCheck extends AsyncTask<Void, Void, Boolean> {

        private Consumer mConsumer;

        public interface Consumer {
            void accept(Boolean internet);
        }

        public InternetCheck(Consumer consumer) {
            mConsumer = consumer;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(PING_HOST, PING_PORT), 1500);
                socket.close();
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error al verificar la conexión a internet", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean internet) {
            super.onPostExecute(internet);
            if (mConsumer != null) {
                mConsumer.accept(internet);
            }
        }
    }
}


/*
package com.example.tele_clima_20125424;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tele_clima_20125424.NavigationActivity;
import com.example.tele_clima_20125424.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnIngresar = findViewById(R.id.botonIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetAvailable()) {
                    // Redirigir a la actividad Navigation
                    Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                    startActivity(intent);
                } else {
                    showNoInternetDialog();
                }
            }
        });
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        }
        return false;
    }

    private void showNoInternetDialog() {
        new MaterialAlertDialogBuilder(this)
                .setMessage("No se pudo establecer una conexión con internet")
                .setCancelable(false)
                .setNegativeButton("Configuración", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}
*/



