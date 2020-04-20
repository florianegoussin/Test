package com.example.test.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.model.Person;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    private List<Person> mPersons;

    public PersonAdapter(Context context, List<Person> persons) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mPersons = persons;
    }

    @Override
    public PersonAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.person_item, parent, false);
        PersonAdapter.PersonViewHolder holder = new PersonAdapter.PersonViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        // Adapt the ViewHolder state to the new element
        //holder.mFirstNameTextView.setText(mPersons.get(position).getFirstName());
        //holder.mLastNameTextView.setText(mPersons.get(position).getLastName());
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }

    // Pattern ViewHolder :
    class PersonViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.person_adapter_firstname)
        TextView mFirstNameTextView;

        @BindView(R.id.person_adapter_lastname)
        TextView mLastNameTextView;

        public PersonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
