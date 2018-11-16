package com.nokor.common.app.content.model.article;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.content.model.base.AbstractContent;
import com.nokor.common.app.content.model.eref.EContentCategoryDefault;
import com.nokor.common.app.content.model.eref.EDependencyType;
import com.nokor.common.app.content.model.eref.EFileType;
import com.nokor.common.app.content.model.file.FileData;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_cms_article")
public class Article extends AbstractContent {

    /** */
    private static final long serialVersionUID = 5239835904580629183L;

    private Boolean isRestrict;
    private Boolean isIsolated;
    private String keyIsolated;
    
    private String url;

	private List<ArtContributor> contributors;
	private List<ArtClassification> classifications;
    private List<ArtAttachment> attachments;
	private List<ArtDependency> dependencies;

    private Integer sortIndex;

    /**
     * 
     */
    public static Article createInstance() {
    	Article art = EntityFactory.createInstance(Article.class);
    	art.initDefault();
    	art.setCategory(EContentCategoryDefault.DEFAULT.getEntity());
    	
    	return art;
    }
    
    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "art_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    @Transient
    public Date getDate() {
    	return getContentDate();
    }
    
    /**
     * 
     * @param date
     */
	public void setDate(Date date) {
		setContentDate(date);
	}
    
    /**
     * @return the url
     */
    @Column(name = "art_url", nullable = true)
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(final String url) {
        this.url = url;
    }


    @Transient
	public Boolean getIsPrivate() {
		return !getIsPublic();
	}

 	/**
	 * @param isPrivate the isPrivate to set
	 */
	public void setIsPrivate(Boolean isPrivate) {
		setIsPublic(!isPrivate);
	}


    /**
     * @return the sortIndex
     */
    @Column(name = "sort_index", nullable = true)
    public Integer getSortIndex() {
        return sortIndex;
    }

    /**
     * @param sortIndex the sortIndex to set
     */
    public void setSortIndex(final Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

	/**
	 * @return the contributors
	 */
	@OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
	public List<ArtContributor> getContributors() {
		if (contributors == null) {
			contributors = new ArrayList<>();
		}
		return contributors;
	}

	/**
	 * @param contributors the contributors to set
	 */
	public void setContributors(List<ArtContributor> contributors) {
		this.contributors = contributors;
	}

	/**
	 * @return the classifications
	 */
	@OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
	public List<ArtClassification> getClassifications() {
		if (classifications == null) {
			classifications = new ArrayList<>();
		}
		return classifications;
	}

	/**
	 * @param classifications the classifications to set
	 */
	public void setClassifications(List<ArtClassification> classifications) {
		this.classifications = classifications;
	}

	/**
	 * @return the attachments
	 */
	@OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
	public List<ArtAttachment> getAttachments() {
		if (attachments == null) {
			attachments = new ArrayList<>();
		}
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(List<ArtAttachment> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the dependencies
	 */
	@OneToMany(mappedBy = "other", fetch = FetchType.LAZY)
	public List<ArtDependency> getDependencies() {
		if (dependencies == null) {
			dependencies = new ArrayList<>();
		}
		return dependencies;
	}

	/**
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(List<ArtDependency> dependencies) {
		this.dependencies = dependencies;
	}

	@Transient
	public List<ArtDependency> getDependencies(EDependencyType type) {
		List<ArtDependency> dependencies = new ArrayList<>();
		for (ArtDependency dep : getDependencies()) {
			if (dep.getType().getId().equals(type.getId())) {
				dependencies.add(dep);
			}
		}
		
		return dependencies;
	}

	@Transient
	public List<ArtDependency> getChildren() {
		return getDependencies(EDependencyType.CHILD_CONTENT);
	}
	
	/** -------------------------------------------------------------------
	 * Methods used in JSP page (Struts)
	 ** -------------------------------------------------------------------*/
 
	@Transient
	public boolean isVideoExists() {
		boolean isExists = false;
		for (ArtAttachment att : getAttachments()) {
			if (att.getFile() != null 
					&& att.getFile().isVideo()) {
				isExists = true;
			}
		}
		
		return isExists;
	}		
	
	/**
	 * Data : other type than Image => PDF, XLS, DOC...
	 * @return
	 */
	@Transient
	public boolean isFileDataExists() {
		boolean isExists = false;
		for (ArtAttachment att : getAttachments()) {
			if (att.getFile() != null 
					&& att.getFile().getType().getId() != EFileType.IMG.getId()
					&& StringUtils.isNotEmpty(att.getFile().getUrl())) {
				isExists = true;
			}
		}
		
		return isExists;
	}
	
	@Transient
	public boolean isFileImageExists() {
		boolean isExists = false;
		for (ArtAttachment att : getAttachments()) {
			if (att.getFile() != null 
					&& att.getFile().getType().equals(EFileType.IMG)
					&& StringUtils.isNotEmpty(att.getFile().getUrl())) {
				isExists = true;
			}
		}
		
		return isExists;
	}
	
	@Transient
	public FileData getDefaultImage() {
		FileData img = null;
		for (ArtAttachment att : getAttachments()) {
			if (att.getFile() != null 
					&& att.getFile().getType().equals(EFileType.IMG.getId())
					&& StringUtils.isNotEmpty(att.getFile().getUrl())
					&& img == null) {
				img = att.getFile();
			}
		}
		
		return img;
	}
	
	@Transient
	public FileData getDefaultFile() {
		FileData file = null;
		for (ArtAttachment att : getAttachments()) {
			if (att.getFile() != null 
					&& att.getFile().getType().getId() != EFileType.IMG.getId()
					&& StringUtils.isNotEmpty(att.getFile().getUrl())
					&& file == null) {
				file = att.getFile();
			}
		}
		
		return file;
	}
	

	@Transient
	public FileData getFile() {
		return getAttachments().get(0).getFile();
	}
	
	@Transient
	public void setFile(FileData file) {
		getAttachments().get(0).setFile(file);
	}

	
	@Transient
	public FileData[] getFiles() {
		FileData[] files = new FileData[getAttachments().size()];
		
		for (int i = 0;  i < getAttachments().size(); i++) {
			files[i] = getAttachments().get(i).getFile();
		}
		
		return files;
	}
	


	@Column(name = "art_is_restrict", length = 1)
	public Boolean getIsRestrict() {
		return isRestrict;
	}

	public void setIsRestrict(Boolean isRestrict) {
		this.isRestrict = isRestrict;
	}



	
	@Transient
	public boolean isRequiredLogin() {
		boolean isRequiredLogin = (getIsPrivate() != null && getIsPrivate()) 
				|| (getIsRestrict() != null && getIsRestrict());		
		return isRequiredLogin;
	}

	/**
	 * @return the isIsolated
	 */
	@Column(name = "art_is_isolated", length = 1)
	public Boolean getIsIsolated() {
		return isIsolated;
	}

	/**
	 * @param isIsolated the isIsolated to set
	 */
	public void setIsIsolated(Boolean isIsolated) {
		this.isIsolated = isIsolated;
	}

	/**
	 * @return the keyIsolated
	 */
	@Column(name = "art_key_isolated")
	public String getKeyIsolated() {
		return keyIsolated;
	}

	/**
	 * @param keyIsolated the keyIsolated to set
	 */
	public void setKeyIsolated(String keyIsolated) {
		this.keyIsolated = keyIsolated;
	}


}
