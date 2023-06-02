package com.uberalles.dicodingstorysubmission.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener

class CustomEditText : AppCompatEditText, View.OnTouchListener {
    private val MIN_PASSWORD_LENGTH = 8
    private var isNameValid: Boolean = true
    private var isPasswordValid: Boolean = true
    private var isEmailValid: Boolean = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    init {
        addTextChangedListener { text ->
            when (id) {
                com.uberalles.dicodingstorysubmission.R.id.ed_register_name -> {
                    validateName(text.toString())
                }

                com.uberalles.dicodingstorysubmission.R.id.ed_register_email -> {
                    validateEmail(text.toString())
                }

                com.uberalles.dicodingstorysubmission.R.id.ed_register_password -> {
                    validatePassword(text.toString())
                }
            }
        }
    }

    private fun validateName(name: String) {
        this.inputType = android.text.InputType.TYPE_TEXT_VARIATION_PERSON_NAME

        if (name.isEmpty()) {
            error = "Name must not be empty"
            isNameValid = false
        } else {
            setError(null)
            isNameValid = true
        }
    }

    private fun validateEmail(email: String) {
        this.inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        if (email.isEmpty()) {
            error = "Email must not be empty"
            isEmailValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error = "Invalid email address"
            isEmailValid = false
        } else {
            error = null
            isEmailValid = true
        }
    }

    private fun validatePassword(password: String) {
        this.inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

        if (password.isEmpty()) {
            error = "Password must not be empty"
            isPasswordValid = false
        } else if (password.length < MIN_PASSWORD_LENGTH) {
            error = "Password must have at least $MIN_PASSWORD_LENGTH characters"
            isPasswordValid = false
        } else {
            error = null
            isPasswordValid = true
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= (right - compoundDrawables[2].bounds.width())) {
                text?.clear()
                return true
            }
        }
        return false
    }

    fun isValid(): Boolean {
        return isNameValid && isEmailValid && isPasswordValid
    }

}


