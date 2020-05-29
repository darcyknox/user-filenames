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

    int k = 1;
    int prevJobSite = -1;
    int prevLabDesk = -1;
    int prevJobNumber = -1;


    for (FileContents f : files) {

      String[] split = f.filename.toString().split("-");
      String[] nameSplit = f.filename.toString().split("");
      StringBuilder suffix = new StringBuilder();

      boolean match1 = Pattern.compile("^0[1-5]$").matcher(split[0].toString()).matches();
      boolean match2 = Pattern.compile("^0[1-9]|1[0-9]|2[0-5]$").matcher(split[1].toString()).matches();
      boolean match3 = Pattern.compile("^(0[1-9]|[1-9][0-9])$").matcher(split[2].toString().substring(0, 2)).matches();

      for (int i = f.filename.toString().length() - 1; i > f.filename.toString().length() -5; i--) {
        suffix.append(nameSplit[i]);
      }   

      System.out.println(suffix.toString());
      //System.out.println("Here");
      //System.out.println(Arrays.toString(split));
      //System.out.println(split[2].replace(".txt", ""));

      int jobSite = Integer.parseInt(split[0]);
      int labDesk = Integer.parseInt(split[1]);
      int jobNumber = Integer.parseInt(split[2].replace(".txt", ""));

      if (suffix.toString() != "txt.") {
        System.out.println("Invalid: filenames must end in .txt");
        continue;
      } else if (split.length != 3) {
        System.out.println("Invalid: Numbers in file name must be separated by 2 hyphens.");
        continue;
      } else if (nameSplit.length != 11){
        System.out.println("Invalid: Filename must be exactly 11 characters long.");
        continue;
      } else if (!match1) {
        System.out.println("Invaild: job site must be from 01 - 05");
        continue;
      } else if (!match2) {
        System.out.println("Invaild: lab desk must be from 01 - 25");
        continue;
      } else if (!match3) {
        System.out.println("Invaild: job number must be from 01 - 99");
        continue;
      } /*else if (jobNumber != k) {
        System.out.println("Invalid: Non-sequential job number");
      }*/

      // THIS NEEDS TO BE DONE AFTER SORTING
      if (prevJobSite == -1 && prevLabDesk == -1 && prevJobNumber == -1) {
        prevJobSite = jobSite;
        prevLabDesk = labDesk;
        prevJobNumber = jobNumber;
      } else {
        if (jobSite == prevJobSite && labDesk == prevLabDesk) { // if jobSite and labDesk haven't changed
          if (jobNumber != prevJobNumber + 1) { // if jobNumber is out of sequence
            System.out.println("Invalid: Non-sequential job number");
          }
        } else { // if jobSite and labDesk have changed
          if (jobNumber != 1) { // jobSite must be 1 to be valid
            System.out.println("Invalid: Non-sequential job number");
          }
        }
        prevJobSite = jobSite;
        prevLabDesk = labDesk;
        prevJobNumber = jobNumber;
      }

      k++;



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
      } catch (IOException e) {
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

    public FileContents(String filename, String pathString, ) {
      this.filename = filename;
      this.path = Paths.get(pathString);
      this.pathString = pathString;
    }
  }

}
