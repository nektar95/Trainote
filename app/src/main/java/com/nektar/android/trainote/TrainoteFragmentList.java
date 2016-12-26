package com.nektar.android.trainote;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/**
 * Created by olo35 on 11.04.2016.
 */
public class TrainoteFragmentList extends Fragment {

    private RecyclerView mRecyclerView;
    private TextView mTextView;
    private TextView mItemSettings;
    private TrainAdapter mAdapter;
    private View days[];
    private Date startWeek,endWeek;
    public static final String EXTRA_NOTE_ID = "note_passing_id";
    public static final String EXTRA_WEEK =
            "com.nektar.android.trainote.week";
    public static final String EXTRA_PERIOD =
            "com.nektar.android.trainote.period";
    public static final int SAVE_CODE = 11;

    private int mWeek;
    private NotePagerInterface mCallback;

    public static Fragment newInstance()
    {
        return new TrainoteFragmentList();
    }
    public static Fragment newInstance(int week)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_WEEK,week);

        TrainoteFragmentList frament = new TrainoteFragmentList();
        frament.setArguments(bundle);
        return frament;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mWeek = (int)getArguments().getSerializable(EXTRA_WEEK);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (NotePagerInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " not implemented interface");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_trainote, menu);
        menu.clear();
        List<String> weekPeriods = NoteContainer.get(getActivity()).getWeekPeriods();
        for (int i=0;i<weekPeriods.size();i++)
        {
            menu.add(0,i,Menu.NONE,weekPeriods.get(i));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        List<String> weekPeriods = NoteContainer.get(getActivity()).getWeekPeriods();
        if(item.getItemId()>weekPeriods.size())
        {
            return super.onContextItemSelected(item);
        }
        for (int i=0;i<weekPeriods.size();i++)
        {
            if(item.getItemId()==i)
            {

                mCallback.setCurrentWeek(i);
            }
        }
        return true;
    }

    private void updateUI()
    {
        NoteContainer container = NoteContainer.get(getActivity());

        //Days without activity color
        for (int i=0;i<7;i++)
        {
            days[i].setBackgroundResource(R.drawable.red_square);
        }

        List<Note> notes;
        Calendar calendar = Calendar.getInstance();
        if(container.getWeeks().get(mWeek).size()==0)
        {
            calendar.setTime(new Date());
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if ((dayOfWeek - 2) < 0) {
                calendar.add(Calendar.DAY_OF_YEAR, -6);
            } else {
                calendar.add(Calendar.DAY_OF_YEAR, (-1) * (dayOfWeek - 2));
            }

            startWeek = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, 6);
            endWeek = calendar.getTime();
            if(mAdapter==null)
            {
                mAdapter = new TrainAdapter(null);
                mRecyclerView.setAdapter(mAdapter);
            }
            else
            {
                mAdapter.setNotes(null);
                mAdapter.notifyDataSetChanged();
            }
        }
        else {
            notes = container.getWeeks().get(mWeek);
            calendar.setTime(notes.get(0).getDate());


            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if ((dayOfWeek - 2) < 0) {
                calendar.add(Calendar.DAY_OF_YEAR, -6);
            } else {
                calendar.add(Calendar.DAY_OF_YEAR, (-1) * (dayOfWeek - 2));
            }

            startWeek = calendar.getTime();
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < notes.size(); j++) {
                    //Toast.makeText(getActivity(),notes.get(j).getDate().toString(),Toast.LENGTH_SHORT).show();
                    if (notes.get(j).getDate().toString().equals(calendar.getTime().toString())) {
                        //setting color for days with activity
                        days[i].setBackgroundResource(R.drawable.green_square);
                    }
                }

                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            endWeek = calendar.getTime();
            if(mAdapter==null)
            {
                mAdapter = new TrainAdapter(notes);
                mRecyclerView.setAdapter(mAdapter);
            }
            else
            {
                mAdapter.setNotes(notes);
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public  View onCreateView(final LayoutInflater inflater,ViewGroup container, Bundle savedInstaneState) {
        View v = inflater.inflate(R.layout.trainote_fragment_list,container,false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.trainote_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTextView = (TextView) v.findViewById(R.id.week_area);
        mItemSettings = (TextView) v.findViewById(R.id.week_settings);

        mAdapter = new TrainAdapter(NoteContainer.get(getActivity()).getNotes());

        List<Note> list = NoteContainer.get(getActivity()).getNotes();

        days = new View[7];

        days[0] = v.findViewById(R.id.day_1);
        days[1] = v.findViewById(R.id.day_2);
        days[2] = v.findViewById(R.id.day_3);
        days[3] = v.findViewById(R.id.day_4);
        days[4] = v.findViewById(R.id.day_5);
        days[5] = v.findViewById(R.id.day_6);
        days[6] = v.findViewById(R.id.day_7);

        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TrainoteAddActivity.class);

                getActivity().startActivityForResult(intent,SAVE_CODE);

            }
        });
        updateUI();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startWeek);
        int sday = calendar.get(Calendar.DAY_OF_MONTH);
        int smonth = calendar.get(Calendar.MONTH);

        calendar.setTime(endWeek);
        int eday = calendar.get(Calendar.DAY_OF_MONTH);
        int emonth = calendar.get(Calendar.MONTH);
        smonth++;
        emonth++;
        mTextView.setText("Tydzień: "+sday+"."+smonth+" - "+eday+"."+emonth);
        mItemSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(),v);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.new_category:
                                Intent intentCat = new Intent(getActivity(),TrainoteCategoryActivity.class);
                                startActivity(intentCat);
                                return true;
                            case R.id.export:
                                isStoragePermissionGranted(); //glupia nakladka ekranowa.
                                String state = Environment.getExternalStorageState();

                                if (Environment.MEDIA_MOUNTED.equals(state)) {
                                    File file = new File(Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_DOCUMENTS),"TrainoteExport.txt");

                                    try {
                                        if (file.exists()) {
                                            file.delete();
                                            file.createNewFile();
                                        } else {
                                            file.createNewFile();
                                        }

                                        FileOutputStream f = new FileOutputStream(file);
                                        List<Note> list1 = NoteContainer.get(getActivity()).getNotes();
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        for (int i = 0; i < list1.size(); i++) {
                                            String temp = dateFormat.format(list1.get(i).getDate()) + " " + list1.get(i).getCategory() + " " + list1.get(i).getText() + "\n";
                                            f.write(temp.getBytes());
                                        }

                                        f.close();
                                        Toast.makeText(getActivity(), "Zapisano w Dokumentach", Toast.LENGTH_LONG).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getActivity(), "Zapis niemożliwy", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Nie można zapisać", Toast.LENGTH_LONG).show();
                                }
                                return true;
                            case R.id.information:
                                Intent intent = new Intent(getActivity(),TrainoteInfoActivity.class);
                                startActivity(intent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.menu_trainote);
                popupMenu.show();
            }
        });

        registerForContextMenu(mTextView);

        return v;
    }

    private class TrainHolder extends RecyclerView.ViewHolder// implements View.OnClickListener
    {
        private TextView mTextView;
        private TextView mCategory;
        private TextView mDate;
        private Note mNote;
        private View mView;

        public TrainHolder(View itemView) {
            super(itemView);
            mView = (LinearLayout) itemView.findViewById(R.id.list_item_linear);
           // itemView.setOnClickListener(this);
            mTextView = (TextView) itemView.findViewById(R.id.list_item_title);
            mCategory=(TextView)itemView.findViewById(R.id.list_item_category);
            mDate=(TextView) itemView.findViewById(R.id.list_item_date);


            mDate.setEnabled(false);

        }

        /*
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),TrainoteAddActivity.class);
            intent.putExtra(EXTRA_NOTE_ID,mNote.getId());
            startActivity(intent);
        }*/

        public void bindNote(final Note note)
        {
            mNote = note;

            Calendar calendar=Calendar.getInstance();
            calendar.setTime(mNote.getDate());
            //Toast.makeText(getActivity(),mNote.getDate().toString(),Toast.LENGTH_LONG).show();

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            switch (dayOfWeek)
            {
                case 1:
                {
                    //sunday
                    mView.setBackgroundResource(R.drawable.sunday);
                    break;
                }
                case 2:
                {
                    //monday
                    //mView.setBackgroundColor(Color.rgb(220 ,20, 60));
                    mView.setBackgroundResource(R.drawable.monday);
                    break;
                }
                case 3:
                {
                    //tuesday
                    mView.setBackgroundResource(R.drawable.tuesday);
                    break;
                }
                case 4:
                {
                    //wednesday
                    mView.setBackgroundResource(R.drawable.wednesday);
                    break;
                }
                case 5:
                {
                    //thursday
                    mView.setBackgroundResource(R.drawable.thursday);
                    break;
                }
                case 6:
                {
                    //friday
                    mView.setBackgroundResource(R.drawable.friday);
                    break;
                }
                case 7:
                {
                    //saturday
                    mView.setBackgroundResource(R.drawable.saturday);
                    break;
                }
            }

            mTextView.setText(mNote.getText());
            mCategory.setText(mNote.getCategory());

            mCategory.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FragmentManager fm = getFragmentManager();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());


                    dialog.setTitle("Usunąć aktywność?");
                    dialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(NoteContainer.get(getActivity()).getWeeks().get(mWeek).size()==1)
                            {

                                NoteContainer.get(getActivity()).delete(note);
                                mCallback.updatePager();


                            }
                            else {

                                NoteContainer.get(getActivity()).delete(note);
                                updateUI();
                            }


                        }
                    });
                    dialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                    return true;
                }
            });
            mCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),TrainoteAddActivity.class);
                    intent.putExtra(EXTRA_NOTE_ID,mNote.getId());
                    startActivityForResult(intent,SAVE_CODE);
                }
            });
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mDate.setText(dateFormat.format(mNote.getDate()));

        }
    }
    private class TrainAdapter extends RecyclerView.Adapter<TrainHolder>
    {
        private List<Note> mList;
        public TrainAdapter(List<Note> list)
        {
            mList=list;
        }

        @Override
        public TrainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.from(parent.getContext()).inflate(R.layout.items_list,parent,false);

            return new TrainHolder(v);
        }

        @Override
        public void onBindViewHolder(TrainHolder holder, int position)
        {
            holder.bindNote(mList.get(position));
        }

        @Override
        public int getItemCount() {
            if(mList == null)
            {
                return 0;
            }
            return mList.size();
        }

        public void setNotes(List<Note> notes)
        {
            mList = notes;
        }
    }
}
