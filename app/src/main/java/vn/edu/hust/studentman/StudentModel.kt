package vn.edu.hust.studentman

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "students")
data class StudentModel(
    @PrimaryKey val studentId: String,
    val studentName: String
) : Serializable