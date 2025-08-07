package org.careerseekers.csmailservice.io

interface AbstractResponse<T> {
    val status: Int
    val message: T?
}