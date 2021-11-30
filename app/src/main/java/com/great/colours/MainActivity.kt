package com.great.colours

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Attach view model
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        //Observe and update UI
        viewModel.getUniqueColorObserver().observe(this, {
            if (it != null) {
                //Hide progressbar
                pbProgress.visibility = View.GONE

                //Update text in UI
                edtUnique.setText(it)
            }
        })

        //Observe and update UI
        viewModel.getTotalBulbObserver().observe(this, {
            if (it != null) {
                tvTotal.text = it
            }
        })

        //COUNT button's click
        btnCount.setOnClickListener {

            //Check every edit text has some valid value
            if (everythingOK()) {

                //Get text from edittext and convert to Int
                val totalColor = edtColours.text.toString().toInt()
                val totalQuantity = edtQuantity.text.toString().toInt()
                val pickNumber = edtPick.text.toString().toInt()
                val repeatNumber = edtRepeat.text.toString().toInt()

                //Start progressbar and send data to view model
                pbProgress.visibility = View.VISIBLE
                viewModel.getTotalBulb(totalColor, totalQuantity)
                viewModel.getUniqueColorsCount(totalColor, totalQuantity, pickNumber, repeatNumber)
            }
        }
    }

    //Check for valid input and set error to particular edittext
    private fun validateInput(view: EditText): Boolean {
        val text = view.text.toString()
        return if (text.contains(".")) {
            view.error = getString(R.string.error_dot)
            false
        } else if (text.contains(",")) {
            view.error = getString(R.string.error_comma)
            false
        } else if (text.contains(" ")) {
            view.error = getString(R.string.error_space)
            false
        } else if (text.contains("-")) {
            view.error = getString(R.string.error_hyphen)
            false
        } else if (text.isEmpty()) {
            view.error = getString(R.string.error_empty)
            false
        } else if (text.toInt() <= 0) {
            view.error = getString(R.string.error_zero)
            false
        } else {
            view.error = null
            true
        }
    }

    //Check every edittext data is valid
    private fun everythingOK(): Boolean {
        if (validateInput(edtColours)) {
            if (validateInput(edtQuantity)) {
                if (validateInput(edtPick)) {
                    if (validateInput(edtRepeat)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}