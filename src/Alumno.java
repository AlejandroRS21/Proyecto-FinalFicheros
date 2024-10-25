import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Alumno implements Serializable {
    private static final Scanner sc = new Scanner(System.in);

    private String dni;
    private String nombreCompleto;
    private String fechaNac;
    private String direccion;


    //Constructor
    public Alumno(String dni, String nombreCompleto, String fechaNac, String direccion) {
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        this.fechaNac = fechaNac;
        this.direccion = direccion;
    }

    //Getter
    public String getDni() {
        return dni;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    //Setter
    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Metodo que introduce los datos de un alumno
    public static boolean introducirAlumnos() throws IOException {

        String dni, nombreCompleto, fechaNac, direccion, intentoAlumno;
        boolean retorno = true;
        boolean alumnoExistente = false;
        Alumno alumnoIntroducido;
        File archivoAlumno = new File(Main.FICHERO_DAT_ALUMNOS);
        ArrayList<Alumno> listaAlumno = new ArrayList<>();
        ObjectInputStream inAlumno;

        // Recogida de datos del alumno
        System.out.println("Introduce los datos del alumno a introducir:");
        System.out.println("DNI:");
        dni = sc.nextLine();

        // Leer alumnos existentes
        if (archivoAlumno.exists()) {
            try {
                inAlumno = new ObjectInputStream(new FileInputStream(archivoAlumno));
                while (true) {
                    try {
                        listaAlumno.add((Alumno) inAlumno.readObject());
                    } catch (EOFException ex) {
                        break; // Fin del archivo
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }

        // Comprobar si el alumno ya existe
        for (Alumno alumno : listaAlumno) {
            intentoAlumno = alumno.getDni();
            if (intentoAlumno.equals(dni)) {
                alumnoExistente = true;
                retorno = false;
                System.out.println("Ya existe un alumno con ese DNI");
                break;
            }
        }

        // Si no existe, introducir nuevo alumno
        if (!alumnoExistente) {
            System.out.println("Nombre completo:");
            nombreCompleto = sc.nextLine();
            System.out.println("Fecha de nacimiento (Formato DD/MM/AAAA):");
            fechaNac = sc.nextLine();
            System.out.println("Introduce tu dirección:");
            direccion = sc.nextLine();
            alumnoIntroducido = new Alumno(dni, nombreCompleto, fechaNac, direccion);

            // Reescribir el archivo con todos los alumnos (incluyendo el nuevo)
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivoAlumno))) {
                // Escribir todos los alumnos existentes
                for (Alumno a : listaAlumno) {
                    out.writeObject(a);
                }
                // Escribir el nuevo alumno
                out.writeObject(alumnoIntroducido);
                out.flush();
            } catch (IOException ex) {
                retorno = false; // Si no se puede escribir, retorna false
            }
        }
        return retorno;
    }

    // Metodo que muestra los datos del alumno
    public static void mostrarAlumno() {
        System.out.println("Introduce el dni del alumno del cual quieres informacion");
        String dni = sc.nextLine();

        // Variables
        boolean finArchivo = false;
        boolean encontrado = false;
        ArrayList<Alumno> listaAlumnos = new ArrayList<>();

        // Leer alumnos e introducirlos en una ArrayList
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(Main.FICHERO_DAT_ALUMNOS))) {
            while (!finArchivo) {
                try {
                    listaAlumnos.add((Alumno) in.readObject());
                } catch (EOFException ex) {
                    finArchivo = true;
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            // Buscar en la lista el alumno con el mismo dni que se pide por parámetro
            for (Alumno alumno : listaAlumnos) {
                if (alumno.getDni().equals(dni)) {
                    System.out.println(alumno);
                    encontrado = true;
                    break;
                }
            }

            // Si no se encuentra el alumno, mostrar mensaje
            if (!encontrado) {
                System.out.println("No se ha podido encontrar el alumno en el archivo 'ALUMNOS.DAT'");
            }

        } catch (FileNotFoundException e) {
            System.out.println("El archivo de alumnos no existe. Por favor, verifique la ruta.");
        } catch (IOException e) {
            System.out.println("Error al acceder al archivo: " + e.getMessage());
        }
    }

    // Metodo que vuelca las asignaturas de cada alumno
    public static void volcarAlumno() {
        // Variables
        boolean finArchivo = false;
        ArrayList<Alumno> listaAlumnos = new ArrayList<>();
        ArrayList<Matricula> listaMatriculas = new ArrayList<>();
        ArrayList<Asignatura> listaAsignaturas = new ArrayList<>();

        // Abrimos los streams de entrada para leer los archivos de Alumnos, Matriculas y Asignaturas
        try (ObjectInputStream inAlumno = new ObjectInputStream(new FileInputStream(Main.FICHERO_DAT_ALUMNOS));
             ObjectInputStream inMatricula = new ObjectInputStream(new FileInputStream(Main.FICHERO_DAT_MATRICULAS));
             ObjectInputStream inAsignatura = new ObjectInputStream(new FileInputStream(Main.FICHERO_DAT_ASIGNATURAS))) {

            // Llenar ArrayList de alumnos
            while (!finArchivo) {
                try {
                    listaAlumnos.add((Alumno) inAlumno.readObject()); // Leer un objeto Alumno y agregarlo a la lista
                } catch (EOFException ex) {
                    finArchivo = true; // Fin del archivo
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println(ex.getMessage()); // Manejo de error
                }
            }

            // Llenar ArrayList de matriculas
            finArchivo = false; // Resetear el flag para el siguiente archivo
            while (!finArchivo) {
                try {
                    listaMatriculas.add((Matricula) inMatricula.readObject()); // Leer objeto Matricula y agregarlo a la lista
                } catch (EOFException ex) {
                    finArchivo = true; // Fin del archivo
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println(ex.getMessage()); // Manejo de error
                }
            }

            // Llenar ArrayList de asignaturas
            finArchivo = false; // Resetear el flag nuevamente
            while (!finArchivo) {
                try {
                    listaAsignaturas.add((Asignatura) inAsignatura.readObject()); // Leer objeto Asignatura y agregarlo a la lista
                } catch (EOFException ex) {
                    finArchivo = true; // Fin del archivo
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println(ex.getMessage()); // Manejo de error
                }
            }

            // Recorrer cada alumno y volcar su información
            for (Alumno alumno : listaAlumnos) {
                // Crear archivo para cada alumno. El nombre del archivo es el nombre del alumno sin espacios
                File archivoAlumno = new File(Main.DIRECTORIO_ALUMNOS + "\\" + alumno.getDni().replaceAll("\\s", "") + ".txt");

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoAlumno))) {
                    // Escribir datos del alumno en el archivo
                    writer.write("Asignaturas matriculadas:\n");

                    ArrayList<Asignatura> asignaturasAlumno = new ArrayList<>(); // Lista temporal para las asignaturas del alumno actual

                    // Encontrar matriculas asociadas a este alumno
                    for (Matricula matricula : listaMatriculas) {
                        if (alumno.getDni().equals(matricula.getDni())) { // Comparar por DNI para encontrar las matriculas del alumno
                            // Encontrar asignaturas asociadas a la matricula
                            for (Asignatura asignatura : listaAsignaturas) {
                                if (matricula.getCodAsignatura() == asignatura.getCodAsignatura()) {
                                    asignaturasAlumno.add(asignatura); // Añadir asignatura si coincide el código
                                }
                            }
                        }
                    }

                    // Escribir asignaturas del alumno en el archivo
                    for (Asignatura asignatura : asignaturasAlumno) {
                        writer.write("- " + asignatura.getNombreAsignatura() + "\n");
                    }

                    // Mensaje de confirmación
                    System.out.println("Fichero de " + alumno.getNombreCompleto() + " volcado correctamente.");

                } catch (IOException e) {
                    System.out.println("No se pudo escribir el archivo de " + alumno.getNombreCompleto() + ": " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Error al leer los archivos: " + e.getMessage());
        }
    }

    // Metodo que crea la carpeta Alumnos si no esta creada desde un principio
    public static void crearCarpetaAlumnos() {
        File directorioAlumnos = new File(Main.DIRECTORIO_ALUMNOS);
        if (!directorioAlumnos.exists()) {
            try {
                if (directorioAlumnos.mkdir()) {
                    System.out.println("El directorio Alumnos ha sido creado");
                }
            } catch (Exception e) {
                System.err.println("El directorio no se ha podido crear");
            }
        }
    }

    @Override
    public String toString() {
        return "Alumno : " + nombreCompleto + "\nDni : " + dni + "\nFecha de nacimiento : " + fechaNac + "\nDireccion : " + direccion;
    }
}
