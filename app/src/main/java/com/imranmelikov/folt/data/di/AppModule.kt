package com.imranmelikov.folt.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.imranmelikov.folt.data.repository.FoltRepositoryImpl
import com.imranmelikov.folt.domain.repository.FoltRepository
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
    fun injectFireStore():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
    @Singleton
    @Provides
    fun injectRepo(fireStore: FirebaseFirestore)=FoltRepositoryImpl(fireStore) as FoltRepository
}