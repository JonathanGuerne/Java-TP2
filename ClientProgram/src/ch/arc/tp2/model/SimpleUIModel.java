package ch.arc.tp2.model;/*
 * Project Name : javaFXMVC
 * author : jonathan.guerne
 * creation date : 16.03.2017
*/

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SimpleUIModel
{

    private final StringProperty label;

    public SimpleUIModel(){
        this(null);
    }

    public SimpleUIModel(String value)
    {
        this.label = new SimpleStringProperty(value);
    }

    public String getLabel()
    {
        return label.get();
    }

    public StringProperty labelProperty()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label.set(label);
    }
}
