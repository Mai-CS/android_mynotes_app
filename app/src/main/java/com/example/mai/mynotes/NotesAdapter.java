package com.example.mai.mynotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

class NotesAdapter extends ArrayAdapter<Note> {

    NotesAdapter(Context context, int resource, List<Note> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final Note noteObj = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, parent, false);
        }

        // Lookup view for data population
        TextView titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
        TextView descTextView = (TextView) convertView.findViewById(R.id.desc_text_view);
        final CheckBox noteCheckBox = (CheckBox) convertView.findViewById(R.id.note_check_box);

        // Populate the data into the template view using the data object
        if (noteObj != null) {
            titleTextView.setText(noteObj.getTitle());
            descTextView.setText(noteObj.getDescription());


            noteCheckBox.setVisibility(NotesFragment.isCheckBoxVisible);

            // Bug resolved:
            // After deleting the checked notes, positions that hold these notes don't know
            // their checkboxes are unchecked unless they hold different notes
            // Now, new notes stored at the same positions are not selected unless user does

            // Bug resolved:
            // Save state of checkboxes when rotating screens
            if (!NotesFragment.checkedNotes.contains(noteObj))
                noteCheckBox.setChecked(false);
            else
                noteCheckBox.setChecked(true);


            noteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (noteCheckBox.isChecked())
                        NotesFragment.checkedNotes.add(noteObj);
                    if (!noteCheckBox.isChecked())
                        NotesFragment.checkedNotes.remove(noteObj);
                }
            });
        }

        // Return the completed view to render on screen
        return convertView;

    }

}
