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

    //System.out.println(args[0]);

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
        f.valid = false;
        System.err.println("Invalid: Numbers in file name must be separated by 2 hyphens.");
        return;
      }

      /*
      for (int i = f.filename.toString().length() - 1; i > f.filename.toString().length() - 5; i--) {
        suffix.append(nameSplit[i]);
      }
      */

      //System.out.println(suffix);
      //System.out.println("Here");
      //System.out.println(Arrays.toString(split));
      //System.out.println(split[2].replace(".txt", ""));

      /*
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
      */

      // .txt checking
      // if the file doesn't end in .txt
      if (!(f.filename.substring(f.filename.length() - 4, f.filename.length()).equals(".txt"))) {
        f.valid = false;
        f.errorMessage = " - Invalid: file must be in .txt format";
        //System.out.println(f.filename.substring(f.filename.length() - 4, f.filename.length()));
      }

      //System.out.println(f.filename);
      //System.out.println(f.path);
    }

    Comparator<FileContents> compareByFileName = (FileContents f1, FileContents f2) -> f1.filename.compareTo(f2.filename);

    Collections.sort(files, compareByFileName);

    //System.out.println();

    int prevJobSite = -1;
    int prevLabDesk = -1;
    int prevJobNumber = -1;

    for (FileContents f : files) {
      //System.out.println(f.filename);
      //System.out.println(f.path);

      if (f.valid) {

        String[] split = f.filename.toString().split("-");

        int jobSite = Integer.parseInt(split[0]);
        int labDesk = Integer.parseInt(split[1]);
        int jobNumber = Integer.parseInt(split[2].replace(".txt", ""));

        /*
        System.out.println(prevJobSite);
        System.out.println(prevLabDesk);
        System.out.println(prevJobNumber);
        System.out.println(jobSite);
        System.out.println(labDesk);
        System.out.println(jobNumber);
        */

        if (jobSite < 1 || jobSite > 5) {
          f.valid = false;
          System.err.println(f.filename + " - Invalid: Job Site out of valid range");
        } else if (labDesk < 1 || labDesk > 25) {
          f.valid = false;
          System.err.println(f.filename + " - Invalid: Lab Desk out of valid range");
        } else if (jobNumber < 1 || jobNumber > 99) {
          f.valid = false;
          System.err.println(f.filename + " - Invalid: Job Number out of valid range");
        }

        if (prevJobSite == -1 && prevLabDesk == -1 && prevJobNumber == -1) {
          prevJobSite = jobSite;
          prevLabDesk = labDesk;
          prevJobNumber = jobNumber;
        } else {
          if (jobSite == prevJobSite && labDesk == prevLabDesk) { // if jobSite and labDesk haven't changed
            if (jobNumber != prevJobNumber + 1) { // if jobNumber is out of sequence
              f.valid = false;
              System.err.println(f.filename + " - Invalid: Non-sequential job number");
            }
          } else { // if jobSite and labDesk have changed
            if (jobNumber != 1) { // jobSite must be 1 to be valid
              f.valid = false;
              System.err.println(f.filename + " - Invalid: Non-sequential job number");
            }
          }
          prevJobSite = jobSite;
          prevLabDesk = labDesk;
          prevJobNumber = jobNumber;
        }
      } else {
        System.err.println(f.filename + f.errorMessage);
      }
    }

    //System.out.println();

    File result = new File("result.txt");

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(result));) {

      writer.write("");

      for (FileContents f : files) {
        try (BufferedReader br = new BufferedReader(new FileReader(f.pathString))) {
          String line;
          while ((line = br.readLine()) != null) {
           writer.append(line + "\n");
           //System.out.println(line);
          }
        } catch (IOException e) {
          System.out.println("error");
          System.exit(0);
        }
      }

      writer.close();

    } catch (IOException e) {
      System.err.println("IO Error");
      System.exit(0);
    }

  }

  private static class FileContents {

    public String filename;
    public Path path;
    public String pathString;
    public boolean valid = true;
    public String errorMessage;

    public FileContents(String filename, String pathString) {
      this.filename = filename;
      this.path = Paths.get(pathString);
      this.pathString = pathString;
    }
  }

}
