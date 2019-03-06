package utils;

import java.io.File;
import java.io.FileFilter;

/**
 * A class that implements the Java FileFilter interface.
 * https://alvinalexander.com/blog/post/java/how-implement-java-filefilter-list-files-directory
 */
public class FilenameFilter implements FileFilter
{
    private String[] okFileExtensions;

    public FilenameFilter(String[] extensions) {
        this.okFileExtensions = extensions;
    }
//    private final String[] okFileExtensions = new String[] {"xml"};

    public boolean accept(File file)
    {
        for (String extension : okFileExtensions)
        {
            if (file.getName().toLowerCase().endsWith(extension))
            {
                return true;
            }
        }
        return false;
    }
}