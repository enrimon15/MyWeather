package it.univaq.mobileprogramming.myweather.Settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import it.univaq.mobileprogramming.myweather.Background.MyWorker;
import it.univaq.mobileprogramming.myweather.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().add(android.R.id.content, new FSettings()).commit();
    }

    public static class FSettings extends PreferenceFragment {
        private final String TAG = "work";
        private Snackbar snackbar;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings);

            Preference myPref = (Preference) findPreference("switch_background");
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    if(Settings.loadBoolean(getActivity(), Settings.SWITCH_BACKGROUND, true)) {
                        scheduleJob();
                    }
                    else {
                        cancelJob();
                    }
                    return true;
                }
            });
        }

        public void scheduleJob(){
                Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

                WorkManager workManager = WorkManager.getInstance();
                PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES).setConstraints(constraints)
                        .build();
                workManager.enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, request);

                snackbar = Snackbar.make(getView(), "Aggiornamento in background attivato", snackbar.LENGTH_INDEFINITE);
                snackbar.setDuration(3000);
                snackbar.show();
        }

        public void cancelJob(){
            WorkManager.getInstance().cancelUniqueWork(TAG);
            snackbar = Snackbar.make(getView(), "Aggiornamento in background disattivato", snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(3000);
            snackbar.show();
        }
    }
}
