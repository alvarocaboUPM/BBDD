package ui;

import java.sql.SQLException;
import java.util.List;

import db.map.CategoriaEdadBD;
import db.map.EquipoBD;
import db.map.JugadorBD;
import db.stats.Estadisticas;
import model.CategoriaEdad;
import model.Equipo;
import model.Jugador;

public class Main {
	// Prueba la creaci�n, carga y borrado de Categor�as de Edad
	public static void pruebaModificacionCategorias() throws SQLException {
		List<CategoriaEdad> categorias = CategoriaEdadBD.getAll();
		CategoriaEdad ce = new CategoriaEdad("Prueba", "Prueba de creación", 99, 110);
		categorias.add(ce);
		categorias.get(0).setEdadMinima(-2);
		CategoriaEdadBD.saveAll(categorias);
		//System.out.println(categorias);
		
		CategoriaEdadBD.deleteCategoria(ce);
		categorias.remove(categorias.size()-1);
		categorias.get(0).setEdadMinima(4);
		CategoriaEdadBD.saveAll(categorias);
		//System.out.println(categorias);
		
	}
	
	// Pruebas b�sicas
	// NO MODIFICAR LAS CABECERAS DE NINGUN METODO
	public static void main(String[] args) throws SQLException {
		
		//pruebaModificacionCategorias();
		List<CategoriaEdad> categorias = CategoriaEdadBD.getAll();
		for (CategoriaEdad categoriaEdad : categorias) {
			System.out.println(categoriaEdad.getNombre());
		}
		List<Jugador> jugadores = JugadorBD.getAll();
		System.out.println(jugadores.size());
		Equipo e = EquipoBD.getById("10451534");
		System.out.println(e);
		
		List<Jugador> resJugAnioEq = Estadisticas.getJugadoresEquipoAnio(e, 2021);
		System.out.println(resJugAnioEq);
		
		List<Jugador> resNoJugAnio = Estadisticas.getJugadoresNoHanEstadoEnEquipo(2021);
		System.out.println(resNoJugAnio.size());
		
		long startTime = System.nanoTime();
		System.out.println(Estadisticas.getNumeroMaximoEquiposDelMismoClubHaEstadoUnJugador());
		System.out.printf("Duración %d\n",System.nanoTime() - startTime); 
		
		List<Jugador> resJugMasAnios = Estadisticas.getJugadoresMasEquiposMismoClub();
		System.out.println(resJugMasAnios.size());
	}
}
