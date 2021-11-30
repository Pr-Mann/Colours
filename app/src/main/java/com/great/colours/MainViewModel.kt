package com.great.colours

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode
import java.text.DecimalFormat

class MainViewModel : ViewModel() {

    private var uniqueColor: MutableLiveData<String> = MutableLiveData()
    private var totalBulb: MutableLiveData<String> = MutableLiveData()

    fun getUniqueColorObserver(): MutableLiveData<String> {
        return uniqueColor
    }

    fun getTotalBulbObserver(): MutableLiveData<String> {
        return totalBulb
    }

    fun getUniqueColorsCount(
        totalColor: Int,
        totalQuantity: Int,
        pickNumber: Int,
        repeatNumber: Int
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            uniqueColor.value = get(totalColor, totalQuantity, pickNumber, repeatNumber)
        }
    }

    fun getTotalBulb(
        totalColor: Int,
        totalQuantity: Int,
    ) {
        viewModelScope.launch(Dispatchers.Main) {

            //Count total lighbulbs
            totalBulb.value = "Total lightbulbs: ${
                totalColor * totalQuantity
            }"
        }
    }

    //CPU extensive task run in background
    suspend fun get(
        totalColor: Int,
        totalQuantity: Int,
        pickNumber: Int,
        repeatNumber: Int
    ): String = withContext(Dispatchers.Default) {
        //Total as Decimal variable
        var total = 0.00

        //Repeat till repeatNumber times
        for (i in 1..repeatNumber) {

            //Add total value in previous total value
            total += getUniqueColor(
                totalColor,
                totalQuantity,
                pickNumber
            )
        }

        //Format for two decimal point
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING

        //return average
        df.format(total / repeatNumber)
    }

    private fun getUniqueColor(totalColor: Int, totalQuantity: Int, pickNumber: Int): Int {
        val bulbs = mutableListOf<Int>()
        var color = totalColor

        //Loop till total numbers of lightbulbs
        for (valueOfj in 0 until (totalColor * totalQuantity)) {
            if (valueOfj % (totalQuantity) == 0 && valueOfj != 0) {
                color--
            }
            bulbs.add(valueOfj, color)      //Assign colors for bulbs
        }

        //Randomly shuffle list and take first (pickNumber) bulb and return total of unique bulb
        return bulbs.shuffled().take(pickNumber).distinct().size
    }
}