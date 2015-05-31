package com.myname.delta;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ListActivity {




    String[] itemname = {
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War"
    };
    Integer[] imageId = {
            R.drawable.file,
            R.drawable.file,
            R.drawable.file,
            R.drawable.file,
            R.drawable.file,
            R.drawable.file,
            R.drawable.file

    };

    private Button mAscButton;
    private Button mDescButton;
    private ListView mNameListView;

    private List<String> stringList;
    private StringAdapter stringAdapter;
    ArrayList<HashMap<String, Object>> searchResults;

    //ArrayList that will hold the original Data
    ArrayList<HashMap<String, Object>> originalValues;
    LayoutInflater inflater;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setListAdapter(new ArrayAdapter<String>(
                this, R.layout.layout,
                R.id.Itemname, itemname));


        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort_list_item);

        mAscButton = (Button) findViewById(R.id.asc_button);
        mDescButton = (Button) findViewById(R.id.desc_button);
        mNameListView = (ListView) findViewById(R.id.name_list);


        stringAdapter = new StringAdapter(MainActivity.this, R.layout.sort_list_item, stringList);
        mNameListView.setAdapter(stringAdapter);

        mAscButton.setOnClickListener((OnClickListener) this);
        mDescButton.setOnClickListener((OnClickListener) this);
        final EditText searchBox=(EditText) findViewById(R.id.editText);
        ListView playerListView=(ListView) findViewById(android.R.id.list);

        //get the LayoutInflater for inflating the customomView
        //this will be used in the custom adapter
        inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //these arrays are just the data that
        //I'll be using to populate the ArrayList
        //You can use our own methods to get the data


        originalValues=new ArrayList<HashMap<String,Object>>();

        //temporary HashMap for populating the
        //Items in the ListView
        HashMap<String , Object> temp;

        //total number of rows in the ListView
        int noOfPlayers=itemname.length;

        //now populate the ArrayList players
        for(int i=0;i<noOfPlayers;i++)
        {
            temp=new HashMap<String, Object>();

            temp.put("name", itemname[i]);


            //add the row to the ArrayList
            originalValues.add(temp);
        }
        //searchResults=OriginalValues initially
        searchResults=new ArrayList<HashMap<String,Object>>(originalValues);

        //create the adapter
        //first param-the context
        //second param-the id of the layout file
        //you will be using to fill a row
        //third param-the set of values that
        //will populate the ListView
        final CustomAdapter adapter=new CustomAdapter(this, R.layout.layout,searchResults);

        //finally,set the adapter to the default ListView


        searchBox.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //get the text in the EditText
                String searchString=searchBox.getText().toString();
                int textLength=searchString.length();
                searchResults.clear();

                for(int i=0;i<originalValues.size();i++)
                {
                    String playerName=originalValues.get(i).get("name").toString();
                    if(textLength<=playerName.length()){
                        //compare the String in EditText with Names in the ArrayList
                        if(searchString.equalsIgnoreCase(playerName.substring(0,textLength)))
                            searchResults.add(originalValues.get(i));
                    }
                }

                adapter.notifyDataSetChanged();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            public void afterTextChanged(Editable s) {


            }
        });


    }

    // Comparator for Ascending Order
    public static Comparator<String> StringAscComparator = new Comparator<String>() {

        public int compare(String app1, String app2) {

            String stringName1 = app1;
            String stringName2 = app2;

            return stringName1.compareToIgnoreCase(stringName2);
        }
    };

    //Comparator for Descending Order
    public static Comparator<String> StringDescComparator = new Comparator<String>() {

        public int compare(String app1, String app2) {

            String stringName1 = app1;
            String stringName2 = app2;

            return stringName2.compareToIgnoreCase(stringName1);
        }
    };

    // Your Own Custom Adapter
    private class StringAdapter extends ArrayAdapter<String> {
        // Attributes
        private List<String> strModel;

        public StringAdapter(Context context, int textViewResourceId,
                             List<String> strModel) {
            super(context, textViewResourceId, strModel);
            this.strModel = strModel;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder = null;

            if (view == null) {
                view = View.inflate(MainActivity.this,
                        R.layout.sort_list_item, null);

                holder = new Holder();
                holder.StringNameTextView = (TextView) view
                        .findViewById(R.id.name_text_view);

                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            String nameText = strModel.get(position);
            holder.StringNameTextView.setText(nameText);
            return view;
        }
    }

    static class Holder {
        private TextView StringNameTextView;
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.asc_button:
                Collections.sort(stringList, StringAscComparator);
                Toast.makeText(MainActivity.this, "Sorting in Ascending Order", Toast.LENGTH_LONG).show();
                break;
            case R.id.desc_button:
                Collections.sort(stringList, StringDescComparator);
                Toast.makeText(MainActivity.this, "Sorting in Descending Order", Toast.LENGTH_LONG).show();
                break;
        }

        stringAdapter.notifyDataSetChanged();
    }

    private class CustomAdapter extends ArrayAdapter<HashMap<String, Object>> {

        public CustomAdapter(Context context, int textViewResourceId,
                             ArrayList<HashMap<String, Object>> Strings) {

            //let android do the initializing :)
            super(context, textViewResourceId, Strings);
        }


        //class for caching the views in a row
        private class ViewHolder {

            TextView itemname;

        }

        ViewHolder viewHolder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout, null);
                viewHolder = new ViewHolder();

                //cache the views

                viewHolder.itemname = (TextView) convertView.findViewById(R.id.Itemname);


                //link the cached views to the convertview
                convertView.setTag(viewHolder);

            } else
                viewHolder = (ViewHolder) convertView.getTag();


            int photoId = (Integer) searchResults.get(position).get("photo");

            //set the data to be displayed

            viewHolder.itemname.setText(searchResults.get(position).toString());


            //return the view to be displayed



            ListView listViewFriends = (ListView) findViewById(R.id.name_list);
            listViewFriends.setEmptyView(findViewById(R.id.empty));

            return convertView;
        }   }


}

