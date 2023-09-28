package com.example.chatapps.createUser.presentation.selectCountry

enum class Country(
    val flag : String = "",
    val code : String = ""
){
    Argentina(
        flag = "\uD83C\uDDE6\uD83C\uDDF7", code = "+54"
    ),
     Bolivia(
        flag = "\uD83C\uDDE7\uD83C\uDDF4", code = "+591"
    ),
     Brazil(
        flag = "\uD83C\uDDE7\uD83C\uDDF7", code = "+55"
    ),
     Chile(
        flag = "\uD83C\uDDE8\uD83C\uDDF1", code = "+56"
    ),
     Colombia(
        flag = "\uD83C\uDDE8\uD83C\uDDF4", code = "+57"
    ),
     CostaRica(
        flag = "\uD83C\uDDE8\uD83C\uDDF7", code = "+506"
    ),
     Ecuador(
        flag = "\uD83C\uDDEA\uD83C\uDDE8", code = "+593"
    ),
    Mexico(
        flag = "\uD83C\uDDF2\uD83C\uDDFD", code = "+52"
    ),
    Peru(
        flag = "\uD83C\uDDF5\uD83C\uDDEA", code = "+51"
    ),
    Venezuela(
        flag = "\uD83C\uDDFB\uD83C\uDDEA", code = "+58"
    )
}
