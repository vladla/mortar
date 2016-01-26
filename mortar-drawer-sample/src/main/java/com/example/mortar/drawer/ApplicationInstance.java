package com.example.mortar.drawer;

import android.app.Application;

import com.example.mortar.drawer.core.ObjectGraphService;
import com.example.mortar.drawer.core.RootModule;

import dagger.ObjectGraph;
import mortar.MortarScope;

/**
 * Created by Lubenets Vladyslav on 1/26/16.
 */
public class ApplicationInstance extends Application {
    private MortarScope rootScope;

    @Override
    public Object getSystemService(String name) {
        if (rootScope == null) {
            rootScope = MortarScope.buildRootScope()
                    .withService(ObjectGraphService.SERVICE_NAME, ObjectGraph.create(new RootModule()))
                    .build("Root");
        }
        return rootScope.hasService(name) ? rootScope.getService(name) : super.getSystemService(name);
    }
}
