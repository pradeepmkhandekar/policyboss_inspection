package policyboss.com.inspect.core.inspection.entity;

import android.graphics.Bitmap;

/**
 * Created by Rajeev Ranjan on 11/12/2017.
 */

public class FrontRearEntity {

    private Bitmap bitmap;
    private int id;
    private String name;
    private String value;
    private boolean item_selected= false;
    private boolean selected_Name = false;
    private boolean isCompleted= false;
    public boolean isItem_selected() {
        return item_selected;
    }

    public void setItem_selected(boolean item_selected) {
        this.item_selected = item_selected;
    }

    public boolean isSelected_Name() {
        return selected_Name;
    }

    public void setSelected_Name(boolean selected_Name) {
        this.selected_Name = selected_Name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }




    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public FrontRearEntity(String name, int id) {
        this.name = name;
        this.id = id;
        this.value = "Select";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
