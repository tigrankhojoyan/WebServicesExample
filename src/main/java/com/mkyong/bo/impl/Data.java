package com.mkyong.bo.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tigrank on 8/24/2015.
 */
public class Data extends Root {

    private String stringData;
    private int intData;
    private Input input;
    private ArrayList<String> arrayList;
    private List<Input> inputList;

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public String getStringData() {
        return stringData;
    }

    public void setStringData(String stringData) {
        this.stringData = stringData;
    }

    public int getIntData() {
        return intData;
    }

    public void setIntData(int intData) {
        this.intData = intData;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    public List<Input> getInputList() {
        return inputList;
    }

    public void setInputList(List<Input> inputList) {
        this.inputList = inputList;
    }
}
