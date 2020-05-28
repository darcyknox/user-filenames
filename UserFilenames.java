import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class UserFilenames {

  public static ArrayList<FileContents> files = new ArrayList<FileContents>();

  public static void dfsFiles(File rootDir) {
    File[] dir = rootDir.listFiles();
    for (File f: dir) {
      if (f.isDirectory()) {
        dfsFiles(f);
      } else {
        FileContents fileInstance = new FileContents(f.getName(), f.toString());
        files.add(fileInstance);
      }
    }
  }

  public static void main(String[] args) throws ParseException {

    System.out.println(args[0]);

    File root = new File(args[0]);

    /*
    if (args.length < 1) {
        System.out.println("Invalid: No input");
        System.exit(0);
    }
    */
    /*
    Scanner scan = null;

    try {
      scan = new Scanner(new FileInputStream(args[0]));
    } catch (FileNotFoundException e) {
      System.out.println("Invalid: No input");
      System.exit(0);
    }
    */

    dfsFiles(root);


    for (FileContents f: files) {
      System.out.println(f.filename);
      System.out.println(f.path);
    }

    Comparator<FileContents> compareByFileName = (FileContents f1, FileContents f2) -> f1.filename.compareTo(f2.filename);

    Collections.sort(files, compareByFileName);

    System.out.println();

    for (FileContents f: files) {
      System.out.println(f.filename);
      System.out.println(f.path);
    }


  }


  private static class FileContents {

    public String filename;
    public Path path;


    public FileContents(String filename, String pathString) {
      this.filename = filename;
      this.path = Paths.get(pathString);
    }
  }

}
