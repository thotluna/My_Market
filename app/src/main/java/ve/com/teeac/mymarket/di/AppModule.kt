package ve.com.teeac.mymarket.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ve.com.teeac.mymarket.domain.repositories.MarketsRepository
import ve.com.teeac.mymarket.data.data_source.AppDatabase
import ve.com.teeac.mymarket.data.repositories.AmountsSetupRepositoryImp
import ve.com.teeac.mymarket.data.repositories.DetailsMarketRepositoryImp
import ve.com.teeac.mymarket.data.repositories.MarketsRepositoryImp
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.repositories.AmountsSetupRepository
import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository
import ve.com.teeac.mymarket.domain.usecases.*
import ve.com.teeac.mymarket.domain.usecases.product_use_cases.*
import ve.com.teeac.mymarket.domain.usecases.setup_use_cases.AddAmountsSetup
import ve.com.teeac.mymarket.domain.usecases.setup_use_cases.GetAmountsSetup
import ve.com.teeac.mymarket.domain.usecases.setup_use_cases.SetupUseCase
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.SetupController
import ve.com.teeac.mymarket.presentation.marketdetails.product_form.ProductFormController
import ve.com.teeac.mymarket.presentation.markets.MarketsViewModel
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
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
    fun provideDetailMarketRepository(db: AppDatabase): DetailMarketRepository {
        return DetailsMarketRepositoryImp(db.detailMarketDao)
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
    fun provideSetupUseCase(repository: AmountsSetupRepository): SetupUseCase {
        return SetupUseCase(
            addSetup = AddAmountsSetup(repository),
            getSetup = GetAmountsSetup(repository)
        )
    }

    @Provides
    @Singleton
    fun providerProductUseCases(repository: DetailMarketRepository): ProductUseCase{
        return ProductUseCase(
            addProduct= AddProduct(repository),
            getAllProducts= GetAllProducts(repository),
            getProduct= GetProduct(repository),
            deleteProduct= DeleteProduct(repository),
            updateProducts= UpdateProductByRate(repository)
        )
    }

    @Provides
    @Singleton
    fun providerDetailsMarketUseCases(
        repositoryAmount: AmountsSetupRepository,
        repositoryMarkets: MarketsRepository,
        repositoryDetail: DetailMarketRepository
    ): DetailsMarketUseCase {
        return DetailsMarketUseCase(
            addAmountsSetup = AddAmountsSetup(repositoryAmount),
            getAmountsSetup = GetAmountsSetup(repositoryAmount),
            addMarket = AddMarket(repositoryMarkets),
            addProduct = AddProduct(repositoryDetail),
            getProduct = GetProduct(repositoryDetail),
            updateProduct = UpdateProductByRate(repositoryDetail),
            getAllProducts = GetAllProducts(repositoryDetail),
            deleteProduct = DeleteProduct(repositoryDetail)
        )
    }

    @Provides
    fun providerSetupController(useCase: SetupUseCase): SetupController{
        return SetupController(
            valueInitial = AmountsSetup(),
            useCase = useCase
        )
    }

    @Provides
    fun providerProductController(useCase: ProductUseCase): ProductFormController{
        return ProductFormController(
            useCase = useCase,
            dispatcher = Dispatchers.Main,
            dispatcherIo = Dispatchers.IO
        )
    }


}