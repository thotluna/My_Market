package ve.com.teeac.mymarket.presentation.marketdetails.amountssetup

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.usecases.setup_use_cases.AddAmountsSetup
import ve.com.teeac.mymarket.domain.usecases.setup_use_cases.GetAmountsSetup
import ve.com.teeac.mymarket.domain.usecases.SetupUseCase

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class SetupControllerTest {

    private val standardTestDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var addSetup: AddAmountsSetup

    @MockK
    private lateinit var getSetup: GetAmountsSetup

    private lateinit var useCase: SetupUseCase

    private lateinit var controller: SetupController

    private val setup = AmountsSetup(
        id = 1,
        marketId = 1,
        rate = 5.0,
        maximumAvailableBolivares = 0.0,
        maximumAvailableDollar = 100.0,
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = SetupUseCase(
            addSetup = addSetup,
            getSetup = getSetup
        )
        controller = SetupController(
            valueInitial = AmountsSetup(),
            useCase = useCase,
            dispatcher = standardTestDispatcher,
            dispatcherIo = Dispatchers.IO
        )
    }

    @Test
    fun `controller started`() {
        assertThat(controller.rate.value.number).isNull()
        assertThat(controller.rate.value.title).isEqualTo(SetupController.NAME_RATE)
        assertThat(controller.maxBolivares.value.number).isNull()
        assertThat(controller.maxBolivares.value.title).isEqualTo(SetupController.NAME_BOLIVARES)
        assertThat(controller.maxDollar.value.number).isNull()
        assertThat(controller.maxDollar.value.title).isEqualTo(SetupController.NAME_DOLLAR)
    }

    @Test
    fun `load controller with setup existent`() = runTest {

        coEvery { useCase.getSetup(setup.id!!) } coAnswers { setup }

        controller.loadSetup(AmountSetupEvent.LoadSetup(setup.id!!))

        assertThat(controller.rate.value.number).isEqualTo(setup.rate)
        assertThat(controller.maxBolivares.value.number).isEqualTo(setup.maximumAvailableBolivares)
        assertThat(controller.maxDollar.value.number).isEqualTo(setup.maximumAvailableDollar)

        coVerify(exactly = 1) { useCase.getSetup(setup.id!!) }
        confirmVerified(getSetup)
    }


    @Test
    fun `Save new Setup `()=runTest{
        coEvery { useCase.addSetup(any()) } coAnswers { setup.id!! }

        controller.onEvent(AmountSetupEvent.EnteredRate(setup.rate))
        controller.onEvent(AmountSetupEvent.EnteredMaxBolivares(setup.maximumAvailableBolivares))
        controller.onEvent(AmountSetupEvent.EnteredMaxDollar(setup.maximumAvailableDollar))

        val expected = setup.copy(id= null, maximumAvailableBolivares = 500.00)

        val value = controller.get(setup.marketId!!)

        assertThat(value).isEqualTo(expected)

        controller.save(AmountSetupEvent.Save(setup.marketId!!))

        coVerify(exactly = 1) { useCase.addSetup(expected) }
        confirmVerified(useCase.addSetup)

    }



}