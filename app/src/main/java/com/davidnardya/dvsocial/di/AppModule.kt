package com.davidnardya.dvsocial.di

import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFeedViewModel() = FeedViewModel()
}