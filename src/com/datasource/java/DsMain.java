package com.datasource.java;

import java.util.ArrayList;
import com.main.java.ThreadRunner;

/**
 * 
 * @author Priyanka Kulkarni
 * DsMain is a main interface of data sources
 */

public interface DsMain {
	/**
	 * Declaring String message and abstract method to get information about runners
	 */
	public static final String START_MESSAGE = "\nGet set...Go!\n";
	public abstract ArrayList<ThreadRunner> getRunners();
}
