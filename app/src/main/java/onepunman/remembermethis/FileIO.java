package onepunman.remembermethis;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileIO {
    final static String appDirectory = File.separator + "RememberMeThis";
    final static String courseDirectory =  File.separator + "Courses";
    final static String basePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    final static String COURSES_PATH = basePath + appDirectory  + courseDirectory;
    final static String COURSE_EXTENSION = ".course";
    private final static String TAG = "Debug";

    public static void initDirectory () {
        File rootDir = new File(basePath + appDirectory);
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }

        File coursesDir = new File(COURSES_PATH);
        if (!coursesDir.exists()) {
            coursesDir.mkdir();
        }
    }

    public static File [] getFilesInDir(String dir) {
        try{
            return new File(dir).listFiles();
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error reading directory", e);
            return null;
        }
    }


    public static File[] getFilesInSubDir(String subDir) {
        return getFilesInDir(basePath.toString() + File.separator + subDir);
    }

    public static File [] getCourseFiles() {
        return getFilesInDir(COURSES_PATH);
    }

    public static File writeToFile(String fileName, String data) {
        // The name of the file to open.
        String newPath = COURSES_PATH + File.separator + fileName + COURSE_EXTENSION;
        File resFile;

        try {
            // Assume default encoding.
            resFile = new File(newPath);
            FileWriter fileWriter = new FileWriter(resFile);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // write() does not automatically append new line
            if (data != null) {
                bufferedWriter.write(data);
            }

            bufferedWriter.close();
            return resFile;
        }
        catch(IOException e) {
           Log.e(TAG, "Error writing to file '" + fileName + "'", e);
           return null;
        }
        catch (Exception e) {
            Log.e(TAG, "Other Error: ", e);
            return null;
        }
    }

    public static String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(basePath + "test.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: ", e);
        }
        catch (IOException e) {
            Log.e(TAG, "Can not read file: ", e);
        }
        return ret;
    }

    public static boolean isAlreadyExist(String fileName) {
        try {
            String goodName = fileName.contains(COURSE_EXTENSION) ? fileName : fileName + COURSE_EXTENSION;
            File temp = new File(COURSES_PATH + File.separator + goodName);
            return temp.exists();
        }
        catch (Exception e) {
            Log.e(TAG, "Error opening new file: ", e);
            return false;
        }
    }
}