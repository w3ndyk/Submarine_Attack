import java.io.*;
import java.util.*;

public class SubAttack
{
    public static final int WORLD_DIMENSION = 300;
    
    public static final int DESTROY_RADIUS = 30;
    public static final int DAMAGE_RADIUS = 50;

    public static void main(String args[]) {
        final int MIN_LENGTH = 40;
        final int MAX_LENGTH = 70;
        final int MIN_HEIGHT = 10;
        final int MAX_HEIGHT = 20;
        
        boolean play = true;
        while (play) {
            boolean xValid = false, yValid = false;
            int subHP = 2, depth_charges = 5, input_x, input_y;
            int sub_length, sub_height, sub_x, sub_y, far_distance;
            String strX = null, strY = null, again = null;
            
            Scanner scan = new Scanner(System.in);

            // generate submarine dimensions
            sub_length = (int) (Math.random() * (MAX_LENGTH - MIN_LENGTH + 1)) + MIN_LENGTH;
            sub_height = (int) (Math.random() * (MAX_HEIGHT - MIN_HEIGHT + 1)) + MIN_HEIGHT;
            
            // spawn submarine
            sub_x = (int) (Math.random() * (WORLD_DIMENSION - sub_length + 1));
            sub_y = (int) (Math.random() * (WORLD_DIMENSION - sub_height + 1));
            
            // gameplay starts
            System.out.println("A submarine has been spotted!");
            for (depth_charges = 5; depth_charges > 0; depth_charges--) {
                System.out.println("\n** Depth charges: " + depth_charges + " **");
                // INPUT
                System.out.print("\nEnter the x position of the depth charge: ");
                strX = scan.nextLine();
                xValid = circleLegal(strX);
                while (!xValid) {
                    System.out.print("Re-enter the x position of the depth charge: ");
                    strX = scan.nextLine();
                    xValid = circleLegal(strX);
                }
                input_x = Integer.parseInt(strX);
                
                System.out.print("Enter the y position of the depth charge: ");
                strY = scan.nextLine();
                yValid = circleLegal(strY);
                while (!yValid) {
                    System.out.print("Re-enter the y position of the depth charge: ");
                    strY = scan.nextLine();
                    yValid = circleLegal(strY);
                }
                input_y = Integer.parseInt(strY);
                
                // HIT OR MISS
                far_distance = far(input_x, input_y, sub_x, sub_y, sub_length, sub_height);
                if (overLap(input_x, input_y, sub_x, sub_y, sub_length, sub_height)) {
                    System.out.println("\n** Direct Hit! **");
                    subHP = 0;
                }
                else if (far_distance <= 30) {
                    System.out.println("\n** Direct Hit! **");
                    subHP = 0;
                }
                else if (far_distance <= 50) {
                    System.out.println("\n** Submarine has taken damage! **");
                    subHP--;
                }
                else if (far_distance > 50) {
                    System.out.println("\n** Miss! **");
                }
                
                // STATUS REPORT
                if (subHP == 0) {
                    System.out.println("The submarine has been destroyed!\n");
                    break;
                }
                else if (subHP == 1) {
                    System.out.println("One more hit until submarine is destroyed!");
                }
                else if (subHP == 2) {
                    System.out.println("The submarine is still intact.");
                }
            }
            
            // ENDING MESSAGE
            if (subHP == 0) {
                System.out.println("We did it Captain!");
            }
            else if (depth_charges == 0 && (subHP > 0)) {
                System.out.println("\n** Depth charges: 0 **");
                System.out.println("We'll get them next time Captain!");
            }
            
            // PLAY AGAIN
            System.out.print("\nAnother submarine has been spotted! Would you like to engage? ");
            again = scan.nextLine();
            while (true) {
                if (again.equalsIgnoreCase("no") || again.equalsIgnoreCase("n")) {
                    System.out.println("Until next time, Captain!");
                    play = false;
                    break;
                }
                else if (again.equalsIgnoreCase("yes") || again.equalsIgnoreCase("y")) {
                    System.out.println("Good luck Captain!\n");
                    System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
                    break;
                } 
                else {
                    System.out.println("\tError! Incorrect response.");
                    System.out.print("Another submarine has been spotted! Would you like to engage? ");
                    again = scan.nextLine();
                }
            }
        }
    }
    
    public static boolean isNumString(String str) {
        boolean condition = true;
        // checks if string contains numbers only
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                condition = false;
            }
        }
        if (!condition) {
            System.out.println("\tError! A coordinate must be entered!");
        }
        return condition;
    }
    
    public static boolean circleLegal(String str) {
        boolean condition = false;
        int input = 0;
        
        if (isNumString(str)) {
            input = Integer.parseInt(str);
            if (input < WORLD_DIMENSION && input > 0) {
                if ((input <= (WORLD_DIMENSION - DESTROY_RADIUS)) && (input >= DESTROY_RADIUS)) {
                    condition = true;
                }
                else {
                   System.out.println("\tError! The depth charge must explode within the spotted area!");
                   condition = false; 
                }
            }
            else if (input >= WORLD_DIMENSION || input <= 0) {
                System.out.println("\tError! The submarine is within the 300x300 meter area!");
                condition = false;
            }
        }
        return condition;
    }
    
    public static boolean overLap(int x, int y, int subX, int subY, int subL, int subH) {   
        // check in x coordinate
        boolean xCheck = (x >= subX) && (x <= (subX + subL));
        
        // check in y coordinate
        boolean yCheck = (y >= subY) && (y <= (subY + subH));
        
        if (xCheck && yCheck) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public static int far(int x, int y, int subX, int subY, int subL, int subH) {    
        int dx, dy, distance = 0;
        
        if (x < subX) { // LEFT
            dx = Math.abs(x - subX);
            if (y < subY) { // upper left
                dy = Math.abs(y - subY);
                distance = (int) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy,2));
            }
            else if ((y >= subY) && (y <= (subY + subH))) { // middle left
                distance = dx;
            }
            else if (y > (subY + subH)) { // bottom left
                dy = Math.abs(y - (subY + subH));
                distance = (int) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy,2));
            }
        }
        else if ((x >= subX) && (x <= (subX + subL))) { // CENTER
            if (y <= subY) { // center top
                distance = Math.abs(y - subY);
            }
            else if (y >= (subY + subH)) { // center bottom
                distance = Math.abs(y - (subY + subH));
            }
        }
        else if (x > (subX + subL)) { // RIGHT
            dx = Math.abs(x - (subX + subL));
            if (y < subY) { // upper right
                dy = Math.abs(y - subY);
                distance = (int) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy,2));
            }
            else if ((y >= subY) && (y <= (subY + subH))) { // middle right
                distance = dx;
            }
            else if (y > (subY + subH)) { // bottom right
                dy = Math.abs(y - (subY + subH));
                distance = (int) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy,2));
            }
        }

        return distance;
    }
}
