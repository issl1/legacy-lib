package com.nokor.efinance.core.shared;

/**
 * @author ly.youhort
 */
public interface FMEntityField {

    String ID = "id";
    String CODE = "code";
    String LAST_NAME = "lastName";
    String FIRST_NAME = "firstName";
    String LAST_NAME_EN = "lastNameEn";
    String FIRST_NAME_EN = "firstNameEn";
    String FULL_NAME = "fullName";
    String DESC_EN = "descEn";
    String DESC = "desc";
    String FULL_DESC = "fullDesc";
    String SORT_INDEX = "sortIndex";
    String GENDER = "gender";
    String BIRTH_DATE = "birthDate";
    String ADDRESS = "address";
    String SECURITY_USER = "securityUser";
    String TELEPHONE = "telephone";
    String MOBILEPHONE = "mobilePhone";
    String MOBILEPHONE2 = "mobilePhone2";
    String EMAIL = "email";
    String WEBSITE = "website";
    String LOG_IN = "login";
    String CREATE_USER = "createUser";
    String CREATE_DATE = "createDate";
    String UPDATE_USER = "updateUser";
    String UPDATE_DATE = "updateDate";
    String STATUS_RECORD = "statusRecord";
    String START_DATE = "startDate";
    String START_TIME = "startTime";
    String END_DATE = "endDate";
    String END_TIME = "endTime";
    String TITLE_EN = "titleEng";
    String TITLE = "title";
    String PARENT = "parent";
    String AGENT_TYPE = "agentType";
    String ASSET_MAKE = "assetMake";
    String ASSET_RANGE = "assetRange";
    String INTEREST_RATE = "interestRate";
    String STATUS = "status";
    String NAME = "name";
    String NAME_EN = "nameEn";
    String INTERNAL_CODE = "code";
    String EXTERNAL_CODE = "externalCode";
    String VAT_REGISTRATION_NO = "vatRegistrationNo";
    String LICENCE_NO = "licenceNo";
    
    String HOUSE_NO = "houseNo";
    String STREET = "street";
    String VILLAGE = "village";
    String COMMUNE="commune";
    String PROVINCE="province";
    String DISTRICT="district";
    String VILLAGE_KH = "villageKh";
    String COMMUNE_KH ="communekh";
    String PROVINCE_KH ="provinceKh";
    String DISTRICT_KH ="districtKh";
    String SERVICE = "service";
    String MANDATORY = "mandatory";
	String HIDDEN = "hidden";
	String PLACE_OF_BIRTHDAY = "placeOfBirth";
	String POSTAL_CODE = "postalCode";

	String QUOTATION_DATE = "quotationDate";
	
	String FINANCIAL_PRODUCT = "financialProduct";
	
	String WKF_STATUS = "wkfStatus";
	String WKF_SUB_STATUS = "wkfSubStatus";
	
	String ASSET = "asset";
	String SERIE = "serie";
	
	String APPLICANT_TYPE = "applicantType";
	String APPLICANT_ADDRESSES = "applicantAddresses";
	String DEALER = "dealer";
	String CUSTOMER = "customer";
	String SEC_USER = "secUser";
	String SEC_USER_BACKUP = "secUserBackup";
	String EXPIRATION_DATE = "expirationDate";
	String TI_PRICE = "tiPrice";
	String TE_PRICE = "tePrice";
	String VAT_PRICE = "vatPrice";
	String VALUE = "value";
	
	//contract
	String REFERENCE = "reference";
	String EXTERNAL_REFERENCE = "externalReference";
	String CONTRACT_STATUS = "contractStatus";
	String APPLICANT = "applicant";
	String INDIVIDUAL = "individual";
	String COMPANY = "company";
	String CREDITLINE = "creditLine";
	String INITIAL_START_DATE = "initialStartDate";
	String INITIAL_END_DATE = "initialEndDate";
	//
	String AUTOCONFIRM = "autoConfirm";
	String CATEGORY_PAYMENT_METHOD = "categoryPaymentMethod";
	String PAYMENT_METHOD = "paymentMethod";
	
	String DATE = "date";
	String COLOR = "color";
	String YEAR = "year";
	
	String STARTDATE = "start";
	String ENDDATE = "end";
	String CURRENCY_FROM = "from";
	String CURRENCY_TO = "to";
	String RATE = "rate";
	
	String PRODUCT_LINE_TYPE = "productLineType";
	String PAYMENT_CONDITION_FIN = "paymentConditionFin"; 
	String PAYMENT_CONDITION_CAP = "paymentConditionCap"; 
	String PAYMENT_CONDITION_IAP = "paymentConditionIap"; 
	String PAYMENT_CONDITION_IMA = "paymentConditionIma"; 
	String PAYMENT_CONDITION_FEE = "paymentConditionFee"; 
	String PRODUCT_LINE = "productLine";
	
	String DELAY = "delay";
	String END_OF_MONTH = "endOfMonth";
	String TI_PAID_USD = "tiPaidUsd";
	String PAYMENT_STATUS = "paymentStatus";
	String PAYMENT_TYPE = "paymentType";
	String PAYMENT_DATE = "paymentDate";
	String PASS_TO_DEALER_DATE = "passToDealerPaymentDate";
	String PURCHASE_ORDER_DATE = "purchaseOrderDate";
	String SECOND_PAYMENT_DATE = "secondPaymentDate";
	
	String GRACE_PERIOD = "gracePeriod";
	
	String EMPLOYMENT_STATUS = "employmentStatus";
	
	String ASSET_MODEL = "assetModel";
	
	String CALCUL_METHOD= "calculMethod";
	
	String TIME_ADDRESS_IN_YEARS = "timeAtAddressInYear";
	String TIME_ADDRESS_IN_MONTHS = "timeAtAddressInMonth";
	
	String PENALTY_CALCULMETHOD = "penaltyCalculMethod";
	String TI_PENALTY_AMOUNT_PER_DAY = "tiPenaltyAmounPerDaytUsd";
	String PENALTY_RATE = "penaltyRate";
	
	String PAYMENT = "payment";
	String ISSUE_DATE = "issueDate";
	String REAL_ISSUE_DATE = "realIssueDate";
	String INSTALLMENT_DATE = "installmentDate";
	String DUE_DATE = "dueDate";
	String REAL_INSTALLMENT_DATE = "realInstallmentDate";
	String PERIOD_START_DATE = "periodStartDate";
	String PERIOD_END_DATE = "periodEndDate";
	
	String UNPAID_DATE = "unpaidDate";
	String CANCELATION_DATE = "cancelationDate";
	String CANCEL = "cancel";
	String PAID = "paid";
	String UNPAID = "unpaid";
	
	String BY_PASS_RULE_FROM = "byPassFrom";
	String BY_PASS_RULE_TO = "byPassTo";
	String COL_BY_PASS_RULE = "colByPassRule";
	
	String COL_TYPE = "colType";
	
	String SUBSIDY_AMOUNT = "subsidyAmount";
	String MONTH_FROM = "monthFrom";
	String MONTH_TO = "monthTo";
	
	String HOLD_REASON = "histComment2";
}
