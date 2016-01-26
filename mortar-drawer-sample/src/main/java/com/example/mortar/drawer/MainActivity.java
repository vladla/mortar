package com.example.mortar.drawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.example.mortar.drawer.core.ObjectGraphService;
import com.example.mortar.drawer.pathview.HandlesBack;
import com.example.mortar.drawer.screen.GalleryScreen;
import com.example.mortar.drawer.screen.ImportScreen;
import com.example.mortar.drawer.screen.MainScreen;
import com.example.mortar.drawer.screen.SendScreen;
import com.example.mortar.drawer.screen.ShareScreen;
import com.example.mortar.drawer.screen.SlideShowScreen;
import com.example.mortar.drawer.screen.ToolsScreen;
import com.example.mortar.drawer.util.GsonParceler;

import flow.Flow;
import flow.FlowDelegate;
import flow.History;
import flow.path.Path;
import flow.path.PathContainerView;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Flow.Dispatcher {

    private FlowDelegate flowDelegate;
    private PathContainerView container;
    private HandlesBack containerAsBack;
    private MortarScope activityScope;

    private enum Screens {
        CAMERA(R.id.nav_camara, new ImportScreen()),
        GALLERY(R.id.nav_gallery, new GalleryScreen()),
        SLIDESHOW(R.id.nav_slideshow, new SlideShowScreen()),
        TOOLS(R.id.nav_manage, new ToolsScreen()),
        SHARE(R.id.nav_share, new ShareScreen()),
        SEND(R.id.nav_send, new SendScreen());

        private final int id;
        private final Path screen;

        Screens(int id, Path screen) {
            this.id = id;
            this.screen = screen;
        }

        public int getId() {
            return id;
        }

        public Path getScreen() {
            return screen;
        }

        public static Screens getScreenById(int itemId) {
            for (Screens screen : Screens.values()) {
                if (screen.getId() == itemId) {
                    return screen;
                }
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MortarScope parentScope = MortarScope.getScope(getApplication());

        String scopeName = getLocalClassName() + "-task-" + getTaskId();

        activityScope = parentScope.findChild(scopeName);
        if (activityScope == null) {
            activityScope = parentScope.buildChild()
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .build(scopeName);
        }
        ObjectGraphService.inject(this, this);


        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        container = (PathContainerView) findViewById(R.id.container);
        containerAsBack = (HandlesBack) this.container;
        GsonParceler parceler = new GsonParceler(new Gson());
        @SuppressWarnings("deprecation") FlowDelegate.NonConfigurationInstance nonConfig =
                (FlowDelegate.NonConfigurationInstance) getLastNonConfigurationInstance();
        flowDelegate = FlowDelegate.onCreate(nonConfig, getIntent(), savedInstanceState, parceler, History.single(new MainScreen()), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Flow.get(this).set(Screens.getScreenById(item.getItemId()).getScreen());
        return true;
    }

    @Override
    public void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback) {
        container.dispatch(traversal, callback);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        flowDelegate.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flowDelegate.onResume();
    }

    @Override
    protected void onPause() {
        flowDelegate.onPause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
        flowDelegate.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            if (activityScope != null) activityScope.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (containerAsBack.onBackPressed()) return;
        if (flowDelegate.onBackPressed()) return;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Object getSystemService(String name) {
        if (flowDelegate != null) {
            Object flowService = flowDelegate.getSystemService(name);
            if (flowService != null) return flowService;
        }

        return activityScope != null && activityScope.hasService(name) ? activityScope.getService(name)
                : super.getSystemService(name);
    }

}
