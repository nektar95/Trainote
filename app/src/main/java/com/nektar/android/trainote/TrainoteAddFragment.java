package com.nektar.android.trainote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by olo35 on 11.04.2016.
 */
public class TrainoteAddFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private Button mDateButton;
    private EditText mEditText;
    private Spinner mSpinner;
    private Note mNote;
    private String mCategory;
    private String mTitle;
    private Date mDate;
    private boolean mNew;

    private static final String ARG_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int SAVE_CODE = 11;

    private static final int REQUEST_DATE = 0;

    public static Fragment newInstance(UUID id)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ID, id);

        TrainoteAddFragment fragment = new TrainoteAddFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static Fragment newInstance()
    {
        TrainoteAddFragment fragment = new TrainoteAddFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if(args != null)
        {
            UUID id = (UUID) args.getSerializable(ARG_ID);
            mNote = NoteContainer.get(getActivity()).getNote(id);
            mNew = false;
        }
        else{
            mNew = true;
            mNote = new Note();
        }
        mDate = mNote.getDate();
        //Toast.makeText(getActivity(),mDate.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        mCategory=parent.getItemAtPosition(position).toString();
        //mNote.setCategory(mCategory);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mCategory=parent.getItemAtPosition(1).toString();
        //mNote.setCategory(mCategory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_fragment, container, false);

        mDateButton = (Button) v.findViewById(R.id.add_date);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mDateButton.setText(dateFormat.format(mNote.getDate()));
        mEditText=(EditText) v.findViewById(R.id.add_text_title);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSpinner=(Spinner) v.findViewById(R.id.add_category);
        ArrayList<String> mList = new ArrayList<>(NoteContainer.get(getActivity()).getCategories());
        //ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(),R.array.empty_array,android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, mList);

        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mNote.getDate());

                dialog.setTargetFragment(TrainoteAddFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        if(mNew==false)
        {
            mEditText.setText(mNote.getText());
            mSpinner.setSelection(adapter.getPosition(mNote.getCategory()));
        }

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.confirm_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNote.setCategory(mCategory);
                mNote.setText(mTitle);
                mNote.setDate(mDate);

                //getting number of week
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mDate);
                int mWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                mNote.setWeek(mWeek);

                //Log.i("TAG",mCategory+" "+mTitle+" "+mDate.toString());

                if(mNew)
                {
                    NoteContainer.get(getActivity()).add(mNote);
                }
                else {
                    NoteContainer.get(getActivity()).updateNote(mNote);
                }
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();

            }
        });

        return v;
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(resultCode != Activity.RESULT_OK)
        {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);


            mDate = date;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mDateButton.setText(dateFormat.format(date));

        }
    }
}
