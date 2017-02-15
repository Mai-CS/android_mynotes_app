package com.example.mai.mynotes;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    // Store the position of the chosen item
    private int position;

    // Make sure not to save the current note twice
    private int isSaved;

    private AppCompatEditText titleEditText;
    private EditText descEditText;

    // Store inputs
    private String titleStr, descStr;

    // Store the content of the chosen item
    private Bundle mExtras;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment
        setRetainInstance(true);

        // Allow options menu at this fragment
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView timeTextView = (TextView) root.findViewById(R.id.time_text_view);
        titleEditText = (AppCompatEditText) root.findViewById(R.id.title_edit_text);
        descEditText = (EditText) root.findViewById(R.id.desc_edit_text);

        // Receive the intent
        mExtras = getActivity().getIntent().getExtras();

        // mExtras != null means the intent is received from ListView item
        if (mExtras != null) {
            timeTextView.setVisibility(View.VISIBLE);
            timeTextView.setText(mExtras.getString("timeKey"));
            titleEditText.setText(mExtras.getString("titleKey"));
            descEditText.setText(mExtras.getString("descKey"));
            position = mExtras.getInt("positionKey");
        }

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();

        titleStr = titleEditText.getText().toString();
        descStr = descEditText.getText().toString();

        // Make sure the inputs are filled
        // Avoid adding an already existed note by checking if mExtras == null
        // mExtras == null means the intent is received from the "Add" fab button instead of ListView item
        if (mExtras == null && (!titleStr.equals("") || !descStr.equals(""))) {
            saveNote();
        }

        // Make sure fields are not empty to edit them
        else if (mExtras != null && (!titleStr.equals("") || !descStr.equals(""))) {
            editNote();
        }

        // The inputs are empty
        else if (titleStr.equals("") && descStr.equals(""))
            (Toast.makeText(getActivity(), R.string.warning_save, Toast.LENGTH_SHORT)).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_detail, menu);

        // Allow user to copy the note
        if (mExtras != null) {
            MenuItem copyItem = menu.findItem(R.id.action_copy);
            copyItem.setVisible(true);
        }

        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_copy:
                copyNote();
                return true;

            case R.id.action_share:
                shareNote();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Add new note
    public void saveNote() {
        if (isSaved == 0) {
            Note noteObj = new Note();
            noteObj.setTitle(titleStr);
            noteObj.setDescription(descStr);
            setNoteTime(noteObj);

            NotesFragment.notesList.add(0, noteObj);

            (Toast.makeText(getActivity(), R.string.confirm_save, Toast.LENGTH_SHORT)).show();
        }
        isSaved = 1;
    }

    // Edit the note details
    public void editNote() {
        Note noteObj = NotesFragment.notesList.get(position);
        if (!noteObj.getTitle().equals(titleStr) || !noteObj.getDescription().equals(descStr)) {
            setNoteTime(NotesFragment.notesList.get(position));
            NotesFragment.notesList.get(position).setTitle(titleStr);
            NotesFragment.notesList.get(position).setDescription(descStr);
            (Toast.makeText(getActivity(), R.string.confirm_edit, Toast.LENGTH_SHORT)).show();
        }
    }

    // Copy the note title and description
    public void copyNote() {
        ClipboardManager clipboard =
                (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip =
                ClipData.newPlainText(getString(R.string.confirm_copy), descEditText.getText());
        clipboard.setPrimaryClip(clip);
        (Toast.makeText(getActivity(), R.string.confirm_copy, Toast.LENGTH_SHORT)).show();
    }

    // Share note description
    public void shareNote() {
        String text = descEditText.getText().toString();
        if (!text.equals("")) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else
            (Toast.makeText(getActivity(), R.string.warning_share, Toast.LENGTH_SHORT)).show();
    }

    public void setNoteTime(Note noteObj) {
        Calendar calendar = Calendar.getInstance();

        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " ";
        String month =
                calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + ", ";
        String year = String.valueOf(calendar.get(Calendar.YEAR)) + ", ";
        String hour = String.valueOf(calendar.get(Calendar.HOUR)) + ":";
        String minute = String.valueOf(calendar.get(Calendar.MINUTE)) + " ";
        if (minute.length() < 3)
            minute = "0" + minute;
        String am_pm_str;
        int am_pm_val = calendar.get(Calendar.AM_PM);
        if (am_pm_val == Calendar.AM)
            am_pm_str = "AM";
        else
            am_pm_str = "PM";

        noteObj.setTime(day + month + year + hour + minute + am_pm_str);
    }

}
