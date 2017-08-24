package com.libertymutual.todolist.services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import com.libertymutual.todolist.models.ToDoItem;

@Service
public class ToDoItemRepository {

	//creating new variable instances to use throughout the class
	private int nextId = 1;
	private ArrayList<ToDoItem> itemList;

	/**
	 * Get all the items from the file.
	 * 
	 * @return A list of the items. If no items exist, returns an empty list.
	 */

	public List<ToDoItem> getAll() {

		//creates the file reader and CSV parser to be used within the getAll() method
		try (FileReader reader = new FileReader("list.csv");
				CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180);) {

			//creating a list of CSV records and grabbing each and setting a variable to check on the existing ID
			List<CSVRecord> record = CSVFormat.DEFAULT.parse(reader).getRecords();
			itemList = new ArrayList<ToDoItem>();
			int existingID = 0;

			//for each CSV record, create a new to do item, set the ID, text, and complete values, and add them to the list
			for (CSVRecord current : record) {
				ToDoItem item = new ToDoItem();
				item.setId(Integer.parseInt(current.get(0)));
				item.setText(current.get(1));
				item.setComplete(Boolean.parseBoolean(current.get(2)));
				itemList.add(item);
				
				//if the current ID at ID within the array list (denote by 0 index), set the existing ID to that current ID
				if (Integer.parseInt(current.get(0)) > existingID) {
					existingID = Integer.parseInt(current.get(0));
				}

				//otherwise set the current ID to the existing ID + 1
				nextId = existingID + 1;
			}
			
		} catch (IOException ioe) {
			System.out.println("Could not read the record in the file.");
		}

		//return an empty list if there are no values in it
		if (itemList.size() == 0) {
			return Collections.emptyList();
		}

		return itemList;

	}

	/**
	 * Assigns a new id to the ToDoItem and saves it to the file.
	 * 
	 * @param item
	 *            The to-do item to save to the file.
	 */
	public void create(ToDoItem item) {

		//setting the item ID to next ID and incrementing the value by 1
		item.setId(nextId);
		nextId += 1;

		//creates the file reader and CSV parser to be used within the create() method
		try (FileWriter writer = new FileWriter("list.csv", true);
				CSVPrinter printer = CSVFormat.DEFAULT.print(writer)) {

			//creates the String array that houses the array list items
			String[] record = { Integer.toString(item.getId()), item.getText(), Boolean.toString(item.isComplete()) };

			// writes the record to the file
			printer.printRecord(record);

		} catch (IOException e) {
			System.out.println("No.");

		}
	}

	/**
	 * Gets a specific ToDoItem by its id.
	 * 
	 * @param id
	 *            The id of the ToDoItem.
	 * @return The ToDoItem with the specified id or null if none is found.
	 */
	public ToDoItem getById(int id) {
	
		//for every item in the list, get the id at index i, if the current ID equals the param id, return the current ID 
		for (int i = 0; i < itemList.size(); i += 1) {
			ToDoItem current = itemList.get(i);
			if (current.getId() == id) {
				return current;
			}
		}

		return null;
	}

	/**
	 * Updates the given to-do item in the file.
	 * 
	 * @param item
	 *            The item to update.
	 */
	public void update(ToDoItem item) {

		//for every item in the list, get the id at index i 
		for (int i = 0; i < itemList.size(); i += 1) {
			ToDoItem current = itemList.get(i);

			//if the current ID equals the item id, make setComplete true
			if (current.getId() == item.getId()) {

				current.setComplete(true);
			}
		}
			try (FileWriter writer = new FileWriter("list.csv"); CSVPrinter printer = CSVFormat.DEFAULT.print(writer)) {

				//for all the items in the to do list, print the current values
				for (int j = 0; j < itemList.size(); j += 1) {
					ToDoItem current2 = itemList.get(j);
					String[] record = { Integer.toString(current2.getId()), current2.getText(),Boolean.toString(current2.isComplete()) };
					printer.printRecord(record);
				}

			} catch (IOException e) {
				System.out.println("No.");

			}

		}

}
