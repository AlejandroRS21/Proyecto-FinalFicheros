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

        // Verificar si la matrícula ya existe
        if (matriculaExiste(codMatric, dni, codAsig)) {
            System.out.println("Ya existe una matrícula con el código " + codMatric + " para el alumno con DNI " + dni + " en la asignatura con código " + codAsig);
            return; // Salir del método si la matrícula ya existe
        }
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

    // Metodo que comprueba si la matricula ya estaba introducida en ese alumno
    public static boolean matriculaExiste(int codMatricula, String dni, int codAsignatura) {
        File archivoMatriculas = new File(Main.FICHERO_DAT_MATRICULAS);

        // Verificar si el archivo existe antes de intentar leer
        if (!archivoMatriculas.exists()) {
            return false; // El archivo no existe, así que no hay matrículas
        }

        try (FileInputStream fis = new FileInputStream(archivoMatriculas);
             ObjectInputStream in = new ObjectInputStream(fis)) {

            while (true) {
                Matricula matricula = (Matricula) in.readObject();

                //Comprobar que existe una matricula con el mismo codigo de asignatura
                if (asignaturaMatriculada(matricula, dni, codAsignatura)) {
                    return true; // Se encontró una matrícula con el mismo DNI y código de asignatura
                }
            }
        } catch (EOFException e) {
            // Fin de archivo alcanzado, no hay más matrículas que leer
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer las matrículas: " + e.getMessage());
        }

        return false; // No se encontró ninguna matrícula con el mismo DNI y código de asignatura
    }

    // Metodo para comprobar si un alumno ya tiene una asignatura en concreto matriculada
    public static boolean asignaturaMatriculada(Matricula matricula, String dni, int codAsignatura) {
        return matricula.getDni().equals(dni) && matricula.getCodAsignatura() == codAsignatura;
    }
}

