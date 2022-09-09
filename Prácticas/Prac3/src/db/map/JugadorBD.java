package db.map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import db.AdministradorConexion;
import model.Jugador;

public class JugadorBD {
	/**
	 * Obtiene de la base de datos todos los jugador; 
	 *    devolviendo una lista de objetos del tipo model.Jugador
	 * @return
	 */
	public static List<Jugador> getAll() {
		List<Jugador> result; String query;
		result = new ArrayList<Jugador>();
		query = "select * from jugador;";
		try (PreparedStatement st = AdministradorConexion.prepareStatement(query)) {
			ResultSet rs = st.executeQuery();
			while(rs.next()){

				String nif = rs.getString(1);
				String nombre= rs.getString(2);
				String apellido1= rs.getString(3); 
				String apellido2= rs.getString(4);
				LocalDate fechaNacimiento= rs.getDate(5).toLocalDate();

				Jugador j1 = new Jugador(nif, nombre, apellido1, apellido2, fechaNacimiento);
				result.add(j1);
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Obtiene de la base de datos el jugador con nif igual al par�metro nifJugador; 
	 *    creando un objeto del tipo model.Jugador
	 * @param nifJugador
	 * @return
	 */
	public static Jugador getById(String nifJugador) {
		Jugador result; String query;
		query = "select * from jugador where id=?" + ";";
		try (PreparedStatement st = AdministradorConexion.prepareStatement(query)) {
			st.setString(1, nifJugador);
			ResultSet rs = st.executeQuery();
			System.out.println("Executed getById de JugadorBD");
			if(rs.next()){

				String nif = rs.getString(1);
				String nombre= rs.getString(2);
				String apellido1= rs.getString(3); 
				String apellido2= rs.getString(4);
				LocalDate fechaNacimiento= rs.getDate(5).toLocalDate();
				result = new Jugador(nif, nombre, apellido1, apellido2, fechaNacimiento);
				return result;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
