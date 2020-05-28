import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.regex.*;

public class UserFilenames {

  public static ArrayList<FileContents> files = new ArrayList<FileContents>();

  public static void dfsFiles(File rootDir) {
    File[] dir = rootDir.listFiles();
    for (File f : dir) {
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
     * if (args.length < 1) { System.out.println("Invalid: No input");
     * System.exit(0); }
     */
    /*
     * Scanner scan = null;
     *
     * try { scan = new Scanner(new FileInputStream(args[0])); } catch
     * (FileNotFoundException e) { System.out.println("Invalid: No input");
     * System.exit(0); }
     */

    dfsFiles(root);

    for (FileContents f : files) {

      String[] split = f.filename.toString().split("-");
      String[] nameSplit = f.filename.toString().split("");
      StringBuilder suffix = new StringBuilder();

      boolean match1 = Pattern.compile("^0[1-5]$").matcher(split[0].toString()).matches();
      boolean match2 = Pattern.compile("^0[1-9]|1[0-9]|2[0-5]$").matcher(split[1].toString()).matches();
      boolean match3 = Pattern.compile("^(0[1-9]|[1-9][0-9])$").matcher(split[2].toString().substring(0, 2)).matches();

      if (split.length != 3) {
        System.out.println("Invalid: Numbers in file name must be separated by 2 hyphens.");
        return;
      }
      for (int i = f.filename.toString().length() - 1; i > f.filename.toString().length() - 5; i--) {
        suffix.append(nameSplit[i]);
      }

      //System.out.println(suffix);

      if (suffix.toString() != "txt.") {
        System.out.println("Invalid: files must be .txt files/file names must end in .txt");
      }
      if (!match1) {
        System.out.println("Invaild: job site must be from 01 - 05");
      } else if (!match2) {
        System.out.println("Invaild: lab desk must be from 01 - 25");
      } else if (!match3) {
        System.out.println("Invaild: job number must be from 01 - 99");
      }

      System.out.println(f.filename);
      System.out.println(f.path);
    }

    Comparator<FileContents> compareByFileName = (FileContents f1, FileContents f2) -> f1.filename
        .compareTo(f2.filename);

    Collections.sort(files, compareByFileName);

    System.out.println();

    System.out.println("");

    for (FileContents f : files) {
      System.out.println(f.filename);
      System.out.println(f.path);
    }

    System.out.println();

    for (FileContents f : files) {
      try (BufferedReader br = new BufferedReader(new FileReader(f.pathString))) {
        String line;
        while ((line = br.readLine()) != null) {
         System.out.println(line);
        }
      } catch (IOException i) {
        System.out.println("error");
        System.exit(0);
      }
    }



  }

  /*
  public static void printContents(ArrayList<FileContents> fileList) throws IOException {
    for (FileContents f : fileList) {
      try (BufferedReader br = new BufferedReader(new FileReader(f.pathString))) {
        String line;
        while ((line = br.readLine()) != null) {
         System.out.println(line);
        }
      }
    }
  }
  */

  private static class FileContents {

    public String filename;
    public Path path;
    public String pathString;

    public FileContents(String filename, String pathString) {
      this.filename = filename;
      this.path = Paths.get(pathString);
      this.pathString = pathString;
    }
  }

}
