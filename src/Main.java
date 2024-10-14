import java.util.Scanner;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.print("MENU");
            System.out.print("\n1.Introduce alumno");
            System.out.print("\n2.Introduce matricula");
            System.out.print("\n3.Introduce asignatura");
            System.out.print("\n4.Mostrar informacion alumno");
            System.out.print("\n5.Volcar todos los alumnos a fichero.");
            System.out.print("\n6.Borrar todo\n");
            opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                    introducirAlumnos();
                    break;
                case 2:
                    introducirMatricula();
                    break;
                case 3:
                    introducirAsignatura();
                    break;
                case 4:
                    mostrarAlumno();
                    break;
                case 5:
                    volcarAlumno();
                    break;
                case 6:
                    borrar();
                    break;
                default:
                    System.out.print("Numero incorrecto vuelva a escribir uno");
            }
        } while (opcion != 3);
        sc.close();
    }

    private static boolean introducirAlumnos() {
        String dni,nombreCompleto,fechaNac,direccion;
        boolean retorno = true;
        Alumno alumnoIntroducido;
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce los datos del alumno a introducir:");
        System.out.println("DNI:");
        dni = sc.nextLine();
        System.out.println("Nombre completo:");
        nombreCompleto = sc.nextLine();
        System.out.println("Fecha de nacimiento(Formato DD/MM/AAAA)");
        fechaNac = sc.nextLine();
        System.out.println("Introduce tu direccion");
        direccion = sc.nextLine();

        alumnoIntroducido = new Alumno(dni,nombreCompleto,fechaNac,direccion);

        try{
            ObjectOutputStream out=null;
                out = new ObjectOutputStream(new FileOutputStream("ALUMNOS.DAT"));
            out.writeObject(alumnoIntroducido);
        }catch(IOException ex){
            retorno = false;
        }
        return retorno;
    }
    private static void introducirMatricula() {

    }
    private static void introducirAsignatura() {

    }
    private static void mostrarAlumno() {

    }
    private static void volcarAlumno() {

    }
    private static void borrar() {

    }

}