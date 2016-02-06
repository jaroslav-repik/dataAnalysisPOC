package cz.katalpa.dataanalysispoc.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author jaroslav.repik
 */

public class Template implements Serializable {

    static final long serialVersionUID = 1L;
    private String name;
    private List<Table> tables;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }
}
