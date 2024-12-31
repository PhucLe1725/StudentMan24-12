package vn.edu.hust.studentman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch

class AddEditStudentFragment : Fragment() {

    private lateinit var studentRepository: StudentRepository
    private lateinit var editTextName: EditText
    private lateinit var editTextId: EditText
    private lateinit var buttonSave: Button
    private var student: StudentModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_edit_student, container, false)
        studentRepository = StudentRepository(requireContext())
        editTextName = view.findViewById(R.id.edit_text_name)
        editTextId = view.findViewById(R.id.edit_text_id)
        buttonSave = view.findViewById(R.id.button_save)

        student = arguments?.getSerializable("student") as? StudentModel
        student?.let {
            editTextName.setText(it.studentName)
            editTextId.setText(it.studentId)
            editTextId.isEnabled = false
        }

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val id = editTextId.text.toString()
            lifecycleScope.launch {
                if (student == null) {
                    studentRepository.insertStudent(StudentModel(id, name))
                } else {
                    studentRepository.updateStudent(StudentModel(id, name))
                }
                findNavController().navigateUp()
            }
        }

        return view
    }
}