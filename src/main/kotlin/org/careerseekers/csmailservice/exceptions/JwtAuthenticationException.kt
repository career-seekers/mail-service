package org.careerseekers.csmailservice.exceptions

import javax.naming.AuthenticationException

class JwtAuthenticationException(message: String) : AuthenticationException(message)