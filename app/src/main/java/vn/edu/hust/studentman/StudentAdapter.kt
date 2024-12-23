package vn.edu.hust.studentman

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class StudentAdapter(private val context: Context, private var cursor: Cursor) : BaseAdapter() {

  override fun getCount(): Int = cursor.count

  override fun getItem(position: Int): Any {
    cursor.moveToPosition(position)
    return StudentModel(
      cursor.getString(cursor.getColumnIndexOrThrow(StudentDatabaseHelper.COLUMN_NAME)),
      cursor.getString(cursor.getColumnIndexOrThrow(StudentDatabaseHelper.COLUMN_ID))
    )
  }

  override fun getItemId(position: Int): Long = position.toLong()

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.layout_student_item, parent, false)
    cursor.moveToPosition(position)
    val nameTextView: TextView = view.findViewById(R.id.text_student_name)
    val idTextView: TextView = view.findViewById(R.id.text_student_id)
    nameTextView.text = cursor.getString(cursor.getColumnIndexOrThrow(StudentDatabaseHelper.COLUMN_NAME))
    idTextView.text = cursor.getString(cursor.getColumnIndexOrThrow(StudentDatabaseHelper.COLUMN_ID))
    return view
  }

  fun updateCursor(newCursor: Cursor) {
    cursor.close()
    cursor = newCursor
    notifyDataSetChanged()
  }
}