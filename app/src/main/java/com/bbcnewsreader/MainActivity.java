package com.bbcnewsreader;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbcnewsreader.activities.AboutActivity;
import com.bbcnewsreader.activities.DetailsActivity;
import com.bbcnewsreader.fragments.BookmarkFragment;
import com.bbcnewsreader.fragments.HomeFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView toolbarTitleTV;
    private ImageView helpIV;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        references();
        initViews();

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFL, new HomeFragment()).commit();
        toolbarTitleTV.setText("Home");

        helpIV.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Help")
                .setMessage("To get help about this activity please click on ok button to follow help link.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("url", "https://www.bbc.co.uk/news/10628494#whatisrss");
                    startActivity(intent);                        })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                toolbarTitleTV.setText("Home");
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFL, new HomeFragment()).commit();
                break;
            case R.id.nav_bookmarks:
                toolbarTitleTV.setText("Bookmark");
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFL, new BookmarkFragment()).commit();
                break;
            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;    }

    private void references() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        toolbarTitleTV = findViewById(R.id.toolbarTitleTV);
        helpIV = findViewById(R.id.helpIV);

    }

    private void initViews() {

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer_string,
                R.string.close_drawer_string);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

}