package com.guffly.course10;

public class Task {
    private int id;
    
    private String name;
    
    private int price;
    
    private Object date;

    /**
     * @return the id
     */
    public int getId() {
	return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
	this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the price
     */
    public int getPrice() {
	return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(int price) {
	this.price = price;
    }

    /**
     * @return the date
     */
    public Object getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Object date) {
        this.date = date;
    }

}
