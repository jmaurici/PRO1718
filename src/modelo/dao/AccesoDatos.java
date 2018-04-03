package modelo.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class AccesoDatos {
	private String usuario;
	private String clave;
	private String host;
	private String bd;

	public AccesoDatos() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Connection conexion(String dominio, String bd, String usr, String clave) {
		try {
			String url = "jdbc:mysql://" + dominio + "/" + bd;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, usr, clave);
			System.out.println("��Has conectado con la bbdd!!");
			return con;
		} catch (InstantiationException e) {
			System.out.println(e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("��NO has conectado con la bbdd!!");
		return null;

	}

	public ArrayList<HashMap<String, Object>> getAllRecords(String dominio, String bd, String usr, String clave,
			String tabla) {

		try {
			ArrayList<HashMap<String, Object>> registros = new ArrayList<HashMap<String, Object>>();
			Connection conexion = this.conexion(dominio, bd, usr, clave);
			String sql = "SELECT * FROM " + tabla;
			Statement stm = conexion.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();

			while (rs.next()) {
				HashMap<String, Object> registro = new HashMap<String, Object>();
				registros.add(registro);
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					registro.put(metaData.getColumnName(i), rs.getString(i));
					System.out.print(metaData.getColumnName(i) + " => " + rs.getString(i) + "\t");
				}
				System.out.println();
			}
			stm.close();
			rs.close();
			return registros;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public ArrayList<ArrayList<Object>> getAllRecords2(String dominio, String bd, String usr, String clave,
			String tabla) {

		try {
			ArrayList<ArrayList<Object>> registros = new ArrayList<ArrayList<Object>>();
			Connection conexion = this.conexion(dominio, bd, usr, clave);
			String sql = "SELECT * FROM " + tabla;
			Statement stm = conexion.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			// primera fila: nombres de campos
			ArrayList<Object> registro = new ArrayList<Object>();
			registros.add(registro);
			for (int i = 1; i <= metaData.getColumnCount(); i++) 
				registro.add(metaData.getColumnName(i));
			// resto filas: valores de los campos	
			while (rs.next()) {
				registro = new ArrayList<Object>();
				registros.add(registro);
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					registro.add(rs.getString(i));
					System.out.print(metaData.getColumnName(i) + " => " + rs.getString(i) + "\t");
				}
				System.out.println();
			}
			stm.close();
			rs.close();
			return registros;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
