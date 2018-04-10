package onepunman.remembermethis;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
public class Course {
    final String TAG = "Debug";
    public static String DELIMITER = "\\|";
    public static String EMPTY_PLACEHOLDER = "<No Description>";

    private String _id;
    private String _name;
    private String _description;
    private File _courseFile;
    private ArrayList<Definition> _definitions;
    private ArrayList<Definition> _difficultDefinitions;

    public Course(){
        this._id = null;
        this._name = null;
        this._description = null;
        this._courseFile = null;
        this._definitions = new ArrayList<Definition>();
        this._difficultDefinitions = new ArrayList<Definition>();
    }

    public boolean createNew(String name, String description){
        if (name == null || name.trim().length() <= 0) return false;
        _name = name;
        _description = (description == null|| description.length() <= 0) ? EMPTY_PLACEHOLDER : description;
        return true;
    }

    public void loadFromFile(String filePath){
        return;
    }

    public void loadFromFile(File courseFile){
        _courseFile = courseFile;
        FileInputStream fStream = null;
        try {
            fStream = new FileInputStream(_courseFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fStream));

            int lineCount = 1;
            String line = "Empty";
            while (line != null) {
                Log.d(TAG, line);
                line = reader.readLine();

                if (line == null) break;
                if (line.length() < 2) continue;

                if (lineCount == 1) {
                    _name = line;
                }
                else if (lineCount == 2) {
                    _description = line;
                }
                else {
                    String [] sections = line.split(DELIMITER);
                    if (sections.length == Definition.NUM_FIELDS) {
                        _definitions.add(
                                new Definition(
                                        sections[Definition.Section.NAME.ordinal()],
                                        sections[Definition.Section.DESCRIPTION.ordinal()],
                                        Integer.parseInt(sections[Definition.Section.LEVEL.ordinal()]),
                                        sections[Definition.Section.TIME_CREATED.ordinal()],
                                        sections[Definition.Section.LAST_TESTED.ordinal()],
                                        Integer.parseInt(sections[Definition.Section.TIMES_TESTED.ordinal()]),
                                        Integer.parseInt(sections[Definition.Section.TIMES_CORRECT.ordinal()]),
                                        Integer.parseInt(sections[Definition.Section.STREAK.ordinal()]),
                                        Integer.parseInt(sections[Definition.Section.STAGE.ordinal()]),
                                        Boolean.parseBoolean(sections[Definition.Section.IGNORE.ordinal()]),
                                        Boolean.parseBoolean(sections[Definition.Section.DIFFICULT.ordinal()]))
                        );
                    }
                    else {
                        Log.e(TAG, "Bad line: " + lineCount);
                        for (int i = 0; i < sections.length; i++) {
                            Log.e(TAG, i + sections[i]);
                        }
                        continue;
                    }
                }
                lineCount += 1;
            }

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Other Error: ", e);
        }
        finally{
            try {
                if (fStream != null)
                    fStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Definition> getAll(int level){
        return _definitions;
    }

    public ArrayList<Definition> getAll(){
        return getAll(-1);
    }

    public String getName() {
        return _name;
    }

    public String getDescription() {
        return _description;
    }

    public String toString(){
        return null;
    }

    public void addDefinition(String name, String description, int level){
        Definition newDef = new Definition(name, description, level);
        _definitions.add(newDef);
    }

    public void addDefinition(Definition newDef){
        _definitions.add(newDef);
    }

    public boolean save(){
        if (_name == null || _name.trim().length() <= 0) {
            Log.e(TAG, "Unable to save, course is in an invalid state.");
            return false;
        }

        if (_courseFile == null) {
            _courseFile = FileIO.writeToFile(_name, _name + "\n" + _description);
        } else {
            // Write to file
        }
        return true;
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
