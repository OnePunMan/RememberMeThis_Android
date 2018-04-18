package onepunman.remembermethis;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Course {
    final String TAG = "Debug";
    public final static String DELIMITER = "|";
    public final static String EMPTY_COURSE_DESC_PLACEHOLDER = "<No Description>";

    private String _id;
    private String _name;
    private String _description;
    private File _courseFile;
    private ArrayList<Definition> _definitions;
    private ArrayList<Definition> _difficultDefinitions;

    public Course() {
        this._id = null;
        this._name = null;
        this._description = null;
        this._courseFile = null;
        this._definitions = new ArrayList<Definition>();
        this._difficultDefinitions = new ArrayList<Definition>();
    }

    // Getters
    public String getName() {
        return _name;
    }
    public String getDescription() { return _description; }

    // Setters
    public boolean setName(String newName) {
        if (newName == null || newName.trim().isEmpty()) return false;
        _name = newName;
        return true;
    }

    public boolean setDescription(String newDesc) {
        if (newDesc == null || newDesc.trim().isEmpty()) {
            _description = EMPTY_COURSE_DESC_PLACEHOLDER;
            return false;
        }
        _description = newDesc;
        return true;
    }

    public boolean createNew(String name, String description){
        if (name == null || name.trim().length() <= 0) return false;
        _name = name;
        _description = (description == null|| description.length() <= 0) ? EMPTY_COURSE_DESC_PLACEHOLDER : description;
        return true;
    }

    public void loadFromFile(String filePath) {
        return;
    }

    public void loadFromFile(File courseFile) {
        _courseFile = courseFile;
        FileInputStream fStream = null;
        try {
            fStream = new FileInputStream(_courseFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fStream));

            int lineCount = 1;
            String line = "Empty";
            String currentDelimiter = DELIMITER;
            while (line != null) {
                line = reader.readLine();

                if (line == null) break;
                if (line.trim().length() < 1) continue;

                if (lineCount == 1) {
                    _name = line;
                }
                else if (lineCount == 2) {
                    _description = line;
                }
                else if (lineCount == 3) {
                    currentDelimiter = line.trim();
                }
                else {
                    String [] sections = line.split(Pattern.quote(currentDelimiter != null ? currentDelimiter : DELIMITER));
                    if (sections.length == Definition.NUM_FIELDS) {
                        _definitions.add(
                                new Definition(
                                        sections[Definition.Section.NAME.ordinal()],
                                        sections[Definition.Section.DESCRIPTION.ordinal()],
                                        sections[Definition.Section.LEVEL.ordinal()],
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
                        Log.e(TAG, "Sections length: " + sections.length);
                        for (int i = 0; i < sections.length; i++) {
                            Log.e(TAG, i + sections[i]);
                        }
                        continue;
                    }
                }
                lineCount += 1;
            }
            reader.close();

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

    public ArrayList<Definition> getAll(String level) {
        return _definitions;
    }

    public ArrayList<Definition> getAll() {
        return getAll(null);
    }

    public int defCount() {
        return (_definitions == null ? 0 : _definitions.size());
    }

    public String toString() {
        return null;
    }

    public void addDefinition(String name, String description, String level) {
        Definition newDef = new Definition(name, description, level);
        _definitions.add(newDef);
    }

    public void addDefinition(Definition newDef) {
        _definitions.add(newDef);
    }

    public boolean removeDefinition(Definition def) {
        return _definitions.remove(def);
    }

    public boolean save() {
        if (_name == null || _name.trim().length() <= 0) {
            Log.e(TAG, "Unable to save, course is in an invalid state.");
            return false;
        }

        if (_courseFile == null) {
            // write to new file
            _courseFile = FileIO.writeToFile(_name, _name + "\n" + _description + "\n" + DELIMITER);

            if (_courseFile != null) {
                return true;
            }
            else {
                Log.e(TAG, "Failed to create new file!");
                return false;
            }
        }
        else {
            // Write to existing file
            try {
                FileWriter fileWriter = new FileWriter(_courseFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write(_name + "\n");
                bufferedWriter.write(_description + "\n");
                bufferedWriter.write(DELIMITER + "\n");
                bufferedWriter.write("\n");

                String line;
                for (Definition def : _definitions) {
                    line = def.getName() + DELIMITER +
                            def.getDescription() + DELIMITER +
                            def.getLevel() + DELIMITER +
                            def.getTimeCreated() + DELIMITER +
                            def.getLastTested() + DELIMITER +
                            def.getTimesTested() + DELIMITER +
                            def.getTimesCorrect() + DELIMITER +
                            def.getStreak() + DELIMITER +
                            def.getStage() + DELIMITER +
                            def.isIgnore() + DELIMITER +
                            def.isDifficult() + "\n";
                    bufferedWriter.write(line);
                }
                bufferedWriter.close();
                return true;
            }
            catch (Exception e) {
                Log.e(TAG, "Saving Error: ", e);
                return false;
            }
        }
    }

    public void reset() {
        return;
    }

    // This is ReviewRecommended
    public ArrayList<Definition> getReviewList() {
        return getReviewList(null);
    }

    public ArrayList<Definition> getReviewList(String level) {
        ArrayList<Definition> reviewList = new ArrayList<>();
        for (Definition def : _definitions) {
            if ((def.isReviewTime() || def.isLowCorrectRate()) && !def.isIgnore()) {
                reviewList.add(def);
            }
        }
        return reviewList;
    }

    public ArrayList<Definition> getDifficultDefinitions() {
        return _difficultDefinitions;
    }
}