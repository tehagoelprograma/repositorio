package pruebasMain;

public class principal {

	public static void main(String[] args) {

Aspirante.metodo();
Becario.metodo();
Aspirante servicio3 = new Becario();
servicio3.metodo();
int a = 10;
System.out.println("");
	}



}


class Aspirante {
	static void metodo() {
		System.out.println("Aspirante");
	}
}

class Becario extends Aspirante {
	
	static void metodo() {
		System.out.println("Becario");
	}
}
