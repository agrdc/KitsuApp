package com.example.kitsuapp.domain


class ValidatePasswordUseCase {

    operator fun invoke(password: String): Boolean {
        return password.length >= minCharacters
    }

    companion object {
        private const val minCharacters = 6
    }
}