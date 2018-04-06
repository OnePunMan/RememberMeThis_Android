package onepunman.remembermethis;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

public class Course {
    public static String DELIMITER = "|";

    private String _id;
    private String _name;
    private String _description;
    private String _filePath;
    private ArrayList<Definition> _definitions;
    private ArrayList<Definition> _difficultDefinitions;

    public Course(){
        this._id = null;
        this._name = null;
        this._description = null;
        this._filePath = null;
        this._definitions = new ArrayList<Definition>();
        this._difficultDefinitions = new ArrayList<Definition>();
    }

    public void createNew(String name, String description, String filePath){
        _name = name;
        _description = description;
        _filePath = filePath;
    }

    public void loadFromFile(String filePath){
        return;
    }


    public ArrayList<Definition> getAll(int level){
        return null;
    }

    public ArrayList<Definition> getAll(){
        return getAll(-1);
    }

    public String toString(){
        return null;
    }

    public void addDefinition(String name, String description, int level){
        return;
    }

    public void addDefinition(Definition newDef){
        return;
    }

    public void save(){
        return;
    }

    public void reset(){
        return;
    }

    // This is ReviewRecommended
    public ArrayList<Definition> getReviewList() {
        return _definitions;
    }

    public ArrayList<Definition> getReviewList(int level) {
        return _definitions;
    }

    public ArrayList<Definition> getDifficultDefinitions() {
        return _difficultDefinitions;
    }
}
