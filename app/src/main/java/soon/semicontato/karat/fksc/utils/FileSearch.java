package soon.semicontato.karat.fksc.utils;

import java.io.File;
import java.util.ArrayList;

public class FileSearch {

    /**
     * Returns all the directories that are inside a specific directory.
     * @param directory a specific folder (directory) path
     * @return all the directories' paths that are inside this folder (do not return files)
     */
    public static ArrayList<String> getDirectoryPaths(String directory){

        ArrayList<String> pathArray = new ArrayList<>();

        // E.G directory = "storage/emulated/0/Pictures
        // 1) Create the directory and list all (Files or Directories) inside it.
        File file = new File(directory);
        File[] listfiles = file.listFiles();

        // 2) Loop through each one and check if it is a directory.
        // 3) If so, add to the list.
        // Condition: It'll loop through the directories only if there is at least
        // one file in the File Array
        if (listfiles != null) {
            for (File singleFile: listfiles) {
                if (singleFile.isDirectory()) {
                    pathArray.add(singleFile.getAbsolutePath());
                }
            }
        }

        return pathArray;
    }


    /**
     * Returns all the files paths that are inside a directory
     * @param directory a specific folder (directory) path
     * @return all the files' paths that are inside this folder (do not return directories)
     */
    public static ArrayList<String> getFilePaths(String directory){

        ArrayList<String> pathArray = new ArrayList<>();

        // 1) Create the directory and list all (Files or Directories) inside it.
        File file = new File(directory);
        File[] listfiles = file.listFiles();

        // 2) Loop through each one and check if it is a file.
        // 3) If so, add to the list.
        // Condition: It'll loop through the files only if there is at least
        // one file in the File Array
        if (listfiles != null) {
            for (File singleFile: listfiles) {
                if (singleFile.isFile()) {
                    pathArray.add(singleFile.getAbsolutePath());
                }
            }
        }

        return pathArray;

    }

}
