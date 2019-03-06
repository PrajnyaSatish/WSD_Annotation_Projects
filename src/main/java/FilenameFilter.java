import java.io.*;

/**
 * A class that implements the Java FileFilter interface.
 * https://alvinalexander.com/blog/post/java/how-implement-java-filefilter-list-files-directory
 */
public class FilenameFilter implements FileFilter
{
    private final String[] okFileExtensions = new String[] {"030811.xml.parse"};

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
