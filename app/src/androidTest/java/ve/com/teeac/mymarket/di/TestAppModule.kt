package ve.com.teeac.mymarket.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ve.com.teeac.mymarket.data.data_source.AppDatabase
import ve.com.teeac.mymarket.data.data_source.MarketDao
import ve.com.teeac.mymarket.data.repositories.AmountsSetupRepositoryImp
import ve.com.teeac.mymarket.data.repositories.MarketsRepositoryImp
import ve.com.teeac.mymarket.domain.repositories.AmountsSetupRepository
import ve.com.teeac.mymarket.domain.repositories.MarketsRepository
import ve.com.teeac.mymarket.domain.usecases.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            app,
            AppDatabase::class.java,
        ).build()
    }

    @Provides
    @Singleton
    fun provideMarketRepository(db: AppDatabase): MarketsRepository {
        return MarketsRepositoryImp(db.marketDao)
    }

    @Provides
    @Singleton
    fun provideAmountSetupRepository(db: AppDatabase): AmountsSetupRepository {
        return AmountsSetupRepositoryImp(db.amountsSetupDao)
    }

    @Provides
    @Singleton
    fun provideMarketUseCases(repository: MarketsRepository): MarketUseCases {
        return MarketUseCases(
            addMarket = AddMarket(repository),
            getMarkets = GetMarkets(repository)
        )
    }

    @Provides
    @Singleton
    fun providerDetailsMarketUseCases(
        repositoryAmount: AmountsSetupRepository,
        repositoryMarkets: MarketsRepository
    ): DetailsMarketUseCase{
        return DetailsMarketUseCase(
            addAmountsSetup = AddAmountsSetup(repositoryAmount),
            getAmountsSetup = GetAmountsSetup(repositoryAmount),
            addMarket = AddMarket(repositoryMarkets)
        )
    }
}