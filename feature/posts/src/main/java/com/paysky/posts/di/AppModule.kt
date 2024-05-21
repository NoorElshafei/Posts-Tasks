package com.paysky.posts.di


import android.content.Context
import androidx.room.Room
import com.paysky.posts.data.data_source.PostService
import com.paysky.posts.data.database.PostDao
import com.paysky.posts.data.database.PostDatabase
import com.paysky.posts.domain.repository.PostRepository
import com.paysky.posts.domain.repository.PostRepositoryImpl
import com.paysky.posts.domain.usecase.PostUseCase
import com.paysky.posts.domain.usecase.PostUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideService(
        retrofit: Retrofit
    ): PostService = retrofit.create(PostService::class.java)

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PostDatabase {
        return Room.databaseBuilder(
            context,
            PostDatabase::class.java,
            "post_database"
        ).build()
    }

    @Singleton
    @Provides
    fun providePostDao(database: PostDatabase): PostDao {
        return database.postDao()
    }
}


@Module
@InstallIn(SingletonComponent::class)
abstract class BindingModule {

    @Binds
    @Singleton
    abstract fun bindRepository(
        repositoryImpl: PostRepositoryImpl
    ): PostRepository

    @Binds
    @Singleton
    abstract fun bindPostUseCase(
        postUseCaseImpl: PostUseCaseImpl
    ): PostUseCase

}
