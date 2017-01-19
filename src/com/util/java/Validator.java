package com.util.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @author Priyanka Kulkarni
 * Validator class handles validations on the user inputs.
 */

public class Validator {
	
	private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    
	/**
	 * This method ensures about integer input
	 * @param str -  input string from user
	 * @return value of string
	 */
	static public int readInt(String str)
    {
        int value = 0;
        boolean exitFlag = false;
        while( exitFlag == false )
        {
            System.out.print(str);
            try {
                value = Integer.parseInt(br.readLine());
                exitFlag = true;
            } catch (Exception e) {
                System.out.println("Please input int value.\n"
                		+ "");
            }
        }
        return value;
    }

	/**
	 * This method forces user to enter string value
	 * @return value
	 */
    private static String readString() {
        String value = "";
        try {
            value = br.readLine();
        } catch (IOException e) {
            System.out.println("Please input string value.\n");
        }

        return value;
    }

    /**
     * Method reads String entered by user 
     * @param str - to read user provided string
     * @return string
     */
    static public String readString(String str) {
        System.out.print(str);
        return readString();
    }
 	
    /**
	 * This method reads the value entered by the user and checks whether if it
	 * is valid If the user enters blank spaces or nothing an error message is
	 * displayed.
	 * @param prompt - the text to be shown to user for entering the value
	 * @return String - the valid value entered by the user
	 */
 	static public String getRequiredFileName(String prompt) {
 		String s = "";
 		boolean isValid = false;
 		while (isValid == false) {
 			System.out.print(prompt);
 			try {
				s = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
 			if (s.equals("")) {
 				System.out.println("File name can not be emplty.\n");
 			} else {
 				isValid = true;
 			}
 		}
 		return s;
 	}
}
