package com.bbyy.weeat.ui.page;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.bbyy.weeat.R;
import com.bbyy.weeat.models.config.Const;
import com.bbyy.weeat.repositories.StorageRepository;
import com.bbyy.weeat.ui.page.fragment.ClockFragment;
import com.bbyy.weeat.utils.SharedPreferencesUtil;
import com.bbyy.weeat.viewModels.ClockViewModel;
import com.bbyy.weeat.ui.widget.MyForegroundService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class MainActivity extends AppCompatActivity {
    private boolean started = false;
    private FragmentManager fragmentManager;
    private ClockViewModel clockViewModel;
    private StorageRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferencesUtil.incrementValue(this);
        repository=new StorageRepository(getApplicationContext());
        repository.updateAllFreshItems();
        if (!Settings.canDrawOverlays(this)) {
            showPermissionExplanationDialog(this);
        }
        fragmentManager=getSupportFragmentManager();
        String USER_INFO = "user_info";
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        Intent intent = getIntent();
        Fragment clock = ClockFragment.newInstance();
        if (intent != null) {
            Bundle bundle = new Bundle();
            String PICK_TIME = "time";
            bundle.putLong("time", intent.getLongExtra("time", sharedPreferences.getLong(PICK_TIME, 10L)));
            Log.d("test ", "main activity time " + intent.getLongExtra("time", 10L));
            clock.setArguments(bundle);
        }
        NavHostFragment navHostFragment= (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController=navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navi);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
        clockViewModel= new ViewModelProvider(this).get(ClockViewModel.class);
        clockViewModel.isStarted().observe(this, aBoolean -> {
            started=aBoolean;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopForegroundService();
    }

    private void requestForegroundPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (started) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            if(fragmentManager.findFragmentById(R.id.fragmentContainerView) instanceof ClockFragment){
                super.onBackPressed();
            }else{
                backHome();
            }
        }
    }

    public void backHome() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//新的任务栈中启动主屏幕活动（这是通常的最佳实践
        startActivity(homeIntent);
    }

    public void showPermissionExplanationDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Need access to usage data")
                .setMessage("For the normal operation of the application, we need to obtain your access to usage data. Please grant this permission on the following page.")
                .setPositiveButton("Go to settings", (dialog, which) -> requestForegroundPermission(context))
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void startForegroundService() {
        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        }
    }

    public void stopForegroundService() {
        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        stopService(serviceIntent);
    }
}
