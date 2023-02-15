package com.masum.metro.datastore.di

import com.masum.pdfreader.util.Dispatcher
import com.masum.pdfreader.util.Dispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(Dispatchers.IO)
    fun provideDispather() : CoroutineDispatcher = kotlinx.coroutines.Dispatchers.IO
}