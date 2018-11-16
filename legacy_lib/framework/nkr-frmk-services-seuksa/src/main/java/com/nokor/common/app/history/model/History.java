package com.nokor.common.app.history.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;



/**
 * History table
 * 
 * @author prasnar
 */
@Entity
@Table(name = "td_history")
public class History extends BaseHistoryItem {
    /** */
	private static final long serialVersionUID = -1506648432306545432L;

	/**
     * 
     * @return
     */
    public static History createInstance() {
    	History his = EntityFactory.createInstance(History.class);
        return his;
    }
    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "his_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	
    
}