package io.ashdavies.dagger.sample;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {

  @ContributesAndroidInjector
  abstract MainActivity mainActivity();
}
