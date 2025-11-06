package org.careerseekers.csmailservice.enums

enum class ParticipantStatus(private val alias: String) {
    NOT_STATED("Не указано"),
    PARTICIPANT("Участник"),
    FINALIST("Финалист"),
    PRIZEWINNER("Призёр"),
    WINNER("Победитель"),
    REFUSED("Отказался от участия");
}