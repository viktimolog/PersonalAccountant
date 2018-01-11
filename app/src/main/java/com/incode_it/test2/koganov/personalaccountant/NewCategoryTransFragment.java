package com.incode_it.test2.koganov.personalaccountant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class NewCategoryTransFragment extends Fragment {


    private Controller con;
    private EditText etNewCategory;

    public NewCategoryTransFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_new_category_trans, container, false);

        con = ((MainActivity)getActivity()).getCon();

        etNewCategory = v.findViewById(R.id.etNewCategory);

        v.findViewById(R.id.btnAddNewCategory).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                if(etNewCategory.getText().length()== 0)
                {
                    Toast.makeText(getActivity(), "Input name for new category!", Toast.LENGTH_LONG).show();
                }
                else {

                    if (!con.isCategory(etNewCategory.getText().toString())) {
                        con.getUser().getCategoriesTransaction().add(etNewCategory.getText().toString());
                        con.addNewCategotyTrans();

//                        getFragmentManager().popBackStack();
                        ((MainActivity) getActivity()).installFragment(new NewTransactionFragment(), false);//todo
                    }
                    else {
                        Toast.makeText(getActivity(), "Sorry, this category has already created!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        return v;
    }
}
