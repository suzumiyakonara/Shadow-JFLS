package moe.konara.shadow.sql;

public class ColumnValue {
    private final String Column;
    private final Object Value;
    public ColumnValue(String column,Object value){
        this.Column = column;
        this.Value = value;
    }

    public String getColumn() {
        return Column;
    }

    public Object getValue() {
        return Value;
    }
}
