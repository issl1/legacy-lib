package com.nokor.efinance.gui.report.xls;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.tools.report.Report;
import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * GLF Purchase order report for front office
 * @author sok.vina
 */
public class GLFPurchaseOrder implements Report {

	protected EntityService entityService = (EntityService) SecApplicationContextHolder
			.getContext().getBean("entityService");

	/**
	 * @see com.nokor.efinance.tools.report.Report#generate(com.nokor.efinance.core.shared.report.ReportParameter)
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {

		Long paymnId = (Long) reportParameter.getParameters().get("paymnId");
		Payment payment = entityService.getById(Payment.class, paymnId);
		Cashflow cashflow = payment.getCashflows().get(0);
		Contract contract = cashflow.getContract();
		Asset asset = contract.getAsset();
		Dealer dealer = contract.getDealer();
		Address address = dealer.getMainAddress();
		
		String mobilePhone = dealer.getMobile();
		String workPhone = dealer.getTel();
		StringBuffer dealerPhoneNumbers = new StringBuffer(); 
		
		if (workPhone != null && !workPhone.isEmpty()) {
			if (mobilePhone != null && !mobilePhone.isEmpty()) {
				dealerPhoneNumbers.append(getDefaultString(workPhone));
				dealerPhoneNumbers.append(", ");
				dealerPhoneNumbers.append(getDefaultString(mobilePhone));
			} else {
				dealerPhoneNumbers.append(getDefaultString(workPhone));
			}
		} else {
			if (mobilePhone != null && !mobilePhone.isEmpty()) {
				dealerPhoneNumbers.append(getDefaultString(mobilePhone));
			} else {
				dealerPhoneNumbers.append(getDefaultString(""));
			}
		}
		
		// initialize list of departments in some way
		Map<String, String> beans = new HashMap<String, String>();
		beans.put("poNo", getDefaultString(payment.getReference().replaceAll("-OR", "")));
		beans.put("poDate", DateUtils.getDateLabel(payment.getPaymentDate(),
				DateUtils.DEFAULT_DATE_FORMAT));
		beans.put("dealer", getDefaultString(dealer.getNameEn()));
		beans.put("houseNo", getDefaultString(address.getHouseNo()));
		beans.put("strNo", getDefaultString(address.getStreet()));
		beans.put("commue", getDefaultString(address.getCommune().getDesc()));
		beans.put("district", getDefaultString(address.getDistrict().getDesc()));
		beans.put("province", getDefaultString(address.getProvince().getDesc()));
		beans.put("phoneNumber", dealerPhoneNumbers.toString());
		beans.put("item", "" + getDefaultString(asset.getDescEn()));
		beans.put("description", asset.getDescEn() +"," + asset.getEngine().getDescEn());
		beans.put("color", getDefaultString(asset.getColor().getDescEn()));
		beans.put("model", getDefaultString(asset.getYear().toString()));
		beans.put("qty", "1");

		beans.put("price", AmountUtils.format(asset.getTiAssetPrice()));
		beans.put("itemAmount", AmountUtils.format(asset.getTiAssetPrice()));
		
		ECurrency currency = ECurrency.getDefault();
		String symbol = "$";
		if (currency != null && StringUtils.isNotEmpty(currency.getSymbol())) {
			symbol = currency.getSymbol();
		}
		
		beans.put("amount", symbol + " " + AmountUtils.format(asset.getTiAssetPrice()));
		String templatePath = AppConfig.getInstance().getConfiguration()
				.getString("specific.templatedir");
		String templateFileName = templatePath + "/GLFPurchaseOrder.xlsx";
		String outputPath = AppConfig.getInstance().getConfiguration()
				.getString("specific.tmpdir");

		String prefixOutputName = "GLFPurchaseOrder";
		String sufixOutputName = "xlsx";
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String xlsFileName = outputPath + "/" + prefixOutputName + uuid + "."
				+ sufixOutputName;
		XLSTransformer transformer = new XLSTransformer();	
		transformer.transformXLS(templateFileName, beans, xlsFileName);
		
		return prefixOutputName + uuid + "." + sufixOutputName;
	}
	
	/**
	 * @param value
	 * @return
	 */
	private String getDefaultString(String value) {
		return StringUtils.defaultString(value);
	}
}