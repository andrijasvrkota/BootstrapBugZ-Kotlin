package org.bootstrapbugz.api.shared.validator.impl

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern
import org.bootstrapbugz.api.shared.constants.Regex
import org.bootstrapbugz.api.shared.validator.UsernameOrEmail

class UsernameOrEmailImpl : ConstraintValidator<UsernameOrEmail?, String> {
  override fun isValid(usernameOrEmail: String?, context: ConstraintValidatorContext): Boolean {
    if (usernameOrEmail == null) return false
    return Pattern.compile(Regex.USERNAME).matcher(usernameOrEmail).matches() ||
      Pattern.compile(Regex.EMAIL).matcher(usernameOrEmail).matches()
  }
}
