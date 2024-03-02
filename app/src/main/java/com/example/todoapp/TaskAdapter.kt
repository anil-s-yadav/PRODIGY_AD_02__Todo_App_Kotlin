import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R

class TaskAdapter(private val tasksList: MutableList<String>) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.one_item_task_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasksList[position]
        holder.bind(task, position)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }
    fun removeItem(position: Int) {
        tasksList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateItem(position: Int, newItem: String) {
        tasksList[position] = newItem
        notifyItemChanged(position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        private val editImageView: ImageView = itemView.findViewById(R.id.img_edit)
        private val deleteImageView: ImageView = itemView.findViewById(R.id.img_delete)

        fun bind(task: String, position: Int) {
            val parts = task.split("\n")
            val title = parts[0]
            val description =
                parts.getOrNull(1) ?: "" // Second part is the description (if available)

            titleTextView.text = title
            descriptionTextView.text = description

            checkBox.setOnCheckedChangeListener(null) // Remove previous listener to avoid interference
            checkBox.isChecked = titleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG != 0

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    titleTextView.paintFlags =
                        titleTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    titleTextView.paintFlags =
                        titleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }

            // Set an OnClickListener for the editImageView
            editImageView.setOnClickListener {
                showEditDialog()
            }

            deleteImageView.setOnClickListener {
                showDeleteConfirmationDialog()
            }
        }

        private fun showDeleteConfirmationDialog() {
            AlertDialog.Builder(deleteImageView.context)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes") { _, _ ->
                    // User clicked Yes, delete the item
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        (itemView.context as? TaskAdapter)?.removeItem(position)
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }

        private fun showEditDialog() {
            val alertDialogBuilder = AlertDialog.Builder(itemView.context)
            alertDialogBuilder.setTitle("Edit Task")

            val inputTitle = EditText(itemView.context)
            inputTitle.hint = "Enter Title"
            inputTitle.setText(titleTextView.text)

            val inputDescription = EditText(itemView.context)
            inputDescription.hint = "Enter Description"
            inputDescription.setText(descriptionTextView.text)

            val layout = LinearLayout(itemView.context)
            layout.orientation = LinearLayout.VERTICAL
            layout.addView(inputTitle)
            layout.addView(inputDescription)

            alertDialogBuilder.setView(layout)

            alertDialogBuilder.setPositiveButton("Done") { _, _ ->
                val newTitle = inputTitle.text.toString().trim()
                val newDescription = inputDescription.text.toString().trim()

                if (newTitle.isNotEmpty()) {
                    titleTextView.text = newTitle
                    descriptionTextView.text = newDescription

                    // Update the tasksList with the edited data
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        (itemView.context as? TaskAdapter)?.updateItem(position, "$newTitle\n$newDescription")
                    }
                }
            }

            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        // Add this method to TaskAdapter

    }
}
