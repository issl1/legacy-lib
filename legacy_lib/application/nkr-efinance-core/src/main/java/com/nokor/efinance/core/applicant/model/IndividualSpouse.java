package com.nokor.efinance.core.applicant.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.ersys.core.hr.model.eref.ETitle;

/**
 * @author youhort.ly
 *
 */
@Entity
@Table(name = "td_individual_spouse")
public class IndividualSpouse extends EntityA {

	private static final long serialVersionUID = 9116233885593641677L;
	
	private Individual individual;
	private String lastName;
	private String firstName;
	private String nickName;
	private String middleName;
	private ECivility civility;
	private Date birthDate;
	private ETitle title;
	private EGender gender;
	
	
	/**
	 * 
	 * @return
	 */
	public static IndividualSpouse createInstance() {
		IndividualSpouse instance = EntityFactory.createInstance(IndividualSpouse.class);
        return instance;
    }
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ind_spo_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}	    
    
	/**
	 * @return the individual
	 */
    @ManyToOne
	@JoinColumn(name="ind_id", nullable = false)
	public Individual getIndividual() {
		return individual;
	}

	/**
	 * @param individual the individual to set
	 */
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

	/**
	 * @return the lastName
	 */
	@Column(name = "ind_spo_lastname", nullable = true, length = 100)
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the firstName
	 */
	@Column(name = "ind_spo_firstname", nullable = true, length = 100)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return the nickName
	 */
	@Column(name = "ind_spo_nickname", nullable = true, length = 50)
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}	
	
	/**
	 * @return the middleName
	 */
	@Column(name = "ind_spo_middlename", nullable = true, length = 50)
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the civility
	 */
	@Column(name = "civ_id", nullable = true)
    @Convert(converter = ECivility.class)
	public ECivility getCivility() {
		return civility;
	}

	/**
	 * @param civility the civility to set
	 */
	public void setCivility(ECivility civility) {
		this.civility = civility;
	}

	/**
	 * @return the birthDate
	 */
	@Column(name = "ind_spo_birth_date", nullable = true)
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the title
	 */
	@Column(name = "tit_id", nullable = true)
    @Convert(converter = ETitle.class)
	public ETitle getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(ETitle title) {
		this.title = title;
	}

	/**
	 * @return the gender
	 */
	@Column(name = "gen_id", nullable = true)
    @Convert(converter = EGender.class)
	public EGender getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(EGender gender) {
		this.gender = gender;
	}	
}
