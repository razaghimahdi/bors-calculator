package com.razzaghi.borsCalculator.view.fragments;

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.razzaghi.borsCalculator.R
import com.razzaghi.borsCalculator.util.PersianNumbersToLettersConverter
import kotlinx.android.synthetic.main.calulator_fragment.*
import java.text.DecimalFormat


class CalculatorFragment : Fragment(R.layout.calulator_fragment) {

    val TAG = "CalculatorFragment"


    var wage = 0f
    var singlePrice = 0f
    var count = 0L
    var finaPrice = 0f


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIsFinalPriceOrCount()

        clearCal()

        calculate()


    }

    private fun calculate() {
        btnCal.setOnClickListener {
            if (checkWage() && checkSinglePrice() && checkCountOrFinalPrice()) {

                wage = edtWage.text.toString().trim().toFloat()
                singlePrice = edtSinglePrice.text.toString().trim().toFloat()
                getFinalPriceOrCount()

                checkShowCal()
            }
        }
    }

    private fun getFinalPriceOrCount() {
        if (linearFinalPrice.visibility == View.VISIBLE) {
            finaPrice = edtFinalPrice.text.toString().trim().toFloat()
            count = 0L
            calculateCount()
        } else {
            count = edtCount.text.toString().trim().toLong()
            finaPrice = 0f
            calculateFinalPrice()

        }
    }

    private fun calculateCount() {
        count = (finaPrice / (singlePrice * (1 + wage))).toLong()
        txtFinalCount.text = count.toString()
    }

    private fun calculateFinalPrice() {
        finaPrice = count * singlePrice * (1 + wage)
        val formatWithoutZero = DecimalFormat("0.#")
        val priceWithoutZero=formatWithoutZero.format(finaPrice).toDouble()

        val pntlc = PersianNumbersToLettersConverter()
        val resultString = pntlc.getParsedString(priceWithoutZero.toLong().toString())
        txtFinalPriceToText.text="$resultString تومان"

        val decimalFormatPrice = DecimalFormat("###,###,###")
        txtFinalPrice.text = decimalFormatPrice.format(priceWithoutZero).toString()
    }

    private fun checkCountOrFinalPrice(): Boolean {
        if (edtCount.text.toString().trim().isNullOrEmpty() &&
            edtFinalPrice.text.toString().trim().isNullOrEmpty()
        ) {

            txtErrorCount.visibility = View.VISIBLE

            txtErrorFinalPrice.visibility = View.VISIBLE

            doVibrate()

            return false
        } else {

            txtErrorCount.visibility = View.INVISIBLE
            txtErrorFinalPrice.visibility = View.INVISIBLE
        }
        return true
    }

    private fun checkWage(): Boolean {
        if (edtWage.text.toString().trim().isEmpty() ||
            (edtWage.text.toString().trim().toFloat() > 1 || edtWage.text.toString().trim()
                .toFloat() < 0)
        ) {
            txtErrorWage.visibility = View.VISIBLE
            doVibrate()
            return false
        } else {
            txtErrorWage.visibility = View.INVISIBLE
        }
        return true
    }


    private fun checkSinglePrice(): Boolean {
        if (edtSinglePrice.text.toString().trim().isEmpty()) {
            txtErrorSinglePrice.visibility = View.VISIBLE
            doVibrate()
            return false
        } else {
            txtErrorSinglePrice.visibility = View.INVISIBLE
        }
        return true
    }

    private fun doVibrate() {

        val v = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            v.vibrate(250)
        }
    }

    private fun clearCal() {
        btnClear.setOnClickListener {
            edtWage.setText("")
            edtFinalPrice.setText("")
            edtCount.setText("")
            edtSinglePrice.setText("")
            checkShowCal()
        }
    }

    private fun checkIsFinalPriceOrCount() {

        edtCount.doOnTextChanged { char, _, _, _ ->
            // Log.i(TAG, "checkIsFinalPriceOrCount edtCount char: ${char!!.length}")
            if (char!!.isNotEmpty()) {
                linearFinalPrice.visibility = View.GONE
            } else {
                linearFinalPrice.visibility = View.VISIBLE
            }

        }

        edtFinalPrice.doOnTextChanged { char, _, _, _ ->
            // Log.i(TAG, "checkIsFinalPriceOrCount edtFinalPrice char!!.length: ${char!!.length}")
            if (char!!.isNotEmpty()) {
                linearCount.visibility = View.GONE
            } else {
                linearCount.visibility = View.VISIBLE
            }

        }


    }

    private fun checkShowCal() {

        if (linearCount.visibility == View.VISIBLE && linearFinalPrice.visibility == View.VISIBLE) {
            linearCalFinaPrice.visibility = View.INVISIBLE
            linearCalCount.visibility = View.INVISIBLE

        } else {

            checkLinearCountVisibility()

            checkLinearFinalPrice()

        }
    }

    private fun checkLinearFinalPrice() {

        if (linearFinalPrice.visibility == View.VISIBLE) {
            linearCalCount.visibility = View.VISIBLE
        } else {
            linearCalCount.visibility = View.INVISIBLE
        }

    }

    private fun checkLinearCountVisibility() {
        if (linearCount.visibility == View.VISIBLE) {
            linearCalFinaPrice.visibility = View.VISIBLE
        } else {
            linearCalFinaPrice.visibility = View.INVISIBLE
        }
    }


}
