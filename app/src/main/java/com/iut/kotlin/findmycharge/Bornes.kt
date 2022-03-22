package com.iut.kotlin.findmycharge

import java.io.Serializable

data class Bornes (val id : String, val name : String, val address : String, val zip : String, val city : String, val nbPoints : String) : Serializable