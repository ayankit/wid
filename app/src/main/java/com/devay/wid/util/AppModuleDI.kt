package com.devay.wid.util

import android.app.Application
import androidx.room.Room
import com.devay.wid.data.repo.TodoRepo
import com.devay.wid.data.repo.TodoRepoImpl
import com.devay.wid.data.room.TodoDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModuleDI {

    @Provides
    @Singleton
    fun providesDatabase(app: Application): TodoDB {
        return Room.databaseBuilder(
            context = app,
            klass = TodoDB::class.java,
            name = "db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesRepo(db: TodoDB): TodoRepo {
        return TodoRepoImpl(db.dao)
    }

}