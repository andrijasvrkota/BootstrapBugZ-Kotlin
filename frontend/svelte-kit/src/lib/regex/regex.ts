/*
 * Check for regex on: https://regexr.com/
 * */
/*
 * At least 2 characters
 * It supports only words with exception to thees characters: ^-`' ",.
 * It supports most languages and their special characters like: šđžčć
 *
 * e.g.
 * Dejan Zdravković
 * Дејан Здравковић
 * 德揚·茲德拉夫科維奇
 * ñÑâê都道府県Федерации
 * আবাসযোগ্য জমির걸쳐 있는
 *
 * */
export const FIRST_AND_LAST_NAME_REGEX = '^[^0-9_!¡?÷?¿/\\\\+=@#$%ˆ&*(){}\\[\\]|~<>;:]{2,}$';
/*
 * Between 2 and 16 characters long
 * It supports only letters, numbers, dot and underscore
 * Username can't start or end with dot or underscore
 * Username can't have two underscores or dots in a row
 * Username can't have underscore(_) and dot(.) in a row
 *
 * e.g.
 * test
 * test_test123
 * test.test123
 * test_test.test123
 * test123
 *
 * */
export const USERNAME_REGEX = '^(?=[a-zA-Z0-9._]{2,16}$)(?!.*[_.]{2})[^_.].*[^_.]$';
/*
 * Email Regex base on this article:
 * https://www.baeldung.com/java-email-validation-regex#regular-expression-by-rfc-5322-for-email-validation
 * */
export const EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
/*
 * At least one letter
 * At least one number
 * It can have a special characters
 * It can have an uppercase letter
 * It must be minimum 8 characters long
 *
 * e.g.
 * qwerty321
 * BlaBla123
 * blaBLA23"#
 *
 * */
export const PASSWORD_REGEX = '^(?=.*\\d)(?=.*[a-z])(?=.*[a-zA-Z]).{8,}$';
/*
 * It supports any string
 * */
export const ANYTHING_REGEX = '[\\s\\S]+';
