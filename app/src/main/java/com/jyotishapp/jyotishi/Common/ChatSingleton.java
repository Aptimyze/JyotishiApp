package com.jyotishapp.jyotishi.Common;

import com.jyotishapp.jyotishi.Messages;

import java.util.ArrayList;
import java.util.List;

public class ChatSingleton {
    public static List<Messages> mList = new ArrayList<Messages>();

    public List<Messages> getList(){
        return mList;
    }

    public void add(Messages messages){
        mList.add(messages);
    }

    public void clear(){
        mList.clear();
    }
}
