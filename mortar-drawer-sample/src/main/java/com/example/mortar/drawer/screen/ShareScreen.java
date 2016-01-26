package com.example.mortar.drawer.screen;

import android.os.Bundle;

import com.example.mortar.drawer.R;
import com.example.mortar.drawer.core.RootModule;
import com.example.mortar.drawer.mortarscreen.WithModule;
import com.example.mortar.drawer.pathview.Layout;
import com.example.mortar.drawer.view.ShareView;

import javax.inject.Inject;
import javax.inject.Singleton;

import flow.path.Path;
import mortar.ViewPresenter;

/**
 * Created by Lubenets Vladyslav on 1/26/16.
 */
@Layout(R.layout.share_view)
@WithModule(ShareScreen.Module.class)
public class ShareScreen extends Path {

    @dagger.Module(injects = ShareView.class, addsTo = RootModule.class)
    public static class Module {
    }

    @Singleton
    public static class Presenter extends ViewPresenter<ShareView> {

        @Inject
        Presenter() {
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (!hasView()) return;
            getView().showContent();
        }
    }

}
