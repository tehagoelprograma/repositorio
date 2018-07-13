package test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import es.carlos.JPAeHibernate.modelo.Direccion;
import es.carlos.JPAeHibernate.modelo.Empleado;

public class TestEmpleados {



	private static 	EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistencia"); //en persistencia.xml

	public static void main(String[] args) {



		insertInnicial();



		imprimirTodo();

		EntityManager manager = emf.createEntityManager() ;
		manager.getTransaction().begin();
		Empleado e = manager.find(Empleado.class, 12L);
		e.setApellidos("lopez lopez");
		manager.getTransaction().commit();

		manager.close();
		System.exit(0);
	}

	private static void insertInnicial() {

		EntityManager manager = emf.createEntityManager() ;

		Empleado e = new Empleado(12L , "Perez", "pepito", LocalDate.of(1969, 7, 12) );
		Direccion d = new Direccion(15L, "falsea", "Springfield");
		e.setDireccion(d);
		manager.getTransaction().begin();

		manager.persist(e);


		manager.getTransaction().commit();
		manager.close();
	}

	@SuppressWarnings("unchecked")
	private static void imprimirTodo() {
		EntityManager manager = emf.createEntityManager() ;
		List<Empleado> le= (List<Empleado>) manager.createQuery("FROM Empleado").getResultList();
		for(Empleado e : le) {

			System.out.println(e.toString());
		}
	}

}
