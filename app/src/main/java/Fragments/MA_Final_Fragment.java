package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boaz.big_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MA_Final_Fragment.MA_Final_FIListener} interface
 * to handle interaction events.
 * Use the {@link MA_Final_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MA_Final_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MA_Final_FIListener mListener;
    //private ArrayList<String> NameArray;
    private ArrayList<String> NameArray = new ArrayList<>();
    private List<String> Array_Shifts = new ArrayList<>();

    public MA_Final_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MA_Final_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MA_Final_Fragment newInstance(String param1, String param2) {
        MA_Final_Fragment fragment = new MA_Final_Fragment();
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

    /**
     * -----------------------------\/\/\/-------------------------------onCreateView-------------------------------\/\/\/-----------------------------------------
     */


    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    final CollectionReference docRef = db
            .collection("User");
    final Calendar calender = Calendar.getInstance(); // calender.get(Calendar.WEEK_OF_YEAR)
    final String Current_Week = String.valueOf(calender.get(Calendar.WEEK_OF_YEAR));
    final ArrayList<String> Emp_name = new ArrayList<>();  // ArrayList of employee

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ma__final_, container, false);
        ViewGroup rootView = (ViewGroup) v.findViewById(R.id.fragment_ma_final).getRootView();  // מקבל את כל VIEW שיש בפרימנט
//        fiilTextview(rootView);
        List_fiil(v);
        spinner_test(v);
        organizer_shift(v);
        Send_data(v);
        return v;
        //return inflater.inflate(R.layout.fragment_ma_final, container, false);
    }

    private void Send_data(View v) {
        final ViewGroup rootView = (ViewGroup) v.findViewById(R.id.fragment_ma_final).getRootView();  // מקבל את כל VIEW שיש בפרימנט
        final int childViewCount = rootView.getChildCount();

        // Create a new user with a first and last name
        final Map<String, Object> Final_shifts = new HashMap<>();

        Button button = v.findViewById(R.id.button_Send);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < childViewCount; i++) {
                    View workWithMe = rootView.getChildAt(i);
                    // chack what is Spinner
                    if (workWithMe instanceof TextView) {
                        TextView textViewCheack = (TextView) workWithMe;
                        final String IDname = getResources().getResourceEntryName(textViewCheack.getId());
                        int IDnumber = textViewCheack.getId();
//                        Log.d(TAG, ""+IDname+".startsWith(\"text\",0): "+IDname.startsWith("text",0));
                        if (!IDname.startsWith("text",0))
                        {
                            Toast.makeText(getActivity(),"הזנת המשמרות בוצעה בהצלחה!",Toast.LENGTH_LONG).show();
                            Final_shifts.put(IDname,textViewCheack.getText());
                        }


                    }
                }

                //Set the data in DB
                db.collection("Company").document(""+user.getUid())
                        .set(Final_shifts)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });

            }
        });





    }

    private void organizer_shift(final View view) {

        final ArrayList<String> Emp_name_by_Shiff = new ArrayList<>();  // ArrayList of employee
        final CollectionReference docRef = db.collection("User"); // db path




        final ViewGroup rootView = (ViewGroup) view.findViewById(R.id.fragment_ma_final).getRootView();  // מקבל את כל VIEW שיש בפרימנט
        final int childViewCount = rootView.getChildCount();
        final ListView listView = view.findViewById(R.id.ListViewFinal);

        for (int i = 0; i < childViewCount; i++) {
            View workWithMe = rootView.getChildAt(i);
            // chack what is Spinner
            if (workWithMe instanceof TextView) {
                TextView textViewCheack = (TextView) workWithMe;
                final String IDname = getResources().getResourceEntryName(textViewCheack.getId());
                int IDnumber = textViewCheack.getId();


                final TextView clickTextView = (TextView) view.findViewById(IDnumber);
                clickTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),"בחר עובד",Toast.LENGTH_LONG).show();
                        Log.d(TAG, "IDname: "+IDname);
                        Emp_name_by_Shiff.clear();

                        docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {
                                for (final DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    // check if is Employee
                                    if (documentSnapshot.get("isMang").toString().equals("false")) {
                                        // Run again on DB
                                        db.collection("User").document("" + documentSnapshot.getId())
                                                .collection("UserCompany").document("Shifts_week").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot document = task.getResult();

                                                if (document.get(Current_Week) != null) {

                                                    ArrayList Shifts_Arry = (ArrayList) document.get(Current_Week);
                                                    if (Shifts_Arry.contains(IDname))
                                                    {
                                                        Emp_name_by_Shiff.add(documentSnapshot.get("name").toString());
                                                    }
                                                    ListView lV = (ListView) view.findViewById(R.id.ListViewFinal);
                                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Emp_name_by_Shiff);
                                                    listView.setAdapter(adapter);


                                                };

                                            }
                                        });

                                    }
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

//                        final ArrayList<String> Emp_name = new ArrayList<>();  // ArrayList of employee
//                        final CollectionReference docRef = db.collection("User"); // db path
//
//                        docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                            @Override
//                            public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {
//
//                                for (final DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                                    // check if is Employee
//                                    if (documentSnapshot.get("isMang").toString().equals("false")) {
//                                        // Enter the name of employee to ArrayList
//                                        Emp_name_byShiff.add(documentSnapshot.get("name").toString());
//                                    }
//                                }
//                                // Display the name in the ListView
//                                ListView lV = (ListView) view.findViewById(R.id.ListViewFinal);
//                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Emp_name);
//                                lV.setAdapter(adapter);
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                            }
//                        });




                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                Log.d(TAG, "listView.getItemAtPosition: " + listView.getItemAtPosition(position));
                                clickTextView.setText(listView.getItemAtPosition(position).toString());

                            }
                        });


                    }

                });

            }
        }
    }



    private View spinner_test(final View v) {

        final ViewGroup rootView = (ViewGroup) v.findViewById(R.id.fragment_ma_final).getRootView();  // מקבל את כל VIEW שיש בפרימנט

        // DB get info
        docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {

                for (final DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // check if is Employee
                    if (documentSnapshot.get("isMang").toString().equals("false")) {
                        // Run again on DB
//                        Log.d(TAG, "documentSnapshot.get(\"name\"): " + documentSnapshot.get("name"));

                        db.collection("User").document("" + documentSnapshot.getId())
                                .collection("UserCompany").document("Shifts_week").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {

                                    DocumentSnapshot document = task.getResult();
                                    if (document.get(Current_Week) != null) {

                                        ArrayList Shifts_Arry = (ArrayList) document.get(Current_Week);

                                        // Insert the Emp name in the TextView
                                        for (Object e : Shifts_Arry) {
                                            int id = chackID(rootView, e.toString());
                                            String name = documentSnapshot.get("name").toString();
                                            TextView textView = (TextView) v.findViewById(id);
                                            textView.setText(name);

                                        }
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                                NameArray.clear();

                                Array_Shifts.clear();
                            }

                        });

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        return v;
    }


    public int chackID(ViewGroup rootView, String name) {
        final int childViewCount = rootView.getChildCount();
        //Spinner spinnerCheack = (Spinner) v.findViewById();
        // give all ids
        for (int i = 0; i < childViewCount; i++) {
            View workWithMe = rootView.getChildAt(i);
            // chack what is Spinner
            if (workWithMe instanceof TextView) {
                TextView textViewCheack = (TextView) workWithMe;
//                textViewCheack.setText("לא נבחר");

                String IDname = getResources().getResourceEntryName(textViewCheack.getId());

                int IDnumber = textViewCheack.getId();

                if (name.equals(IDname)) {
//                    Log.d(TAG, "chackID  " + "IDname: " + IDname + " name: " + name);
                    return IDnumber;
                }

            }
        }
        return 0;
    }

    // Fiil the ListView in all Emp & MA name
    private void List_fiil(final View returnView) {

//        final ArrayList<String> Emp_name = new ArrayList<>();  // ArrayList of employee
        final CollectionReference docRef = db.collection("User"); // db path

        docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {

                for (final DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // check if is Employee
                    if (documentSnapshot.get("isMang").toString().equals("false")) {
                        // Enter the name of employee to ArrayList
                        Emp_name.add(documentSnapshot.get("name").toString());
                    }
                }
                // Display the name in the ListView
                ListView lV = (ListView) returnView.findViewById(R.id.ListViewFinal);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Emp_name);
                lV.setAdapter(adapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    /**
     * ----------------------------------^^^--------------------------onCreateView----------------------------------------^^^----------------------------------
     */

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.MA_Final_FIListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MA_Final_FIListener) {
            mListener = (MA_Final_FIListener) context;
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
    public interface MA_Final_FIListener {
        // TODO: Update argument type and name
        void MA_Final_FIListener(Uri uri);
    }
}