package db.map;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.AdministradorConexion;
import model.Equipo;

public class EquipoBD {
	/**
	 * Obtiene de la base de datos el equipo con licencia igual al par�metro licenciaEquipo; 
	 *    creando un objeto del tipo model.Equipo
	 * @param licenciaEquipo
	 * @return
	 */
	public static Equipo getById(String licenciaEquipo) {
		Equipo result; 
		String query;
		query = "select * from equipo where licencia=?" +  ";";
		try (PreparedStatement st = AdministradorConexion.prepareStatement(query)) {
			st.setString(1, licenciaEquipo);
			ResultSet rs = st.executeQuery();
			if(rs.next()){
				String licencia = rs.getString(1);
				String nombre= rs.getString(2);
				int telefono= rs.getInt(3); 
				String nombreClub= rs.getString(4); 
				int categoriaEdad= rs.getInt(5);
				int categoriaCompeticion= rs.getInt(6);

				result = new Equipo(licencia, nombre, telefono, nombreClub, categoriaEdad,
						categoriaCompeticion);

				return result; 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
