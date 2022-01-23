package ve.com.teeac.mymarket.domain.usecases.product_use_cases

import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository

class ActivatedProduct (
    private val repository: DetailMarketRepository
) {

    suspend operator fun invoke(id: Long, isActivated: Boolean){
        repository.changeActivatedProduct(id, isActivated)
    }
}