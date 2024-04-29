package com.tgyuu.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "chatting_log",
)
data class ChattingLogEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "last_chatting")
    val lastChatting: String,
    val publishDate: String,
)
