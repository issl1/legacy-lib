package com.nokor.efinance.core.quotation.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.applicant.model.ECommentType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.document.model.DocumentUwGroup;
import com.nokor.frmk.security.model.SecUser;

/**
 * Comment class
 * @author ly.youhort
 */
@Entity
@Table(name = "td_comment")
public class Comment extends EntityA {

	private static final long serialVersionUID = 8935751397622214350L;
	
	private Quotation quotation;
	private Contract contract;
	private String desc;
	private boolean onlyForUW;
	private boolean forManager;
	private SecUser user;
	private DocumentUwGroup documentUwGroup;
	private ECommentType commentType;
	
	/**
     * 
     * @return
     */
    public static Comment createInstance() {
    	Comment instance = EntityFactory.createInstance(Comment.class);
        return instance;
    }
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "com_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the quotation
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quo_id")
	public Quotation getQuotation() {
		return quotation;
	}

	/**
	 * @param quotation the quotation to set
	 */
	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}	
	
	/**
	 * @return the contract
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "con_id")
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}

	/**
	 * @return the desc
	 */
	@Column(name = "com_va_desc", nullable = true, length = 1000)
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the onlyForUW
	 */
	@Column(name = "com_bl_only_for_uw", nullable = true)
	public boolean isOnlyForUW() {
		return onlyForUW;
	}

	/**
	 * @param onlyForUW the onlyForUW to set
	 */
	public void setOnlyForUW(boolean onlyForUW) {
		this.onlyForUW = onlyForUW;
	}

	/**
	 * @return the forManager
	 */
	@Column(name = "com_bl_for_ma", nullable = true)
	public boolean isForManager() {
		return forManager;
	}

	/**
	 * @param forManager the forManager to set
	 */
	public void setForManager(boolean forManager) {
		this.forManager = forManager;
	}

	/**
	 * @return the user
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id")
	public SecUser getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(SecUser user) {
		this.user = user;
	}

	/**
	 * @return the documentUwGroup
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_uw_grp_id")
	public DocumentUwGroup getDocumentUwGroup() {
		return documentUwGroup;
	}

	/**
	 * @param documentUwGroup the documentUwGroup to set
	 */
	public void setDocumentUwGroup(DocumentUwGroup documentUwGroup) {
		this.documentUwGroup = documentUwGroup;
	}
	
	/**
	 * @return the commentType
	 */
	@Column(name = "com_typ_id", nullable = true)
    @Convert(converter = ECommentType.class)
	public ECommentType getCommentType() {
		return commentType;
	}

	/**
	 * @param commentType the commentType to set
	 */
	public void setCommentType(ECommentType commentType) {
		this.commentType = commentType;
	}
}
