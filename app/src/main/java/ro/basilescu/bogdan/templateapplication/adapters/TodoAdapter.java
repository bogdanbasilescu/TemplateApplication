package ro.basilescu.bogdan.templateapplication.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder;

import ro.basilescu.bogdan.templateapplication.R;

public class TodoAdapter extends RecyclerViewCursorAdapter<TodoAdapter.TodoViewHolder> {

    /**
     * Constructor
     *
     * @param context The Context the Adapter is displayed in
     */
    public TodoAdapter(Context context) {
        super(context);
        setupCursorAdapter(null, 0, R.layout.list_item_todo, false);
    }

    /**
     * Returns the ViewHolder to use for this adapter.
     *
     * @param parent   The ViewGroup
     * @param viewType The viewType of the view
     * @return The ViewHolder for the Adapter
     */
    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TodoViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent));
    }

    /**
     * Moves the Cursor of the CursorAdapter to the appropriate position and binds the view for
     * that item.
     *
     * @param holder   The ViewHolder for the Adapter
     * @param position The position to which is pointing in the Adapter
     */
    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        // Move cursor to this position
        mCursorAdapter.getCursor().moveToPosition(position);

        // Set the ViewHolder
        setViewHolder(holder);

        // Bind this view
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }

    /**
     * ViewHolder class used to the display a TodoItem
     */
    public class TodoViewHolder extends RecyclerViewCursorViewHolder implements View.OnClickListener {
        private TextView mTodoSummary;
        private TextView mTodoDescription;

        public TodoViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mTodoSummary = (TextView) view.findViewById(R.id.todo_summary);
            mTodoDescription = (TextView) view.findViewById(R.id.todo_description);
        }

        @Override
        public void bindCursor(Cursor cursor) {
            mTodoSummary.setText(cursor.getString(1));
            mTodoDescription.setText(cursor.getString(2));
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, "Clicked item", Toast.LENGTH_SHORT).show();
        }
    }
}

