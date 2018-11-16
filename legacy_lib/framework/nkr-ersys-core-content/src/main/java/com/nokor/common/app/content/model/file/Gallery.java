/**
 * 
 */
package com.nokor.common.app.content.model.file;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

/**
 * @author sok.pongsametrey
 *
 */
@Entity
@Table(name = "td_cms_gallery")
public class Gallery extends EntityA {

    /** */
    private static final long serialVersionUID = -392884815351887775L;

    private String title;
    private String desc;

    private List<FileData> files;

    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gal_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

 
    /**
     * @return the title
     */
    @Column(name = "gal_title", nullable = false)
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * @return the desc
     */
    @Column(name = "gal_desc")
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(final String desc) {
        this.desc = desc;
    }

    /**
     * @return the files
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "td_gallery_file", 
    		joinColumns = @JoinColumn(name = "gal_id"), 
    		inverseJoinColumns = @JoinColumn(name = "fil_id"))
    public List<FileData> getFiles() {
    	if (files == null) {
    		files = new ArrayList<FileData>();
		}
        return files;
    }

    /**
     * @param files the files to set
     */
    public void setFiles(final List<FileData> files) {
        this.files = files;
    }

    
	
}
