package org.careerseekers.csmailservice.utils

object CodeGenerator {
    fun generateVerificationCode(): String {
        val code = (1..999_999).random()
        return "%06d".format(code)
    }
}