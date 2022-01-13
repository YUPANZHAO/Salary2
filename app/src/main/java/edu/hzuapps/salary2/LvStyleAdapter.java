package edu.hzuapps.salary2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class LvStyleAdapter extends BaseAdapter {

    private Context context;
    private List<Style> styleList;

    public LvStyleAdapter(Context context, List<Style> styleList) {
        this.context = context;
        this.styleList = styleList;
    }

    public void setStyleList(List<Style> styleList) {
        this.styleList = styleList;
    }

    @Override
    public int getCount() {
        return styleList.size();
    }

    @Override
    public Object getItem(int position) {
        return styleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.style_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_stylename = convertView.findViewById(R.id.styleitem_tv_sitename);
            viewHolder.tv_price = convertView.findViewById(R.id.styleitem_tv_price);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Style style = styleList.get(position);
        viewHolder.tv_stylename.setText("款式：" + String.valueOf(style.getStylename()));
        viewHolder.tv_price.setText("单价：" + style.getPrice().stripTrailingZeros().toPlainString());
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_stylename, tv_price;
    }

}
