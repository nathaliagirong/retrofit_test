package com.example.prueba_retrofit.models

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.util.*


data class Data(

    var comment: String? = null,
    var name: String? = null,
    //var date: LocalDateTime = LocalDateTime.now()
    var date: String? = null

)