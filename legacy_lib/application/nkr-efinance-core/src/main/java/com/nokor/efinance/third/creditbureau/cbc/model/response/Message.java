package com.nokor.efinance.third.creditbureau.cbc.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author sun.vanndy
 *
 */
public class Message implements Serializable{
	
	private static final long serialVersionUID = -8286531022947383755L;
	
	private List<Item> items;
	private Parser parser;
		
	/**
	 * @return the items
	 */
	public List<Item> getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(List<Item> items) {
		this.items = items;
	}
	/**
	 * @return the parser
	 */
	public Parser getParser() {
		return parser;
	}
	/**
	 * @param parser the parser to set
	 */
	public void setParser(Parser parser) {
		this.parser = parser;
	}
}
