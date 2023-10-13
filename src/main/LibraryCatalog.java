package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import data_structures.ArrayList;
import data_structures.DoublyLinkedList;
import data_structures.SinglyLinkedList;
import interfaces.FilterFunction;
import interfaces.List;
/**
 * This class represents a library catalog responsible for managing books and users efficiently. 
 * It utilizes ArrayLists to store book and user data. Thanks to this we have efficient access and usage.
 */
public class LibraryCatalog{
	
	List<Book> libraryBooks;
	List<User> users;
	
	public LibraryCatalog() throws IOException {
		libraryBooks = this.getBooksFromFiles();
		users = this.getUsersFromFiles();
	}
	/**
	 * Reads the book information from a CSV file and gets the list of books.
	 *
	 * @return A list of Book objects read from the CSV file.
	 * @throws IOException If an IO error occurs.
	 */
	private List<Book> getBooksFromFiles() throws IOException {
		List<Book> BooksFromFiles = new ArrayList<>();	// Using arrayList for efficiency and being able to check if the data added is correct handling objects is easier
		
		String pathBooks = "data/catalog.csv";		// Define the path to the CSV file containing book information
		BufferedReader BReader = new BufferedReader(new FileReader(pathBooks));		// Initialize a buffered reader to read from the file
		String currLine = BReader.readLine();		// Read the first line of the CSV file (header)
		boolean skipsFirstLineCsv = true;

		while(currLine != null) {		// Loop to read each line of the CSV file
			String[] bookInfo = currLine.split("\\s*,\\s*");
			if(!skipsFirstLineCsv) {		// Check if this is not the first line (skip the header line) (Because it gives an error if we don't check this)
				int id = Integer.parseInt(bookInfo[0]);
				String title = bookInfo[1];
				String author = bookInfo[2];
				String genre = bookInfo[3];

				LocalDate lastCheckOut = LocalDate.parse(bookInfo[4], DateTimeFormatter.ofPattern("yyyy-MM-d"));
				boolean checkedOut = Boolean.parseBoolean(bookInfo[5]);

				BooksFromFiles.add(new Book(id, title, author, genre, lastCheckOut, checkedOut));
			}
			skipsFirstLineCsv = false;		// Set skipFirstLine to false after processing the first non-header line
			currLine = BReader.readLine();		// Read the next line from the file
			
		}

		BReader.close();		// Close the buffered reader to avoid errors
		return BooksFromFiles;		// Return the list of books read from the file
	}
	/**
	 * Reads the user information from a CSV file and gets the list of users.
	 *
	 * @return A list of User objects read from the CSV file.
	 * @throws IOException If an IO error occurs.
	 */
	private List<User> getUsersFromFiles() throws IOException {
		List<User> UserFromFiles = new ArrayList<>();	// Using arrayList for efficiency and being able to check if the data added is correct handling objects is easier
		
		String pathUsers = "data/user.csv";
		BufferedReader UReader = new BufferedReader(new FileReader(pathUsers));
		String userLines = UReader.readLine();
		boolean skipFirstLine = true;

		while(userLines != null) {
			// Split the CSV line into an array of user information using comma as the delimiter.
			String[] userInfo = userLines.split(",");
			// Check if the user info contains 2 elements (id and name), and it's not the first line (skip the header line).
			if(userInfo.length == 2 && !skipFirstLine) {
				
				String id = userInfo[0].trim();
				String name = userInfo[1].trim();
				List<Book> checkedOutBooks = new SinglyLinkedList<Book>(); 
				// Create a new User object and add it to the list of users
				User user = new User(Integer.parseInt(id), name, checkedOutBooks);
				UserFromFiles.add(user);
			
			} 
			// Check if the user info contains 3 elements (id, name, and book IDs), and it's not the first line (skip the header line)
			else if(userInfo.length == 3 && !skipFirstLine) {
				// Parse user information from the CSV array
				String id = userInfo[0].trim();
				String name = userInfo[1].trim();
				String listIds = userInfo[2].replace("{", "").replace("}", "");
				String[] ids = listIds.split(" ");
				List<Book> checkedOutBooks = new SinglyLinkedList<Book>(); 
				// Iterate through book IDs and find corresponding books from libraryBooks
				for(String bookID: ids) {
					int numberID = Integer.parseInt(bookID);
					for(Book book: libraryBooks) {
						if(book.getId() == numberID) {
							checkedOutBooks.add(book);
						}
					}
				}
				
				// Create a new User object with checked-out books and add it to the list of users.
				User user = new User(Integer.parseInt(id), name, checkedOutBooks);
				UserFromFiles.add(user);
			}
			userLines = UReader.readLine();
			skipFirstLine = false;
		}
		UReader.close();
		return UserFromFiles;
	}
	public List<Book> getBookCatalog()    {
		return this.libraryBooks;		//returns the list of books
	}
	public List<User> getUsers()  {
		return this.users;		//returns the list of users
	}
	/**
	 * 
	 * // Create a new Book object with the parameters and publication date of September 15, 2023
	 * // Add the new Book to the library list
	 * 
	 */
	public void addBook(String title, String author, String genre)  {		
		int bookID = this.libraryBooks.size()+1;
		
		Book newBook = new Book(bookID, title, author, genre, LocalDate.of(2023, 9, 15), false);		
		
		this.libraryBooks.add(newBook);		
		
	}
	/**
	 * // Iterate through each book in the libraryBooks list
	 * 
	 * // Check if the current book's ID matches the provided ID
	 * //Removes book from list after checking if Id's matched
	 */
	public void removeBook(int id) {
		
		for(Book book: libraryBooks) {		
			if(book.getId() == id) {		
				this.libraryBooks.remove(book);		
			}
		}
		
	}
	/**
	 * // Check if the book ID matches and the book is not already checked out
	 * 
	 * // Return false if the book is not found or if it's already checked out
	 * 
	 */
	public boolean checkOutBook(int id)  {
		
		for(Book book: libraryBooks) {
			if(book.getId() == id && !book.isCheckedOut()) {		
				book.setCheckedOut(true);
				 book.setLastCheckOut(LocalDate.of(2023, 9, 15));
				 return true;
			}
		}
		return false;		
	}
	/*
	 * If the book is CheckedOut I set it to false so it basically gets returned or marked as not checkedOut
	 * We return true so it confirms that it got returned successfully 
	 * Also return false if the book was never checkedOut
	 */
	public boolean returnBook(int id) {
		for(Book book: libraryBooks) {
			if(book.getId() == id && book.isCheckedOut()) {
				book.setCheckedOut(false);
				 return true;
			}
		}
		return false;
	}
		
	/*
	 * Checks by id if the book is checkedOut
	 * Return true if the book is available (not checked out)
	 */
	public boolean getBookAvailability(int id) {
		for (Book book : libraryBooks) {
		     if (book.getId() == id) {
		            return !book.isCheckedOut();  
		        }
		    }
		    return false;
	}
	/*
	 * We iterate and compare titles to determine if it should count it
	 * If it matches the given title then we count it and return the count of books
	 */
	public int bookCount(String title) {
		int count = 0;
		for(Book book: libraryBooks) {
			if(book.getTitle().equals(title)) { 
				count++;
			}
			
		}
		return count; //returns the amount of books
		
	}
	public void generateReport() throws IOException {
		
		String output = "\t\t\t\tREPORT\n\n";
		output += "\t\tSUMMARY OF BOOKS\n";
		output += "GENRE\t\t\t\t\t\tAMOUNT\n";
		/*
		 * In this section you will print the amount of books per category.
		 * 
		 * Place in each parenthesis the specified count. 
		 * 
		 * Note this is NOT a fixed number, you have to calculate it because depending on the 
		 * input data we use the numbers will differ.
		 * 
		 * How you do the count is up to you. You can make a method, use the searchForBooks()
		 * function or just do the count right here.
		 */
		/**
		 * //each loop gets each book of each genre and returns a count of the amount of books of that genre
		 * 
		 */
		
		int countAdv = 0;
		for(Book books: libraryBooks) {
			if(books.getGenre().equals("Adventure")) {
				countAdv++;
			}
		}
		int countFi = 0;
		for(Book books: libraryBooks) {
			if(books.getGenre().equals("Fiction")) {
				countFi++;
			}
		}
		int countCl = 0;
		for(Book books: libraryBooks) {
			if(books.getGenre().equals("Classics")) {
				countCl++;
			}
		}
		int countMys = 0;
		for(Book books: libraryBooks) {
			if(books.getGenre().equals("Mystery")) {
				countMys++;
			}
		}
		int countSF = 0;
		for(Book books: libraryBooks) {
			if(books.getGenre().equals("Science Fiction")) {
				countSF++;
			}
		}
		
		
		output += "Adventure\t\t\t\t\t" + countAdv + "\n";
		output += "Fiction\t\t\t\t\t\t" + countFi + "\n";
		output += "Classics\t\t\t\t\t" + countCl + "\n";
		output += "Mystery\t\t\t\t\t\t" + countMys + "\n";
		output += "Science Fiction\t\t\t\t\t" + countSF + "\n";
		output += "====================================================\n";
		output += "\t\t\tTOTAL AMOUNT OF BOOKS\t" + libraryBooks.size() + "\n\n";
		
		/*
		 * This part prints the books that are currently checked out
		 */
		output += "\t\t\tBOOKS CURRENTLY CHECKED OUT\n\n";
		/*
		 * Here you will print each individual book that is checked out.
		 * 
		 * Remember that the book has a toString() method. 
		 * Notice if it was implemented correctly it should print the books in the 
		 * expected format.
		 * 
		 * PLACE CODE HERE
		 */

		/*
		 *  Check if the book is checked out
		 *  Append the book information to the output and increment the counter
		 */
		int counter = 0;
		for(Book books: libraryBooks) { 
			
			if(books.isCheckedOut()) {		
				
				output+= books + "\n";      
				counter++;
				
			}
			
		}
		
		
		
		
		
		output += "====================================================\n";
		output += "\t\t\tTOTAL AMOUNT OF BOOKS\t" + counter  + "\n\n";
		
		
		/*
		 * Here we will print the users the owe money.
		 */
		output += "\n\n\t\tUSERS THAT OWE BOOK FEES\n\n";
		/*
		 * Here you will print all the users that owe money.
		 * The amount will be calculating taking into account 
		 * all the books that have late fees.
		 * 
		 * For example if user Jane Doe has 3 books and 2 of them have late fees.
		 * Say book 1 has $10 in fees and book 2 has $78 in fees.
		 * 
		 * You would print: Jane Doe\t\t\t\t\t$88.00
		 * 
		 * Notice that we place 5 tabs between the name and fee and 
		 * the fee should have 2 decimal places.
		 * 
		 * PLACE CODE HERE!
		 */
		
		/*
		 *  Calculate the fee for each book and add up the total fee
		 *  Add the total due based on the fees for the user checked-out books
		 *  Format the fee to two decimal places
		 *  Add the user's name and fee to the output with two decimal places
		 */
		float totalDue = 0;
		for (User user : users) {
		    float fee = 0;
		    if (user.getCheckedOutList().size() > 0) {
		        List<Book> userBooks = user.getCheckedOutList();
		        for (Book book : userBooks) {
		            fee += book.calculateFees();  
		        }
		        totalDue += fee;  

		        
		        String formattedFee = String.format("%.2f", fee);

		        
		        output += user.getName() + "\t\t\t\t\t" + "$" + formattedFee + "\n";
		    }
		}
		 // Format the totalDue to two decimal places
		String formattedTotalDue = String.format("%.2f", totalDue);
		output += "====================================================\n";
		output += "\t\t\t\tTOTAL DUE\t$" +  formattedTotalDue + "\n\n\n";
		output += "\n\n";
		System.out.println(output);// You can use this for testing to see if the report is as expected.
		
		/*
		 * Here we will write to the file.
		 * 
		 * The variable output has all the content we need to write to the report file.
		 * 
		 * PLACE CODE HERE!!
		 */
	
	
	/*
	 * THIS IS THE REPORT WRITER 
	 */
	try {
		String pathN = "report.txt";
		File reportF = new File("./report/"+pathN);
		
		//Use this below instead of a reader to actually write the file
		FileWriter fileWriter = new FileWriter(reportF);
		BufferedWriter bufferedReportWriter = new BufferedWriter(fileWriter);
		//This writes the report on the text file
		bufferedReportWriter.write(output);
		//Closes the writer to avoid errors
		bufferedReportWriter.close();
		
		
	
	}catch (IOException e) {
    e.printStackTrace();
}
	}

	
	/*
	 * BONUS Methods
	 * 
	 * You are not required to implement these, but they can be useful for
	 * other parts of the project.
	 */
	/*
	 * We iterate through the list of books and use the filter
	 * If we get true for the filter applied then we go ahead and add the book to the list
	 * Should get a list of filtered books 
	 */
	public List<Book> searchForBook(FilterFunction<Book> func) {
		  List<Book> filteredBooks = new ArrayList<>();
	        for (Book book : libraryBooks) {
	            if (func.filter(book)) {
	                filteredBooks.add(book);
	            }
	        }
	        return filteredBooks;
	    }

	
	/*
	 * Applied the same as above
	 * We iterate through the list of users and use the filter
	 * If we get true for the filter applied then we go ahead and add the user to the list
	 * Should get a list of filtered users 
	 */
	public List<User> searchForUsers(FilterFunction<User> func) {
		 List<User> filteredUsers = new ArrayList<>();
	        for (User user : users) {
	            if (func.filter(user)) {
	                filteredUsers.add(user);
	            }
	        }
	        return filteredUsers;
	    }
	
		
	 
	
	    }
	 


	        
	         
	       
	    
	




	

