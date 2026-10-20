package peralta.sophia.waterapp.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import peralta.sophia.waterapp.modelos.Register

class WaterViewModel: ViewModel() {
    private val _registers = mutableStateListOf<Register>()
    val registers: List<Register> = _registers

    private var nextId = 1

    fun addRegister() {
        _registers.add(0, Register(id = nextId))
    }
    fun answer(id: Int, drankWater: Boolean) {
        val i = _registers.indexOfFirst { it.id == id }
        if (i != -1) {
            _registers[i] = _registers[i].copy(drankWater = drankWater)
        }
    }
}