package authorizationservice.domain.exceptions

abstract class DomainException : Exception() {

    open fun details(): List<String> = arrayListOf()
}
