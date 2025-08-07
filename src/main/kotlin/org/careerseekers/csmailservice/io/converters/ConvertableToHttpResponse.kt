@file:Suppress("UNCHECKED_CAST")

package org.careerseekers.csmailservice.io.converters

import org.careerseekers.csmailservice.io.BasicSuccessfulResponse

interface ConvertableToHttpResponse<T : ConvertableToHttpResponse<T>> {
    fun toHttpResponse(): BasicSuccessfulResponse<T> = BasicSuccessfulResponse(this as T)
}