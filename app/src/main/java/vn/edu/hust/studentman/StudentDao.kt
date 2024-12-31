package vn.edu.hust.studentman

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StudentDao {
    @Insert
    suspend fun insertStudent(student: StudentModel)

    @Update
    suspend fun updateStudent(student: StudentModel)

    @Query("SELECT * FROM students")
    suspend fun getAllStudents(): List<StudentModel>

    @Query("DELETE FROM students WHERE studentId = :id")
    suspend fun deleteStudentById(id: String)
}