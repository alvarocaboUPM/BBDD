package db.map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList; //AÑADIDO
import java.util.List;

import db.AdministradorConexion;
import model.CategoriaEdad;

public class CategoriaEdadBD {
	/**
	 * Obtiene de la base de datos todas las categor�a de edad devolviendo una lista de objetos CategoriaEdad 
	 * @return
	 * @throws SQLException
	 */
	public static List<CategoriaEdad> getAll() {
		List<CategoriaEdad> result; 
		String query;
		result = new ArrayList<CategoriaEdad>();
		query = "select * from categoria_edad;";
		try (Statement st = AdministradorConexion.getStatement()) {
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				int id = rs.getInt("id");
				String nom = rs.getString(2);
				String desc = rs.getString(3);
				int e_min = rs.getInt(4);
				int e_max = rs.getInt(5);

				CategoriaEdad cat = new CategoriaEdad(id, nom, desc, e_min, e_max);
				result.add(cat);	
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param categoriaEdad
	 * @return Obtiene de la base de datos la categor�a de edad con id igual al par�metro categoriaEdad, 
	 *    creando un objeto del tipo model.CategoriaEdad
	 * @throws SQLException
	 */
	public static CategoriaEdad getById(int categoriaEdad) {
		CategoriaEdad result; String query;
		query = "select * from categoria_edad where id=?" + ";";
		try (PreparedStatement st = AdministradorConexion.prepareStatement(query)) {
			st.setInt(1, categoriaEdad);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
			int id = rs.getInt("id");
			String nom = rs.getString(2);
			String desc = rs.getString(3);
			int e_min = rs.getInt(4);
			int e_max = rs.getInt(5);

			result = new CategoriaEdad(id, nom, desc, e_min, e_max);
			return result;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param ce
	 * @return Borra de la base de datos la categor�a de edad con id igual al identificador del objeto ce
	 * @throws SQLException
	 */
	public static boolean deleteCategoria(CategoriaEdad ce) {
		boolean result=false; String query;
		query = "delete from categoria_edad where id=?";
		try (PreparedStatement st = AdministradorConexion.prepareStatement(query)) {
			st.setInt(1, ce.getId());
			int rs = st.executeUpdate();
			if(rs != 0){
				result = true;
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Este m�todo guarda en la base de datos (actualiza o crea) los objetos del tipo CategoriaEdad que recibe en
	 * la lista data
	 * Si el objeto CategoriaEdad tiene id igual a -1 (se ha creado en Java) se realiza un insert y se actualiza el
	 * id en el objeto
	 * Si el objeto tiene id (se ha recuperado de la bbdd) se hace un update
	 * @param data
	 */
	public static void saveAll(List<CategoriaEdad> data) {
		String queryInsert = "INSERT INTO categoria_edad (nombre, descripcion, edad_minima, edad_maxima) VALUES (?,?,?,?); " ;
		String queryUpdate = "UPDATE categoria_edad SET nombre=?, descripcion=?, edad_minima=?, edad_maxima=? WHERE id=?; " ;
		PreparedStatement psInsert = null, psUpdate = null;
		try {
			psInsert = AdministradorConexion.prepareStatement(queryInsert);
			psUpdate = AdministradorConexion.prepareStatement(queryUpdate);
			for (CategoriaEdad ce : data) {
				if (ce.getId()<0) {
					psInsert.setString(1, ce.getNombre());
					psInsert.setString(2, ce.getDescripcion());
					psInsert.setInt(3, ce.getEdadMinima());
					psInsert.setInt(4, ce.getEdadMaxima());
					//boolean done = 
					psInsert.execute();	
					ResultSet rs = psInsert.getGeneratedKeys();
					if (rs.next()) {
					  int newId = rs.getInt(1);
					  ce.setId(newId);
					}
				}
				else {
					psUpdate.setString(1, ce.getNombre());
					psUpdate.setString(2, ce.getDescripcion());
					psUpdate.setInt(3, ce.getEdadMinima());
					psUpdate.setInt(4, ce.getEdadMaxima());
					psUpdate.setInt(5, ce.getId());
					//boolean done = 
					psUpdate.execute();
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (psInsert!=null && !psInsert.isClosed())
					psInsert.close();
				if (psUpdate!=null && !psUpdate.isClosed())
					psUpdate.close();
					
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
	}
}
