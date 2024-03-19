package com.mshauchenka.habbit

data class Note (val id: Int, val title : String) {
}

fun generateNotes(): List<Note>{
    val list = emptyList<Note>().toMutableList()
    for (i in 1..10){
        list[i-1] = Note(i,"Test title")
    }
    return list
}