/*
 * Copyright © 2026 IxComputerTechnologies (a.k.a. IxCT) and Belov Ivan Alekseevich (a.k.a. MrWooly357).
 *  Licensed under the MIT licence.
 */

package net.ixct.commons

import java.util.concurrent.ExecutorService
import kotlin.concurrent.Volatile

class FutureValue<T> {

    @Volatile
    private var value: OptionalValue<T> = OptionalValue.absent()
    @Volatile
    var error: Throwable? = null
        private set
    val isPresent: Boolean
        get() = value.isPresent
    val isAbsent: Boolean
        get() = value.isAbsent


    companion object {


        fun <T> supplied(
            executor: ExecutorService,
            supplier: () -> T,
            then: (T) -> Unit = {}
        ): FutureValue<T> {
            val future = FutureValue<T>()
            executor.submit {
                try {
                    future.supply(supplier)

                    if (future.isPresent)
                        then(future.orElseThrow())
                } catch (error: Throwable) {
                    future.supplyError(error)
                }
            }

            return future
        }
    }


    fun orElseThrow(): T = value.orElseThrow()

    fun orElse(value: T): T = this.value.orElse(value)

    fun supply(value: T) {
        if (isAbsent)
            synchronized(this) {
                if (isAbsent) {
                    this.value = OptionalValue.Present(value)

                    return
                }
            }

        throw ValueIsAlreadyPresentException("$value is already present!")
    }

    fun supply(supplier: () -> T) {
        supply(supplier())
    }

    fun supplyError(error: Throwable) {
        if (isAbsent)
            synchronized(this) {
                if (this.error == null) {
                    this.error = error

                    return
                }
            }

        throw ValueIsAlreadyPresentException("$error is already present!")
    }
}
