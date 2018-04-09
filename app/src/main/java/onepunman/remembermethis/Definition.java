package onepunman.remembermethis;

public class Definition {

    public static int[] TIME_INTERVALS = {2, 4, 4, 8, 12, 12, 24, 48, 48, 120, 840};
    public static String NOT_TESTED = "Not Tested Yet";
    public static String TIME_FORMAT = "%Y-%m-%d %H:%M:%S";
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
    private String _timeCreated; // Change to Date later
    private String _lastTested; // Change to Date later
    private int _streak;
    private int _stage;
    private boolean ignore;
    private boolean difficult;

    public Definition(String name , String description, int level, String timeCreated, String lastTested, int streak, int stage, boolean ignore, boolean difficult){
        this._name = name;
        this._description = description;
        this._level = level;
        this._timeCreated = timeCreated;
        this._lastTested = lastTested;
        this._streak = streak;
        this._stage = stage;
        this.ignore = ignore;
        this.difficult = difficult;
    }

    public String formatTime(){
        return null;
    }

    public boolean isReviewtime(){
        if (_timeCreated == null) return false;

        return nextReviewtime() > 0;
    }

    public int nextReviewtime(){
        double previousTime;
        double now;

        return 0;
    }

    public double correctPercent(){
        return 0;
    }

    public void setBeginning(){
        return;
    }

    public void updateTime(){
        return;
    }

    public void updateReviewed(boolean isCorrect, boolean advanceStage, boolean difficult){
        return;
    }

    public void nextStage(){
        return;
    }

    public void previousStage(){
        return;
    }
}
