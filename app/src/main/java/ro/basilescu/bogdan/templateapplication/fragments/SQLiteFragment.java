package ro.basilescu.bogdan.templateapplication.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ro.basilescu.bogdan.templateapplication.R;
import ro.basilescu.bogdan.templateapplication.adapters.TodoAdapter;
import ro.basilescu.bogdan.templateapplication.database.TodoContract;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentSQLiteListener} interface
 * to handle interaction events.
 * Use the {@link SQLiteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SQLiteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Callback interface
    private OnFragmentSQLiteListener mListener;

    // RecyclerViewCursorAdapter
    private TodoAdapter mTodoAdapter;
    // Constant for CursorLoader for todos
    private static final int TODO_LOADER = 0;

    public SQLiteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SQLiteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SQLiteFragment newInstance(String param1, String param2) {
        SQLiteFragment fragment = new SQLiteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this method to create a basic new instance
     * of this fragment
     *
     * @return A new instance of fragment FirebaseFragment
     */
    public static SQLiteFragment newInstance() {
        return new SQLiteFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSQLiteListener) {
            mListener = (OnFragmentSQLiteListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentFirebaseListener");
        }
        // Initialize Todos Loader
        getActivity().getSupportLoaderManager().initLoader(TODO_LOADER, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sqlite, container, false);

        // Set up RecyclerView
        RecyclerView mTodoRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_sqlite);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTodoRecyclerView.setLayoutManager(linearLayoutManager);

        // Set up adapter for RecyclerView
        mTodoAdapter = new TodoAdapter(getActivity());
        mTodoRecyclerView.setAdapter(mTodoAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case TODO_LOADER:
                return new CursorLoader(
                        getActivity(),
                        TodoContract.TodoEntry.CONTENT_URI,
                        new String[]{
                                TodoContract.TodoEntry.TABLE_NAME + "." + TodoContract.TodoEntry._ID,
                                TodoContract.TodoEntry.COLUMN_SUMMARY,
                                TodoContract.TodoEntry.COLUMN_DESCRIPTION
                        },
                        null,
                        null,
                        null
                );
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case TODO_LOADER:
                mTodoAdapter.swapCursor(data);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case TODO_LOADER:
                mTodoAdapter.swapCursor(null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentSQLiteListener {
        // TODO: Update argument type and name
        void onFragmentSQLiteInteraction(String sqliteEvent);
    }
}
