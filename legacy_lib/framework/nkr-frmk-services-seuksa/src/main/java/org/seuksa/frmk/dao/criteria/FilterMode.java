package org.seuksa.frmk.dao.criteria;


/**
 * 
 * @author prasnar
 *
 */
public class FilterMode {
	protected int value;
    public static final FilterMode EQUALS = new FilterMode(0);
    public static final FilterMode GREATER_THAN = new FilterMode(1);
    public static final FilterMode GREATER_OR_EQUALS = new FilterMode(2);
    public static final FilterMode LESS_THAN = new FilterMode(3);
    public static final FilterMode LESS_OR_EQUALS = new FilterMode(4);
    public static final FilterMode BETWEEN = new FilterMode(5);
    public static final FilterMode NULL = new FilterMode(6);
    public static final FilterMode BLANK = new FilterMode(7);
    public static final FilterMode EMPTY = new FilterMode(8);
    public static final FilterMode NOT_EMPTY = new FilterMode(9);
    public static final FilterMode IN = new FilterMode(10);

    /**
     * 
     * @param clazz
     */
    public FilterMode() {
    	
    }
    /**
     * 
     * @param value
     */
    protected FilterMode(int value) {
		this.value = value;
	}
    
    /**
     * 
     * @return
     */
	public FilterMode getDefaultFilterMode() {
		return EQUALS;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	
}
