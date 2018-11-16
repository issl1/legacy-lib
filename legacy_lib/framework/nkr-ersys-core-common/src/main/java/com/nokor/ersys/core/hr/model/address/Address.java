package com.nokor.ersys.core.hr.model.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.ersys.core.hr.model.eref.ETypeAddress;

/**
 * @author ly.youhort
 */
@Entity
@Table(name="td_address",
	indexes = {
		@Index(name = "td_address_cou_id_idx", columnList = "cou_id"),
		@Index(name = "td_address_pro_id_idx", columnList = "pro_id"),
		@Index(name = "td_address_dis_id_idx", columnList = "dis_id"),
		@Index(name = "td_address_com_id_idx", columnList = "com_id"),
		@Index(name = "td_address_vil_id_idx", columnList = "vil_id")
	}
)
public class Address extends BaseAddress implements MAddress {
	private static final long serialVersionUID = -7725133018950243733L;
	
	/**
	 * 
	 * @return
	 */
	public static Address createInstance() {
        final Address addr = EntityFactory.createInstance(Address.class);
        addr.setType(ETypeAddress.MAIN);
        return addr;
    }

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "add_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
}
