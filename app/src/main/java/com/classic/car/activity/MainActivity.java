package com.classic.car.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import com.classic.car.R;
import com.classic.car.fragment.ChartFragment;
import com.classic.car.fragment.ListFragment;
import com.classic.core.activity.BaseActivity;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends BaseActivity {
    /*@Bind(R.id.toolbar) */Toolbar              mToolbar;
    /*@Bind(R.id.fab)     */FloatingActionButton mFab;
    private             BottomBar            mBottomBar;

    @Override public void initInstanceState(Bundle savedInstanceState) {
        mBottomBar = BottomBar.attach(this, savedInstanceState);
    }

    @Override public void initView() {
        //ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.bb_menu_list:
                        changeFragment(R.id.fragment_layout, new ListFragment());
                        break;
                    case R.id.bb_menu_chart:
                        changeFragment(R.id.fragment_layout, new ChartFragment());
                        break;
                    case R.id.bb_menu_timeline:

                        break;
                    case R.id.bb_menu_about:

                        break;
                }
            }

            @Override public void onMenuTabReSelected(@IdRes int menuItemId) {
                Toast.makeText(getApplicationContext(), "onMenuTabReSelected", Toast.LENGTH_SHORT)
                     .show();
            }
        });

        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.purple));
        mBottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.red));
        mBottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.green));
        mBottomBar.mapColorForTab(3, ContextCompat.getColor(this, R.color.orange));
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override public void unRegister() {
        //ButterKnife.unbind(this);
    }
}
