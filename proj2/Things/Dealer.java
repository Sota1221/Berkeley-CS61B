package Things;

import com.sun.scenario.effect.impl.state.LinearConvolveKernel;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import db.Database;
import sun.awt.image.ImageWatched;

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
    public static String dealSelect(String[] columnTitle, String[] tableName, String condition, String newColTitle){
//        String columnTitle = m.group(1); // x ( -- different case for * !)
//        String tableName = m.group(2);   // T1
//        String condition = m.group(3);   // x > 2
        for (int i = 0; i < tableName.length; i++) {
            if (!(Database.hasTable(tableName[i]))) {
                return "There isn't table called " + tableName + " in database...";
            }
        }
        Table temp;
        int tableNameLen = tableName.length;
        if (tableNameLen != 1) {
            while (true) {
                if (tableNameLen == 2) {
                    String[] newTableName = new String[]{tableName[0], tableName[1]};
                    temp = joinSelect(columnTitle, newTableName, condition);
                    break;
                } else {
                    String[] newTableName = new String[]{tableName[0], tableName[1]};
                    joinSelect(columnTitle, newTableName, condition);
                    for (int i = 2; i < tableName.length; i++) {
                        tableName[i - 1] = tableName[i];
                    }
                    tableName[0] = "joinedTable";
                    tableNameLen -= 1;
                }
            }
        } else {
            temp = Database.getTable(tableName[0]);
        }
        if (columnTitle[0].equals("*")) {
            return temp.toString();
        }
        Table anonTable = new Table("anon", columnTitle);
        for (int k = 0; k < columnTitle.length; k++) {
            LinkedList[] newColumn = new LinkedList[columnTitle.length];
            String[] array = containOperator(columnTitle[k]); // e.g. array = ["x", "+", "y"]
            if (temp.getExactColName(array[0]) == null) {
                    return "There isn't a column called " + array[0] + " in " + tableName;
            }
            if (temp.getExactColName(array[2]) == null) {
                return "There isn't a column called " + array[2] + " in " + tableName;
            }
            Integer[] convertedInt = new Integer[temp.getNumRow()];
            Float[] convertedFloat = new Float[temp.getNumRow()];
            boolean flagFirst = false;
            boolean flagSecond = false;
            if (!(array.equals(null))) {
                if (temp.checkType(array[0], "int")) {
                    convertedInt = convertInt(temp.getColumn(array[0]));
                    flagFirst = true;
                }
                if (temp.checkType(array[0], "float")) {
                    convertedFloat = convertFloat(temp.getColumn(array[0]));
                }
                if (temp.checkType(array[2], "int")) {
                    if (flagFirst) {
                        flagSecond = true;
                        Integer[] tempIntArray = convertInt(temp.getColumn(array[2]));
                        for (int t = 0; t < tempIntArray.length; t++) {
                            convertedInt[t] = operateInt(convertedInt[t], tempIntArray[t], array[1]);
                        }
                    } else {
                        convertedInt = convertInt(temp.getColumn(array[2]));
                    }
                }
                if (temp.checkType(array[2], "float")) {
                    if (!(flagFirst)) {
                        Float[] tempFloatArray = convertFloat(temp.getColumn(array[2]));
                        for (int t = 0; t < tempFloatArray.length; t++) {
                            convertedFloat[t] = operateFloat(convertedFloat[t], tempFloatArray[t], array[1]);
                        }
                    } else {
                        flagSecond = true;
                        convertedFloat = convertFloat(temp.getColumn(array[2]));
                    }
                }
                if (flagFirst && flagSecond) {
                    anonTable.addColumnLast(convertIntToString(convertedInt));
                } else if (flagFirst && !flagSecond) {
                    Float[] tempNewCol = new Float[temp.getNumRow()];
                    for (int a = 0; a < tempNewCol.length; a++) {
                        tempNewCol[a] = operateFloatInt(convertedFloat[a], convertedInt[a], array[1], false);
                    }
                    anonTable.addColumnLast(convertFloatToString(tempNewCol));
                } else if (!flagFirst && flagSecond) {
                    Float[] tempNewCol = new Float[temp.getNumRow()];
                    for (int b = 0; b < tempNewCol.length; b++) {
                        tempNewCol[b] = operateFloatInt(convertedFloat[b], convertedInt[b], array[1], true);
                    }
                    anonTable.addColumnLast(convertFloatToString(tempNewCol));
                } else {
                    anonTable.addColumnLast(convertFloatToString(convertedFloat));
                }
            } else {
                if (temp.getExactColName(columnTitle[k]) == null) {
                    return "There isn't a column called " + columnTitle[k] + " in " + tableName;
                }
                anonTable.addColumnLast(temp.getColumn(columnTitle[k]));
            }
        }
        return anonTable.toString();
    }


    private static String[] convertFloatToString(Float[] array) {
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(null)) {
                result[i] = "NaN";
            } else {
                result[i] = Float.toString(array[i]);
            }
        }
        return result;
    }

    private static String[] convertIntToString(Integer[] array) {
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(null)) {
                result[i] = "NaN";
            } else {
                result[i] = Integer.toString(array[i]);
            }
        }
        return result;
    }

    private static Float operateFloatInt(Float a, Integer b, String operator, boolean flag) {
        if (operator.equals("+")) {
                return a + b;
        } else if (operator.equals("-")) {
            if (flag) {
                return a - b;
            }
            return b - a;
        } else if (operator.equals("*")) {
            return a * b;
        } else {
            if (flag) {
                if (b == 0) {
                    return null;
                }
                return a / b;
            }
            if (a == 0) {
                return null;
            }
            return b / a;
        }
    }

    private static Integer operateInt(Integer a, Integer b, String operator) {
        if (operator.equals("+")) {
            return a + b;
        } else if (operator.equals("-")) {
            return a - b;
        } else if (operator.equals("*")) {
            return a * b;
        } else {
            if (b == 0) {
                return null;
            }
            return a / b;
        }
    }

    private static Float operateFloat(Float a, Float b, String operator) {
        if (operator.equals("+")) {
            return a + b;
        } else if (operator.equals("-")) {
            return a - b;
        } else if (operator.equals("*")) {
            return a * b;
        } else {
            if (b == 0) {
                return null;
            }
            return a / b;
        }
    }

    private static Integer[] convertInt(List<String> p) {
        Integer[] array = new Integer[p.size()];
        for (int i = 0; i < p.size(); i++) {
            array[i] = Integer.parseInt((p.get(i)));
        }
        return array;
    }

    private static Float[] convertFloat(List<String> p) {
        Float[] array = new Float[p.size()];
        for (int i = 0; i < p.size(); i++) {
            array[i] = Float.parseFloat(p.get(i));
        }
        return array;
    }

    private static boolean contains(String[] array, String str) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].split("\\s* \\s*")[0].equals(str)) {
                return true;
            }
        }
        return false;
    }

    private static String[] containOperator(String str) {
        String[] array;
        if (str.length() == 1) {
            array = str.split("\\s*\\s*");
        } else {
            array = str.split("\\s* \\s*");
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals("+") || array[i].equals("-") || array[i].equals("*") || array[i].equals("/")) {
                    return array;
            }
        }
        return null;
    }

    private static Table joinSelect(String[] columnTitle, String[] tableName, String condition) {
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
                if (joited.getColumnName()[i].equals(joited.getColumnName()[indexToBeDeleted])) {
                    joited.removeColmnTitle(indexToBeDeleted);
                    joited.removeColumn(indexToBeDeleted);
                }
            }
        }
        Database.saveTable(joited);
        return joited;
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

