package pruebasMain;

public class principal {

	private static String path;
	
	
	public static void main(String[] args) {

		//Padre.metodo();
		//new Padre().metodo();
		//Hijo.metodo();
		//new Hijo().metodo();
		
		Padre objeto = new Hijo();
		objeto.metodo();  //aunque el objeto es Hijo, al ser metodo estatico se invoca al del Padre
		
		int a = 10;
		System.out.println("");
	}



}


abstract class Padre {
	static void metodo()  {
		System.out.println("Padre");
	}
}

class Hijo extends Padre {

	 static void metodo() {
		System.out.println("Hijo");
	}
}
