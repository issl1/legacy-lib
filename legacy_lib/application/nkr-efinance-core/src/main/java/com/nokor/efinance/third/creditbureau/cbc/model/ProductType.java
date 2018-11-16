package com.nokor.efinance.third.creditbureau.cbc.model;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;

/**
 * @author ly.youhort
 */
public enum ProductType implements RefDataId {
	
	POD("Authorized Over Draft"),
	CHCK("Bounced Cheques"),
	VIN("Car Instalment Agreement"),
	VLS("Car Lease Agreement"),
	VRA("Car Rentals Agreement"),
	CHC("Charge Card"),
	CDC("Consumer Durables Card"),
	CDL("Consumer Durables Loan"),
	CRC("Credit Card"),
	EUDF("Education Finance"),
	MGLD("Margin Lending"),
	MBL("Mobile Phone"),
	MIS("Miscellaneous"),
	MTG("Mortgage"),
	LND("Landline Phone"),
	PLN("Personal Finance"),
	PE("Public"),
	MRA("Real Estate Rentals Agreement"),
	RSTF("Restructured Facility"),
	SME("Small Medium Enterprise"),
	SFB("Secured Finance Program"),
	STFM("Stock Finance"),
	TPLN("Top-up Loan"),
	VCLM("Vehicle Claim"),
	TOD("Unauthorized Over Draft"),
	
	
	
	MPL("Mobile Phone Loan"),
	CPL("Computer Loan"),
	MTL("Motor Loan"),
	CAL("Car Loan"), 
	HIL("Home Improvement Loan"), 
	EDU("Education Loan"), 
	STL("Staff Loan"), 
	PEL("Personal Loan"), 
	RLE("Real Estate Loan"), 
	PHL("Public Housing Loan"),
	SHL("Staff Housing Loan"),
	WCL("Working Capital Loan"),  
	AFI("Asset Financing"),  
	INL("Investment Loan"),
	CON("Construction Loan"), 
	STF("Inventory Loan/Stock Finance"),  
	ODF("Overdraft Facility"), 
	REL("Revolving Loan"), 
	SYL("Syndicate Loan"), 
	AGI("Agriculture Loan"),   
	MCL("Machinery Loan"),      
	TFL("Trade Finance Loan"), 
	GRL("Green Loan"), 
	EML("Emergency Loan"), 
	COM("Community Loan"),
	SCC("Secured Credit Card"),
	UCC("Unsecured Credit Card"),
	CCC("Combined Credit Card"),
	BCC("Business Credit Card"),
	MFI("MFI Loan"), 
	PRL("Private Loan"),   
	ASL("Association Loan"); 
;
	
	
	private final String code;

	/**
     * 
     */
	private ProductType(final String code) {
		this.code = code;
	}

	/**
	 * return code
	 */
	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDescEn() {
		return I18N.value(code);
	}
	
	/**
	 * return desc
	 */
	@Override
	public String getDesc() {
		return I18N.value(code);
	}

	/**
	 * List of service type
	 * 
	 * @return
	 */
	public static List<ProductType> list() {
		List<ProductType> productTypes = new ArrayList<ProductType>();
		
		/*productTypes.add(POD);
		productTypes.add(CHCK);
		productTypes.add(VIN);
		productTypes.add(VLS);
		productTypes.add(VRA);
		productTypes.add(CHC);
		productTypes.add(CDC);
		productTypes.add(CDL);
		productTypes.add(CRC);
		productTypes.add(EUDF);
		productTypes.add(MGLD);
		productTypes.add(MBL);
		productTypes.add(MIS);
		productTypes.add(MTG);
		productTypes.add(LND);
		productTypes.add(PLN);
		productTypes.add(PE);
		productTypes.add(MRA);
		productTypes.add(RSTF);
		productTypes.add(SME);
		productTypes.add(SFB);
		productTypes.add(STFM);
		productTypes.add(TPLN);
		productTypes.add(VCLM);
		productTypes.add(TOD);*/
		productTypes.add(MTL);
		return productTypes;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return 0L;
	}
}