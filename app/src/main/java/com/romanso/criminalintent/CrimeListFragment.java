package com.romanso.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.romanso.criminalintent.model.Crime;
import com.romanso.criminalintent.model.CrimeLab;
import com.romanso.criminalintent.util.WithDate;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private static final String TAG = "CrimeListFragment";

    private static final int REQUEST_CRIME = 1;

    private int mClickedItemPosition = -1;

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = v.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Log.v(TAG, "Activity result is not OK!");
            return;
        }
        if (requestCode == REQUEST_CRIME) {
            Log.v(TAG, "Updating item " + mClickedItemPosition);
            mAdapter.notifyItemChanged(mClickedItemPosition);
            mClickedItemPosition = -1;
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Crime mCrime;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mHighCrimeImageView;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent, int layoutId) {
            super(inflater.inflate(layoutId, parent, false));
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mHighCrimeImageView = itemView.findViewById(R.id.high_crime_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickedItemPosition = getLayoutPosition();
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId(), mClickedItemPosition);
            startActivityForResult(intent, REQUEST_CRIME);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(WithDate.convertDate(mCrime.getDate()));
            mHighCrimeImageView.setVisibility(crime.isRequiresPolice() ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(inflater, parent, R.layout.list_item_crime);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }
}
