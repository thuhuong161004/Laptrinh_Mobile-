package com.example.laptrinh_mobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Toolbar toolbar;
    private boolean isExpenseScreen = true;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nếu đã có mục tiêu tiết kiệm → chuyển sang màn tiến độ
        if (hasGoal()) {
            startActivity(new Intent(this, SavingsProgressActivity.class));
            finish();
            return;
        }

        // Nếu chưa có mục tiêu → chuyển sang màn đặt mục tiêu
        if (!getSharedPreferences("UserPrefs", MODE_PRIVATE).getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            currentFragment = new ExpenseFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, currentFragment)
                    .commit();
        }
    }

    private boolean hasGoal() {
        return getSharedPreferences("savings_prefs", MODE_PRIVATE)
                .contains("goal_amount");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem repeatItem = menu.findItem(R.id.action_repeat);
        repeatItem.setVisible(currentFragment instanceof ExpenseFragment || currentFragment instanceof IncomeFragment);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_repeat) {
            isExpenseScreen = !isExpenseScreen;
            currentFragment = isExpenseScreen ? new ExpenseFragment() : new IncomeFragment();
            toolbar.setTitle(isExpenseScreen ? "Chi tiêu" : "Thu nhập");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, currentFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Tùy chọn");
            builder.setItems(new CharSequence[]{"Cài đặt", "Đăng xuất"}, (dialog, which) -> {
                if (which == 1) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", false);
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshFragment() {
        if (currentFragment instanceof ExpenseFragment) {
            currentFragment = new ExpenseFragment();
        } else if (currentFragment instanceof IncomeFragment) {
            currentFragment = new IncomeFragment();
        }
        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, currentFragment)
                    .commit();
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    if (item.getItemId() == R.id.nav_wallet) {
                        selectedFragment = isExpenseScreen ? new ExpenseFragment() : new IncomeFragment();
                        toolbar.setTitle(isExpenseScreen ? "Chi tiêu" : "Thu nhập");
                    } else if (item.getItemId() == R.id.nav_savings) {
                        startActivity(new Intent(MainActivity.this, SetSavingsGoalActivity.class));
                        return true;
                    } else if (item.getItemId() == R.id.nav_report) {
                        selectedFragment = new ReportFragment();
                        toolbar.setTitle("Báo cáo");
                    } else if (item.getItemId() == R.id.nav_notification) {
                        selectedFragment = new NotificationFragment();
                        toolbar.setTitle("Thông báo");
                    } else if (item.getItemId() == R.id.nav_profile) {
                        Intent intent = new Intent(MainActivity.this, TroGiupVaHoTroActivity.class);
                        startActivity(intent);
                        return true;
                    }




                    if (selectedFragment != null) {
                        currentFragment = selectedFragment;
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();
                        invalidateOptionsMenu();
                    }
                    return true;
                }

            };
}