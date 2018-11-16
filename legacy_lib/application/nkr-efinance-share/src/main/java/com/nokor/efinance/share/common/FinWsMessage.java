package com.nokor.efinance.share.common;


/**
 * 
 * @author prasnar
 *
 */
public enum FinWsMessage {
	OK("OK"),

	ERROR_EXCEPTION("Error exception"),

	REFDATA_NOT_FOUND("Param not found"),

	CONTRACT_ALREADY_PROCESSED("Contract already processed"),
	CONTRACT_NOT_FOUND("Contract not found"),
	CONTRACT_BALANCE_NOT_ZERO("Contract balance not 0"),
	CONTRACT_MANDATORY("Contract is mandatory"),
	CONTRACT_NOTE_NOT_FOUND("Contract note not found"),
	
	DEALER_NOT_FOUND("Dealer not found"),
	FIN_PROD_NOT_FOUND("Financial product not found"),
	FIN_PROD_MANDATORY("Financial product is mandatory"),
	TERM_MANDATORY("Term is mandatory"),
	TERM_NOT_FOUND("Term not found"),
	EFF_RATE_MANDATORY("Eff rate is mandatory"),
	FLAT_RATE_MANDATORY("Flat rate is mandatory"),
	APPLICATION_ID_MANDATORY("Application id is mandatory"),	
	FINANCE_AMOUNT_MANDATORY("Finance amount is mandatory"),
	DEFAULT_FINANCE_AMOUNT_MANDATORY("Default finance amount is mandatory"),
	DOWN_PAYMENT_AMOUNT_MANDATORY("Down payment amount is mandatory"),
	INSTALLMENT_AMOUNT_MANDATORY("Installment amount is mandatory"),
	DOWN_PAYMENT_PERCENTAGE_MANDATORY("Down payment percentage is mandatory"),
	FREQUENCY_CODE_MANDATORY("Frequency code is mandatory"),
	FREQUENCY_NOT_FOUND("Frequency is not found"),
	
	SERVICE_NOT_FOUND("Service fee is not found"),
	SERVICE_PRICE_MANDATORY_AND_GREATER_THAN_ZERO("Service fee is mandatory and greater than zero"),
	COMMISSION_CODE_MANDATORY("Commission code is mandatory"),
	COMMISSION_NOT_FOUND("Commission is not found"),
	COMMISSION_PRICE_MANDATORY_AND_GREATER_THAN_ZERO("Commission price is mandatory and greater than zero"),
	COMMISSION_VAT_PRICE_MANDATORY("Commission vat price is mandatory"),
	DOCUMENT_NOT_FOUND("Document is not found"),
	
	LESSEE_MANDATORY("Lessee is madatory"),
	LESSEE_APPLICANT_CATEGORY_MANDATORY("Lessee applicant category is madatory"),
	LESSEE_APPLICANT_CATEGORY_NOT_FOUND("Lessee applicant category is not found"),
	LESSEE_DATA_MANDATORY("Lessee individual data is madatory"),
	GUARANTOR_APPLICANT_CATEGORY_MANDATORY("Guarantor applicant category is madatory"),
	GUARANTOR_DATA_MANDATORY("Guarantor individual data is madatory"),
	GUARANTOR_APPLICANT_CATEGORY_NOT_FOUND("Guarantor applicant category is not found"),
	LESSEE_DATA_NOT_FOUND("Lessee data is not found"),
	GUARANTOR_DATA_NOT_FOUND("Guarantor data is not found"),
	INDIVIDUAL_SPOUSE_NOT_FOUND("Individual spouse is not found"),
	COMPANY_NOT_FOUND("Company is not found"),
	
	DUPLICATE_ASSET_MODEL_CODE("Duplicate asset model code"),
	ASSET_NOT_FOUND("Asset is not found"),
	ASSET_DATA_MISSING("Asset data is missing"),
	ASSET_MODEL_MANDATORY("Asset model code is madatory"),
	ASSET_MODEL_NOT_FOUND("Asset model is not found"),
	ASSET_PROVINCE_NOT_FOUND("Asset province is not found"),
	ASSET_REGISTRATION_DATE_MANDATORY("Asset registration date is mandatory"),
	ASSET_REGISTRATION_PLATE_NO_MANDATORY("Asset registration plate no is mandatory"),
	ASSET_REGISTRATION_PLATE_TYPE_MANDATORY("Asset registration plate type is mandatory"),
	ASSET_REGISTRATION_PLATE_TYPE_NOT_FOUND("Asset registration plate type is not found"),
	ASSET_COLOR_ID_MANDATORY("Asset color id is mandatory"),
	ASSET_COLOR_NOT_FOUND("Asset color is not found"),
	ASSET_ENGINE_CODE_MANDATORY("Asset engine code is mandatory"),
	ASSET_ENGINE_NOT_FOUND("Asset engine is not found"),
	ASSET_MANUFACTURING_YEAR_MANDATORY("Asset manufacturing year type is mandatory"),
	ASSET_PRICE_MANDATORY("Asset price is mandatory"),
	ASSET_TYPE_MANDATORY("Asset type is mandatory"),
	CALCULATE_METHOD_MANDATORY("Calculate method is mandatory"),
	ASSET_RANGE_NOT_FOUND("Asset range is not found"),
	ASSET_RANGE_MANDATORY("Asset range is mandatory"),
	ASSET_BRAND_MANDATORY("Asset brand is mandatory"),
	ASSET_DESC_EN_MANDATORY("Asset description (en) is mandatory"),
	ASSET_BRAND_NOT_FOUND("Asset brand is not found"),
	ASSET_BRAND_CODE_MANDATORY("Asset brand code is mandatory"),
	ASSET_RANGE_CODE_MANDATORY("Asset range code is mandatory"),
	DUPLICATE_ASSET_BRAND_CODE("Duplicate asset brand code"),
	DUPLICATE_ASSET_RANGE_CODE("Duplicate asset range code"),
	ASSET_CATEGORY_NOT_FOUND("Asset category is not found"),
	ASSET_CATEGORY_MANDATORY("Asset category is not found"),
	ASSET_CATEGORY_NAME_EN_MANDATORY("Asset category name (en) is mandatory"),
	DEALER_ATTRIBUTE_MANDATORY("Dealer attribute of asset category and brand is not configured"),
	
	DATA_MISSING("Data is missing"),
	REFERENCE_MANDATORY("Reference is mandatory"),
	REFERENCE_TYPE_CODE_MANDATORY("Reference type code is mandatory"),
	REFERENCE_TYPE_CODE_NOT_FOUND("Reference type code is not found"),
	REFERENCE_RELATIONSHIP_CODE_MANDATORY("Reference relationship code is mandatory"),
	REFERENCE_RELATIONSHIP_NOT_FOUND("Reference relationship is not found"),
	REFERENCE_FIRST_NAME_MANDATORY("Reference first name is mandatory"),
	REFERENCE_LAST_NAME_MANDATORY("Reference last name is mandatory"),
	ID_TYPE_MANDATORY("Id type is mandatory"),
	ID_NUMBER_MANDATORY("Id number is mandatory"),
	ISSUING_ID_DATE_MANDATORY("Issuing id date is mandatory"),
	EXPIRING_ID_DATE_MANDATORY("Expiring id date is mandatory"),
	FIRSTNAME_MANDATORY("Firstname is mandatory"),
	LASTNAME_MANDATORY("Lastname is mandatory"),
	PREFIX_MANDATORY("Prefix is mandatory"),
	PREFIX_NOT_FOUND("Prefix is not found"),
	MARITAL_STATUS_MANDATORY("Marital status is mandatory"),
	MARITAL_STATUS_NOT_FOUND("Marital status is not found"),
	DOB_MANDATORY("Date of birth is mandatory"),
	TYPE_CONTACT_MANDATORY("Type contact is mandatory"),
	NUMBER_OF_CHILDREN_MANDATORY("Number of children is mandatory"),
	HOUSE_NO_MANDATORY("House No is mandatory"),
	STREET_MANDATORY("Street is mandatory"),
	PROVINCE_MANDATORY("Province is mandatory"),
	PROVINCE_NOT_FOUND("Province is not found"),
	DISTRICT_MANDATORY("District is mandatory"),
	DISTRICT_NOT_FOUND("District is not found"),
	SUB_DISTRICT_MANDATORY("Sub district is mandatory"),
	SUB_DISTRICT_NOT_FOUND("Sub district is not found"),
	POSTAL_CODE_MANDATORY("Postal code is mandatory"),
	LIVING_PERIOD_IN_YEAR_MANDATORY("Living period in year is mandatory"),
	LIVING_PERIOD_IN_MONTH_MANDATORY("Living period in month is mandatory"),
	RESIDENCE_STATUS_ID_MANDATORY("Residence status id is mandatory"),
	RESIDENCE_STATUS_NOT_FOUND("Residence status is not found"),
	INCOME_TYPE_ID_MANDATORY("Income type id is mandatory"),
	INCOME_TYPE_NOT_FOUND("Income type is not found"),
	CATEGORY_INCOME_ID_MANDATORY("Category income id is mandatory"),
	CATEGORY_INCOME_NOT_FOUND("Category income is not found"),
	COMPANY_NAME_MANDATORY("Company name is mandatory"),
	JOB_TITLE_MANDATORY("Job position is mandatory"),
	SINCE_MANDATORY("Since is mandatory"),
	WORKING_PERIOD_IN_YEAR_MANDATORY("Working period in year is mandatory"),
	WORKING_PERIOD_IN_MONTH_MANDATORY("Working period in month is mandatory"),
	INCOME_MANDATORY("Income is mandatory"),
	COMPANY_PHONE_MANDATORY("Company phone is mandatory"),
	PHONE_NUMBER_MANDATORY("Phone number is mandatory"),
	
	TYPE_INFO_CODE_MANDATORY("Type info code is mandatory"),
	TYPE_INFO_NOT_FOUND("Type info is not found"),
	TYPE_INFO_VALUE_MANDATORY("Type info value is mandatory"),
	
	DEALER_NAME_EN_MANDATORY("Dealer name (en) is mandatory"),
	DEALER_TYPE_MANDATORY("Dealer type is mandatory"),
	DEALER_TYPE_NOT_FOUND("Dealer type is not found"),
	DEALER_INTERNAL_CODE_MANDATORY("Dealer internal code is mandatory"),
	
	CAMPAIGN_NOT_FOUND("Campaign not found"),
	CAMPAIGN_CODE_MANDATORY("Campaign code is mandatory"),
	CAMPAIGN_DESC_EN_MANDATORY("Campaign description (en) is mandatory"),
	CAMPAIGN_FULL_DESC_MANDATORY("Campaign full description is mandatory"),
	CAMPAIGN_START_DATE_MANDATORY("Campaign start date is mandatory"),
	CAMPAIGN_END_DATE_MANDATORY("Campaign end date is mandatory"),
	DUPLICATE_CAMPAIGN_CODE("Duplicate campaign code"),
	DUPLICATE_INSURANCE_CAMPAIGN_CODE("Duplicate insurance campaign code"),
	INSURANCE_CAMPAIGN_NOT_FOUND("Insurance campaign not found"),
	INSURANCE_CAMPAIGN_CODE_MANDATORY("Insurance campaign code is mandatory"),
	INSURANCE_CAMPAIGN_DESC_EN_MANDATORY("Insurance campaign description (en) is mandatory"),
	INSURANCE_CAMPAIGN_START_DATE_MANDATORY("Insurance campaign start date is mandatory"),
	INSURANCE_CAMPAIGN_END_DATE_MANDATORY("Insurance campaign end date is mandatory"),
	
	ORGANIZATION_MANDATORY("Organization is mandatory"),
	ORGANIZATION_NOT_FOUND("Organization not found"),
	ORGANIZATION_NAME_MANDATORY("Organization name is mandatory"),
	ORGANIZATION_NAME_EN_MANDATORY("Organization name (en) is mandatory"),
	
	BRANCH_NOT_FOUND("Branch not found"),
	BRANCH_NAME_MANDATORY("Branch name is mandatory"),
	BRANCH_NAME_EN_MANDATORY("Branch name (en) is mandatory"),

	FIN_PRODUCT_CODE_MANDATORY("Financial product code is mandatory"),
	FIN_PRODUCT_DESC_EN_MANDATORY("Financial product description (en) is mandatory"),
	
	PRODUCT_LINE_NOT_FOUND("Product line not found"),
	PRODUCT_LINE_MANDATORY("Product line is mandatory"),
	PRODUCT_LINE_DESC_EN_MANDATORY("Product line description (en) is mandatory"),
	PRODUCT_LINE_DESC_MANDATORY("Product line description is mandatory"),
	PRODUCT_LINE_TYPE_MANDATORY("Product line type is mandatory"),
	
	FIN_PRODUCT_START_DATE_MANDATORY("Financial product start date is mandatory"),
	FIN_PRODUCT_END_DATE_MANDATORY("Financial product end date is mandatory"),
	DUPLICATE_FIN_PRODUCT_CODE("Duplicate financial product code"),
	
	PROVINCE_DESC_EN_MANDATORY("Province description (en) is mandatory"),
	DISTRICT_DESC_EN_MANDATORY("District description (en) is mandatory"),
	COMMUNE_NOT_FOUND("Commune not found"),
	COMMUNE_DESC_EN_MANDATORY("Commune description (en) is mandatory"),
	VILLAGE_NOT_FOUND("Village not found"),
	VILLAGE_DESC_EN_MANDATORY("Village description (en) is mandatory"),
	
	SUBSIDY_NOT_FOUND("Subsidy is not found"),
	
	COMPENSATION_NOT_FOUND("Compensation is not found"),
	
	APPOINTMENT_NOT_FOUND("Appointment not found"),
	APPLICANT_TYPE_MANDATORY("Applicant type is mandatory"),
	LOCATION_MANDATORY("Location is mandatory"),
	LOCATION_NOT_FOUND("Location not found"),
	CONTRACT_REQUEST_NOT_FOUND("Contract request not found"),
	LETTER_NOT_FOUND("Letter not found"),
	CONTRACT_FLAG_NOT_FOUND("Contract flag not found"),
	FLAG_TYPE_MANDATORY("Flag type is mandatory"),
	FLAG_TYPE_NOT_FOUND("Flag type not found"),
	CONTRACT_OPERATION_NOT_FOUND("Contract operation not found"),
	CALL_CENTER_HISTORY_NOT_FOUND("Call center history not found"),
	COL_ACTION_NOT_FOUND("Col action not found"),
	COL_HISTORY_NOT_FOUND("Col history not found"),
	EMAIL_NOT_FOUND("Email not found"),
	SMS_NOT_FOUND("Sms not found"),
	REMINDER_NOT_FOUND("Reminder not found"),
	COMMENT_MANDATORY("Comment is mandatory"),
	DATE_MANDATORY("Date is mandatory"),
	COMMENT_NOT_FOUND("Comment not found"),
	ADDRESS_MANDATORY("Address is mandatory"),
	MESSAGE_MANDATORY("Message is mandatory"),
	
	RECEIPT_CODE_MANDATORY("Receipt code is mandatory"),
	LOCKSPLIT_STATUS_MANDATORY("Lock split status is mandatory"),
	MINIMUM_INTEREST_NOT_FOUND("Minimum interest not found"),
	PAYMENT_CHANNEL_NOT_FOUND("Payment channel not found"),
	LOCKSPLIT_TYPE_NOT_FOUND("Lock split type not found"),
	LOCKSPLIT_TYPE_CODE_MANDATORY("Lock split type code is mandatory"),
	LOCKSPLIT_TYPE_DESC_EN_MANDATORY("Lock split type description (en) is mandatory"),
	
	CALL_TYPE_NOT_FOUND("Call type not found"),
	CALL_TYPE_MANDATORY("Call type is mandatory"),
	
	COL_SUBJECT_NOT_FOUND("Subject not found"),
	COL_SUBJECT_CODE_MANDATORY("Subject code is mandatory"),
	COL_SUBJECT_DESC_EN_MANDATORY("Subject description (en) is mandatory"),
	COL_RESULT_NOT_FOUND("Result not found"),
	COL_RESULT_CODE_MANDATORY("Result code is mandatory"),
	COL_RESULT_DESC_EN_MANDATORY("Result description (en) is mandatory"),
	EMPLOYEE_NOT_FOUND("Branch not found");

	private String desc;
	
	/**
	 */
	private FinWsMessage(String desc) {
		this.desc = desc;
	}
	

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}


	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}		
}
