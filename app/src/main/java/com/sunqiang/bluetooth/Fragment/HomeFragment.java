package com.sunqiang.bluetooth.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sunqiang.bluetooth.Adapter.BindingListAdapter;
import com.sunqiang.bluetooth.Adapter.CustomLinearManager;
import com.sunqiang.bluetooth.Database.Record;
import com.sunqiang.bluetooth.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HomeFragment extends Fragment {

    private View rootView;
    private RecyclerView lvRecord;
    private BindingListAdapter recordAdapter;
    private ArrayList<Record> recordsList =new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.fragment_home, container, false);
        lvRecord= (RecyclerView) rootView.findViewById(R.id.lv_records);
        CustomLinearManager linearManager = new CustomLinearManager(getActivity());
        linearManager.setScrollEnable(false);
        lvRecord.setLayoutManager(linearManager);
        recordAdapter = new BindingListAdapter(R.layout.record_item_1);
        for(int i=0;i<5;i++){
            Record re=new Record();
            re.setName("hah");
            re.setScore(90-i);
            recordsList.add(re);
        }
        recordAdapter.setBindingData(recordsList);
        recordAdapter.setOnItemClickListener(new BindingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(view.getContext(),position+"wei",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        lvRecord.setAdapter(recordAdapter);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
