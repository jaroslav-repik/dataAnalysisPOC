package cz.katalpa.dataanalysispoc.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author jaroslav.repik
 */
public class Table implements Serializable{

    static final long serialVersionUID = 1L;
    private String name;
    private List<Column> columns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
