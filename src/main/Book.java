package main;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
/**
 * The Book class represents Book object from a library that has parameters like: title, author, genre, lastCheckout, checkout and id.
 */
public class Book {
	
	
	private String title;
	private String author;
	private String genre;
	private LocalDate lastCheckOut;
	private Boolean checkOut;
	private int id;
	
	/**
	 * This constructor is used to build a book with (integer id, String title, String author, String genre, LocalDate lastCheckOut, Boolean checkOut) as parameters.
	 */
	public Book(int id, String title, String author, String genre, LocalDate lastCheckOut, Boolean checkOut) {
        this.id = id;
		this.title = title;
        this.author = author;
        this.genre = genre;
        this.lastCheckOut = lastCheckOut;
        this.checkOut = checkOut;
    }
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return this.author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getGenre() {
		return this.genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public LocalDate getLastCheckOut() {
		return this.lastCheckOut;
	}
	public void setLastCheckOut(LocalDate lastCheckOut) {
		this.lastCheckOut = lastCheckOut;
	}
	public boolean isCheckedOut() {
		return this.checkOut;
	}
	public void setCheckedOut(boolean checkedOut) {
		 this.checkOut = checkedOut;
	}
	/**
     * This toString method basically takes a string and outputs it in the following format below:
     * {TITLE} BY {AUTHOR}.
     */
	@Override
	public String toString() {
		/*
		 * This is supposed to follow the format
		 * 
		 * {TITLE} By {AUTHOR}
		 * 
		 * Both the title and author are in uppercase.
		 */
		String capitalizedTitle = title.toUpperCase();
		String capitalizedAuthor = author.toUpperCase();
		
		return capitalizedTitle + " BY " + capitalizedAuthor;
		// Return the capitalized title and author in the specified format
	}
	/**
     * This calculateFees method makes a calculation based on how much time a user has had a book for
     * If the user has the book for 31 days then the base fee is $10
     * More than 31 days means that this calculation will have to be applied: (10 + (1.5 * (days - 31.0) 
     * @return Returns The calculated late fee for the book.
     */
	public float calculateFees() {

		float fee = 0;	
		if(this.isCheckedOut()) {		// Check if the book is checked out
			
			float days = ChronoUnit.DAYS.between(this.lastCheckOut, LocalDate.of(2023, 9, 15));		 // Calculate the number of days the book has been checked out
				
			if(days == 31) {
				fee = 10;  
			} else if(days > 31) {
				fee =  (float) (10 + (1.5 * (days - 31.0)));
			}
		}
			
		return fee;		 // Return the calculated fee for the checked-out book
	}
}
