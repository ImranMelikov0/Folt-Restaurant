package com.imranmelikov.folt.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.imranmelikov.folt.constants.APIConstants
import com.imranmelikov.folt.constants.DbConstant
import com.imranmelikov.folt.constants.SharedPrefConstant
import com.imranmelikov.folt.data.local.FoltDao
import com.imranmelikov.folt.data.local.FoltDataBase
import com.imranmelikov.folt.data.remote.webservice.CountryApi
import com.imranmelikov.folt.data.repository.FoltRepositoryImpl
import com.imranmelikov.folt.domain.repository.FoltRepository
import com.imranmelikov.folt.sharedpreferencesmanager.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectDataBase(@ApplicationContext context: Context)=Room.databaseBuilder(
        context,FoltDataBase::class.java,DbConstant.foltDb
    ).fallbackToDestructiveMigration().build()
    @Singleton
    @Provides
    fun injectDao(foltDataBase: FoltDataBase)=foltDataBase.foltDao()
    @Singleton
    @Provides
    fun injectFireStore():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
    @Provides
    @Singleton
    fun injectFireBaseAuth():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    fun injectSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SharedPrefConstant.myPref, Context.MODE_PRIVATE)
    }
    @Provides
    @Singleton
    fun injectSharedPreferencesManager(sharedPreferences: SharedPreferences): SharedPreferencesManager {
        return SharedPreferencesManager(sharedPreferences)
    }
    @Singleton
    @Provides
    fun injectCountryApi(): CountryApi {
        return Retrofit.Builder()
            .baseUrl(APIConstants.countryBASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountryApi::class.java)
    }
    @Singleton
    @Provides
    fun injectRepo(fireStore: FirebaseFirestore,dao: FoltDao,countryApi: CountryApi,auth: FirebaseAuth)=FoltRepositoryImpl(fireStore,dao,countryApi,auth) as FoltRepository
}