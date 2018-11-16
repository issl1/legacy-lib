package com.nokor.common.app.content.model.file;

import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.common.app.content.model.eref.EFileType;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_cms_file")
public class FileData extends EntityA  implements MFileData {

    /** */
    private static final long serialVersionUID = 8204256418967993469L;

    private EFileType type;
    private String name; // abc
    private String desc;
    private Long size; // Size of file

    private String url; // /employee/abc.png
    private String link; // Link to other website
    private String caption;
    private String alt; // Default is fileName
    private String mimeType;
    
    /**
     * 
     * @return
     */
    public static FileData createInstance() {
        FileData fileData = EntityFactory.createInstance(FileData.class);
        return fileData;
    }
    
    /**
     * 
     * @return
     */
    public static FileData createInstance(String fileUrl, EFileType type) {
        FileData fileData = EntityFactory.createInstance(FileData.class);
        fileData.setUrl(fileUrl);
        fileData.setType(type);
        return fileData;
    }
    
    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fil_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    
    /**
	 * @return the type
	 */
    @Column(name = "typ_fil_id", nullable = true)
    @Convert(converter = EFileType.class)
	public EFileType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EFileType type) {
		this.type = type;
	}

	/**
     * @return the filename
     */
    @Column(name = "fil_name")
    public String getName() {
        return name;
    }

    /**
     * @param name the filename to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the fileUrl
     */
    @Column(name = "fil_url")
    public String getUrl() {
        return url;
    }

    /**
     * @param fileUrl the fileUrl to set
     */
   
    public void setUrl(final String fileUrl) {
        this.url = fileUrl;
    }

    /**
     * @return the desc
     */
    @Column(name = "fil_desc")
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
     * @return the caption
     */
    @Column(name = "fil_caption")
    public String getCaption() {
        return caption;
    }

    /**
     * @param caption the caption to set
     */
    public void setCaption(final String caption) {
        this.caption = caption;
    }

    /**
     * @return the alt
     */
    @Column(name = "fil_alt")
    public String getAlt() {
        return alt;
    }

    /**
     * @param alt the alt to set
     */
    public void setAlt(final String alt) {
        this.alt = alt;
    }

    @Column(name = "fil_size")
    public Long getSize() {
        return size;
    }

    public void setSize(final Long fileSize) {
        this.size = fileSize;
    }
    
	/**
	 * @return the fileLink
	 */
    @Column(name = "fil_link")
	public String getLink() {
		return link;
	}
	/**
	 * @param fileLink the fileLink to set
	 */
	public void setLink(String fileLink) {
		this.link = fileLink;
	}


	
	/**
	 * @return the mimeType
	 */
    @Column(name = "fil_mime_type")
	public String getMimeType() {
		return mimeType;
	}
	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	/**
	 * @return the fileSizeKB
	 */
	@Transient
	public String getSizeKB() {
		if (getSize() != null) {
			DecimalFormat format = new DecimalFormat("#,##0.00");
			return format.format(getSize() / 1024D) + " KB";
		}
		return "N/A";
	}
	
    
    @Transient
    public boolean isVideo() {
    	return  EFileType.VIDEO.equals(type.getId());
    }
    
    

}
