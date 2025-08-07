package org.careerseekers.csmailservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CsMailServiceApplication

fun main(args: Array<String>) {
    runApplication<CsMailServiceApplication>(*args)
}
