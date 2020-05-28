package com.droidteahouse.rainydays.ui.login

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */

enum class Status {
    SUCCESS,
    FAILED,
    NULL
}


data class Result constructor(
    var status: Status = Status.NULL,
    var data : Any? = null,
    var msg: String? = null)

{

}
