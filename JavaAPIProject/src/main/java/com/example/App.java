package com.example;

import java.util.ArrayList;
import java.util.Scanner; 

public class App {
       public static void Start(){
            //loops until the user is done using the program
            while(true){
                try {
                //scanner obj for selecting options
                Scanner Obj = new Scanner(System.in); 
                System.out.println(); 
                System.out.println(); 
                System.out.println("__| |_________________________________________________________________________| |__\r\n" + //
                                        "__   _________________________________________________________________________   __\r\n" + //
                                        "  | |                                                                         | |  \r\n" + //
                                        "  | |__        __   _                            _           ____ ____  ____  | |  \r\n" + //
                                        "  | |\\ \\      / /__| | ___ ___  _ __ ___   ___  | |_ ___    / ___|  _ \\/ ___| | |  \r\n" + //
                                        "  | | \\ \\ /\\ / / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\  | |  _| |_) \\___ \\ | |  \r\n" + //
                                        "  | |  \\ V  V /  __/ | (_| (_) | | | | | |  __/ | || (_) | | |_| |  __/ ___) || |  \r\n" + //
                                        "  | | __\\_/\\_/ \\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/   \\____|_|   |____/ | |  \r\n" + //
                                        "  | || __ )  ___ | |_| |                                                      | |  \r\n" + //
                                        "  | ||  _ \\ / _ \\| __| |                                                      | |  \r\n" + //
                                        "  | || |_) | (_) | |_|_|                                                      | |  \r\n" + //
                                        "  | ||____/ \\___/ \\__(_)                                                      | |  \r\n" + //
                                        "__| |_________________________________________________________________________| |__\r\n" + //
                                        "__   _________________________________________________________________________   __\r\n" + //
                                        "  | |                                                                         | |  "); 
                System.out.println();
                System.out.println("-------------------------------------------------------------------------------------------------------------"); 
                System.out.println(); 
                System.out.println("To ask for directions, enter D");
                System.out.println();
                System.out.println("To read travel history, enter H");
                System.out.println();
                System.out.println("To read travel history sorted from shortest to longest, enter HS");
                System.out.println();
                System.out.println("To get the average distance traveled per trip, enter E");
                System.out.println();
                System.out.println("If you enter anything else you will be brought to the closing menu");
                System.out.println();
            
                //depending on whats entered, the program will execute a certian line of code

                String var = Obj.nextLine();
                
                //Reading sorted history
                if(var.equals("HS")){
                    ArrayList<Double> values = new ArrayList<>();
                    String history = Api.getHistory(); // Read file contents
                    String[] lines = history.split("\\r?\\n"); // Split into lines (had to ask copilot on how to split each line)

                    // Parse strings into double 
                    for (int i = 0; i < lines.length; i++) {
                        String line = lines[i].trim(); //trim method to remove and spaces (also asked copilot to help me with this to debug)
                        if (!line.isEmpty()) {
                            try {
                                values.add(Double.parseDouble(line)); //adds each line to values as a double
                            } catch (NumberFormatException e) {
                                System.out.println("Skipping invalid line: " + line);
                            }
                        }
                    }

                    // add each line to an array
                    double[] arr = new double[values.size()];
                    for (int i = 0; i < values.size(); i++) {
                        arr[i] = values.get(i);
                    }

                    //use selection sort to sort the list
                    for (int i = 0; i < arr.length - 1; i++) {
                        int minIndex = i;
                        for (int j = i + 1; j < arr.length; j++) {
                            if (arr[j] < arr[minIndex]) {
                                minIndex = j;
                            }
                        }
                        // Swap smallest with current index
                        double temp = arr[i];
                        arr[i] = arr[minIndex];
                        arr[minIndex] = temp;
                    }

                    // Print out each sorted value in array
                    System.out.println("Sorted distances (shortest to longest):");
                    for (int i = 0; i < arr.length; i++) {
                        System.out.println(arr[i]);
                    }
                }
                
                //returns the contents of TravelData.txt
                if(var.equals("H")){
                    System.out.println("Meters traveled:");

                    //uses the getHistory method to return the contents of TravelData.txt
                    System.out.println(Api.getHistory());
                }

                if(var.equals("E")){
                    String history = Api.getHistory(); // Get all values as a multiline string
                    String[] lines = history.split("\\r?\\n"); // Split by line breaks(I learned this part form copilot)
                    double sum = 0.0;

                    //for loop to find sum of all the meters traveled
                    for (String line : lines) {
                        if (!(line.trim()==null)) {
                            sum += Double.parseDouble(line.trim());//trim method to remove and spaces (learned from copilot earlier)
                        }
                    }
                    //gets the amount of trips 
                    double trips = Api.getLines();
                    System.out.println("Average meters traveled per trip:");
                    System.out.println(sum/trips); //divides for average
                }

                if(var.equals("D")){
                    //asks for your address 
                    Scanner adr1 = new Scanner(System.in);
                    System.out.println("Note: please be sure your address is valid");
                    System.out.println("Please enter your address:");
                    String Adr1 = adr1.nextLine();

                    //asks for your destination's address
                    Scanner adr2 = new Scanner(System.in);
                    System.out.println("Please enter your destination's address:");
                    String Adr2 = adr2.nextLine();

                    //converts the adresses to coordinates (double array)
                    double[] coord1 =Api.getCoordinatesFromAddress(Adr1);
                    double[] coord2 =Api.getCoordinatesFromAddress(Adr2);
                    
                    //Uses the given data to request the api for directions
                    String directions = Api.getDirections(coord1[0], coord1[1], coord2[0], coord2[1]); 
                    Api.filterData(directions); //filters the json data
                    Api.saveData(Double.toString(Api.getTotalDistance(directions))); //saves the distance traveled
                }
                //ask the user if they want to go back the main menu(if they do the loop will continue)
                Scanner Rep = new Scanner(System.in);
                System.out.println(); 
                System.out.println("Would you like to go back to main menu? (Y/N)");
                System.out.println();
                System.out.println("If you enter something that isnt Y the program will end");
                System.out.println();
                String rep = Rep.nextLine();
                if(!(rep.equals("Y"))){ //if they dont the loop will break and the message will appear
                    System.out.println();
                    System.out.println("Thank you for using GPS Bot!");
                    System.out.println();
                    break;
                }
            } catch (Exception e) {
                //error message in case 
                System.out.println();
                System.out.println("🚨 An error has occured please make sure you entered valid addresses 🚨");
                System.out.println();
                System.out.println("🔃 Going back to main menu 🔃");
                System.out.println();
            }
        }
    }
    //I used https://www.asciiart.eu/image-to-ascii for the terminal
}
