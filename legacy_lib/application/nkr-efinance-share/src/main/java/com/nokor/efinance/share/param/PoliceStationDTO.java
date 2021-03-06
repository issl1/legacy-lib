package com.nokor.efinance.share.param;

import com.nokor.common.messaging.share.refdata.RefDataDTO;
import com.nokor.ersys.messaging.share.address.ProvinceDTO;

/**
 * 
 * @author buntha.chea
 *
 */
public class PoliceStationDTO extends RefDataDTO {
	
	/**
	 */
	private static final long serialVersionUID = -2665232672385047129L;
	
	private ProvinceDTO provinceDTO;	
	
	/**
	 * @return the provinceDTO
	 */
	public ProvinceDTO getProvinceDTO() {
		return provinceDTO;
	}

	/**
	 * @param provinceDTO the provinceDTO to set
	 */
	public void setProvinceDTO(ProvinceDTO provinceDTO) {
		this.provinceDTO = provinceDTO;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof PoliceStationDTO)) {
			 return false;
		 }
		 PoliceStationDTO policeStationDTO = (PoliceStationDTO) arg0;
		 return getId() != null && getId().equals(policeStationDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
