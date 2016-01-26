package com.example.mortar.drawer.mortarscreen;

import android.content.res.Resources;

/** @see WithModuleFactory */
public abstract class ModuleFactory<T> {
  public abstract Object createDaggerModule(Resources resources, T screen);
}
