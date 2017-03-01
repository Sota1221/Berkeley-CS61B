package Things;

import db.Database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.*;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringJoiner;


public class Dealer {

    public static String dealCreateTable(String tableName, String[] columnTitles){

        Table newTable = new Table(tableName, columnTitles);
        Database.saveTable(newTable);
        /*
        newTable.addNameRow(tableName);
        newTable.addItemRow(tableColumns);
        */
        return "";
    }

    public static String dealStore(String tableName){
        Table t = Database.getTable(tableName);
        // make a file for the table somehow.
        return "dealStore!!, Table t = " + tableName;
    }

    public static String dealLoad(String fileName){
//        Table newTable = new Table(fileName);
//        Database.saveTable(newTable);
//        // load the file somehow
//        return "dealStore!!, Table t = " + fileName;
        try{
            File file;
            File tempFile0 = new File(fileName + ".tbl");
            File tempFile1 = new File("examples/" + fileName + ".tbl");
            if (!(checkBeforeReadfile(tempFile0) || checkBeforeReadfile(tempFile1))) {
                return "ERROR: .*"; //couldn't open or find the file.
            } else if (checkBeforeReadfile(tempFile0)) {
                file = tempFile0;
            } else {
                file = tempFile1;
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String[] columnTitles = br.readLine().split("\\s*,\\s*");
            Table newTable = new Table(fileName, columnTitles);
            String str = br.readLine();
            while(str != null){
                newTable.addRowLast(str.split("\\s*,\\s*"));
                str = br.readLine();
            }
            Database.saveTable(newTable);
            br.close();
        }catch(FileNotFoundException e){
            System.out.println("ERROR: " + e);
        }catch(IOException e){
            System.out.println(e);
        }
        return "";
    }

    private static boolean checkBeforeReadfile(File file){
        if (file.exists()){
            if (file.isFile() && file.canRead()){
                return true;
            }
        }
        return false;
    }


    public static String dealDrop(String tableName){
        Database.removeTable(tableName);
        return "";
    }

    public static String dealInsert(String tableName, String[] values){
        if (!(Database.hasTable(tableName))) {
            return "There isn't table called " + tableName + " in database...";
        }
        Table t = Database.getTable(tableName);
        t.addRowLast(values);

        return "";
    }

    public static String dealPrint(String tableName){
        if (Database.hasTable(tableName)) {
            Table t = Database.getTable(tableName);
            return t.toString();
        }
        // make a string to return. ここちゃんとできてない
        return "There isn't such the table";
    }
    public static String dealSelect(String[] columnTitle, String[] tableName, String condition){
//        String columnTitle = m.group(1); // x ( -- different case for * !)
//        String tableName = m.group(2);   // T1
//        String condition = m.group(3);   // x > 2
        for (int i = 0; i < tableName.length; i++) {
            if (!(Database.hasTable(tableName[i]))) {
                return "There isn't table called " + tableName + " in database...";
            }
        }
        if (tableName.length != 1) {
            return joinSelect(columnTitle, tableName, condition);
        } else {
            Table originalTable = Database.getTable(tableName[0]);
            for (String elem : columnTitle) {
                if (!(Arrays.asList(originalTable.getColumnName()).contains(elem))){
                    return "There isn't a column called " + elem + " in " + tableName;
                }
            }
            LinkedList[] newCol;
            Table anonTable;
            if (columnTitle[0].equals("*")) {
                anonTable = new Table("anon", originalTable.getColumnName());
                newCol = new LinkedList[originalTable.getNumCol()];
            } else {
                anonTable = new Table("anon", columnTitle);
                newCol = new LinkedList[columnTitle.length];
            }
            for (int i = 0; i < newCol.length; i++) {
                newCol[i] = (LinkedList) originalTable.getColumn(columnTitle[i]);
                anonTable.addColumnLast(newCol[i]);  // specified column added to anon table
            }
            return anonTable.toString();
        }
    }

    private static String joinSelect(String[] columnTitle, String[] tableName, String condition) {
        Table tempTableP =  Database.getTable(tableName[0]);
        Table tempTableQ = Database.getTable(tableName[1]);
        String[] tempColNameP = tempTableP.getColumnName();
        String[] tempColNameQ = tempTableQ.getColumnName();
        String[] newColTitle = combine(tempColNameP, tempColNameQ);
        Table joited = new Table("joinedTable", newColTitle);
        LinkedList<String> pRowContents;
        LinkedList<String> qRowContents;
        for (int pRow = 0; pRow < tempTableP.getNumRow(); pRow++) {
            pRowContents = tempTableP.getRow(pRow);
            for (int qRow = 0; qRow < tempTableQ.getNumRow(); qRow++) {
                qRowContents = tempTableQ.getRow(qRow);
                if (check(tempTableP, pRowContents, tempTableQ, qRowContents)) {
                    joited.addRowLast(catenate(pRowContents, qRowContents));
                }
            }
        }
        for (int i = 0; i < joited.getNumCol(); i++) {
            for (int indexToBeDeleted = i + 1; indexToBeDeleted < joited.getNumCol(); indexToBeDeleted++){
                if (joited.getColumnName()[i] == joited.getColumnName()[indexToBeDeleted]) {
                    joited.removeColmnTitle(indexToBeDeleted);
                    joited.removeColumn(indexToBeDeleted);
                }
            }
        }
        return joited.toString();
    }

    private static String[] combine(String[] p, String[] q) {
        String[] result = new String[p.length + q.length];
        for (int i = 0; i < p.length; i++) {
            result[i] = p[i];
        }
        for (int k = 0; k < q.length; k++) {
            result[p.length + k] = q[k];
        }
        return result;
    }

    private static LinkedList<String> catenate(LinkedList<String> p, LinkedList<String> q) {
        LinkedList<String> result = new LinkedList();
        for (int i = 0; i < p.size(); i++) {
            result.addLast(p.get(i)); {
            }
        }
        for (int k = 0; k < q.size(); k++) {
            result.addLast(q.get(k));
        }
        return result;
    }

    //checks if two columns have common value
    private static boolean check(Table pTable, LinkedList<String> p, Table qTable, LinkedList<String> q) {
        //specify two columns
        for (int categoryIndexOfP = 0; categoryIndexOfP < p.size(); categoryIndexOfP++) {
            for (int categoryIndexOfQ = 0; categoryIndexOfQ < q.size(); categoryIndexOfQ++) {
                //check if they have the same name
                if (pTable.getColumnName()[categoryIndexOfP].equals(qTable.getColumnName()[categoryIndexOfQ])) {
                    //check if they have the same value for specific rows(indexOfP and indexOfQ)
                    if (!(p.get(categoryIndexOfP).equals(q.get(categoryIndexOfQ)))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

/*
    public static void dealWhere(String condition) {

    }
*/
}

