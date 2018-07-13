package es.carlos.JPAeHibernate.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table (name= "EMPLEADO")
public class Empleado implements Serializable{
	



	private static final long serialVersionUID = 1L;
	
	@Id
	@Column (name = "Cod_empleado")
	private Long codigo;
	

	private String apellidos;

	private String nombre;
	@Column
	private LocalDate fechaNacimiento;
	@OneToOne (cascade= {CascadeType.ALL}) //para que se inserten los objetos dependientes automaticamente, si no lo haces hay que persistirlos antes
	@JoinColumn (name = "ID_DIRECCION") //empleado es el propietario de esta relacion, se añade a empleado una columna ID_DIRECCION que referencia a la otra
	private Direccion direccion;
	


	public Empleado() {
		//necesario para Hibernate
	}
	
	public Empleado(Long codigo, String apellidos, String nombre, LocalDate fechaNacimiento) {
		
		this.codigo = codigo;
		this.apellidos = apellidos;
		this.nombre = nombre;
		this.fechaNacimiento = fechaNacimiento;
	}
	
	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	@Override
	public String toString() {
		return "Empleado [codigo=" + codigo + ", apellidos=" + apellidos + ", nombre=" + nombre + ", fechaNacimiento="
				+ fechaNacimiento + ", direccion=" + direccion + "]";
	}




}
