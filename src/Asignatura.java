import java.io.*;
import java.util.Scanner;

public class Asignatura implements Serializable{
    private static final Scanner sc = new Scanner(System.in);

    private int codAsignatura;
    private String nombreAsignatura;

    // Constructor
    public Asignatura(int codAsig,String nombreAsig) {
        this.codAsignatura = codAsig;
        this.nombreAsignatura = nombreAsig;
    }

    // Getters and Setters
    public int getCodAsignatura() {
        return codAsignatura;
    }

    public void setCodAsignatura(int codAsignatura) {
        this.codAsignatura = codAsignatura;
    }

    public String getNombreAsignatura() {
        return nombreAsignatura;
    }

    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }

    // Introduce una asignatura nueva
    public static void introducirAsignatura() {
        String nombreAsig;
        int codAsig;

        // Recogida de datos
        System.out.println("Introduce el código de la asignatura:");
        codAsig = sc.nextInt();
        sc.nextLine();

        // Verificar si la asignatura ya existe
        if (Asignatura.asignaturaExists(codAsig)) {
            System.out.println("Ya existe una asignatura con el código " + codAsig + " introduzca otro numero.");
            return;
        }

        System.out.println("Introduce el nombre de la asignatura:");
        nombreAsig = sc.nextLine();


        Asignatura asignatura = new Asignatura(codAsig, nombreAsig);
        File archivoAsignaturas = new File(Main.FICHERO_DAT_ASIGNATURAS);

        // Verificar si el archivo ya existe para decidir si escribir el encabezado o no
        boolean existeArchivo = archivoAsignaturas.exists();

        try (FileOutputStream fis = new FileOutputStream(archivoAsignaturas, true);
             ObjectOutputStream out = existeArchivo ?
                     new ObjectOutputStream(fis) {
                         @Override
                         protected void writeStreamHeader() throws IOException {
                             reset(); // Evitar escribir el encabezado
                         }
                     } :
                     new ObjectOutputStream(fis)) {

            // Escribir el objeto asignatura en el archivo
            out.writeObject(asignatura);
            out.flush();
            System.out.println("Asignatura introducida correctamente");

        } catch (IOException e) {
            System.out.println("Error al introducir matricula: " + e.getMessage());
        }
    }

    // Metodo que devuelve un booleano que comprueba si existe la asignatura
    public static boolean asignaturaExists(int codAsig) {
        File archivoAsignaturas = new File(Main.FICHERO_DAT_ASIGNATURAS);

        // Verificar si el archivo existe antes de intentar leer
        if (!archivoAsignaturas.exists()) {
            return false; // El archivo no existe, así que no hay asignaturas
        }

        try (FileInputStream fis = new FileInputStream(archivoAsignaturas);
             ObjectInputStream in = new ObjectInputStream(fis)) {

            while (true) {
                Asignatura asignatura = (Asignatura) in.readObject();
                if (asignatura.getCodAsignatura() == codAsig) {
                    return true; // Se encontró una asignatura con el mismo código
                }
            }
        } catch (EOFException e) {
            // Fin de archivo alcanzado, no hay más asignaturas que leer
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer las asignaturas: " + e.getMessage());
        }

        return false; // No se encontró ninguna asignatura con el mismo código
    }

}
