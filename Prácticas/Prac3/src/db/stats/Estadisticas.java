package db.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.map.*;

import model.Equipo;
import model.Jugador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import db.AdministradorConexion;

public class Estadisticas {
	/**
	 * M�todo que debe devolver el listado de los jugadores que no han estado en ning�n equipo 
	 * en el a�o recibido como par�metro
	 * @param anio
	 * @return
	 */
	public static List<Jugador> getJugadoresNoHanEstadoEnEquipo (int anio) throws SQLException {
		List<Jugador> res = new ArrayList<Jugador> ();
		String query = "SELECT j.nif, j.nombre, j.apellido_1, j.apellido_2, j.fecha_nacimiento " +   
				"FROM jugador j " +
				"INNER JOIN jugador_milita_equipo as jme ON j.nif=jme.nif_jugador " +
				"where (jme.fecha_fin < CAST(? AS DATE) or jme.fecha_inicio > CAST(? AS DATE))"+
				" UNION SELECT j.nif, j.nombre, j.apellido_1, j.apellido_2, j.fecha_nacimiento "
				+ "FROM jugador j "
				+ "LEFT JOIN jugador_milita_equipo jme ON j.nif=jme.nif_jugador "
				+ "WHERE jme.nif_jugador IS NULL;";

		try (PreparedStatement pst = AdministradorConexion.prepareStatement(query)) {
			pst.setString(1, anio + "-01-01");
			pst.setString(2, anio + "-12-31");

			ResultSet rs = pst.executeQuery();
			System.out.printf("Executed getJugadoresNoHanEstadoEnEquipo(%d)\n", anio);
			while (rs.next()) {
				String nif = rs.getString(1);
				String nombre= rs.getString(2);
				String apellido1= rs.getString(3); 
				String apellido2= rs.getString(4);
				LocalDate fechaNacimiento= rs.getDate(5).toLocalDate();
				Jugador jugRes = new Jugador(nif, nombre, apellido1, apellido2, fechaNacimiento);
				res.add(jugRes);
			}
			rs.close();
			pst.close();
			return res;
		}  catch (SQLException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
	/*Función auxiliar que devuelve el nº de equipos del mismo club
	en los que ha jugado un jugador entregado por parámetro*/
	
	private static int numeroEquiposJugador(Jugador jugador){
		//Obtenemos los equipos en los que ha jugado
		List<Equipo> equipos = new ArrayList<Equipo>();
		String query1="SELECT jme.licencia_equipo "+
		"from jugador as j inner join jugador_milita_equipo as jme ON j.nif=jme.nif_jugador "
		+"where j.nif= ?";
		try (PreparedStatement pst = AdministradorConexion.prepareStatement(query1)) {
			/* @param */
			pst.setString(1, jugador.getNif());
			//ResultSet
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Equipo e1=  EquipoBD.getById(rs.getString(1));
				equipos.add(e1);
			}
			//Cerramos la conexión
			rs.close();
			pst.close();
		}  catch (SQLException e) {
			e.printStackTrace();
		}

		if(equipos.isEmpty())
		return 0;
		//Ahora convertimos la lista de equipos en una tupla con clubes y su frecuencia
		Map<String, Integer> clubes= new HashMap<String,Integer >();
		for (Equipo equipo : equipos) {
			String c = equipo.getClub().getNombre();
			int prev=1;

			if(clubes.get(c) != null){
				prev = clubes.get(c)+1;
			}
			clubes.put(c, prev);
		}

		//Devolvemos el máximo número de equipos en un mismo club jugados

		Map.Entry<String, Integer> maxEntry = null;
			for (Map.Entry<String, Integer> entry : clubes.entrySet())
			{
				if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
				{
					maxEntry = entry;
				}
			}
		int local_max= 0;
		if(maxEntry!=null)
			local_max=maxEntry.getValue();
		return local_max;
	}

	/**
	 * M�todo que devuelve el n�mero de equipos del mismo club m�ximo en los que alg�n jugador ha estado
	 * @return
	 */
	public static int getNumeroMaximoEquiposDelMismoClubHaEstadoUnJugador(){
		int max =-1;
		//Primero obtenemos la lista de todos los jugadores
		List<Jugador> jugadores = JugadorBD.getAll();
		//Para cada jugador, consultamos el nº de equipos en los que ha jugado
		for (Jugador jugador : jugadores) {
			int local_max = numeroEquiposJugador(jugador);
			if(max < local_max) max = local_max;
		}
		return max;
	}
	
	/**
	 * M�todo que debe devolver el listado de los jugadores que han estado en el mayor n�mero de equipos
	 * del mismo club 
	 * @return
	 */
	public static List<Jugador> getJugadoresMasEquiposMismoClub(){
		int max = getNumeroMaximoEquiposDelMismoClubHaEstadoUnJugador();
		List<Jugador> result= new ArrayList<Jugador>();

		//Lista de todos los jugadores
		List<Jugador> jugadores = JugadorBD.getAll();
		for (Jugador jugador : jugadores) {
			int local_max = numeroEquiposJugador(jugador);
			if(max == local_max) 
				result.add(jugador);
			;
		}
		return result;
	}

	/**
	 * M�todo que debe devolver el listado de los jugadores que han estado en el equipo recibido como
	 * par�metro el a�o (anio)
	 * @param equipo
	 * @param anio
	 * @return
	 */
	public static List<Jugador> getJugadoresEquipoAnio(Equipo equipo, int anio) {
		//Comprobamos parámetros
		List<Jugador> res = new ArrayList<Jugador> ();
		if(equipo==null) return res;
			String query = "SELECT j.nif, j.nombre, j.apellido_1, j.apellido_2, j.fecha_nacimiento " +   
				"FROM jugador j " +
				"INNER JOIN jugador_milita_equipo as jme ON j.nif=jme.nif_jugador " +
				"where jme.fecha_inicio < CAST(? AS DATE) and jme.fecha_fin > CAST(? AS DATE) "+
				"AND jme.licencia_equipo = ? ";
	
			try (PreparedStatement pst = AdministradorConexion.prepareStatement(query)) {
				pst.setString(1, anio + "-12-31");
				pst.setString(2, anio + "-01-01");
				pst.setString(3, equipo.getLicencia());
				ResultSet rs = pst.executeQuery();
				System.out.printf("Jugadores que jugaron en el %s en el año %d\n", equipo.getNombre(), anio);
				while (rs.next()) {
					String nif = rs.getString(1);
					String nombre= rs.getString(2);
					String apellido1= rs.getString(3); 
					String apellido2= rs.getString(4);
					LocalDate fechaNacimiento= rs.getDate(5).toLocalDate();
					Jugador jugRes = new Jugador(nif, nombre, apellido1, apellido2, fechaNacimiento);
					res.add(jugRes);
				}
				rs.close();
				pst.close();
				return res;
			}  catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			return null;
	}
}
