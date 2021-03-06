/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Nikos Bousios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gr.rambou.secheader.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import gr.rambou.secheader.DatabaseHandler;
import gr.rambou.secheader.R;
import gr.rambou.secheader.Website;
import gr.rambou.secheader.adapter.ResultAdapter;

/**
 * Created by Nickos on 23/3/2015.
 */
public class ResultFragment extends Fragment {

    private Website data[];
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_result, container, false);
        loadData();

        return rootView;
    }


    public void loadData() {
        //We open/create our sqlite database
        DatabaseHandler mydb = new DatabaseHandler(getActivity().getApplicationContext());
        //We get the column with websites
        String[] Websites = mydb.getColumnValues(DatabaseHandler.Type.KEY_WEBSITE);

        int i = 0;
        data = new Website[Websites.length];
        for (String w : Websites) {
            HashMap<String, String> res = mydb.getHeader(w);
            Set<Map.Entry<String, String>> keys = res.entrySet();
            String info = "";
            for (Map.Entry<String, String> key : keys) {
                info += key.getKey() + ":" + ((Integer.parseInt(key.getValue()) == 1) ? "Secure" : "Not") + "|";
            }

            data[i++] = new Website(w, info);
        }

        //Result Fragment Initialization
        ResultAdapter adapter = new ResultAdapter(getActivity(), R.layout.custom_list, data);
        ListView list = (ListView) rootView.findViewById(R.id.list);
        list.setAdapter(adapter);

    }
}