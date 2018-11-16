package com.nokor.common.app.content.model.doc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.content.model.base.AbstractContent;
import com.nokor.common.app.content.model.eref.EDocDependencyType;
import com.nokor.common.app.history.HistoryException;
import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.common.app.workflow.model.WkfHistoryItem;
import com.nokor.common.app.workflow.service.WkfHistoryItemRestriction;
import com.nokor.frmk.helper.SeuksaServicesHelper;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="td_cms_document")
public class DocContent extends AbstractContent implements MDocContent {
	/** */
	private static final long serialVersionUID = -6531914292980411551L;

	private List<Text> texts;
	private TocTemplate tocTemplate;

	private List<DocContributor> contributors;
	private List<DocClassification> classifications;
	private List<DocAttachment> attachments;
	private List<DocDependency> dependencies;
	
	
	
	/**
	 * 
	 * @return
	 */
	public static DocContent createInstance() {
		DocContent doc = EntityFactory.createInstance(DocContent.class);
		doc.initDefault();
        return doc;
    }
	

	
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "doc_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the texts
	 */
	@OneToMany(mappedBy = "document", fetch = FetchType.LAZY)
	public List<Text> getTexts() {
		if (texts == null) {
			texts = new ArrayList<>();
		}
		return texts;
	}

	/**
	 * @param texts the texts to set
	 */
	public void setTexts(List<Text> texts) {
		this.texts = texts;
	}
	
	
	/**
	 * @return the classifications
	 */
	@OneToMany(mappedBy = "document", fetch = FetchType.LAZY)
	public List<DocClassification> getClassifications() {
		if (classifications == null) {
			classifications = new ArrayList<>();
		}
		return classifications;
	}

	/**
	 * @param classifications the classifications to set
	 */
	public void setClassifications(List<DocClassification> classifications) {
		this.classifications = classifications;
	}

	/**
	 * @return the attachments
	 */
	@OneToMany(mappedBy = "document", fetch = FetchType.LAZY)
	public List<DocAttachment> getAttachments() {
		if (attachments == null) {
			attachments = new ArrayList<>();
		}
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(List<DocAttachment> attachments) {
		this.attachments = attachments;
	}


	/**
	 * @return the dependencies
	 */
	@OneToMany(mappedBy = "other", fetch = FetchType.LAZY)
	public List<DocDependency> getDependencies() {
		if (dependencies == null) {
			dependencies = new ArrayList<>();
		}
		return dependencies;
	}
	
	@Transient
	public List<DocDependency> getDependencies(EDocDependencyType type) {
		List<DocDependency> dependencies = new ArrayList<>();
		for (DocDependency dep : getDependencies()) {
			if (dep.getType().getId().equals(type.getId())) {
				dependencies.add(dep);
			}
		}
		
		return dependencies;
	}

	@Transient
	public List<DocDependency> getChildren() {
		return getDependencies(EDocDependencyType.CHILD_CONTENT);
	}
	
	@Transient
	public List<DocDependency> getReferences() {
		return getDependencies(EDocDependencyType.REFERENCE);
	}
	
	@Transient
	public List<DocDependency> getBibliographies() {
		return getDependencies(EDocDependencyType.BIBLIOGRAPHY);
	}
	
	@Transient
	public List<DocDependency> getAmendements() {
		return getDependencies(EDocDependencyType.AMENDEMENT);
	}
	
	
	/**
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(List<DocDependency> dependencies) {
		this.dependencies = dependencies;
	}

	/**
	 * @return the contributors
	 */
	@OneToMany(mappedBy = "document", fetch = FetchType.LAZY)
	public List<DocContributor> getContributors() {
		if (contributors == null) {
			contributors = new ArrayList<>();
		}
		return contributors;
	}

	/**
	 * @param contributors the contributors to set
	 */
	public void setContributors(List<DocContributor> contributors) {
		this.contributors = contributors;
	}

	/**
	 * @return the tocTemplate
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "toc_tem_id", nullable = true)
	public TocTemplate getTocTemplate() {
		return tocTemplate;
	}

	/**
	 * @param tocTemplate the tocTemplate to set
	 */
	public void setTocTemplate(TocTemplate tocTemplate) {
		this.tocTemplate = tocTemplate;
	}



	@Transient
	public List<DocContributor> getContributorsByType(Long conTypId) {
		List<DocContributor> authors = new ArrayList<DocContributor>();
        if (contributors != null) {
            for (DocContributor cont : contributors) {
            	if (cont.getType().getId().equals(conTypId)) {
                	authors.add(cont);
            	}
            }
        }
        return authors;
	}

	/**
	 * 
	 * @param conTypId
	 * @return
	 */
	@Transient
    public DocContributor getFirstContributorsByType(Long conTypId) {
    	List<DocContributor> authors = getContributorsByType(conTypId);
    	if (authors.size() > 0) {
    		return authors.get(0);
    	}
        return null;
    }
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transient
	public List<WkfHistoryItem> getLast10Histories() throws Exception {
		List<WkfHistoryItem> list = null;
		if (getId() != null) {

			WkfHistoryItemRestriction<WkfHistoryItem> restrictions = new WkfHistoryItemRestriction<WkfHistoryItem>(WkfHistoryItem.class);
			restrictions.setEntityId(getId());
			restrictions.setPropertyName(EntityWkf.WKFSTATUS);
			restrictions.setMaxResults(10);

			try {
				list = SeuksaServicesHelper.WKF_SRV.getHistories(restrictions);
			} catch (HistoryException e) {
				throw new HistoryException("getHistories", e);
			}
		}
		return list;
	}
	
	@Transient
	public Date getLastWkfHistoryDate() throws Exception {
		List<WkfHistoryItem> list = getLast10Histories();

		if (list != null && !list.isEmpty()) {
			return list.get(0).getCreateDate();
		}

		return null;
	}
}