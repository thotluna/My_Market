package ve.com.teeac.mymarket.domain.usecases.product_use_cases

import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository

class DeleteProducts(
    private val repository: DetailMarketRepository
) {

    suspend operator fun invoke(idMarket: Long){
        repository.deleteProducts(idMarket)
    }
}