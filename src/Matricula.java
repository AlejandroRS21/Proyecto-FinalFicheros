import java.io.*;
import java.util.Scanner;

public class Matricula implements Serializable {
    private static final Scanner sc = new Scanner(System.in);

    private int codMatricula;
    private String dni;
    private int codAsignatura;

    // Constructor
    public Matricula(int codMatric, String dni, int codAsig){
        this.codMatricula = codMatric;
        this.dni = dni;
        this.codAsignatura = codAsig;
    }

    // Getter
    public String getDni() {
        return dni;
    }
    public int getCodMatricula() {
        return codMatricula;
    }
    public int getCodAsignatura() {
        return codAsignatura;
    }
    // Setter
    public void setCodMatricula(int codMatricula) {
        this.codMatricula = codMatricula;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public void setCodAsignatura(int codAsignatura) {
        this.codAsignatura = codAsignatura;
    }

    // Metodo  que introduce una nueva matricula
    public static void introducirMatricula() {
        String dni;
        int codMatric, codAsig;

        // Recogida de datos
        System.out.println("Introduce el codigo de la matricula");
        codMatric = sc.nextInt();
        sc.nextLine();
        System.out.println("Introduce el DNI del alumno");
        dni = sc.nextLine();
        System.out.println("Introduce el codigo de la asignatura");
        codAsig = sc.nextInt();

        Matricula matricula = new Matricula(codMatric, dni, codAsig);
        File archivoMatriculas = new File(Main.FICHERO_DAT_MATRICULAS);

        // Verificar si el archivo ya existe para decidir si escribir el encabezado o no
        boolean existeArchivo = archivoMatriculas.exists();
        try (FileOutputStream fos = new FileOutputStream(archivoMatriculas, true);
             ObjectOutputStream out = existeArchivo ?
                     new ObjectOutputStream(fos) {
                         @Override
                         protected void writeStreamHeader() throws IOException {
                             reset(); // Evitar escribir el encabezado
                         }
                     } :
                     new ObjectOutputStream(fos)) {

            // Escribir el objeto Matricula en el archivo
            out.writeObject(matricula);
            out.flush();
            System.out.println("Matricula introducida correctamente");

        } catch (IOException e) {
            System.out.println("Error al introducir matricula: " + e.getMessage());
        }
    }





}
