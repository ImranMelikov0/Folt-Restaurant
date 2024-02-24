package com.imranmelikov.folt.data.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.imranmelikov.folt.constants.DbConstant
import com.imranmelikov.folt.data.local.FoltDao
import com.imranmelikov.folt.data.local.FoltDataBase
import com.imranmelikov.folt.data.repository.FoltRepositoryImpl
import com.imranmelikov.folt.domain.repository.FoltRepository
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
    fun injectDataBase(@ApplicationContext context: Context)=Room.databaseBuilder(
        context,FoltDataBase::class.java,DbConstant.foltDb
    ).build()
    @Singleton
    @Provides
    fun injectDao(foltDataBase: FoltDataBase)=foltDataBase.foltDao()
    @Singleton
    @Provides
    fun injectFireStore():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
    @Singleton
    @Provides
    fun injectRepo(fireStore: FirebaseFirestore,dao: FoltDao)=FoltRepositoryImpl(fireStore,dao) as FoltRepository
}