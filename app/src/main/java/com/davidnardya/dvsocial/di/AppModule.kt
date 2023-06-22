package com.davidnardya.dvsocial.di

import android.content.Context
import com.davidnardya.dvsocial.api.UserApi
import com.davidnardya.dvsocial.api.createRetrofitInstance
import com.davidnardya.dvsocial.repositories.UserRepository
import com.davidnardya.dvsocial.utils.Constants
import com.davidnardya.dvsocial.utils.UserPreferencesDataStore
import com.davidnardya.dvsocial.viewmodel.ChatViewModel
import com.davidnardya.dvsocial.viewmodel.FeedViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ) = UserPreferencesDataStore(context)

    @Singleton
    @Provides
    fun provideFeedViewModel(userRepository: UserRepository) = FeedViewModel(userRepository)

    @Singleton
    @Provides
    fun provideUserService(): UserApi = createRetrofitInstance(Constants.BASE_URL)

    @Singleton
    @Provides
    fun provideUserRepository(
        userApi: UserApi,
        userPreferencesDataStore: UserPreferencesDataStore
    ) = UserRepository(userApi, userPreferencesDataStore)

    @Singleton
    @Provides
    fun provideChatViewModel() = ChatViewModel()

}