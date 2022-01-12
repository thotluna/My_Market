package ve.com.teeac.mymarket.domain.usecases

import kotlinx.coroutines.flow.first
import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository

class UpdateProductsMarketUseCase(
    private val repository: DetailMarketRepository
) {
    suspend operator fun invoke(rate: Double, marketId: Long) {
        val list = repository.getAllProducts(marketId).first()

        repository.updateProductByRate(
            list.map{
                    it.addRate(rate)
            }
        )
    }
}