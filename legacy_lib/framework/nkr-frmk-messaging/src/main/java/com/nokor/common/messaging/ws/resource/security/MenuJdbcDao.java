package com.nokor.common.messaging.ws.resource.security;

import java.util.List;

import org.seuksa.frmk.dao.sql.SqlScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.messaging.ws.resource.security.vo.MenuItemVO;

/**
 * 
 * @author prasnar
 *
 */
public class MenuJdbcDao {
	private static final Logger logger = LoggerFactory.getLogger(MenuJdbcDao.class);

	/**
	 * 
	 * @return
	 */
	public static boolean resetMenu() {
		String sql = "update tu_menu set sec_ctl_id = null; "
				+ "update tu_menu_item set sec_ctl_id = null; "
				+ "delete from tu_menu_item; "
				+ "delete from tu_sec_control_profile_privilege; " // sec_ctl_typ_id = 1
				+ "delete from tu_sec_control_privilege; " // sec_ctl_typ_id = 1
				+ "delete from tu_sec_control;" // sec_ctl_typ_id = 1
				+ "select setval('tu_menu_item_mnu_ite_id_seq', 1);"
				+ "select setval('tu_sec_control_profile_privilege_sec_cpp_id_seq', 1);"
				+ "select setval('tu_sec_control_privilege_sec_cop_id_seq', 1);"
				+ "select setval('tu_sec_control_sec_ctl_id_seq', 1);"
				;
		List<Object[]> res = SqlScript.execSql(sql);
		return res != null;
	}
	/**
	 * 
	 * @param code
	 * @param where
	 */
	public static boolean createMenuItem(MenuItemVO itemVO) {
		String sql = "INSERT INTO tu_menu_item (mnu_ite_id, mnu_ite_code,mnu_ite_desc, mnu_id, parent_mnu_ite_id, mnu_ite_action, mnu_ite_is_popup, mnu_ite_icon_path, sort_index, sta_rec_id, dt_cre,usr_cre,dt_upd,usr_upd)"
				+ " values (" + itemVO.getId() + ", '" + itemVO.getCode() + "', '" + itemVO.getDesc() + "'," 
				+			itemVO.getMenuId()  + "," 
				+ 			(itemVO.getParentItemId() != null ? itemVO.getParentItemId() : "null") + ", '" + itemVO.getAction() + "', " 
				+ 			( Boolean.TRUE.equals(itemVO.getIsPopup()) ? "true" : "false") + ", "  
				+ 			(itemVO.getIconPath() != null ? "'" + itemVO.getIconPath() + "'" : "null") + "," 
				+			itemVO.getSortIndex() + ", " 
				+ 			(Boolean.TRUE.equals(itemVO.getIsActive()) ? "1" : "2") 
				+ 			", now(), 'system', now(), 'system'"
				+ ");";
		
		List<Object[]> res = SqlScript.execSql(sql);
		return res != null;
	}
	
	
}
