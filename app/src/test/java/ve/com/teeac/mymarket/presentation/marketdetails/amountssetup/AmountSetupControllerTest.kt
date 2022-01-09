package ve.com.teeac.mymarket.presentation.marketdetails.amountssetup

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class AmountSetupControllerTest {

    private lateinit var controller: AmountSetupController

    @Before
    fun setUp() {
        controller = AmountSetupController()
    }

    @Test
    fun `controller started`() {
        assertThat(controller.rate.value.number).isNull()
        assertThat(controller.rate.value.title).isEqualTo(AmountSetupController.NAME_RATE)
        assertThat(controller.maxBolivares.value.number).isNull()
        assertThat(controller.maxBolivares.value.title).isEqualTo(AmountSetupController.NAME_BOLIVARES)
        assertThat(controller.maxDollar.value.number).isNull()
        assertThat(controller.maxDollar.value.title).isEqualTo(AmountSetupController.NAME_DOLLAR)
    }

    @Test
    fun `enter new number at rate`(){
        val number = 5
        controller.onAmountsSetupEvent(AmountSetupEvent.EnteredConvert(number))
        assertThat(controller.rate.value.number)
            .isEqualTo(number)
        controller.onAmountsSetupEvent(AmountSetupEvent.EnteredConvert(null))
        assertThat(controller.rate.value.number)
            .isEqualTo(null)
    }
    @Test
    fun `enter new number at maxBolivares`(){
        val number = 5.3
        controller.onAmountsSetupEvent(AmountSetupEvent.EnteredAmounts(number))
        assertThat(controller.maxBolivares.value.number)
            .isEqualTo(number)
        controller.onAmountsSetupEvent(AmountSetupEvent.EnteredAmounts(null))
        assertThat(controller.maxBolivares.value.number)
            .isEqualTo(null)
    }
    @Test
    fun `enter new number at maxDollar`(){
        val number = 5.9
        controller.onAmountsSetupEvent(AmountSetupEvent.EnteredAmountsDollar(number))
        assertThat(controller.maxDollar.value.number)
            .isEqualTo(number)
        controller.onAmountsSetupEvent(AmountSetupEvent.EnteredAmountsDollar(null))
        assertThat(controller.maxDollar.value.number)
            .isEqualTo(null)
    }


}