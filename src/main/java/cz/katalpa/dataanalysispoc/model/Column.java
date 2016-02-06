package cz.katalpa.dataanalysispoc.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author jaroslav.repik
 */

public class Column implements Serializable {

    static final long serialVersionUID = 1L;
    private String name;
    private boolean selected;

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
}
