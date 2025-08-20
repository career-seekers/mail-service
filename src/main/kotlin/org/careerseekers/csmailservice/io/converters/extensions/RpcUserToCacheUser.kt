package org.careerseekers.csmailservice.io.converters.extensions

import com.careerseekers.grpc.users.User
import org.careerseekers.csmailservice.dto.UsersCacheDto
import org.careerseekers.csmailservice.enums.UsersRoles

fun User.toCache() : UsersCacheDto {
    return UsersCacheDto(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        patronymic = this.patronymic,
        dateOfBirth = this.dateOfBirth.toDate(),
        email = this.email,
        mobileNumber = this.mobileNumber,
        password = this.password,
        role = UsersRoles.valueOf(this.role),
        avatarId = this.avatarId,
        verified = this.verified,
        isMentor = this.isMentor,
    )
}