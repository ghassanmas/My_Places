package com.example.jbt.placess_3;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.CallLog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.jbt.placess_3.Adapters.SearhAdapter;
import com.example.jbt.placess_3.Database.DB_Handler;
import com.example.jbt.placess_3.Tasks.SearchByMyPlace;
import com.example.jbt.placess_3.provider.PlaceProvider;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    onClickedjad callback;
    private String table_name="history";
 //  public DB_Handler db_handler=new DB_Handler(getActivity());
    SearhAdapter searhAdapter;
    public  Cursor lastcursor;
    public Spinner spinner;
    public DB_Handler db_handler =new DB_Handler(this.getActivity());
    ArrayAdapter<CharSequence> arrayAdapter;
    ArrayAdapter<String> adapter;

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        Cursor cursor=db_handler.returnAll(table_name);
       // return new CursorLoader(getActivity(),)

        Uri uri= PlaceProvider.CONTENT_URI;
        String [] projection={"_id","name","image","city","lat","lang"};
       return new CursorLoader(this.getActivity(),uri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        searhAdapter.swapCursor(cursor);
        lastcursor=cursor;

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        searhAdapter.swapCursor(null);


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        MainActivity.faviorteState=false;
          callback.kabas(l);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch(i){

            case 0:
                MainActivity.type_select=null;
                break;


            case 1:
                MainActivity.type_select="bar";
                break;
            case 2:
                MainActivity.type_select="restaurant";
                break;
            case 3:
                MainActivity.type_select="hospital";
                break;
            case 4:
                MainActivity.type_select="pharmacy";
                break;
            case 5:
                MainActivity.type_select="police";
                break;
            case 6:
                MainActivity.type_select="park";
                break;
            case 7:
                MainActivity.type_select="health";
                break;








        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        MainActivity.type_select=null;

    }

    public interface onClickedjad{

        public void dooooIt();

        void kabas(long l);

        void searchIT();

        void saveIT(long id);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TABLE_NAME="history";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listView;
    SearchLocationEvent searchLocationEvent;




    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View v= inflater.inflate(R.layout.fragment_list, container, false);
        final Button locationButton= (Button) v.findViewById(R.id.location);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    callback.dooooIt();

            }
        });
    

        Button searchName = (Button) v.findViewById(R.id.search);
        searchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               
                callback.searchIT();
            }
        });
        
         listView= (ListView) v.findViewById(R.id.listView);
        searhAdapter=new SearhAdapter(getActivity(),null,1);

        this.getLoaderManager().initLoader(1, null, this);
        listView.setAdapter(searhAdapter);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);
        spinner = (Spinner) v.findViewById(R.id.spinner);
        String [] arraytext ={"Bar","Resturant"};
        arrayAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.types_list,android.R.layout.simple_spinner_item);
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,arraytext);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
               callback= (onClickedjad) activity;
        }
        catch (ClassCastException e){
            throw  new ClassCastException(activity.toString()+ " must implement doooIt ");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.context,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){

            case R.id.addFav:



                long id=info.id;
                callback.saveIT(id);



                break;
            case R.id.share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                db_handler=new DB_Handler(getActivity());
                Cursor cursor=db_handler.takeRow(info.id);
                cursor.moveToNext();
                String sharebody=cursor.getString(cursor.getColumnIndex("name"));

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Place");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharebody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));


                break;
        }

        return true;
    }
}
