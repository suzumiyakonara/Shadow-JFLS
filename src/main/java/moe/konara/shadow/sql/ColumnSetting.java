package moe.konara.shadow.sql;

public class ColumnSetting {
    private String Name;
    private ColumnType Type;
    private boolean Nullable;
    private boolean Iskey;
    public enum ColumnType{
        INT("INT"),
        TINYINT("TINYINT"),
        BIGINT("BIGINT"),
        REAL("REAL"),
        CHAR("CHAR"),
        VARCHAR("VARCHAR"),
        TEXT("TEXT"),
        BLOB("BLOB");
        private final String type;
        private int value = 0;
        ColumnType(String type){
            this.type = type;
        }
        public ColumnType setValue(int value){
            this.value = value;
            return this;
        }
        @Override
        public String toString() {
            return this.type + (value == 0 ? "" : "(" + value + ")");
        }
    }
    private void init(String name, ColumnType type, boolean nullable, boolean iskey){
        this.Name = name;
        this.Type = type;
        this.Nullable = nullable;
        this.Iskey = iskey;
    }
    public ColumnSetting(String name,ColumnType type){
        init(name,type,true,false);
    }
    public ColumnSetting(String name,ColumnType type,boolean nullable){
        init(name,type,nullable,false);
    }
    public ColumnSetting(String name,ColumnType type,boolean nullable,boolean iskey){
        init(name,type,nullable,iskey);
    }
    @Override
    public String toString(){
        return this.Name + " " + this.Type + " " + (this.Iskey ? "PRIMARY KEY " : "") + (this.Nullable ? "" : "NOT NULL");
    }
}
