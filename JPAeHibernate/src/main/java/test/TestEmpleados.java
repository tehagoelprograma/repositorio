package test;

import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import es.carlos.JPAeHibernate.modelo.Empleado;

public class TestEmpleados {
	
	
	private static EntityManager manager;
	private static EntityManagerFactory emf;
	
	public static void main(String[] args) {
		
		emf = Persistence.createEntityManagerFactory("Persistencia"); //en persistencia.xml
		manager = emf.createEntityManager() ;
		
		Empleado e = new Empleado(12L , "Perez", "pepito", new GregorianCalendar(1979, 6 , 6).getTime());
		Empleado e2 = new Empleado(10L , "Perez", "pepito", new GregorianCalendar(1979, 6 , 6).getTime());
		manager.getTransaction().begin();
		
		manager.persist(e);
		manager.persist(e2);
		
		manager.getTransaction().commit();
		
		
		
		imprimirTodo();
		manager.close();
	}

	@SuppressWarnings("unchecked")
	private static void imprimirTodo() {
		List<Empleado> le= (List<Empleado>) manager.createQuery("FROM Empleado").getResultList();
		for(Empleado e : le) {
		
		System.out.println(e.toString());
		}
	}

}
