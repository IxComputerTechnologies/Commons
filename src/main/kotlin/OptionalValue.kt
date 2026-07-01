package net.ixct.commons

sealed interface OptionalValue<T> {

    val isPresent: Boolean
    val isAbsent: Boolean


    companion object {


        fun <T> absent(): Absent<T> = Absent.of()
    }


    fun orElse(fallback: T): T

    fun orElseThrow(): T

    fun orElseThrow(t: Throwable): T

    fun <O> map(mapper: (T) -> O): OptionalValue<O>

    fun ifPresent(action: (T) -> Unit): OptionalValue<T>

    fun ifAbsent(action: () -> Unit): OptionalValue<T>

    fun validated(validator: (T) -> Boolean): OptionalValue<T>


    data class Present<T>(
        val value: T
    ) : OptionalValue<T> {

        override val isPresent: Boolean = true
        override val isAbsent: Boolean = false

        override fun orElse(fallback: T): T = value

        override fun orElseThrow(): T = value

        override fun orElseThrow(t: Throwable): T = value

        override fun <O> map(mapper: (T) -> O): OptionalValue<O> = Present(mapper(value))

        override fun ifPresent(action: (T) -> Unit): OptionalValue<T> {
            action(value)

            return this
        }

        override fun ifAbsent(action: () -> Unit): OptionalValue<T> = this

        override fun validated(validator: (T) -> Boolean): OptionalValue<T> {
            return if (validator(value))
                this
            else
                absent()
        }

        override fun toString(): String = value.toString()
    }


    class Absent<T> private constructor() : OptionalValue<T> {

        override val isPresent: Boolean = false
        override val isAbsent: Boolean = true


        companion object {

            private val INSTANCE: Absent<*> = Absent<Any?>()


            @Suppress("unchecked_cast")
            internal fun <T> of(): Absent<T> = INSTANCE as Absent<T>
        }


        override fun orElse(fallback: T): T = fallback

        override fun orElseThrow(): T = throw NoSuchElementException()

        override fun orElseThrow(t: Throwable): T = throw t

        override fun <O> map(mapper: (T) -> O): OptionalValue<O> = of()

        override fun ifPresent(action: (T) -> Unit): OptionalValue<T> = this

        override fun ifAbsent(action: () -> Unit): OptionalValue<T> {
            action()

            return this
        }

        override fun validated(validator: (T) -> Boolean): OptionalValue<T> = this

        override fun toString(): String = "Absent"
    }
}
