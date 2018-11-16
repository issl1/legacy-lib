package com.ext.testing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import junit.framework.TestCase;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.client.jersey.ClientApplicant;
import com.nokor.efinance.client.jersey.ClientAsset;
import com.nokor.efinance.client.jersey.ClientAssetBrand;
import com.nokor.efinance.client.jersey.ClientAssetModel;
import com.nokor.efinance.client.jersey.ClientAssetRange;
import com.nokor.efinance.client.jersey.ClientBlackListItem;
import com.nokor.efinance.client.jersey.ClientCampaign;
import com.nokor.efinance.client.jersey.ClientContract;
import com.nokor.efinance.client.jersey.ClientCreditControl;
import com.nokor.efinance.client.jersey.ClientDealer;
import com.nokor.efinance.client.jersey.ClientFinancialProduct;
import com.nokor.efinance.client.jersey.ClientIndividual;
import com.nokor.efinance.client.jersey.ClientInsuranceCampaign;
import com.nokor.efinance.client.jersey.ClientJournalEntry;
import com.nokor.efinance.client.jersey.ClientLetterTemplate;
import com.nokor.efinance.client.jersey.ClientLockSplit;
import com.nokor.efinance.client.jersey.ClientOrganization;
import com.nokor.efinance.client.jersey.ClientProductLine;
import com.nokor.efinance.client.jersey.ClientRefData;
import com.nokor.efinance.client.jersey.ClientRiskSegment;
import com.nokor.efinance.client.jersey.ClientSMSTemplate;
import com.nokor.efinance.client.jersey.ClientScoreCard;
import com.nokor.efinance.client.jersey.ClientVat;
import com.nokor.efinance.client.jersey.address.ClientDistrict;
import com.nokor.efinance.client.jersey.address.ClientProvince;
import com.nokor.efinance.client.jersey.address.ClientSubDistrict;
import com.nokor.efinance.client.jersey.address.ClientVillage;
import com.nokor.efinance.share.asset.AssetCriteriaDTO;
import com.nokor.efinance.share.locksplit.LockSplitCriteriaDTO;


/**
 * 
 * @author uhout.cheng
 */
public class TestAllWS extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestAllWS.class);
	
//	private static final String URLRA = "http://172.255.152.141:8081/efinance-ra";
//	private static final String URLAPP = "http://172.255.152.141:8081/efinance-app";
	private static final String URLRA = "http://localhost:8080/efinance-ra";
	private static final String URLAPP = "http://localhost:8080/efinance-app";
	
	private final static String ORGANIZATIONTYPES = "organizationtypes";
	private final static String ORGANIZATIONSUBTYPES = "organizationsubtypes";
	private final static String ORGANIZATIONLEVELS = "organizationlevels";
	private final static String LEGALFORMS = "legalforms";
	private final static String CURRENCIES = "currencies";
	private final static String COLORS = "colors";
	private final static String LANGUAGES = "languages";
	private final static String OPTIONALITIES = "optionalities";
	private final static String ADDRESSTYPES = "addresstypes";
	private final static String POSTALCODES = "postalcodes";
	private final static String COUNTRIES = "countries";
	private final static String CIVILITIES = "civilities";
	private final static String TITLES = "titles";
	private final static String GENDERS = "genders";
	private final static String MARITALSTATUS = "maritalstatus";
	private final static String NATIONALITIES = "nationalities";
	private final static String POSITIONS = "positions";
	private final static String CONTACTTYPES = "contacttypes";
	private final static String CONTACTINFOTYPES = "contactinfotypes";
	private final static String TYPEIDNUMBERS = "typeidnumbers";
	private final static String RESIDENCESTATUSES = "residencestatuses";
	private final static String RESIDENCETYPES = "residencetypes";
	private final static String EDUCATIONS = "educations";
	private final static String RELIGIONS = "religions";
	private final static String RELATIONSHIPS = "relationships";
	private final static String SENIORTYLEVELS = "seniortylevels";
	private final static String OCCUPATIONTYPES = "occupationtypes";
	private final static String EMPLOYMENTSTATUSES = "employmentstatuses";
	private final static String EMPLOYMENTCATEGORIES = "employmentcategories";
	private final static String OCCUPATIONGROUPS = "occupationgroups";
	private final static String EMPLOYMENTTYPES = "employmenttypes";
	private final static String EMPLOYMENTCONTRACTTYPES = "employmentcontracttypes";
	private final static String BANKACCOUNTTYPE = "bankaccounttypes";
	private final static String DOCUMENTSTATUS = "documentstatus";
	private final static String LOCATIONS = "locations";
	private final static String TIMEUNITS = "timeunits";
	private final static String TASKTYPES = "tasktypes";
	private final static String TASKPRIORITY = "taskpriority";
	private final static String TASKSEVERITY = "taskseverity";
	private final static String PROJECTTYPE = "projecttype";
	private final static String PROJECTCATEGORY = "projectcategory";
	private final static String PROJECTROLE = "projectrole";
	private final static String ASSETGENDERS = "assetgenders";
	private final static String FINASSETTYPES = "finassettypes";
	private final static String CASHFLOWCODES = "cashflowcodes";
	private final static String DEALERTYPES = "dearlertypes";
	private final static String DOCUMENTSTATES = "documentstates";
	private final static String STOCKREASONS = "stockreasons";
	private final static String APPLICANTREFTYPES = "applicantreftypes";
	private final static String ENGINES = "engines";
	private final static String INSTALLMENTPLACES = "installmentplaces";
	private final static String REGLATETYPES = "regplatetypes";
	private final static String SERVICETYPES = "servicetypes";
	private final static String AFTERSALEEVENTTYPE = "aftersaleeventtype";
	private final static String APPLICANTCATEGORIES = "applicantcategories";
	private final static String ROUNDINGFORMATS = "roundingformats";
	private final static String COMPLAINTTYPES = "complainttypes";
	private final static String ASSETOWNERSHIPS = "assetownerships";
	private final static String ASSETTYPES = "assettypes";
	private final static String ASSETSUBTYPES = "assetsubtypes";
	private final static String BLACKLISTSOURCES = "blacklistsources";
	private final static String BLACKLISTREASONS = "blacklistreasons";
	private final static String LESSEEFLAWSCATEGORIES = "lesseeflawscategories";
	private final static String LESSEEFLAWS = "lesseeflaws";
	private final static String GUARANTORFLAWSCATEGORIES = "guarantorflawscategories";
	private final static String GUARANTORFLAWS = "guarantorflaws";
	private final static String LESSEEREQUESTCATEGORIES = "lesseerequestscategories";
	private final static String LESSEEREQUESTS = "lesseerequests";
	private final static String GUARANTORREQUESTSCATEGORIES = "guarantorrequestscategories";
	private final static String GUARANTORREQUESTS = "guarantorrequests";
	private final static String DECISIONCATEGORIES = "decisioncategories";
	private final static String DECISIONS = "decisions";
	
	private Response response;
	
	/**
	 * 
	 */
	public void testGetResult() {
		// APP
		Map<String, Response> responsesAPP = new HashMap<String, Response>();
		response = ClientJournalEntry.listJournalEntries(URLAPP);
		responsesAPP.put("JournalEntries", response);
		response = ClientApplicant.getApplicant(URLAPP, 1l);
		responsesAPP.put("Applicant", response);
		response = ClientIndividual.getIndividual(URLAPP, 1l);
		responsesAPP.put("Individual", response);
		response = ClientContract.getContract(URLAPP, "0115000001");
		responsesAPP.put("Contract", response);
		response = ClientIndividual.getContactInfos(URLAPP, 1l);
		responsesAPP.put("ContactInfo of Individual", response);
		
		AssetCriteriaDTO assetCriteriaDTO = new AssetCriteriaDTO();
		assetCriteriaDTO.setChassisNumber("ND125M-9342928");
		response = ClientAsset.searchAsset(URLAPP, assetCriteriaDTO);
		responsesAPP.put("Asset", response);
		
		LockSplitCriteriaDTO lockSplitCriteriaDTO = new LockSplitCriteriaDTO();
		lockSplitCriteriaDTO.setLockSplitNo("0000000001");
		response = ClientLockSplit.searchLockSplit(URLAPP, lockSplitCriteriaDTO);
		responsesAPP.put("LockSplit", response);
		// RA
		Map<String, Response> responsesRA = new HashMap<String, Response>();
		response = ClientProvince.getProvince(URLRA, 1l);
		responsesRA.put("Province", response);
		response = ClientDistrict.getDistrict(URLRA, 1l);
		responsesRA.put("District", response);
		response = ClientSubDistrict.getSubDistrict(URLRA, 1l);
		responsesRA.put("SubDistrict", response);
		response = ClientVillage.getVillages(URLRA);
		responsesRA.put("Village", response);
		response = ClientAssetBrand.getAssetBrand(URLRA, 1l);
		responsesRA.put("Brand", response);
		response = ClientAssetRange.getAssetRange(URLRA, 1l);
		responsesRA.put("Series", response);
		response = ClientAssetModel.getAssetModel(URLRA, 1l);
		responsesRA.put("Model", response);
		response = ClientBlackListItem.getBlackListItems(URLRA);
		responsesRA.put("BlackListItems", response);
		response = ClientInsuranceCampaign.getInsuranceCampaigns(URLRA);
		responsesRA.put("InsuranceCampaigns", response);
		response = ClientOrganization.getCompany(URLRA, 1l);
		responsesRA.put("Company", response);
		response = ClientRiskSegment.getRiskSegments(URLRA);
		responsesRA.put("RiskSegments", response);
		response = ClientCampaign.getCampaigns(URLRA);
		responsesRA.put("Campaigns", response);
		response = ClientCreditControl.getCreditControls(URLRA);
		responsesRA.put("CreditControls", response);
		response = ClientDealer.getDealer(URLRA, 1l);
		responsesRA.put("Dealer", response);
		response = ClientFinancialProduct.getFinProduct(URLRA, 1l);
		responsesRA.put("FinProduct", response);
		response = ClientLetterTemplate.getLetterTemplates(URLRA);
		responsesRA.put("LetterTemplates", response);
		response = ClientProductLine.getProductLine(URLRA, 1l);
		responsesRA.put("ProductLine", response);
		response = ClientScoreCard.getScoreCards(URLRA);
		responsesRA.put("ScoreCards", response);
		response = ClientSMSTemplate.getLetterTemplates(URLRA);
		responsesRA.put("LetterTemplates", response);
		response = ClientVat.getVat(URLRA, 1l);
		responsesRA.put("Vat", response);
		// REF Table
		final List<String> refTableNames = Arrays.asList(new String[] 
				{
					ORGANIZATIONTYPES, ORGANIZATIONSUBTYPES, ORGANIZATIONLEVELS, LEGALFORMS, CURRENCIES,
					COLORS, LANGUAGES, OPTIONALITIES, ADDRESSTYPES, POSTALCODES, COUNTRIES, CIVILITIES,
					TITLES, GENDERS, MARITALSTATUS, NATIONALITIES, POSITIONS, CONTACTTYPES, CONTACTINFOTYPES,
					TYPEIDNUMBERS, RESIDENCESTATUSES, RESIDENCETYPES, EDUCATIONS, RELIGIONS, RELATIONSHIPS,
					SENIORTYLEVELS, OCCUPATIONTYPES, EMPLOYMENTSTATUSES, EMPLOYMENTCATEGORIES, OCCUPATIONGROUPS,
					EMPLOYMENTTYPES, EMPLOYMENTCONTRACTTYPES, BANKACCOUNTTYPE, DOCUMENTSTATUS, LOCATIONS, TIMEUNITS,
					TASKTYPES, TASKPRIORITY, TASKSEVERITY, PROJECTTYPE, PROJECTCATEGORY, PROJECTROLE, ASSETGENDERS,
					FINASSETTYPES, CASHFLOWCODES, DEALERTYPES, DOCUMENTSTATES, STOCKREASONS, APPLICANTREFTYPES,
					ENGINES, INSTALLMENTPLACES, REGLATETYPES, SERVICETYPES, AFTERSALEEVENTTYPE, APPLICANTCATEGORIES,
					ROUNDINGFORMATS, COMPLAINTTYPES, ASSETOWNERSHIPS, ASSETTYPES, ASSETSUBTYPES, BLACKLISTSOURCES,
					BLACKLISTREASONS, LESSEEFLAWSCATEGORIES, LESSEEFLAWS, GUARANTORFLAWSCATEGORIES, GUARANTORFLAWS,
					LESSEEREQUESTCATEGORIES, LESSEEREQUESTS, GUARANTORREQUESTSCATEGORIES, GUARANTORREQUESTS, 
					DECISIONCATEGORIES, DECISIONS
				});
		
		Map<String, Response> mapRefTable = new HashMap<String, Response>();
		for (String refTableName : refTableNames) {
			Response response =  ClientRefData.listRefData(URLRA, refTableName);
			mapRefTable.put(refTableName, response);
		}
		
		logger.info(">>------------------------------------APP------------------------------------->>");
		for (String key : responsesAPP.keySet()) {
			logger.info(displayResult(key, responsesAPP));
		}
		logger.info("<<------------------------------------APP-------------------------------------<<");
		logger.info(">>------------------------------------RA-------------------------------------->>");
		for (String key : responsesRA.keySet()) {
			logger.info(displayResult(key, responsesRA));
		}
		logger.info("<<--------------------------------------RA-------------------------------------<<");
		logger.info(">>-------------------------------PARAM-Ref-Table------------------------------->>");
		for (String key : mapRefTable.keySet()) {
			logger.info(displayResult(key, mapRefTable));
		}
		logger.info("<<-------------------------------PARAM-Ref-Table-------------------------------<<");
	}
	
	/**
	 * 
	 * @param key
	 * @param response
	 * @return
	 */
	private String displayResult(String key, Map<String, Response> response) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Result of " + StringUtils.rightPad(key, 40));
		stringBuffer.append(": Status [");
		stringBuffer.append(response.get(key).getStatus());
		stringBuffer.append("]");
		return stringBuffer.toString();
	}
}
