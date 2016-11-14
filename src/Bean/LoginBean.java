package Bean;

import java.io.Serializable;

public class LoginBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String userName;
	
	public String userId;
	
	public String password;
	
	public String oldPassword;
	
	public String newPassword;

	public String createdOn;
	
	public String lastLogin;
	
	public String roleName;
	
	public Integer roleId;
	
	public Integer departmentId;
	
	public String departmentName;
	
	public String contactNo;
	
	public String confirmPassword;
	
	public boolean newPasswordDis;
	public boolean confirmPasswordDis; 
	
	public boolean oldPasswordDis;
	
	public boolean saveDis;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public boolean isNewPasswordDis() {
		return newPasswordDis;
	}

	public void setNewPasswordDis(boolean newPasswordDis) {
		this.newPasswordDis = newPasswordDis;
	}

	public boolean isOldPasswordDis() {
		return oldPasswordDis;
	}

	public void setOldPasswordDis(boolean oldPasswordDis) {
		this.oldPasswordDis = oldPasswordDis;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public boolean isConfirmPasswordDis() {
		return confirmPasswordDis;
	}

	public void setConfirmPasswordDis(boolean confirmPasswordDis) {
		this.confirmPasswordDis = confirmPasswordDis;
	}

	public boolean isSaveDis() {
		return saveDis;
	}

	public void setSaveDis(boolean saveDis) {
		this.saveDis = saveDis;
	}
	
}
