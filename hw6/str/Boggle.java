import javax.script.ScriptContext;
import java.io.*;
import java.util.Scanner;

public class Boggle{

    public static void main(String[] args) throws IOException {
        int numOfWords = Integer.parseInt(args[0].split(" ")[1]);
        int width = Integer.parseInt(args[1].split(" ")[1]);
        int height = Integer.parseInt(args[2].split(" ")[1]);
        TrieTree tree = new TrieTree();
        GraphDB db = new GraphDB(width, height);
        String dictFile = null;
        String boardFile = null;
        boolean hasDict = false;
        boolean hasBoard = false;
        if (args.length == 3) {
            hasDict = false;
            hasBoard = false;
        } else if (args.length == 4) {
            String[] line = args[3].split(" ");
            if (line[0].equals("-d")) {
                dictFile = line[1];
                hasDict = true;
            } else {
                boardFile = line[1];
                hasBoard = true;
            }
        } else {
            String[] line1 = args[3].split(" ");
            String[] line2 = args[4].split(" ");
            dictFile = line1[1];
            hasDict = true;
            boardFile = line2[1];
            hasBoard = true;
        }


        // create dict
        try {
            File dict;
            if (hasDict) {
                dict = new File(dictFile;
            } else {
                dict = new File("words");
            }
            Scanner sc = new Scanner(dict);
            while (sc.hasNext()) {
                tree.getRoot().add(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }



        // create board
        try {
            File board;
            if (hasBoard) {
                board = new File(boardFile);
                Scanner sc = new Scanner(board);
                int i = 0;
                while (sc.hasNext()) {
                    db.add(sc.nextLine(), i);
                    i++;
                }
            } else {
                db.createRandomBoard();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }



        System.out.println("hello");
    }
}