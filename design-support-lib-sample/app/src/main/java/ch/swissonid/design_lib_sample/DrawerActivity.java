package ch.swissonid.design_lib_sample;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ch.swissonid.design_lib_sample.fragments.StandardAppBarFragment;
import ch.swissonid.design_lib_sample.fragments.TabFragment;
import ch.swissonid.design_lib_sample.util.LogUtils;
import ch.swissonid.design_lib_sample.util.Navigator;

import static ch.swissonid.design_lib_sample.util.LogUtils.LOGD;

public class DrawerActivity extends AppCompatActivity implements DrawerLayout.DrawerListener
        , NavigationView.OnNavigationItemSelectedListener{

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @InjectView(R.id.navigation_view)
    NavigationView mNavigationView;

    private static Navigator mNavigator;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private @IdRes int mCurrentMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.inject(this);
        setupToolbar();
        setupNavDrawer();
        initNavigator();
        mCurrentMenuItem = R.id.standard_app_bar_menu_item;
        mNavigator.setRootFragment(StandardAppBarFragment.newInstance());
    }

    private void initNavigator() {
        mNavigator = new Navigator(getSupportFragmentManager(), R.id.container);
    }

    private void setupToolbar() {
        mToolbar = ButterKnife.findById(this, R.id.toolbar);
        if(mToolbar == null) {
            LOGD(this, "Didn't find a toolbar");
            return;
        }
        setSupportActionBar(mToolbar);
        if(getSupportActionBar() == null) return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setupNavDrawer() {
        if(mDrawerLayout == null) {
            LogUtils.LOGE(this, "mDrawerLayout is null - Can not setup the NavDrawer! Have you set the android.support.v7.widget.DrawerLayout?");
            return;
        }
        mDrawerLayout.setDrawerListener(this);

        mDrawerToggle = new ActionBarDrawerToggle(this
                , mDrawerLayout
                , mToolbar
                , R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);

        mDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        LOGD(this, "setup setupNavDrawer");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mDrawerToggle.onDrawerOpened(drawerView);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mDrawerToggle.onDrawerClosed(drawerView);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        mDrawerToggle.onDrawerStateChanged(newState);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        @IdRes int id = menuItem.getItemId();
        if(id == mCurrentMenuItem) {
            mDrawerLayout.closeDrawers();
            return false;
        }
        switch (id){
            case R.id.standard_app_bar_menu_item:
                mNavigator.setRootFragment(StandardAppBarFragment.newInstance());
                mDrawerLayout.closeDrawers();
                break;
            case R.id.tabs_menu_item:
                mNavigator.setRootFragment(TabFragment.newInstance());
                mDrawerLayout.closeDrawers();
                break;
        }
        mCurrentMenuItem = id;
        menuItem.setChecked(true);
        return false;
    }
}