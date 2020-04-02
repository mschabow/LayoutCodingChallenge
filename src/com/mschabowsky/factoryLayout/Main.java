package com.mschabowsky.factoryLayout;

import org.xml.sax.SAXException;

import javax.annotation.processing.FilerException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    static Scanner in = new Scanner(System.in);
    static int x;
    static int y;
    static Path filePath;
    static FactoryLayoutManager manager;
    static Boolean end = false;

    public enum Direction {X, Y}

    public static void main(String[] args) {

        if (args.length > 0 && args[0] != null)
            filePath = Paths.get(args[0]);
        else filePath = Paths.get("values.xml");
        if(Initialize(filePath)){
            while (!end) {
                RunProgram();
                promptToRunAgain();
            }
        }





    }

    private static boolean Initialize(Path filePath) {
        manager = new FactoryLayoutManager();

        try {

            manager.loadXmlLayout(filePath);

        } catch (IOException e) {
            System.out.println("ERROR: File not found. Please enter a valid xml file name parameter.");
            return false;
        } catch (SAXException e) {
            System.out.println("ERROR: Error parsing Xml. Please check that .xml file is valid");
            e.printStackTrace();
            return false;
        } catch (ParserConfigurationException e) {
            System.out.println("ERROR: Error parsing Xml. Please check that .xml file is valid");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void RunProgram() {
        String typeResponse = promptUserForShiftType();
        if (typeResponse.equalsIgnoreCase("y")) {
            promptUserForShiftAmounts();
            shiftLayout(x, y);
        } else {
            boolean itemFound = false;
            String itemString = "";
            while (!itemFound) {
                System.out.println("Enter an Item ID. (To see a list of items, enter the word 'list'):");
                itemString = in.next();
                if (itemString.equalsIgnoreCase("list"))
                    manager.printItemListToConsole();
                else if (manager.ItemIdExists(itemString)) {
                    itemFound = true;
                } else System.out.println("ERROR: Item Not Found. ID's are case sensitive");


            }
            promptUserForShiftAmounts();
            shiftLayout(x, y, itemString);


        }
        saveXML();


    }

    private static void saveXML() {
        manager.saveXml(filePath);
        System.out.println("Changes saved to XML file.");
        String resp = "";
        while(!isValidYesOrNo(resp)){
            System.out.println("Do you want to view the updated XML file?  (Y/N):");
            resp = in.next();
        }
        if(resp.equalsIgnoreCase("y"))
            manager.printXmlToConsole();
    }

    private static String promptUserForShiftType() {
        System.out.println("Do you want to shift the entire layout? (Y/N):");
        String shiftAll = in.next();
        if (!isValidYesOrNo(shiftAll)) {
            promptUserForShiftType();
        }
        return shiftAll;
    }

    private static void promptToRunAgain() {
        String resp = "";
        while (!isValidYesOrNo(resp)) {
            System.out.println();
            System.out.println("Do you want to execute another shift? (Y/N):");
            resp = in.next();
        }

        if (resp.equalsIgnoreCase("n"))
            end = true;

    }


    private static boolean isValidYesOrNo(String shiftAll) {
        if (shiftAll.equalsIgnoreCase("y") || shiftAll.equalsIgnoreCase("n"))
            return true;
        return false;
    }

    private static void promptUserForShiftAmounts() {
        getShiftAmount(Direction.X);
        getShiftAmount(Direction.Y);


    }

    private static void getShiftAmount(Direction direction) {

            String directionString = "";
            int directionValue = 0;
            String directionOptions;
            String directionPositive;
            String directionNegative;

            if (direction.equals(Direction.X)) {
                directionString = "X";
                directionPositive = "Right";
                directionNegative = "Left";
            } else {
                directionString = "Y";
                directionPositive = "Up";
                directionNegative = "Down";
            }
            directionOptions = "(" + directionPositive + " / " + directionNegative + ")";
            String integerString = "";
            boolean integerEntered = false;
            while(!integerEntered){
                System.out.print(String.format("Please enter the distance you want to shift %s Value: ", directionString));
                integerString = in.next();
                try{
                    directionValue = Integer.parseInt(integerString);
                    integerEntered = true;
                }
                catch (NumberFormatException e){
                    System.out.println("ERROR: Please enter a valid integer.");

                }

            }
            //setting to absolute value to not confuse directions about to be set.
            directionValue = Math.abs(directionValue);

            String d = "";
            while (!(d.equalsIgnoreCase(directionPositive) || d.equalsIgnoreCase(directionNegative))) {
                if (!d.isEmpty()) {
                    System.out.println(String.format("ERROR: Direction Input Not Understood. Please enter either %s:", directionOptions));
                }
                System.out.println(String.format("%s Direction %s: ", directionString, directionOptions));
                d = in.next();
            }

            if (d.equalsIgnoreCase(directionNegative))
                directionValue *= -1;


            if (direction.equals(Direction.X))
                x = directionValue;
            else y = directionValue;

    }

    private static void shiftLayout(int x, int y) {

        manager.modifyXYValues(x, y);
    }

    private static void shiftLayout(int x, int y, String itemId) {
        manager.modifyXYValues(x, y, itemId);

    }

}
