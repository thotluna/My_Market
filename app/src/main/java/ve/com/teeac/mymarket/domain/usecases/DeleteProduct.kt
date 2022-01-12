package ve.com.teeac.mymarket.domain.usecases

import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository

class DeleteProduct(
    private val repository: DetailMarketRepository
) {

    suspend operator fun invoke(id: Long){
        repository.deleteProduct(id)
    }
}