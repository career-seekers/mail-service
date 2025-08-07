package org.careerseekers.csmailservice.exceptions

abstract class AbstractHttpException(
    val status: Int,
    override val message: String?
) : RuntimeException(message)