package com.nokor.ersys.core.hr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.impl.MainEntityServiceImpl;
import org.seuksa.frmk.tools.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.ersys.core.hr.dao.EmployeeDao;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.ersys.core.hr.service.EmployeeRestriction;
import com.nokor.ersys.core.hr.service.EmployeeService;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.helper.SeuksaAppConfigFileHelper;
import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.service.SecUserCreationException;

/**
 * 
 * @author prasnar
 *
 */
@Service("employeeService")
public class EmployeeServiceImpl extends MainEntityServiceImpl implements EmployeeService {
	/** */
	private static final long serialVersionUID = -9033024728615051196L;

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String SELECT_EMPLOYEES_LIKE_NAME = 
			  "select e.emp_id, e.per_lastname, e.per_firstname, c.com_id, c.com_name, j.job_pos_desc "
			+ "from tu_employee e "
			+ "inner join tu_company c on e.com_id=c.com_id "
			+ "left outer join ts_job_position j on e.job_pos_id=j.job_pos_id "
			+ "where (e.sta_rec_id=" + EStatusRecord.ACTIV.getId() + " or e.sta_rec_id is null) "
			+ "and (c.sta_rec_id=" + EStatusRecord.ACTIV.getId() + " or c.sta_rec_id is null) "
			+ "and (e.per_lastname like '%%1%' or e.per_firstname like '%%1%')";

	@Autowired
    private EmployeeDao dao;
	
	
	/**
	 * 
	 */
    public EmployeeServiceImpl() {
    	super();
    }


	@Override
	public EmployeeDao getDao() {
		return dao;
	}
       
	@Override
	public void createProcess(MainEntity mainEntity) throws DaoException {
		getDao().create(mainEntity);
	}

	@Override
	public void updateProcess(MainEntity mainEntity) throws DaoException {
		getDao().update(mainEntity);
	}

	@Override
	public void deleteProcess(MainEntity mainEntity) throws DaoException {
		throwIntoRecycledBin(mainEntity);
	}
	
	@Override
	public Employee findFirstEmployeeByUser(Long secUsrId) {
		List<Employee> lstEmp = findEmployeesByUser(secUsrId);
	    return lstEmp != null && lstEmp.size() > 0 ? lstEmp.get(0) : null;
	}
	
	@Override
	public Employee findEmployeeByUser(Long secUsrId) {
		EmployeeRestriction restriction = new EmployeeRestriction();
		restriction.setSecUsrId(secUsrId);
        return dao.getUnique(restriction);
	}

	@Override
	public List<Employee> findEmployeesByUser(Long secUsrId) {
		EmployeeRestriction restriction = new EmployeeRestriction();
		restriction.setSecUsrId(secUsrId);
        List<Employee> lstEmp = dao.list(restriction);
        
        return lstEmp;
	}
	
	@Override
	public SecUser createUser(Employee emp, SecProfile defaultProfile) {
    	return createUser(emp, null, null, null, defaultProfile);
    }

    @Override
	public SecUser createUser(Employee emp, String login, String descLogin, String passwordRaw, SecProfile defaultProfile) {
    	try {
			if (emp == null) {
				logger.info("The employee is not valid.");
				return null;
			}
			
			if (defaultProfile == null) {
	        	throw new SecUserCreationException("A default profile is mandatory.");
	        }

			
			if (StringUtils.isEmpty(login)) {
//			if (StringUtils.isNotEmpty(emp.getEmailPro())) {
//				login = emp.getEmailPro().trim();
//				descLogin = emp.getFullName();
//			} else 
				if (StringUtils.isNotEmpty(emp.getFullName())) {
					login = "";
					if (StringUtils.isNotEmpty(emp.getLastName())) {
						login = emp.getLastName().trim();
					}
					if (StringUtils.isNotEmpty(emp.getFirstName())) {
						if (StringUtils.isNotEmpty(emp.getLastName())) {
							login += "." ;
						}
						login += emp.getFirstName().trim();
					}
				} else {
					login = SeuksaAppConfigFileHelper.getLoginPrefix().trim() + emp.getId();
				}
			}
			
			if (StringUtils.isEmpty(descLogin)) {
				descLogin = emp.getFullName();
				if (StringUtils.isEmpty(descLogin)) {
					descLogin = login;
				}
			}

	    	login = login.toLowerCase();

	    	if (StringUtils.isEmpty(passwordRaw)) {
	    		passwordRaw = SecurityHelper.getGenerateRandomSecurePassword();
	    		emp.setPwdInTmp(passwordRaw);
	    	}
			SecUser secUser = FrmkServicesHelper.SECURITY_SRV.createUser(login, descLogin, passwordRaw, defaultProfile);
			if (secUser != null) {
				emp.setSecUser(secUser);
				getDao().saveOrUpdate(emp);
				logger.info("Employee[" + emp.getId() + "][" + emp.getFullName() + "] with the user [" + secUser.getId() + "] [" + login + "] [" + descLogin + "] has been created.");
			}
			return secUser;
    	} catch (Exception e) {
    		logger.error(e.getMessage());
    		throw new SecurityException(e);
    	}
	}

    @Override
    public void updatePassword(Employee emp, String passwordRaw) {
		if (StringUtils.isEmpty(passwordRaw)) {
			logger.info("The employee [" + emp.getFullName() + "] has no new password to change.");
			return;
		}

    	SecUser secUser = emp.getSecUser();

    	if (!StringUtils.isEmpty(passwordRaw)) {
    		emp.setPwdInTmp(passwordRaw);
    	}
    	FrmkServicesHelper.SECURITY_SRV.changePassword(secUser, passwordRaw);
		if (secUser != null) {
			emp.setSecUser(secUser);
			getDao().merge(emp);
			logger.info("Employee[" + emp.getId() + "][" + emp.getFullName() + "] with the user [" + secUser.getId() + "] [" + secUser.getLogin() + "] has updated his/her password.");
		}
    	
    }
    
    /**
     * 
     * Note: SecUser will be not removed if it is already used by another employee
     */
    @Override
 	public boolean removeUserAccess(Employee emp) {
 		if (emp == null || emp.getSecUser() == null || emp.getId() == null || emp.getId() <= 0) {
 			return false;
 		}
 		try {
 			List<Employee> employeeBySecIdList = new ArrayList<Employee>();
 	    	
 	        if (emp != null || emp.getSecUser() == null) {
 	        	employeeBySecIdList = findEmployeesByUser(emp.getSecUser().getId());
 	        }
 	        
 	        if (employeeBySecIdList.isEmpty()) {
 	        	FrmkServicesHelper.SECURITY_SRV.removeUser(emp.getSecUser());
 	        }
 		} catch (Exception e) {
 			logger.error("Error while removing of employee - MySecurityService.removeUser(emp) [" + emp.getId() + ", " + emp.getSecUser().getLogin() + "]", e);
 			return false;
 		}
 		
 		try {
 			emp.setSecUser(null);
 			getDao().saveOrUpdate(emp);
 		} catch (Exception e) {
 			logger.error("Error while removing of employee - saveOrUpdate(emp) [" + emp.getId() + "]", e);
 			return false;
 		}
 		return true;
 	}
    
    
    @Override
	public boolean existsEmployeeWithEmail(String emailPro) {
		return findFirstEmployeeByEmail(emailPro) != null;
	}
	
	@Override
	public boolean existsEmployeeWithEmailExcept(String emailPro, Long exceptEmpId) {
		return findFirstEmployeeByEmailExcept(emailPro, exceptEmpId) != null;
	}
	
	@Override
	public Employee findFirstEmployeeByEmail(String emailPro) {
		List<Employee> lstEmp = findEmployeesByEmail(emailPro);
	    return lstEmp != null && lstEmp.size() > 0 ? lstEmp.get(0) : null;
	}
	
	@Override
	public Employee findFirstEmployeeByEmailExcept(String emailPro, Long exceptEmpId) {
		List<Employee> lstEmp = findEmployeesByEmailExcept(emailPro, exceptEmpId);
	    return lstEmp != null && lstEmp.size() > 0 ? lstEmp.get(0) : null;
	}
	
	@Override
	public List<Employee> findEmployeesByEmail(String emailPro) {
		return findEmployeesByEmailExcept(emailPro, null);
	}
	@Override
	public List<Employee> findEmployeesByEmailExcept(String emailPro, Long exceptEmpId) {
		Criteria criteria = createCriteria(Employee.class);
    	criteria.add(Restrictions.eq(Employee.EMAILPERSO, emailPro));
    	if (exceptEmpId != null) {
        	criteria.add(Restrictions.ne(Employee.ID, exceptEmpId));
		}
        criteria.add(Restrictions.or(Restrictions.eq(Employee.STATUSRECORD, EStatusRecord.ACTIV), Restrictions.isNull(Employee.STATUSRECORD)));
        List<Employee> lstEmp = criteria.list();
        
        return lstEmp;
	}

    @Override
    public List<?> listEmployeesInfoByName(String name) {
		List<?> lstEmpsArr = getDao().createSqlQuery(SELECT_EMPLOYEES_LIKE_NAME.replace("%1",  name)).list(); 
    	return lstEmpsArr;
    }


    @Override
    public List<Long> listEmpIdByName(String name) {
    	List<Long> lstEmpIds = new ArrayList<Long>();
		List<?> lstEmpsArr = getDao().createSqlQuery(SELECT_EMPLOYEES_LIKE_NAME.replace("%1",  name)).list();
    	
    	for (Object empArray : lstEmpsArr) {
			Object[] empData = (Object[]) empArray;
			Long empId = Long.valueOf(empData[0].toString());
			lstEmpIds.add(empId);
		}
        
    	return lstEmpIds;
    }


    @Override
	public Employee findFirstEmployeeByEmailAndId(String email, Long empId) {
		Criteria criteria = createCriteria(Employee.class);
        criteria.createAlias("actor", "act");
    	criteria.add(Restrictions.eq("act.email", email));
    	if (empId != null) {
    		criteria.add(Restrictions.ne("id", empId));
    	}
        criteria.add(Restrictions.or(Restrictions.eq("act.statusRecord", EStatusRecord.ACTIV), Restrictions.isNull("act.statusRecord")));
        criteria.add(Restrictions.or(Restrictions.eq("statusRecord", EStatusRecord.ACTIV), Restrictions.isNull("statusRecord")));
        List<Employee> lstEmp = criteria.list();
        
        return lstEmp != null && lstEmp.size() > 0 ? lstEmp.get(0) : null;
	}
    

}
