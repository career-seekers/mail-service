package org.careerseekers.csmailservice.enums

enum class EventTypes(private val alias: String) {
    QUALIFIERS("Отборочный этап"),
    FINAL("Финальный этап"),
    MASTER_CLASS("Образовательный этап");

    companion object {
        fun EventTypes.getAlias() = this.alias
    }
}