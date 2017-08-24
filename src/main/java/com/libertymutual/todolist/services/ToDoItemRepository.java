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

	private int nextId = 1;
	private ArrayList<ToDoItem> itemList;

	/**
	 * Get all the items from the file.
	 * 
	 * @return A list of the items. If no items exist, returns an empty list.
	 */

public List<ToDoItem> getAll() {
		
		try (FileReader reader = new FileReader("list.csv"); CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180);) {
			
		List<CSVRecord> record = CSVFormat.DEFAULT.parse(reader).getRecords();
		itemList = new ArrayList<ToDoItem>();
		int existingID = 0;
		
			for (CSVRecord current: record) {
				ToDoItem item = new ToDoItem();
				item.setId(Integer.parseInt(current.get(0)));
				item.setText(current.get(1));
				item.setComplete(Boolean.parseBoolean(current.get(2)));
				itemList.add(item);
				if (Integer.parseInt(current.get(0)) > existingID) {
					existingID = Integer.parseInt(current.get(0));
				}
				
				nextId = existingID + 1;
			} 	
		}	catch (IOException ioe) {
			System.out.println("Could not read the record in the file.");
		}
		
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
		
		item.setId(nextId);
		nextId += 1;

		try (FileWriter writer = new FileWriter("list.csv", true);
				CSVPrinter printer = CSVFormat.DEFAULT.print(writer)) {

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
		
		if(itemList.size() > 0) {
			return itemList.get(id); 
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

		

	}

}