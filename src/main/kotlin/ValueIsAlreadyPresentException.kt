/*
 * Copyright © 2026 IxComputerTechnologies (a.k.a. IxCT) and Belov Ivan Alekseevich (a.k.a. MrWooly357).
 *  Licensed under the MIT licence.
 */

package net.ixct.commons

class ValueIsAlreadyPresentException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)
