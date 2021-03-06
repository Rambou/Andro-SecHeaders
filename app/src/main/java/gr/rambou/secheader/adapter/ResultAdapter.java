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

package gr.rambou.secheader.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gr.rambou.secheader.R;
import gr.rambou.secheader.Website;

/**
 * Created by Nickos on 23/3/2015.
 */
public class ResultAdapter extends ArrayAdapter<Website> {
    private Context context;
    private int layout;
    private Website data[];

    public ResultAdapter(Context context, int layout, Website[] data) {
        super(context, layout, data);
        this.layout = layout;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SiteHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layout, parent, false);
            holder = new SiteHolder();
            holder.name = (TextView) row.findViewById(R.id.name);
            holder.info = (TextView) row.findViewById(R.id.info);
            row.setTag(holder);
        } else {
            holder = (SiteHolder) row.getTag();
        }

        Website site = data[position];
        holder.name.setText(site.getWebsite());
        holder.info.setText(site.getInfo());
        return row;
    }

    static class SiteHolder {
        TextView name;
        TextView info;
    }
}