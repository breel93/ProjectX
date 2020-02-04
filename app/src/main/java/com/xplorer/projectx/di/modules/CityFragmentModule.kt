package com.xplorer.projectx.di.modules

import com.xplorer.projectx.ui.city.CityDescriptionFragment
import com.xplorer.projectx.ui.city.CityFragment
import com.xplorer.projectx.ui.city.CityMapFragment
import com.xplorer.projectx.ui.city.CityPhotoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CityFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeCityFragment(): CityFragment

    @ContributesAndroidInjector
    internal abstract fun contributeCityPhotoFragment(): CityPhotoFragment

    @ContributesAndroidInjector
    internal abstract fun contributeCityDescriptionFragment(): CityDescriptionFragment

    @ContributesAndroidInjector
    internal abstract fun contributeCityMapFragment(): CityMapFragment
}