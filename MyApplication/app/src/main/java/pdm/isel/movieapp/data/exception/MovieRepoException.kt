package pdm.isel.movieapp.data.exception

open class MovieRepoException : Exception {
    constructor() : super("An Exception Occurred in Repository!")
    constructor(s: String) : super(s)
}