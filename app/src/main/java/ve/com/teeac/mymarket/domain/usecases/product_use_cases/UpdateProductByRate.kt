package ve.com.teeac.mymarket.domain.usecases.product_use_cases

import kotlinx.coroutines.flow.first
import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository

class UpdateProductByRate(
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