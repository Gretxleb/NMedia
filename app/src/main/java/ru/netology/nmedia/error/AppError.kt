package ru.netology.nmedia.error

import java.io.IOException

sealed class AppError(var code: String) : RuntimeException()
class ApiError(val status: Int, code: String) : AppError(code)
object NetworkError : AppError("error_network"), IOException()
object UnknownError : AppError("error_unknown")
