package com.datasource.java;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.main.java.ThreadRunner;
import com.util.java.Validator;

/**
 * @author Priyanka Kulkarni DsXML class is to read runners data from xml source
 *         file This creates ThreadRunner object for each runner in database
 */

public class DsXML implements DsMain {

	/**
	 * Declaring document object
	 */
	private Document document = null;

	/**
	 * Constructor of DsXML to check file is available or not
	 * @throws java.lang.Exception - For IO,SAX,Parser exceptions
	 */
	public DsXML() throws Exception {
		//Ask user to enter name of file
		String filename = Validator.getRequiredFileName("Enter XML file name: ");
		try {
			// Checks file name entered by user
			if (filename.equals("FinalXMLData.xml")) {
				System.out.println(START_MESSAGE);
				// assigning value to document object
				document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filename));
			} else {
				System.out.println("'" +filename+ "'" +" is invalid Filename\n(Please check file name.It is case sensitive.)\n");
			}
		} catch (SAXException e) {
			throw new Exception();
		} catch (IOException e) {
			System.out.println("File: " + filename + " is not found.");
			System.out.println("");
			throw new Exception();
		} catch (ParserConfigurationException e) {
			throw new Exception();
		}
	}

	/**
	 * Override getRunner method from DsMain the interface. This method reads
	 * the data from the derby database creates ThreadRunner objects for each
	 * runner in database
	 * @return Arraylist of type ThreadRunner
	 */
	@Override
	public ArrayList<ThreadRunner> getRunners() {
		// creating ArrayList of ThreadRunners
		ArrayList<ThreadRunner> runnersArrayList = null;
		// retrieving nodes from xml file
		if (document.hasChildNodes()) {
			Node child = document.getFirstChild();

			while (child != null) {
				runnersArrayList = findRunnersNode(child);
				child = child.getNextSibling();
			}
		}
		return runnersArrayList;
	}

	/**
	 * Look for a Runners node
	 * @param child is a child node.
	 * @return Arraylist of type ThreadRunner
	 */
	private ArrayList<ThreadRunner> findRunnersNode(Node child) {
		ArrayList<ThreadRunner> runnersArrayList = null;

		if (child.getNodeName().equals("Runners")) {
			runnersArrayList = traceRunnersNode(child);
		}

		return runnersArrayList;
	}

	/**
	 * Look for some Runner nodes which Runners node contains
	 * @param child is a Runners node
	 * @return Arraylist of type ThreadRunner
	 */
	private ArrayList<ThreadRunner> traceRunnersNode(Node child) {
		ArrayList<ThreadRunner> runnersArrayList = new ArrayList<ThreadRunner>();

		NodeList runners = child.getChildNodes();
		for (int i = 0; i < runners.getLength(); i++) {
			Node eachRunner = runners.item(i);
			if (eachRunner.getNodeName().equals("Runner")) {
				ThreadRunner tr = traceEachRunnerNode(eachRunner);
				if (tr != null) {
					runnersArrayList.add(tr);
				}
			}
		}
		return runnersArrayList;
	}

	/**
	 * Creates a ThreadRunner instance from a Runner node.
	 * @param eachRunner is a Runner node.
	 * @return Arraylist of type ThreadRunner
	 */
	private ThreadRunner traceEachRunnerNode(Node eachRunner) {
		String name;
		String runnersSpeed;
		String restPercentage;

		name = getName(eachRunner);
		if (name == null)
			return null;

		runnersSpeed = getChild(eachRunner, "RunnersMoveIncrement");
		if (runnersSpeed == null)
			return null;

		restPercentage = getChild(eachRunner, "RestPercentage");
		if (restPercentage == null)
			return null;

		return createThreadRunner(name, runnersSpeed, restPercentage);
	}

	/**
	 * Gets a node value from a node named "Name".
	 * @param eachRunner is a Runner node.
	 * @return Runner name.
	 */
	private String getName(Node eachRunner) {
		String runnerName = null;
		NamedNodeMap attrs = eachRunner.getAttributes();
		if (attrs != null) {
			Node name = attrs.getNamedItem("Name");
			runnerName = name.getNodeValue();
		}
		return runnerName;
	}

	/**
	 * Looks for nodeName node and returns textContent of the node
	 * @param eachRunner is a Runner node d
	 * @param nodeName is a child node
	 * @return textContent.
	 */
	private String getChild(Node eachRunner, String nodeName) {
		NodeList parameters = eachRunner.getChildNodes();

		for (int i = 0; i < parameters.getLength(); i++) {
			Node n = parameters.item(i);
			if (n.getNodeName().equals(nodeName)) {
				return n.getTextContent();
			}
		}

		return null;
	}

	/**
	 * Creates a ThreadRunner instance
	 * @param name is a name of the runner.
	 * @param runnersSpeed is speed of the runner.
	 * @param restPercentage is a value how often the runner rests.
	 * @return ThreadRunner instance.
	 */
	private ThreadRunner createThreadRunner(String name, String runnersSpeed, String restPercentage) {
		try {
			int speed = Integer.parseInt(runnersSpeed);
			int rest = Integer.parseInt(restPercentage);

			ThreadRunner tr;
			try {
				tr = new ThreadRunner(name, rest, speed);
				return tr;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.err.println("Either " + runnersSpeed + " or " + restPercentage + " is not a number.");
				return null;
			}
		} catch (NumberFormatException ignored) {
			System.err.println("Either " + runnersSpeed + " or " + restPercentage + " is not a number.");
			return null;
		}
	}

}
