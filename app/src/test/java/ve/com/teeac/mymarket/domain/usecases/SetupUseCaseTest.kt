package ve.com.teeac.mymarket.domain.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.repositories.AmountsSetupRepository
import ve.com.teeac.mymarket.domain.usecases.setup_use_cases.AddAmountsSetup
import ve.com.teeac.mymarket.domain.usecases.setup_use_cases.GetAmountsSetup
import ve.com.teeac.mymarket.presentation.InvalidPropertyApp

@ExperimentalCoroutinesApi
class SetupUseCaseTest {

    @MockK
    private lateinit var repository: AmountsSetupRepository

    private lateinit var useCase: SetupUseCase

    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxUnitFun = true)

        useCase = SetupUseCase(
            addSetup = AddAmountsSetup(repository),
            getSetup = GetAmountsSetup(repository)
        )

    }

    @Test
    fun add_new_setup() = runTest{

        val slot = slot<AmountsSetup>()

        coEvery { repository.addAmounts(capture(slot))}.returns( 1L )

        val id = useCase.addSetup(AmountsSetup())
        assertThat(id).isEqualTo(1L)
        assertThat(slot.captured).isEqualTo(AmountsSetup())

        coVerify(exactly = 1){ repository.addAmounts(AmountsSetup())}
        confirmVerified(repository)
    }

    @Test
    fun getAmounts_exist()= runTest{
        coEvery { repository.getAmounts(1L)}.returns( AmountsSetup(id = 1L) )

        val res = useCase.getSetup(1L)

        assertThat(res).isEqualTo(AmountsSetup(id=1L))

        coVerify(exactly = 1){ repository.getAmounts(1L)}
        confirmVerified(repository)
    }

    @Test
    fun getAmounts_does_not_exist()= runTest{
        coEvery { repository.getAmounts(1L)}.throws(InvalidPropertyApp(InvalidPropertyApp.AMOUNTS_SETUP_DOES_NOT_EXIST))

        try{
            useCase.getSetup(1L)
        }catch (e: InvalidPropertyApp){
            assertThat(e.message).isEqualTo(InvalidPropertyApp.AMOUNTS_SETUP_DOES_NOT_EXIST)
        }

        coVerify(exactly = 1){ repository.getAmounts(1L)}
        confirmVerified(repository)
    }

}