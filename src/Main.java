import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;
        String dni;

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
                    try {
                        if (introducirAlumnos()) {
                            System.out.println("Se ha introducido el alumno correctamente");
                        } else {
                            System.out.println("No se ha podido introducir el alumno");
                        }
                    }catch(IOException ex){
                        System.out.println(ex.getMessage());
                    }
                    break;
                case 2:
                    introducirMatricula();
                    break;
                case 3:
                    introducirAsignatura();
                    break;
                case 4:
                    System.out.println("Introduce el dni del alumno del cual quieres informacion");
                    dni = sc.nextLine();
                    mostrarAlumno(dni);
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

    private static boolean introducirAlumnos() throws IOException {
        String dni,nombreCompleto,fechaNac,direccion;
        boolean retorno = true;
        Alumno alumnoIntroducido;
        Scanner sc = new Scanner(System.in);

        //Recodida de datos del alumno
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

        //Introducido en "ALUMNOS.DAT"
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("ALUMNOS.DAT"))) {
            out.writeObject(alumnoIntroducido);
            out.flush();
        } catch (IOException ex) {
            //Si no se introduce devuelve false la función
            retorno = false;
        }
        return retorno;
    }
    private static void introducirMatricula() {

    }
    private static void introducirAsignatura() {

    }
    private static String mostrarAlumno(String dni) {
        boolean finArchivo = false;
        boolean alumnoEncontrado = false;
        String retorno = "No se ha podido encontrar el alumno en el archivo 'ALUMNOS.DAT'";
        ArrayList<Alumno> listaAlumnos = new ArrayList<>();
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream("ALUMNOS.DAT"))){
            while(!finArchivo){
                try{
                    listaAlumnos.add((Alumno)in.readObject());
                }catch(EOFException ex){
                    finArchivo = true;
                }catch(IOException | ClassNotFoundException ex){
                    System.out.println(ex.getMessage());
                }
            }

            for(Alumno alumno : listaAlumnos){
                if(alumno.getDni().equals(dni)){
                    retorno = alumno.toString();
                    alumnoEncontrado = true;
                }
            }

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return retorno;
    }
    private static void volcarAlumno() {

    }
    private static void borrar() {

    }

}