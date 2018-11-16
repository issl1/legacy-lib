package com.nokor.common.messaging.ws.resource.security.vo;

import java.util.ArrayList;
import java.util.List;

import com.nokor.common.messaging.share.BaseEntityDTO;

/**
 * 
 * @author prasnar
 *
 */
public class MenuVO extends BaseEntityDTO {
	/** */
	private static final long serialVersionUID = -783333212151996772L;

	private List<MenuItemVO> items = new ArrayList<>();
	
	/**
	 * 
	 */
	public MenuVO() {
		
	}

	/**
	 * @return the items
	 */
	public List<MenuItemVO> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<MenuItemVO> items) {
		this.items = items;
	}

	/**
	 * 
	 * @param menuItems
	 */
	public void initItems(List<Object[]> menuItems) {
		if (menuItems == null || menuItems.size() == 0) {
			throw new IllegalStateException("menuItems is empty.");
		}
		
		int iCol = 0;
		int COL_MNU_ITE_ID = iCol;
		int COL_MNU_ITE_CODE = ++iCol;
		int COL_MNU_ITE_DESC = ++iCol;
		int COL_MNU_ID = ++iCol;
		int COL_PARENT_MNU_ITE_ID = ++iCol;
		int COL_MNU_ITE_ACTION = ++iCol;
		int COL_MNU_ITE_IS_POPUP = ++iCol;
		int COL_MNU_ITE_ICON_PATH = ++iCol;
		int COL_MNU_ITE_STATUS_REC = ++iCol;

		for (int iRow = 0; iRow < menuItems.size(); iRow++) {
			Object[] currentRowVO = menuItems.get(iRow);
			
			long mnuIteId = Long.valueOf("" + currentRowVO[COL_MNU_ITE_ID]);
			String mnuIteCode = (String) currentRowVO[COL_MNU_ITE_CODE];
			String mnuIteDesc = (String) currentRowVO[COL_MNU_ITE_DESC];
			long mnuId = Long.valueOf("" + currentRowVO[COL_MNU_ID]);
			Long mnuIteParentId = currentRowVO[COL_PARENT_MNU_ITE_ID] != null ? Long.valueOf("" + currentRowVO[COL_PARENT_MNU_ITE_ID]) : null;
			String action = (String) currentRowVO[COL_MNU_ITE_ACTION];
			Boolean isPopup = (Boolean) currentRowVO[COL_MNU_ITE_IS_POPUP];
			String iconPath = (String) currentRowVO[COL_MNU_ITE_ICON_PATH];
			int statusRec = (int) currentRowVO[COL_MNU_ITE_STATUS_REC];

			MenuItemVO itemVO = new MenuItemVO();
			itemVO.setId(mnuIteId);
			itemVO.setCode(mnuIteCode);
			itemVO.setDesc(mnuIteDesc);
			itemVO.setMenuId(mnuId);
			itemVO.setParentItemId(mnuIteParentId);
			itemVO.setAction(action);
			itemVO.setIsPopup(isPopup);
			itemVO.setIconPath(iconPath);
			itemVO.setSortIndex(itemVO.getId().intValue());
			try {
				itemVO.setIsActive(statusRec == 1);
			} catch (Exception e) { 
				// ignore
			}
			
			getItems().add(itemVO);
		}
	}	
}
