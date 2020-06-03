import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.text.ParseException;

public class UserFilenames {

  public static ArrayList<FileContents> files = new ArrayList<FileContents>();

  public static void dfsFiles(File rootDir) {
    File[] dir = rootDir.listFiles();
    if (dir != null) {
      for (File f : dir) {
        if (f.isDirectory()) {
          dfsFiles(f);
        } else {
          FileContents fileInstance = new FileContents(f.getName(), f.toString());
          files.add(fileInstance);
        }
      }
    }
  }

  public static void main(String[] args) throws ParseException {

    File root = new File(args[0]);

    dfsFiles(root);

    for (FileContents f : files) {

      String[] split = f.filename.toString().split("-");

      // .txt checking
      // if the file doesn't end in .txt
      /*
      if (!(f.filename.substring(f.filename.length() - 4, f.filename.length()).equals(".txt"))) {
        f.valid = false;
        f.errorMessage = " - Invalid: file must be in .txt format";
        //System.out.println(f.filename.substring(f.filename.length() - 4, f.filename.length()));
      }
      */


      // Get the indeces of the 6 numbers in the filename

      int[] numberIndeces = new int[6];
      int j = 0;
      for (int i = 0; i < f.filename.length(); i++) {
        if (Character.isDigit(f.filename.charAt(i)) && j < numberIndeces.length) {
          numberIndeces[j] = i;
          j++;
        }
      }


      // Create a valid filename to sort by, based on the 3 ID number pairs

      StringBuilder validFilename = new StringBuilder();
      int k = 0;
      for (int i: numberIndeces) {
        if (k == 2 || k == 4) {
          validFilename.append("-");
        }
        k++;
        validFilename.append(f.filename.charAt(i));
      }
      validFilename.append(".txt");
      f.filename = validFilename.toString();

      //System.out.println(f.filename);
      //System.out.println(f.path);
    }

    // Sorting by filename

    Comparator<FileContents> compareByFileName = (FileContents f1, FileContents f2) -> f1.filename.compareTo(f2.filename);

    Collections.sort(files, compareByFileName);

    // DON'T ACTUALLY NEED ANY OF THIS

    /*
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
    */

    // Read files and write contents to results.txt

    File result = new File("result.txt");

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(result));) {

      writer.write(""); //initialise writer contents

      for (FileContents f : files) {
        try (BufferedReader br = new BufferedReader(new FileReader(f.pathString))) {
          String line;
          //System.out.println(f.filename);
          while ((line = br.readLine()) != null) {
           writer.append(line + "\n");
          }
        } catch (IOException e) {
          System.err.println("IO Error");
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
    //public boolean valid = true;
    //public String errorMessage;

    public FileContents(String filename, String pathString) {
      this.filename = filename;
      this.path = Paths.get(pathString);
      this.pathString = pathString;
    }
  }

}
