import java.util.Scanner;
import java.io.*;

public class Main {
    public static final String DIRECTORIO_ALUMNOS = "Alumnos";
    public static final String FICHERO_DAT_ALUMNOS = "Alumnos/ALUMNOS.DAT";
    public static final String FICHERO_DAT_MATRICULAS = "Alumnos/MATRICULA.DAT";
    public static final String FICHERO_DAT_ASIGNATURAS = "Alumnos/ASIGNATURA.DAT";
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        Alumno.crearCarpetaAlumnos(); //metodo que crear el directorio Alumnos si no existe

        do {
            System.out.println("""
                    __________________________________________
                    MENU
                    1.Introduce alumno
                    2.Introduce matricula
                    3.Introduce asignatura
                    4.Mostrar informacion alumno
                    5.Volcar todos los alumnos a fichero.
                    6.Borrar todo
                    7.Salir
                    ___________________________________________
                    """);

            //Comprobar si la entrada es un numero
            while (!sc.hasNextInt()) {
                System.out.println("Por favor, introduce un número válido:");
                sc.next(); // Limpiar la entrada incorrecta
            }

            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1:
                    try {
                        if (Alumno.introducirAlumnos()) {
                            System.out.println("Se ha introducido el alumno correctamente");
                        } else {
                            System.out.println("No se ha podido introducir el alumno");
                        }
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case 2:
                    Matricula.introducirMatricula();
                    break;
                case 3:
                    Asignatura.introducirAsignatura();
                    break;
                case 4:
                    Alumno.mostrarAlumno();
                    break;
                case 5:
                    Alumno.volcarAlumno();
                    break;
                case 6:
                    borrar();
                    break;
                case 7:
                    break;
                default:
                    System.out.print("Numero incorrecto vuelva a escribir uno");
            }
        } while (opcion != 7);
        sc.close();
    }

    //Metodo que borra todos los archivos del programa
    private static void borrar() {
        File directorio = new File(Main.DIRECTORIO_ALUMNOS);

        if (directorio.isDirectory()) {
            File[] ficheros = directorio.listFiles();

            if (ficheros != null) {
                for (File fichero : ficheros) {
                    try {
                        if (fichero.isFile() && fichero.delete()) {
                            System.out.println("El fichero " + fichero.getName() + " se ha eliminado.");
                        }
                    } catch (Exception e) {
                        System.err.println("ERROR: No se pudo borrar el archivo : " + fichero.getName() + e.getMessage());
                    }
                }
            } else {
                System.out.println("No se pudieron listar los ficheros del directorio " + directorio.getPath() + " o no había ficheros en el directorio");

            }
        } else {
            System.out.println("La ruta proporcionada no es un directorio");
        }
    }


}