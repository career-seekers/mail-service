package org.careerseekers.csmailservice.io.converters.extensions

import org.careerseekers.csmailservice.io.BasicSuccessfulResponse

fun String.toHttpResponse(): BasicSuccessfulResponse<String> {
    return BasicSuccessfulResponse(this)
}