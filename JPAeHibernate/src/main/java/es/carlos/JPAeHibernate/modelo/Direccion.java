package es.carlos.JPAeHibernate.modelo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DIRECCION")
public class Direccion {
	




	@Id
	private Long id;
	
	private String direccion;
	
	private String localidad;
	
	@OneToOne( mappedBy = "direccion", fetch=FetchType.LAZY)
	private Empleado empleado;
	
	
	
	
	
	public Direccion() {
		
	}
	
	
	
	public Direccion(Long id, String direccion, String localidad) {
		
		this.id = id;
		this.direccion = direccion;
		this.localidad = localidad;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}



	@Override
	public String toString() {
		return "Direccion [id=" + id + ", direccion=" + direccion + ", localidad=" + localidad + ", empleado=" + empleado.getCodigo()+"]";
	}



	public Empleado getEmpleado() {
		return empleado;
	}



	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	
	
	
}
