package com.mysqldoc.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseConfigure<M extends BaseConfigure<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setIp(java.lang.String ip) {
		set("ip", ip);
	}

	public java.lang.String getIp() {
		return get("ip");
	}

	public void setPort(java.lang.Integer port) {
		set("port", port);
	}

	public java.lang.Integer getPort() {
		return get("port");
	}

	public void setUsername(java.lang.String username) {
		set("username", username);
	}

	public java.lang.String getUsername() {
		return get("username");
	}

	public void setPassword(java.lang.String password) {
		set("password", password);
	}

	public java.lang.String getPassword() {
		return get("password");
	}

	public void setDbtype(java.lang.Integer dbtype) {
		set("dbtype", dbtype);
	}

	public java.lang.Integer getDbtype() {
		return get("dbtype");
	}
	public void setSID(java.lang.String SID) {
		set("SID", SID);
	}

	public java.lang.String getSID() {
		return get("SID");
	}

}
