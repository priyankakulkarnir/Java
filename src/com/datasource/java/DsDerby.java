package com.datasource.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.main.java.ThreadRunner;

/**
 * 
 * @author Priyanka Kulkarni
 * DsDerby class is to read data from Derby DataBase
 * This creates ThreadRunner object for each runner in database
 */

public class DsDerby implements DsMain{

	/**
	 * This method is to set derby connection
	 * @return connection
	 */
    private Connection getConnection() {
        Connection connection = null;
        try {
            String dbDirectory = "Resources";
            System.setProperty("derby.system.home", dbDirectory);

            String url = "jdbc:derby:FinalsDB";
            String user = "";
            String password = "";
            connection = DriverManager.getConnection(url, user, password);
        }
        catch(SQLException e)
        {
            System.err.println("Error loading database driver: " + e);
            System.exit(1);
        }
        return connection;
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
        String query = "SELECT * FROM Runners";
	    try (Connection connection = getConnection();
	         PreparedStatement ps = connection.prepareStatement(query);
	         ResultSet rs = ps.executeQuery())                
	    {
	    	while(rs.next()) {
	    		String name = rs.getString("Name");
	            int runnersSpeed = rs.getInt("RunnersSpeed");
	            int restPercentage = rs.getInt("RestPercentage");
	            ThreadRunner runner;
				try {
					runner = new ThreadRunner(name, runnersSpeed, restPercentage);
					runners.add(runner);
				} catch (Exception e) {
					e.printStackTrace();
				}
	            
	        }
	    	disconnect();
	        return runners;
	    } 
	    catch(SQLException sqle) {
	    	 System.out.println("Some error has occured while reading the data from database.\n Please check!!!!!");
	    	 disconnect();
	    	 return null;
	    }
    }
	
	/**
	 * disconnect method disconnects derby database connection
	 */
	public void disconnect() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
                if (!e.getMessage().equals("Derby system shutdown.")) {
                    System.out.println("Database did not get disconnected.");
                }
        }
	}

}
