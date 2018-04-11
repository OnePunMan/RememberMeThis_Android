package onepunman.remembermethis;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Definition implements Serializable {
    final static String TAG = "Debug";
    public static int[] TIME_INTERVALS = {2, 4, 4, 8, 12, 12, 24, 48, 48, 120, 840};
    public static String NOT_TESTED = "Not Tested Yet";
    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(TIME_FORMAT);
    public static int SECONDS_PER_HOUR = 3600;
    public static int HOURS_PER_DAY = 24;
    public static double MIN_PERCENT = 0.75;
    public static int NUM_FIELDS = Section.values().length;

    public static enum Section {
        NAME,
        DESCRIPTION,
        LEVEL,
        TIME_CREATED,
        LAST_TESTED,
        TIMES_TESTED,
        TIMES_CORRECT,
        STAGE,
        STREAK,
        IGNORE,
        DIFFICULT
    }

    private String _name;
    private String _description;
    private int _level;
    private Date _timeCreated;
    private Date _lastTested;
    private int _timesTested;
    private int _timesCorrect;
    private int _streak;
    private int _stage;
    private boolean _ignore;
    private boolean _difficult;

    public Definition(String name , String description, int level, String timeCreated, String lastTested, int timesTested, int timesCorrect, int streak, int stage, boolean ignore, boolean difficult){
        this._name = name;
        this._description = description;
        this._level = level;
        this._timeCreated = parseDate(timeCreated);
        this._lastTested = parseDate(lastTested);
        this._timesTested = timesTested;
        this._timesCorrect = timesCorrect;
        this._streak = streak;
        this._stage = stage;
        this._ignore = ignore;
        this._difficult = difficult;
    }

    public Definition(String name, String description, int level) {
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

    private Definition(String name , String description, int level, Date timeCreated, Date lastTested, int timesTested, int timesCorrect, int streak, int stage, boolean ignore, boolean difficult){
        this._name = name;
        this._description = description;
        this._level = level;
        this._timeCreated = timeCreated;
        this._lastTested = lastTested;
        this._timesTested = timesTested;
        this._timesCorrect = timesCorrect;
        this._streak = streak;
        this._stage = stage;
        this._ignore = ignore;
        this._difficult = difficult;
    }

    public String getName () {
        return _name;
    }

    public String getDescription () {
        return _description;
    }


    public static Date parseDate(String stringDate) {
        try {
            Date res = DATE_FORMATTER.parse(stringDate);
            Log.d(TAG, DATE_FORMATTER.format(res));
            return res;
        } catch (ParseException e) {
            Log.e(TAG, "Parsing Failed!");
            return null;
        }
    }

    public String formatNextReviewTime (){
        return "Not Now";
    }

    public boolean isReviewtime(){
        if (_timeCreated == null) return false;

        return nextReviewTime() <= 0;
    }

    public int nextReviewTime(){
        double previousTime;
        double now;

        return 0;
    }

    public double correctPercent(){
        if (_timesTested <= 0) return 0;
        return (double) _timesCorrect / _timesTested;
    }

    public void setBeginning(){
        _timeCreated = new Date();
    }

    public void updateTime(){
        _lastTested = new Date();
    }

    public void updateReviewed(boolean isCorrect, boolean advanceStage){
        updateTime();
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
        Formatter formatter = new Formatter();
        try {
            String s = formatter.format(
                    "Name: %1$2s\n" +
                            "Description: %2$2s\n" +
                            "Level: %3$2d\n" +
                            "Created: %4$2s\n" +
                            "Last Tested: %5$2s\n" +
                            "Times Tested: %6$2d\n" +
                            "Times Correct: %7$2d\n" +
                            "Streak: %8$2d\n" +
                            "SR Stage: %9$2d\n" +
                            "Accuracy: %10$.1f%%\n" +
                            "Next Review: %11$2s\n" +
                            "Ignore?: %12$2s\n" +
                            "Difficult: %13$2s\n",
                    _name,
                    _description,
                    _level,
                    DATE_FORMATTER.format(_timeCreated),
                    DATE_FORMATTER.format(_lastTested),
                    _timesTested,
                    _timesCorrect,
                    _streak,
                    _stage,
                    correctPercent() * 100,
                    formatNextReviewTime(),
                    (_ignore ? "Yes" : "No"),
                    (_difficult ? "Yes" : "No")
            ).toString();
            Log.e(TAG, (s == null ? "NULL" : s));

            return s;
        } catch (Exception e) {
            Log.e(TAG, "You screwed up: ", e);
            return null;
        }
    }
}
