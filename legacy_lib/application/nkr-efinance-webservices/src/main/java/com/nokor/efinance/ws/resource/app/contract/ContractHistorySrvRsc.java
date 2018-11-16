package com.nokor.efinance.ws.resource.app.contract;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.common.reference.model.BlackListItem;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.share.contract.ContractDTO;
import com.nokor.efinance.share.contract.ContractHistroyDTO;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author buntha.chea
 *
 */
@Path("/contracts/histories")
public class ContractHistorySrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List contract by reference
	 * @param reference
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getByIdNumber(@QueryParam("idNumber") String idNumber, @QueryParam("phoneNumber") String phoneNumber) {
		try {
			
			List<ContractHistroyDTO> contractsHistroyDTOs = new ArrayList<>();
			
			if (StringUtils.isEmpty(idNumber) && StringUtils.isEmpty(phoneNumber)) {
				String errMsg = "ID Number or Phone Number is Requried";
				throw new IllegalStateException(I18N.messageFieldEmptyRequired(errMsg));
			}
			
			LOG.debug("Id Number - Ex-Ref. [" + idNumber + "]");
			LOG.debug("PhoneNumber - Ex-Ref. [" + phoneNumber + "]");
			
			List<Applicant> applicants = getApplicantsByIdNumber(idNumber, phoneNumber);
			
			String blackListIdNumber = idNumber;
			if (!StringUtils.isEmpty(phoneNumber)) {
				blackListIdNumber += getIdNumberByPhone(phoneNumber);
			}
			List<BlackListItem> blackListItems = getBlackListByIdNumber(blackListIdNumber);
			
			contractsHistroyDTOs.addAll(toContractsHistoryDTO(applicants));
			contractsHistroyDTOs.addAll(toBlackListDTOs(blackListItems));
		
			return ResponseHelper.ok(contractsHistroyDTOs);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}

	
	/**
	 * 
	 * @param idNumber
	 * @return
	 */
	private List<Applicant> getApplicantsByIdNumber(String idNumber, String phoneNumber) {
		BaseRestrictions<Applicant> restrictions = new BaseRestrictions<>(Applicant.class);
		restrictions.addAssociation(Individual.class, "indi", JoinType.INNER_JOIN);
		
		if (!StringUtils.isEmpty(idNumber)) {
			String[] idNumbers = idNumber.split(",");
			List<Criterion> criterions = new ArrayList<>();
			if (idNumbers != null && idNumbers.length > 0) {
				for (int i = 0; i < idNumbers.length; i++) {		
					criterions.add(Restrictions.or(Restrictions.ilike("indi.idNumber", idNumbers[i], MatchMode.ANYWHERE)));
				}
			}
			restrictions.addCriterion(Restrictions.or(criterions.toArray(new Criterion[criterions.size()])));
		}
		
		if (!StringUtils.isEmpty(phoneNumber)) {
			String[] phoneNumbers = phoneNumber.split(",");
			restrictions.addAssociation("indi.individualContactInfos", "inConInfo", JoinType.INNER_JOIN);
			restrictions.addAssociation("inConInfo.contactInfo", "conInfo", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.or(Restrictions.eq("conInfo.typeInfo", ETypeContactInfo.MOBILE), Restrictions.eq("conInfo.typeInfo", ETypeContactInfo.LANDLINE)));
			restrictions.addCriterion(Restrictions.in("conInfo.value", phoneNumbers));
		}
		restrictions.setDistinctRootEntity(true);
		List<Applicant> applicants = ENTITY_SRV.list(restrictions);
		return applicants;
	}

	/**
	 * 
	 * @param idNumber
	 * @return
	 */
	private List<BlackListItem> getBlackListByIdNumber(String idNumber) {
		String[] idNumbers = idNumber.split(",");
		BaseRestrictions<BlackListItem> restrictions = new BaseRestrictions<>(BlackListItem.class);
		List<Criterion> criterions = new ArrayList<>();
		if (idNumbers != null && idNumbers.length > 0) {
			for (int i = 0; i < idNumbers.length; i++) {		
				criterions.add(Restrictions.ilike(BlackListItem.IDNUMBER, idNumbers[i], MatchMode.ANYWHERE));
			}
		}
		restrictions.addCriterion(Restrictions.or(criterions.toArray(new Criterion[criterions.size()])));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 */
	protected List<ContractDTO> toContractsDTO(List<Contract> contracts) {
		List<ContractDTO> dtoLst = new ArrayList<>();
		for (Contract contract : contracts) {
			dtoLst.add(toContractDTO(contract));
		}
		return dtoLst;
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	private List<ContractHistroyDTO> toContractHistoryDTOs(Applicant applicant) {
		List<ContractHistroyDTO> contractsHistroyDTOs = new ArrayList<>();
		Contract contract = null;
		
		EApplicantType applicantType = EApplicantType.C;
		
		if (applicant.getContracts() != null && !applicant.getContracts().isEmpty()) {
			contract = applicant.getContracts().get(0);
		} else {
			ContractApplicant contractApplicant = ENTITY_SRV.getByField(ContractApplicant.class, "applicant", applicant);
			if (contractApplicant != null) {
				applicantType = contractApplicant.getApplicantType();
				contract = contractApplicant.getContract();
			}
		}
	
		if (contract != null) {
			ContractHistroyDTO contractsHistroyDTO = new ContractHistroyDTO();
			contractsHistroyDTO.setId(contract.getId());
			contractsHistroyDTO.setApplicantType(applicantType.getDescLocale());
			contractsHistroyDTO.setApplicantNo(contract.getExternalReference());
			contractsHistroyDTO.setApplicantDate(contract.getQuotationDate());
			contractsHistroyDTO.setContractNo(contract.getReference());
			contractsHistroyDTO.setContractDate(contract.getStartDate());
			contractsHistroyDTO.setFirstName(applicant.getFirstNameLocale());
			contractsHistroyDTO.setLastName(applicant.getLastNameLocale());
			contractsHistroyDTO.setContractStatus(contract.getWkfStatus().getDescEn());
			contractsHistroyDTO.setNbOverdue(contract.getCollection() != null ? contract.getCollection().getNbOverdueInDays() : null);
			contractsHistroyDTO.setPhoneNumber(getPhoneNumber(applicant));
			contractsHistroyDTO.setIdNumber(applicant.getIndividual().getIdNumber());
			double totalAR = MyNumberUtils.getDouble(contract.getLoanAmount() != null ? contract.getLoanAmount().getTiAmount() : null);
			contractsHistroyDTO.setTotalAR(totalAR);
			contractsHistroyDTO.setNetIncome(MyNumberUtils.getDouble(contract.getNetInterestAmount()));
			contractsHistroyDTOs.add(contractsHistroyDTO);
		}
		
		return contractsHistroyDTOs;
	}	
	
	/**
	 * blacklist item dto
	 * @param blackListItem
	 * @return
	 */
	private ContractHistroyDTO toBlackListItemDTO(BlackListItem blackListItem) {
		ContractHistroyDTO contractsHistroyDTO = new ContractHistroyDTO();
		contractsHistroyDTO.setId(blackListItem.getId());
		contractsHistroyDTO.setFirstName(blackListItem.getFirstNameLocale());
		contractsHistroyDTO.setLastName(blackListItem.getLastNameLocale());
		String reason = StringUtils.EMPTY;
		if (blackListItem.getReason() != null) {
			reason = blackListItem.getReason().getDescLocale();
		} else {
			reason = blackListItem.getDetails();
		}
		contractsHistroyDTO.setBlackListReson(reason);
		contractsHistroyDTO.setPhoneNumber(blackListItem.getMobilePerso() + ", " + blackListItem.getMobilePerso2());
		contractsHistroyDTO.setIdNumber(blackListItem.getIdNumber());
		return contractsHistroyDTO;
	}
	
	/**
	 * toContractHistoryDTO
	 * @param contracts
	 * @return
	 */
	private List<ContractHistroyDTO> toContractsHistoryDTO(List<Applicant> applicants) {
		List<ContractHistroyDTO> dtoLst = new ArrayList<>();
		for (Applicant applicant : applicants) {
			dtoLst.addAll(toContractHistoryDTOs(applicant));
		}
		return dtoLst;
	}
	
	/**
	 * 
	 * @param blackListItems
	 * @return
	 */
	private List<ContractHistroyDTO> toBlackListDTOs(List<BlackListItem> blackListItems) {
		List<ContractHistroyDTO> dtoLst = new ArrayList<>();
		for (BlackListItem blackListItem : blackListItems) {
			dtoLst.add(toBlackListItemDTO(blackListItem));
		}
		return dtoLst;
	}
	
	/**
	 * Get phone number by Applicant 
	 * @param applicant
	 * @return
	 */
	private String getPhoneNumber(Applicant applicant) {
		String phoneNumber = "";
		Individual individual = applicant.getIndividual();
		List<IndividualContactInfo> individualContactInfos = individual.getIndividualContactInfos();
		for (IndividualContactInfo individualContactInfo : individualContactInfos) {
			ContactInfo contactInfo = individualContactInfo.getContactInfo();
			if (contactInfo != null) {
				if (ETypeContactInfo.MOBILE.equals(contactInfo.getTypeInfo()) || ETypeContactInfo.LANDLINE.equals(contactInfo.getTypeInfo())) {
					phoneNumber += contactInfo.getValue() + ", ";
				}
			}
		}
		return phoneNumber;
	}
	
	/**
	 * 
	 * @param phoneNumber
	 * @return
	 */
	private String getIdNumberByPhone(String phoneNumber) {
		String idNumber = "";
		
		String[] phoneNumbers = phoneNumber.split(",");
		
		BaseRestrictions<Applicant> restrictions = new BaseRestrictions<>(Applicant.class);
		restrictions.addAssociation(Individual.class, "indi", JoinType.INNER_JOIN);
		restrictions.addAssociation("indi.individualContactInfos", "inConInfo", JoinType.INNER_JOIN);
		restrictions.addAssociation("inConInfo.contactInfo", "conInfo", JoinType.INNER_JOIN);
		
		restrictions.addCriterion(Restrictions.or(Restrictions.eq("conInfo.typeInfo", ETypeContactInfo.MOBILE), Restrictions.eq("conInfo.typeInfo", ETypeContactInfo.LANDLINE)));
		restrictions.addCriterion(Restrictions.in("conInfo.value", phoneNumbers));
		
		List<Applicant> applicants = ENTITY_SRV.list(restrictions);
		for (Applicant applicant : applicants) {
			idNumber += "," + applicant.getIndividual().getIdNumber();
		}
		return idNumber;
	}

}
