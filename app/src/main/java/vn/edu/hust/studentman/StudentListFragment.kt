package vn.edu.hust.studentman

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class StudentListFragment : Fragment() {

    private lateinit var studentListView: ListView
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var studentRepository: StudentRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_list, container, false)
        studentListView = view.findViewById(R.id.list_view_students)
        studentRepository = StudentRepository(requireContext())

        lifecycleScope.launch {
            val students = studentRepository.getAllStudents()
            studentAdapter = StudentAdapter(requireContext(), students)
            studentListView.adapter = studentAdapter
        }

        registerForContextMenu(studentListView)
        setHasOptionsMenu(true)
        return view
    }

    private fun updateStudentList() {
        lifecycleScope.launch {
            val students = studentRepository.getAllStudents()
            studentAdapter.updateStudents(students)
        }
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
                lifecycleScope.launch {
                    val student = studentAdapter.getItem(info.position) as StudentModel
                    studentRepository.deleteStudentById(student.studentId)
                    updateStudentList()

                    Snackbar.make(requireView(), "Đã xóa sinh viên", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            lifecycleScope.launch {
                                studentRepository.insertStudent(student)
                                updateStudentList()
                            }
                        }.show()
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}