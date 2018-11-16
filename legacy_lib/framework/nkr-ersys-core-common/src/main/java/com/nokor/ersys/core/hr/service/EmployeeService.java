package com.nokor.ersys.core.hr.service;

import java.util.List;

import org.seuksa.frmk.service.MainEntityService;

import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
public interface EmployeeService extends MainEntityService {

    List<?> listEmployeesInfoByName(String fullName);

	List<Long> listEmpIdByName(String name);

	List<Employee> findEmployeesByEmail(String email);

	List<Employee> findEmployeesByEmailExcept(String emailPro, Long exceptEmpId);

	Employee findFirstEmployeeByEmail(String email);
	
	Employee findFirstEmployeeByEmailExcept(String emailPro, Long exceptEmpId);

	Employee findFirstEmployeeByEmailAndId(String email, Long empId);
	
	boolean existsEmployeeWithEmailExcept(String emailPro, Long exceptEmpId);

	boolean existsEmployeeWithEmail(String emailPro);
	
	
	
	List<Employee> findEmployeesByUser(Long secUsrId);
	
	Employee findFirstEmployeeByUser(Long secUsrId);

	Employee findEmployeeByUser(Long secUsrId);

	SecUser createUser(Employee emp, SecProfile defaultProfile);

	SecUser createUser(Employee emp, String login, String descLogin, String passwordRaw, SecProfile defaultProfile);

	void updatePassword(Employee emp, String passwordRaw);

	boolean removeUserAccess(Employee emp);
	

}
