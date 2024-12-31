package vn.edu.hust.studentman

import android.content.Context

class StudentRepository(context: Context) {
    private val studentDao: StudentDao = StudentDatabase.getDatabase(context).studentDao()

    suspend fun insertStudent(student: StudentModel) {
        studentDao.insertStudent(student)
    }

    suspend fun updateStudent(student: StudentModel) {
        studentDao.updateStudent(student)
    }

    suspend fun getAllStudents(): List<StudentModel> {
        return studentDao.getAllStudents()
    }

    suspend fun deleteStudentById(id: String) {
        studentDao.deleteStudentById(id)
    }
}