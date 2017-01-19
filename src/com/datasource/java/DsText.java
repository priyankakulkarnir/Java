package com.datasource.java;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import com.main.java.ThreadRunner;
import com.util.java.Validator;


/**
 * @author Priyanka Kulkarni
 * DsText class is to read runners data from text source file
 * This creates ThreadRunner object for each runner in database 
 */

public class DsText implements DsMain{

	/**
	 * Declaring bufferReader object
	 */
	private BufferedReader bufferedReader = null;

    /**
    * Constructor of DsText check file is available or not 
    * @throws java.lang.Exception - for File related exception
    */ 
    public DsText() throws Exception {
    	//Ask user to enter name of file
        String filename = Validator.getRequiredFileName("Enter text file name: ");
        try {
        	// Checks file name entered by user
        	if(filename.equals("FinalTextData.txt")){
        		System.out.println(START_MESSAGE);
        		// assigning value to bufferReader object
        		bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        	}
        	else{
				System.out.println("'" +filename+ "'" +" is invalid Filename\n(Please check file name.It is case sensitive.)\n");
			}
        } catch (FileNotFoundException e) {
            System.out.println("File: " + filename + " is not found.");
            System.out.println("");
        }

        if (bufferedReader == null) {
            throw new Exception();
        }
    }

    /**
     * Override getRunner method from DsMain the interface.
     * This method reads the data from the derby database 
     * creates ThreadRunner objects for each runner in database
     * @return Arraylist of type ThreadRunner
     */
    @Override
    public ArrayList<ThreadRunner> getRunners() {
        ArrayList<ThreadRunner> runners = new ArrayList<ThreadRunner>();

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                ThreadRunner runner = readLine(line);
                if (runner != null)
                    runners.add(runner);
            }
        } catch (IOException e) {
            System.err.println("An error has occured when reading a line from the input text file.");
        }

        return runners;
    }

    /**
     * This method creates a ThreadRunner instance from text file line
     * @param line is to read line from the input text file.
     * @return null - returns null value
     */    
    private ThreadRunner readLine(String line) {
        String seperator = "/ \t";
        StringTokenizer st = new StringTokenizer(line, seperator);
        if (st.countTokens() >= 3) {
            String name = st.nextToken();
            String s = st.nextToken();
            String r = st.nextToken();
            try {
                int runnersSpeed = Integer.parseInt(s);
                int restPercentage = Integer.parseInt(r);

                try {
                    return new ThreadRunner(name, restPercentage, runnersSpeed);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException is raised.");
                System.out.println("line: " + line);
            }
        }
        return null;
    }
}
