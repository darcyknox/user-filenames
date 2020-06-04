import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.text.ParseException;

public class UserFilenames {

  public static ArrayList<FileContents> files = new ArrayList<FileContents>();

  // Depth first search the root directory and store each file as an object
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

    }

    // Sorting by filename

    Comparator<FileContents> compareByFileName = (FileContents f1, FileContents f2) -> f1.filename.compareTo(f2.filename);

    Collections.sort(files, compareByFileName);

    // Read files and write contents to results.txt

    File result = new File("result.txt");

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(result));) {

      writer.write(""); //initialise writer contents

      for (FileContents f : files) {
        try (BufferedReader br = new BufferedReader(new FileReader(f.pathString))) {
          String line;
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

  // Class for storing file details
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
