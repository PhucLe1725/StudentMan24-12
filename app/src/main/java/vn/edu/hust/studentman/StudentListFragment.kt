package vn.edu.hust.studentman

import android.content.ContentValues
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

class StudentListFragment : Fragment() {

    private lateinit var studentListView: ListView
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var dbHelper: StudentDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_list, container, false)
        studentListView = view.findViewById(R.id.list_view_students)
        dbHelper = StudentDatabaseHelper(requireContext())
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            StudentDatabaseHelper.TABLE_NAME,
            null, null, null, null, null, null
        )
        studentAdapter = StudentAdapter(requireContext(), cursor)
        studentListView.adapter = studentAdapter
        registerForContextMenu(studentListView)
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_student_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                findNavController().navigate(R.id.action_studentListFragment_to_addEditStudentFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateStudentList() {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            StudentDatabaseHelper.TABLE_NAME,
            null, null, null, null, null, null
        )
        studentAdapter.updateCursor(cursor)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = requireActivity().menuInflater
        inflater.inflate(R.menu.context_menu_student_list, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.action_edit -> {
                val selectedStudent = studentAdapter.getItem(info.position) as StudentModel
                val bundle = Bundle().apply {
                    putSerializable("student", selectedStudent)
                }
                findNavController().navigate(R.id.action_studentListFragment_to_addEditStudentFragment, bundle)
                true
            }
            R.id.action_remove -> {
                val db = dbHelper.writableDatabase
                val student = studentAdapter.getItem(info.position) as StudentModel
                db.delete(StudentDatabaseHelper.TABLE_NAME, "${StudentDatabaseHelper.COLUMN_ID}=?", arrayOf(student.studentId))
                updateStudentList()

                Snackbar.make(requireView(), "Đã xóa sinh viên", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        val values = ContentValues().apply {
                            put(StudentDatabaseHelper.COLUMN_ID, student.studentId)
                            put(StudentDatabaseHelper.COLUMN_NAME, student.studentName)
                        }
                        db.insert(StudentDatabaseHelper.TABLE_NAME, null, values)
                        updateStudentList()
                    }.show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}