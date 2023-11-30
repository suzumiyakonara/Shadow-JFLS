package moe.konara.shadow.sql;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataForm implements Iterable<Map<String,String>> {
    public Map<Integer,Map<String,String>> Data;

    public void insertRow(Map<String,String> row){
        this.Data.put(this.Data.size(),row);
    }
    public Map<String,String> getRow(int row){
        return this.Data.get(row);
    }
    @Override
    public String toString() {
        return this.Data.toString();
    }
    public DataForm(){
        this.Data = new HashMap<>();
    }
    public int size(){
        return this.Data.size();
    }
    public boolean isEmpty(){
        return size()==0;
    }


    @NotNull
    @Override
    public Iterator<Map<String,String>> iterator() {
        return Data.values().iterator();
    }
}
