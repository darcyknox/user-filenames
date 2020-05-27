import java.util.*;
import java.io.*;

public class UserFilenames {

  public static void dfsFiles(File rootDir) {
    File[] dir = rootDir.listFiles();
    for (File f: dir) {
      if (f.isDirectory()) {
        dfsFiles(f);
      } else {
        System.out.println(f.getName());
      }
    }
  }

  public static void main(String[] args) throws IOException {

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

  }

}
