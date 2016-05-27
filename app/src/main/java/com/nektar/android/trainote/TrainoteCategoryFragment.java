package com.nektar.android.trainote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by olo35 on 17.05.2016.
 */
public class TrainoteCategoryFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private EditText mEditText;
    private Button mButtonOk;
    private String newCategory;
    private String deleteCategory;
    private Spinner mSpinner;
    private Button mButtonDelete;


    public static Fragment newInstance()
    {
        return new TrainoteCategoryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.category_trainote,container,false);

        mEditText = (EditText) v.findViewById(R.id.new_category);
        mButtonOk = (Button) v.findViewById(R.id.new_category_ok);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newCategory = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteContainer.get(getActivity()).addCategroy(newCategory);
                update();
            }
        });

        mSpinner=(Spinner) v.findViewById(R.id.delete_category);
        ArrayList<String> mList = new ArrayList<>(NoteContainer.get(getActivity()).getCategories());
        //ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(),R.array.empty_array,android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, mList);

        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        mButtonDelete = (Button) v.findViewById(R.id.delete_button);
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteContainer.get(getActivity()).deleteCategory(deleteCategory);
                update();
            }
        });


        return v;
    }
    private void update()
    {
        ArrayList<String> mList = new ArrayList<>(NoteContainer.get(getActivity()).getCategories());
        //ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(),R.array.empty_array,android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, mList);

        mSpinner.setAdapter(adapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        deleteCategory = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        deleteCategory = parent.getItemAtPosition(1).toString();
    }
}
