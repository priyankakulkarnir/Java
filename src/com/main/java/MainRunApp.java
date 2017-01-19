package com.main.java;

import java.util.ArrayList;
import java.util.Iterator;
import com.datasource.java.DsDerby;
import com.datasource.java.DsMain;
import com.datasource.java.DsText;
import com.datasource.java.DsXML;
import com.util.java.Validator;

/**
 * @author Priyanka Kulkarni
 * This is main run application
 * It allows to run participants from different data sources as per choice of user
 */

public class MainRunApp implements RaceFinish{
    
	static boolean raceFlag ;
	static ArrayList<ThreadRunner> participants = null;
    /**
     * The main method calling run method
     * @param args - an array of String values
     */
    public static void main(String[] args) {
        new MainRunApp().runApp();
    }
    
    /**
     * run mainRoutine
     * do loop until mainRoutine() returns true.
     */
    void runApp() {
        while( true )
        {
        	raceFlag = false;
            rank.clear();
            boolean flag = mainRunMethod();
            if( flag == true )
                break;
            else {
                System.out.println("\nPress any key to continue . . .");
                Validator.readString("");
            }
        }
        System.out.println("\nThank you for using my Marathon Race Program");
    }
    
    /**
     * Creating ArrayList for maintaining ranks
     */
      private final ArrayList<String> rank = new ArrayList<String>();
    
    /**
     * The main routine.
     * Declares an ArrayList object to contain ThreadRunner objects.
     * @return is null when this program is going to end.
     */
    
    private boolean mainRunMethod()
    {        
        /**
         * Declaring startMsg, participants/runners ArrayList, choice, continueFlag
         */
        String startMsg = "\nGet set...Go!\n";       
        boolean continueFlag = false;
        int choice;
        
        /**
         * While loop continues till user do not enter 5 i.e. exit 
         */       
        while( continueFlag == false )
        {
        	/**
        	 * Displays Welcome message
        	 */
            System.out.println("Welcome to the Marathon Race Runner Program \n");
        	raceFlag = false;
            DsMain source;
            displayMenu();
            choice = Validator.readInt("Enter your choice: ");

            /**
             * Shows switch menu to user
             * Allows user to select type of data source or exit
             */           
            switch( choice )
            {
                
                // Race of participants from derby database  
                case 1:
                	System.out.println(startMsg);
                    source = new DsDerby();
                    participants = source.getRunners();
                    continueFlag = true;
                    break;
                // Race of participants from xml file      
                case 2:
                    try {
                        source = new DsXML();
                        participants = source.getRunners();
                        continueFlag = true;
                    } catch (Exception ignored) {

                    }
                    break;
                // Race of participants from text file    
                case 3:
                    try {
                        source = new DsText();
                        participants = source.getRunners();
                        continueFlag = true;
                    } catch (Exception ignored) {

                    }
                    break;
                // Race of default participants
                case 4:
                	System.out.println(startMsg);
                    participants = new ArrayList<ThreadRunner>();
                    participants.add(createThreadRunner("Tortoise", 0, 10));
                    participants.add(createThreadRunner("Hare", 90, 100));
                    continueFlag = true;
                    break;
                case 5:
                    return true;
                // Default message to display if user enters other that 1 to 5 choice    
                default:
                    System.out.println("Please input a number between 1 and 5.\n");
                    break;
            }
        }

        // Checks whether there are runners to run in race or not
        if (participants.size() == 0)
        {
            System.err.println("There is no runners!");

        } else {
            startRace(participants);
        }

        return false;
    }
    
    
    /**
     * Display Menu
     */
    private void displayMenu() {
        System.out.println("Select your data source:");
        System.out.println("");
        System.out.println("1. Derby database");
        System.out.println("2. XML file");
        System.out.println("3. Text file");
        System.out.println("4. Default two runners");
        System.out.println("5. Exit");
        System.out.println("");
    }

    /**
     * @param name  is a runner's name.
     * @param restPercentage  is a rest percentage.
     * @param runnersSpeed is runner's speed.
     * @return is ThreadRunner instance.
     */   
    private ThreadRunner createThreadRunner(String name, int restPercentage, int runnersSpeed) {
        try {
            return new ThreadRunner(name, restPercentage, runnersSpeed);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    /**
     * Runs ThreadRunner objects and shows the result.
     * @param participants is an ArrayList which contains ThreadRunner objects.
     */
	private void startRace(ArrayList<ThreadRunner> participants) {
        if (participants != null) {
            Iterator<?> it = participants.iterator();
            while (it.hasNext()) {
                ThreadRunner r = (ThreadRunner) it.next();
                r.addRestThread(this);
                r.start();
            }

            try {
                it = participants.iterator();
                while (it.hasNext()) {
                    ThreadRunner r = (ThreadRunner) it.next();
                    r.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            it = rank.iterator();
            if (it.hasNext()) {
                System.out.println(it.next() + ": You beat me fair and square.");
                System.out.println("");
                
            }

            while (it.hasNext()) {
                System.out.println(it.next() + ": You beat me fair and square.");
                break;
            }
        }
    }  
       
	/**
	 * Race Finished method to stop rest all runners and display winner.
	 * @param winner - Thread class object
	 * @param winnerName - String takes winner's name
	 */
	public  synchronized void finished(Thread winner, String winnerName) {      
        for(int i = 0; i < participants.size(); i++ )
        {
            if(winnerName.equals(participants.get(i).getRunnersName())){
            	System.out.println(winnerName + ": I finished!\n");
                System.out.println("The race is over! The " + winnerName + " is the winner" + "\n");
            }          	            
        }
        for(int i = 0; i < participants.size(); i++ )
        {
            if(!winnerName.equals(participants.get(i).getRunnersName()))
            	participants.get(i).interrupt();
        }
        for(int i = 0; i < participants.size(); i++ )
        {
            if(!winnerName.equals(participants.get(i).getRunnersName()))
                System.out.println(participants.get(i).getRunnersName() + ": You beat me fair and square." + "\n");           
        }
    }
       
	/**
	 * update method to keep on updating new location and display it
	 */
	@Override
    public void update(ThreadRunner t){
    	String name = t.getRunnersName();
        int location = t.getNewLocation();
        MainRunApp mApp = new MainRunApp();
        System.out.println(name + " : " + location);
        if( location >= 1000 )
        {       	
        	mApp.finished(Thread.currentThread(), name);
        }
    }

	
}
