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
import butterknife.BindView;
import butterknife.ButterKnife;
import com.classic.car.R;
import com.classic.car.fragment.AboutFragment;
import com.classic.car.fragment.ChartFragment;
import com.classic.car.fragment.MainFragment;
import com.classic.car.fragment.TimelineFragment;
import com.classic.core.activity.BaseActivity;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends BaseActivity {
    @BindView(R.id.toolbar)  Toolbar              mToolbar;
    @BindView(R.id.main_fab) FloatingActionButton mFab;
    private                  BottomBar            mBottomBar;

    @Override public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override public void initInstanceState(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mBottomBar = BottomBar.attach(this, savedInstanceState);
    }

    @Override public void initView() {
        super.initView();
        setTitle(R.string.app_name);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(activity, AddConsumerActivity.class);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.bb_menu_main:
                        changeFragment(R.id.main_fragment_layout, new MainFragment());
                        break;
                    case R.id.bb_menu_chart:
                        changeFragment(R.id.main_fragment_layout, new ChartFragment());
                        break;
                    case R.id.bb_menu_timeline:
                        changeFragment(R.id.main_fragment_layout, new TimelineFragment());
                        break;
                    case R.id.bb_menu_about:
                        changeFragment(R.id.main_fragment_layout, new AboutFragment());
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
}
