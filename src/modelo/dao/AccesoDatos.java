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

	// TERCERA EVALUACION
	public Connection conexion(String dominio, String bd, String usr, String clave) {
		try {
			String url = "jdbc:mysql://" + dominio + "/" + bd;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, usr, clave);
			System.out.println("¡¡Has conectado con la bbdd!!");
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
		System.out.println("¡¡NO has conectado con la bbdd!!");
		return null;

	}

	public void actualizaTabla(String dominio, String bd, String usr, String clave, String sql) {
		try {
			Connection conexion = this.conexion(dominio, bd, usr, clave);
			// + " where 1=2"
			Statement stm = conexion.createStatement();
			int resultado = stm.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public ArrayList<HashMap<String, Object>> getAllRecords(String dominio, String bd, String usr, String clave,
			String tabla) {

		try {
			ArrayList<HashMap<String, Object>> registros = new ArrayList<HashMap<String, Object>>();
			Connection conexion = this.conexion(dominio, bd, usr, clave);
			String sql = "SELECT * FROM " + tabla;
			// + " where 1=2"
			Statement stm = conexion.createStatement();
			ResultSet rs = stm.executeQuery(sql);

			ResultSetMetaData metaData = rs.getMetaData();
			rs.first();
			if (rs.getRow() == 0) {
				System.out.println("NO HAY REGISTROS");
				stm.close();
				rs.close();
				return null;
			} else
				rs.beforeFirst();
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

	public void consultaPadronCAProvincias() {

		try {
			Connection conexion = this.conexion("localhost", "paro", "root", "");
			String sql = "select CA as Comunidad, provincia, sum(padron) as padron from padron pa inner join provincias p1, comunidadesautonomas c1, municipios m1 "
					+ " where pa.CodMunicipio = m1.CodMunicipio and m1.CodProvincia = p1.CodProvincia and p1.CodCA = c1.CodCA group by p1.CodProvincia order by c1.CA, p1.Provincia";
			Statement stm = conexion.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			int subtotal = 0, total = 0;
			String ca_ant = "";
			while (rs.next()) {

				if (!rs.getString(1).equals(ca_ant) ) {
					//&& !ca_ant.equals("")
					System.out.println("COMUNIDAD AUTONOMA : " + rs.getString(1));
					total += subtotal;
					
					System.out.println(" TOTAL COMUNIDAD AUTONOMA : " + ca_ant  + ", " + subtotal);
					subtotal = 0;
					ca_ant= rs.getString(1);
				}
				System.out.println("\t\t" +  rs.getString(2) + " = " + rs.getInt(3) );
				subtotal += rs.getInt(3);

			}
			System.out.println("TOTAL ESPAÑA : " + total);
			rs.close();
			stm.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
