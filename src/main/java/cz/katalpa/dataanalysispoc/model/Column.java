package cz.katalpa.dataanalysispoc.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author jaroslav.repik
 */

public class Column implements Serializable {

    static final long serialVersionUID = 1L;
    private String name;
    private Set<String> values;
    private boolean selected;
    private long recordsTotal;
    private long recordsUnique;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsUnique() {
        return recordsUnique;
    }

    public void setRecordsUnique(long recordsUnique) {
        this.recordsUnique = recordsUnique;
    }

    public Set<String> getValues() {
        return values;
    }
    public void setValues(Set<String> values) {
        this.values = values;
    }
}
