package com.ecart.dao;

import java.util.ResourceBundle;

public class DAOFactory {
	private DAO dao;
	
	public DAOFactory() {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("db");
		String db = resourceBundle.getString("db");
		System.out.println("db"+db);
		if(db.equals("postgresql")) {
			dao = new DAOPostgresImplementation();
		}
	}
	
	public DAO getDAO() {
		return dao;
	}
}
