package net.ixct.commons

sealed interface DataResult<T> {

    val isSuccess: Boolean
    val isPartial: Boolean
    val isFailure: Boolean
    val value: OptionalValue<T>


    fun orElse(fallback: T): T

    fun orElseThrow(): T

    fun orElseThrow(t: Throwable): T

    fun withPartial(partial: T): DataResult<T>

    fun <O> map(mapper: (T) -> O): DataResult<O>

    fun ifSuccess(action: (T) -> Unit): DataResult<T>

    fun ifPartial(action: (T) -> Unit): DataResult<T>

    fun ifFailure(action: () -> Unit): DataResult<T>

    fun validated(validator: (T) -> Boolean): DataResult<T>


    data class Success<T>(
        val data: T
    ) : DataResult<T> {

        override val isSuccess: Boolean = true
        override val isPartial: Boolean = false
        override val isFailure: Boolean = false
        override val value: OptionalValue<T> by lazy { OptionalValue.Present(data) }


        override fun orElse(fallback: T): T = data

        override fun orElseThrow(): T = data

        override fun orElseThrow(t: Throwable): T = data

        override fun withPartial(partial: T): DataResult<T> = this

        override fun <O> map(mapper: (T) -> O): DataResult<O> = Success(mapper(data))

        override fun ifSuccess(action: (T) -> Unit): DataResult<T> {
            action(data)

            return this
        }

        override fun ifPartial(action: (T) -> Unit): DataResult<T> = this

        override fun ifFailure(action: () -> Unit): DataResult<T> = this

        override fun validated(validator: (T) -> Boolean): DataResult<T> {
            return if (validator(data))
                this
            else
                Failure("Validation failed!")
        }

        override fun toString(): String = "Success -> $data"
    }


    data class Partial<T>(
        val data: T,
        val message: String? = null,
        val cause: Throwable? = null
    ) : DataResult<T> {

        override val isSuccess: Boolean = false
        override val isPartial: Boolean = true
        override val isFailure: Boolean = false
        override val value: OptionalValue<T> by lazy { OptionalValue.Present(data) }


        override fun orElse(fallback: T): T = data

        override fun orElseThrow(): T = data

        override fun orElseThrow(t: Throwable): T = data

        override fun withPartial(partial: T): DataResult<T> = this

        override fun <O> map(mapper: (T) -> O): DataResult<O> = Partial(mapper(data))

        override fun ifSuccess(action: (T) -> Unit): DataResult<T> = this

        override fun ifPartial(action: (T) -> Unit): DataResult<T> {
            action(data)

            return this
        }

        override fun ifFailure(action: () -> Unit): DataResult<T> = this

        override fun validated(validator: (T) -> Boolean): DataResult<T> {
            return if (validator(data))
                this
            else
                Failure("Validation failed!")
        }

        override fun toString(): String = "Partial(data: $data, message: $message, cause: $cause)"
    }


    data class Failure<T>(
        val message: String? = null,
        val cause: Throwable? = null
    ) : DataResult<T> {

        override val isSuccess: Boolean = false
        override val isPartial: Boolean = false
        override val isFailure: Boolean = true
        override val value: OptionalValue<T> by lazy { OptionalValue.absent() }


        override fun orElse(fallback: T): T = fallback

        override fun orElseThrow(): T = throw NoSuchElementException()

        override fun orElseThrow(t: Throwable): T = throw t

        override fun withPartial(partial: T): DataResult<T> = Partial(partial, message, cause)

        @Suppress("unchecked_cast")
        override fun <O> map(mapper: (T) -> O): DataResult<O> = this as DataResult<O>

        override fun ifSuccess(action: (T) -> Unit): DataResult<T> = this

        override fun ifPartial(action: (T) -> Unit): DataResult<T> = this

        override fun ifFailure(action: () -> Unit): DataResult<T> {
            action()

            return this
        }

        override fun validated(validator: (T) -> Boolean): DataResult<T> = this

        override fun toString(): String = "Failure(message: $message, cause: $cause)"
    }
}
