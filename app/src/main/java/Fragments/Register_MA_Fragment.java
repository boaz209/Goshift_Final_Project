package Fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boaz.big_project.R;

import java.util.Random;

import Activitys.MainActivity;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Register_MA_Fragment.Register_MA_Fragment_InteractionListener} interface
 * to handle interaction events.
 * Use the {@link Register_MA_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register_MA_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Register_MA_Fragment_InteractionListener mListener;

    public Register_MA_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Register_MA_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Register_MA_Fragment newInstance(String param1, String param2) {
        Register_MA_Fragment fragment = new Register_MA_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_register__ma_, container, false);
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.Register_MA_Fragment_InteractionListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Register_MA_Fragment_InteractionListener) {
            mListener = (Register_MA_Fragment_InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface Register_MA_Fragment_InteractionListener {
        // TODO: Update argument type and name
        void Register_MA_Fragment_InteractionListener(Uri uri);
    }


    TextView myTextView;
    private ImageButton myImageButton;
    private Register_MA_Fragment_InteractionListener listener;
    private ClipboardManager myClipboard;
    private ClipData myClip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle inState) {
        View v = inflater.inflate(R.layout.fragment_register__ma_, container, false);
//        myTextView = (TextView)v.findViewById(R.id.MA_RandomFirebaseID);
//        showText();
//
//        View b=inflater.inflate(R.layout.fragment_register__ma_, container, false);
//        myImageButton=(ImageButton) v.findViewById(R.id.Random_copy_Button);
//        myImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                myClipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
//
//                String text;
//                text = myTextView.getText().toString();
//
//                myClip = ClipData.newPlainText("text", myTextView.getText().toString());
//                myClipboard.setPrimaryClip(myClip);
//                Toast.makeText(getActivity(), "Text Copied",Toast.LENGTH_SHORT).show();
//            }
//        });

        return v;
    }

    public void showText() {
        String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random RANDOM = new Random();

        StringBuilder sb = new StringBuilder(7);

        for (int i = 0; i < 7; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        String RandomID=sb.toString();
        myTextView.setText(RandomID);
    }



}
