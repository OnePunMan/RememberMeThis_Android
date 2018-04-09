package onepunman.remembermethis;import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class FileIO {
    final static String fileName = "data.txt";
    final static String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    final static String TAG = FileIO.class.getName();

    public static File[] getFilesInSubDir(String subDir) {
        try{
            String newPath = path.toString() + File.separator + subDir;
            Log.d("FileIO", "Path: " + newPath);
            File directory = new File(newPath);
            File[] files = directory.listFiles();
            Log.d("FileIO", "Size: "+ files.length);
            for (int i = 0; i < files.length; i++)
            {
                Log.d("FileIO", "FileName:" + files[i].getName());
            }
            return files;
        }
        catch (Exception e)
        {
            Log.e("FileIO", e.getMessage());
            return null;
        }
    }

    public static void writeToFile(String data, Context context) {
       /*
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.e("FileIO", "File write success to " + path);
        } catch (IOException e) {
            Log.e("FileIO", "File write failed: " + e.toString());
        }
        */

       /*
       File f = new File(path + File.separator + fileName);
       f.mkdir();
       Log.e("FileIO", f.getAbsolutePath().toString());
       */



    }

    public static String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(path + "test.txt");

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
            Log.e("FileIO", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("FileIO", "Can not read file: " + e.toString());
        }

        return ret;
    }
}