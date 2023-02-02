package com.kseniabl.petsapp.di

import com.kseniabl.petsapp.utils.TimerState
import com.kseniabl.petsapp.utils.TimerStateInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSaveModule {

    @Singleton
    @Binds
    abstract fun bindSaveUser(userSave: TimerState): TimerStateInterface
}