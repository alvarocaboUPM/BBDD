package db.map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.AdministradorConexion;
import model.CategoriaCompeticion;

public class CategoriaCompeticionBD {

	/**
	 * 
	 * @param categoriaCompeticion
	 * @return Obtiene de la base de datos la categoria de competicion con id igual al parametro categoriaCompeticion, 
	 *    creando un objeto del tipo model.CategoriaCompeticion
	 * @throws SQLException
	 */
	public static CategoriaCompeticion getById(int categoriaCompeticion)  {
		CategoriaCompeticion result; 
		String query;
		query = "select * from categoria_competicion where id=?" + ";";
		try (PreparedStatement st = AdministradorConexion.prepareStatement(query)) {
			st.setInt(1, categoriaCompeticion);
			ResultSet rs = st.executeQuery();
			if(rs.next()){
				int id = rs.getInt("id");
				String nom = rs.getString(2);
				String desc = rs.getString(3);
				int e_max = rs.getInt(4);
				result = new CategoriaCompeticion(id, nom, desc, e_max);
				return result;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
