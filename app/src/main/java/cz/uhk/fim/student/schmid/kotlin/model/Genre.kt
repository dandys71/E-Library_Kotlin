package cz.uhk.fim.student.schmid.kotlin.model

//Standardně by byly žárny uloženy formou tabulky v databázi, pro ukázku funkčnosti však postačí enum
enum class Genre {
    NONE,
    KRIMI,
    SCI_FI,
    FANTASY,
    ROMAN,
    POVIDKA,
    POEZIE,
    NAUCNA,
    HISTORICKA,
    CESTOPIS,
    UCEBNICE,
    ODBORNA_PUBLIKACE;

    fun getGenreName() =  name.toLowerCase().capitalize().replace("_", "-")

}