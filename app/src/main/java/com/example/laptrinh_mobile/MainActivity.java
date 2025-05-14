package com.example.laptrinh_mobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Toolbar toolbar;
    private boolean isExpenseScreen = true;
    private Fragment currentFragment;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Kiểm tra trạng thái đăng nhập
        if (!sharedPreferences.getBoolean("isLoggedIn", false) || firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem repeatItem = menu.findItem(R.id.action_repeat);
        repeatItem.setVisible(currentFragment instanceof ExpenseFragment ||
                currentFragment instanceof IncomeFragment ||
                currentFragment instanceof ReportFragment);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_repeat) {
            if (currentFragment instanceof ReportFragment) {
                ((ReportFragment) currentFragment).toggleData();
            } else {
                isExpenseScreen = !isExpenseScreen;
                currentFragment = isExpenseScreen ? new ExpenseFragment() : new IncomeFragment();
                toolbar.setTitle(isExpenseScreen ? "Chi tiêu" : "Thu nhập");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, currentFragment)
                        .commit();
            }
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Tùy chọn");
            builder.setItems(new CharSequence[]{"Cài đặt", "Đăng xuất"}, (dialog, which) -> {
                if (which == 1) {
                    logout();
                }
            });
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        try {
            // Đăng xuất Firebase
            firebaseAuth.signOut();
            // Làm sạch DatabaseManager
            DatabaseManager.getInstance().clear();
            // Cập nhật SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            // Chuyển hướng đến LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi đăng xuất: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void refreshFragment() {
        if (currentFragment instanceof ExpenseFragment) {
            currentFragment = new ExpenseFragment();
        } else if (currentFragment instanceof IncomeFragment) {
            currentFragment = new IncomeFragment();
        } else if (currentFragment instanceof ReportFragment) {
            currentFragment = new ReportFragment();
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
                        selectedFragment = new SavingsFragment();
                        toolbar.setTitle("Tiết kiệm");
                    } else if (item.getItemId() == R.id.nav_report) {
                        selectedFragment = new ReportFragment();
                        toolbar.setTitle("Báo cáo");
                    } else if (item.getItemId() == R.id.nav_notification) {
                        selectedFragment = new NotificationFragment();
                        toolbar.setTitle("Thông báo");
                    } else if (item.getItemId() == R.id.nav_profile) {
                        selectedFragment = new ProfileFragment();
                        toolbar.setTitle("Hồ sơ");
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