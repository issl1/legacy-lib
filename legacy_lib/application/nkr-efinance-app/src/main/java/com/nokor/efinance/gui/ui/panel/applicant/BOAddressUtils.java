package com.nokor.efinance.gui.ui.panel.applicant;
import com.nokor.ersys.core.hr.model.address.Address;

public class BOAddressUtils {

	/**
	 * Copy an address
	 * @param source
	 * @param dest
	 * @return
	 */
	public static Address copy(Address source, Address dest) {
		if (source != null) {
			dest.setCountry(source.getCountry());
			dest.setProvince(source.getProvince());
			dest.setDistrict(source.getDistrict());
			dest.setCommune(source.getCommune());
			dest.setVillage(source.getVillage());
			dest.setHouseNo(source.getHouseNo());
			dest.setStreet(source.getStreet());
			dest.setFreeField1(source.getFreeField1());
			dest.setFreeField2(source.getFreeField2());
			dest.setFreeField3(source.getFreeField3());
			dest.setFreeField4(source.getFreeField4());
			dest.setLine1(source.getLine1());
			dest.setLine2(source.getLine2());
			dest.setLine3(source.getLine3());
		}
		return dest;
	}
	
}
