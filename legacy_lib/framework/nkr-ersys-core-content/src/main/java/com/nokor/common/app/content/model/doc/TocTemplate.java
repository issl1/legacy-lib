package com.nokor.common.app.content.model.doc;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

@Entity
@Table(name="tu_cms_toc_template")
public class TocTemplate extends EntityRefA {
	/** */
	private static final long serialVersionUID = -5191391541109087514L;
	
	private List<TocItem> tocItems; 
	
	public final static long DEFAULT = 1L;

	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "toc_tem_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "toc_tem_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "toc_tem_desc", nullable = false, length = 255)
	@Override
    public String getDesc() {
        return desc;
    }

	/**
	 * @return the tocItems
	 */
	@OneToMany(mappedBy = "tocTemplate", fetch = FetchType.LAZY)
	public List<TocItem> getTocItems() {
		return tocItems;
	}

	/**
	 * @param tocItems the tocItems to set
	 */
	public void setTocItems(List<TocItem> tocItems) {
		this.tocItems = tocItems;
	}

}
