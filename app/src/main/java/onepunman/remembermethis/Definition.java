package onepunman.remembermethis;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;

public class Definition implements Serializable {
    final static String TAG = "Debug";
    public final static String NOT_TESTED = "Not Tested Yet";
    public final static String EMPTY_PLACEHOLDER = "<No Description>";
    public final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(TIME_FORMAT);
    public final static int SECONDS_PER_HOUR = 3600;
    public final static int HOURS_PER_DAY = 24;
    public final static double MIN_PERCENT = 0.75;
    public final static int[] TIME_INTERVALS = {2, 4, 4, 8, 12, 12, 24, 
            2 * HOURS_PER_DAY,
            2 * HOURS_PER_DAY,
            5 * HOURS_PER_DAY,
            10 * HOURS_PER_DAY,
            20 * HOURS_PER_DAY,
            30 * HOURS_PER_DAY
    };
    public final static int NUM_FIELDS = Section.values().length;

    public enum Section {
        NAME,
        DESCRIPTION,
        LEVEL,
        TIME_CREATED,
        LAST_TESTED,
        TIMES_TESTED,
        TIMES_CORRECT,
        STREAK,
        STAGE,
        IGNORE,
        DIFFICULT
    }

    private String _name;
    private String _description;
    private String _level;
    private Date _timeCreated;
    private Date _lastTested;
    private int _timesTested;
    private int _timesCorrect;
    private int _streak;
    private int _stage;
    private boolean _ignore;
    private boolean _difficult;

    public Definition(String name , String description, String level, String timeCreated, String lastTested, int timesTested, int timesCorrect, int streak, int stage, boolean ignore, boolean difficult){
        this._name = (name == null || name.trim().isEmpty()) ? null : name.trim();
        this._description = (description == null || description.trim().isEmpty()) ? EMPTY_PLACEHOLDER : description.trim();
        this._level = (level == null || level.trim().isEmpty()) ? null : level.trim();
        this._timeCreated = parseDate(timeCreated != null ? timeCreated.trim() : null);
        this._lastTested = parseDate(lastTested != null ? lastTested.trim() : null);
        this._timesTested = timesTested;
        this._timesCorrect = timesCorrect;
        this._streak = streak;
        this._stage = stage;
        this._ignore = ignore;
        this._difficult = difficult;
    }

    public Definition(String name, String description, String level) {
        this(
                name,
                description,
                level,
                new Date(),
                null,
                0,
                0,
                0,
                0,
                false,
                false
        );
    }

    private Definition(String name , String description, String level, Date timeCreated, Date lastTested, int timesTested, int timesCorrect, int streak, int stage, boolean ignore, boolean difficult){
        this._name = (name == null || name.trim().isEmpty()) ? null : name.trim();
        this._description = (description == null || description.trim().isEmpty()) ? EMPTY_PLACEHOLDER : description.trim();
        this._level = (level == null || level.trim().isEmpty()) ? null : level.trim();
        this._timeCreated = timeCreated;
        this._lastTested = lastTested;
        this._timesTested = timesTested;
        this._timesCorrect = timesCorrect;
        this._streak = streak;
        this._stage = stage;
        this._ignore = ignore;
        this._difficult = difficult;
    }

    // Getters
    public String getName () { return _name; }
    public String getDescription () { return _description; }
    public String getLevel() { return _level; }
    public String getTimeCreated () { return _timeCreated == null ? "None" : DATE_FORMATTER.format(_timeCreated); }
    public String getLastTested() { return _lastTested == null ? NOT_TESTED : DATE_FORMATTER.format(_lastTested); }
    public int getTimesTested() { return _timesTested; }
    public int getTimesCorrect() { return _timesCorrect; }
    public int getStreak() { return _streak; }
    public int getStage() { return _stage; }
    public boolean isIgnore() { return _ignore; }
    public boolean isDifficult() { return _difficult; }

    // Setters
    public boolean setName (String newName) {
        if (newName == null || newName.length() <= 0) return false;
        _name = newName;
        return true;
    }

    public boolean setDescription (String newDesc) {
        if (newDesc == null || newDesc.length() <= 0) {
            _description = EMPTY_PLACEHOLDER;
            return false;
        }
        _description = newDesc;
        return true;
    }

    public boolean setLevel (String newLevel) {
        if (newLevel == null || newLevel.length() <= 0) {
            _level = null;
            return false;
        }
        _level = newLevel;
        return true;
    }

    public boolean toggleIgnore() {
        _ignore = !_ignore;
        return _ignore;
    }

    public boolean toggleDifficult() {
        _difficult = !_difficult;
        return _difficult;
    }


    public static Date parseDate(String stringDate) {
        if (stringDate == null || stringDate.trim().equals(NOT_TESTED)) return null;
        try {
            return DATE_FORMATTER.parse(stringDate);
        } catch (ParseException e) {
            Log.e(TAG, "Parsing Failed!", e);
            return null;
        }
    }

    public String formatNextReviewTime (){
        double hours = (double)nextReviewTime() / Definition.SECONDS_PER_HOUR;

        if (hours < Definition.HOURS_PER_DAY) {
            //long roundedHours = max(round(hours * 10.0) / 10, 0);
            return String.format("%1$.1f %2$2s", max(hours, 0), hours == 1 ? "hour" : "hours");
        }

        long days = 0;
        while (hours - Definition.HOURS_PER_DAY >= 0) {
            days += 1;
            hours -= Definition.HOURS_PER_DAY;
        }
        long roundedHours = round(hours * 10.0) / 10;
        return String.format("%1$2d %2$2s %3$2d %4$2s", days, (days == 1 ? "day" : "days"), roundedHours, (roundedHours == 1 ? "hour" : "hours"));
    }

    public boolean isReviewTime(){
        if (_timeCreated == null){
            Log.e(TAG, "Time Created should not be null! Check the save data.");
            return false;
        }
        return nextReviewTime() <= 0;
    }

    public long nextReviewTime(){
        Date previousTime = _lastTested == null ? _timeCreated : _lastTested;
        Date now = new Date();

        long timeDiff = (now.getTime() - previousTime.getTime()) / 1000;
        return Definition.TIME_INTERVALS[_stage] * Definition.SECONDS_PER_HOUR - timeDiff;
    }

    public double correctPercent(){
        if (_timesTested <= 0) return 0;
        return (double) _timesCorrect / _timesTested;
    }

    public void reset() {
        setBeginning();
        _lastTested = null;
        _timesTested = 0;
        _timesCorrect = 0;
        _streak = 0;
        _stage = 0;
        _ignore = false;
        _difficult = false;
    }

    private void setBeginning(){
        _timeCreated = new Date();
    }

    public void updateTime(){
        _lastTested = new Date();
    }

    public void updateReviewed(boolean isCorrect, boolean advanceStage){
        updateTime();
        _timesTested += 1;
        if (isCorrect) {
            _timesCorrect += 1;
            if (advanceStage) {
                nextStage();
            }
            _streak += 1;
        } else {
            _streak = 0;
            previousStage();
        }
    }

    public void nextStage(){
        _stage = max(min(_stage + 1, Definition.TIME_INTERVALS.length - 1), 0);
    }

    public void previousStage(){
        _stage = max(min(_stage - 1, Definition.TIME_INTERVALS.length - 1), 0);
    }

    public String toString() {
        try {
            String s = String.format(
                    "Name: %1$2s\n" +
                            "Description: %2$2s\n" +
                            "Level: %3$2s\n" +
                            "Created: %4$2s\n" +
                            "Last Tested: %5$2s\n" +
                            "Times Tested: %6$2d\n" +
                            "Times Correct: %7$2d\n" +
                            "Streak: %8$2d\n" +
                            "SR Stage: %9$2d\n" +
                            "Accuracy: %10$.1f%%\n" +
                            "Next Review: %11$2s\n" +
                            "Ignore: %12$2s\n" +
                            "Difficult: %13$2s\n",
                    _name,
                    _description,
                    _level == null ? "-" : _level,
                    getTimeCreated(),
                    getLastTested(),
                    _timesTested,
                    _timesCorrect,
                    _streak,
                    _stage,
                    correctPercent() * 100,
                    formatNextReviewTime(),
                    (_ignore ? "Yes" : "No"),
                    (_difficult ? "Yes" : "No")
            );

            return s;
        } catch (Exception e) {
            Log.e(TAG, "You screwed up: ", e);
            return null;
        }
    }
}
