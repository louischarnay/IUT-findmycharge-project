package com.iut.kotlin.findmycharge

import java.io.Serializable

data class Bornes (val id : String, val name : String, val latitude : String, val longitude : String, val address : String, val zip : String, val region : String, val city : String, val nbPoints : String, val tel : String, val impl : String) : Serializable