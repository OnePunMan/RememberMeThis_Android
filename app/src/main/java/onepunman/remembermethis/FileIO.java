package onepunman.remembermethis;import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class FileIO {
    final static String appDirectory = File.separator + "RememberMeThis";
    final static String courseDirectory =  File.separator + "Courses";
    final static String basePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    final static String COURSES_PATH = basePath + appDirectory  + courseDirectory;
    final static String COURSE_EXTENSION = ".course";
    final static String TAG = FileIO.class.getName();

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
            Log.d(TAG, "Path: " + dir);
            File directory = new File(dir);
            File[] files = directory.listFiles();
            Log.d(TAG, "Size: "+ files.length);
            for (int i = 0; i < files.length; i++)
            {
                Log.d(TAG, "FileName:" + files[i].getName());
            }
            return files;
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
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
            /*
            bufferedWriter.write(data + "\n");
            bufferedWriter.write("Hello there,");
            bufferedWriter.write(" here is some text.");
            bufferedWriter.newLine();
            bufferedWriter.write("We are writing");
            bufferedWriter.write(" the text to the file.");
            */

            bufferedWriter.close();
            return resFile;
        }
        catch(IOException e) {
           Log.e(TAG, "Error writing to file '" + fileName + "'");
           return null;
        }
        catch (Exception e) {
            Log.e(TAG, "Other Error: " + e.getMessage());
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
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }

        return ret;
    }
}