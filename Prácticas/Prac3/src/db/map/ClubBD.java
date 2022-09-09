package db.map;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.AdministradorConexion;
import model.Club;

public class ClubBD {
	/**
	 * Obtiene de la base de datos el club con nombre igual al par�metro nombreClub, 
	 *    creando un objeto del tipo model.Club
	 * @param nombreClub
	 * @return
	 */

	//Nombre es pk
	public static Club getById(String nombreClub) {
		Club result; String query;
		query = "select * from club where nombre=?";
		try (PreparedStatement st = AdministradorConexion.prepareStatement(query)) {
			st.setString(1, nombreClub);
			ResultSet rs = st.executeQuery();
			if(rs.next()){

				String nom = rs.getString("nombre");
				String calle = rs.getString(2);
				int numero = rs.getInt(3);
				int piso= rs.getInt(4);
				int escalera= rs.getInt(5);
				int cp= rs.getInt(6);
				String localidad= rs.getString(7);
				String telefono= rs.getString(8);
				String personaContacto= rs.getString(9);

				result = new Club(nom, calle, numero,
						piso, escalera, cp, localidad, telefono, personaContacto);
				return result;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
