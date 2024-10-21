import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
//Arreglar volcar alumno que no escribe las asignaturas

public class Main {
    private static final String DIRECTORIO_ALUMNOS = "Alumnos";
    private static final String FICHERO_DAT_ALUMNOS = "Alumnos/ALUMNOS.DAT";
    private static final String FICHERO_DAT_MATRICULAS = "Alumnos/MATRICULA.DAT";
    private static final String FICHERO_DAT_ASIGNATURAS = "Alumnos/ASIGNATURA.DAT";
    private static final Scanner sc = new Scanner(System.in);


    public static void main(String[] args) throws IOException {
        int opcion;
        String dni;
        crearCarpetaAlumnos(); //metodo que crear el directorio Alumnos si no existe

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
                        if (introducirAlumnos()) {
                            System.out.println("Se ha introducido el alumno correctamente");
                        } else {
                            System.out.println("No se ha podido introducir el alumno");
                        }
                    } catch (IOException ex) {
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
                    System.out.println(mostrarAlumno(dni));
                    break;
                case 5:
                    volcarAlumno();
                    break;
                case 6:
                    borrar(DIRECTORIO_ALUMNOS);
                    break;
                case 7:
                    break;
                default:
                    System.out.print("Numero incorrecto vuelva a escribir uno");
            }
        } while (opcion != 7);
        sc.close();
    }


    private static boolean introducirAlumnos() throws IOException {
        String dni, nombreCompleto, fechaNac, direccion, intentoAlumno;
        boolean retorno = true;
        boolean finArchivo = false;
        boolean alumnoExistente = false;
        Alumno alumnoIntroducido;
        File archivoAlumno = new File(FICHERO_DAT_ALUMNOS);
        ArrayList<Alumno> listaAlumno = new ArrayList<>();
        ObjectInputStream inAlumno = null;

        //Recogida de datos del alumno
        System.out.println("Introduce los datos del alumno a introducir:");
        System.out.println("DNI:");
        dni = sc.nextLine();

        if (archivoAlumno.exists()) {
            inAlumno = new ObjectInputStream(new FileInputStream(archivoAlumno));
            while (!finArchivo) {
                try {
                    listaAlumno.add((Alumno) inAlumno.readObject());
                } catch (EOFException ex) {
                    finArchivo = true;
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            for (Alumno alumno : listaAlumno) {
                intentoAlumno = alumno.getDni();
                if (intentoAlumno.equals(dni)) {
                    alumnoExistente = true;
                    retorno = false;
                    System.out.println("Ya existe un alumno con ese DNI");
                }
            }
        }


        if (!alumnoExistente) {
            System.out.println("Nombre completo:");
            nombreCompleto = sc.nextLine();
            System.out.println("Fecha de nacimiento(Formato DD/MM/AAAA)");
            fechaNac = sc.nextLine();
            System.out.println("Introduce tu direccion");
            direccion = sc.nextLine();
            alumnoIntroducido = new Alumno(dni, nombreCompleto, fechaNac, direccion);

            //Introducido en "ALUMNOS.DAT"
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivoAlumno))) {
                out.writeObject(alumnoIntroducido);
                out.flush();
            } catch (IOException ex) {
                //Si no se introduce devuelve false la función
                retorno = false;
            }

        }
        return retorno;
    }

    private static void introducirMatricula() throws IOException {
        String dni;
        int codMatric, codAsig;
        //Recogida de datos
        System.out.println("Introduce el codigo de la matricula");
        codMatric = sc.nextInt();
        sc.nextLine();
        System.out.println("Introduce el DNI del alumno");
        dni = sc.nextLine();
        System.out.println("Introduce el codigo de la asignatura");
        codAsig = sc.nextInt();
        Matricula matricula = new Matricula(codMatric, dni, codAsig);
        //Introducir en Alumnos\\MATRICULA.DAT
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FICHERO_DAT_MATRICULAS))) {
            out.writeObject(matricula);
            out.flush();
            System.out.println("Matricula introducida correctamente");
        } catch (IOException e) {

            System.out.println("Error al introducir matricula");
        }

    }

    private static void introducirAsignatura() {
        String nombreAsig;
        int codAsig;
        //Recogida de datos
        System.out.println("Introduce el codigo de la asignatura");
        codAsig = sc.nextInt();
        sc.nextLine();
        System.out.println("Introduce el nombre de la asignatura");
        nombreAsig = sc.nextLine();
        Asignatura asignatura = new Asignatura(codAsig, nombreAsig);
        //Introducir en ASIGNATURA.DAT
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FICHERO_DAT_ASIGNATURAS))) {
            out.writeObject(asignatura);
            out.flush();
            System.out.println("Asignatura introducida correctamente");
        } catch (IOException e) {
            System.out.println("Error al introducir asignatura" + e.getMessage());
        }
    }

    private static String mostrarAlumno(String dni) {
        //Variables
        boolean finArchivo = false;
        String retorno = "No se ha podido encontrar el alumno en el archivo 'ALUMNOS.DAT'";
        ArrayList<Alumno> listaAlumnos = new ArrayList<>();

        //Leer alumnos e introducirlos un una ArrayList
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FICHERO_DAT_ALUMNOS))) {
            while (!finArchivo) {
                try {
                    listaAlumnos.add((Alumno) in.readObject());
                } catch (EOFException ex) {
                    finArchivo = true;
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            //Buscar en la lista el alumno con el mismo dni que se pide por parametro en la función
            for (Alumno alumno : listaAlumnos) {
                if (alumno.getDni().equals(dni)) {
                    retorno = alumno.toString();
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        /*Si se ha encontrado alumno el retorno será la información del alumno ,
         si no será que no se ha encontrado*/
        return retorno;
    }
    //Volcar Alumno antiguo
    /*private static void volcarAlumno() {
        //Variables
        boolean finArchivo = false;
        boolean alumnoEncontrado = false;
        String retorno = "No se ha podido encontrar el alumno en el archivo 'ALUMNOS.DAT'";
        ArrayList<Alumno> listaAlumnos = new ArrayList<>();
        ArrayList<Matricula> listaMatriculas = new ArrayList<>();
        ArrayList<Asignatura> listaAsignaturas = new ArrayList<>();
        ArrayList<Asignatura> asignaturasAlumno = new ArrayList<>();

        try {
            ObjectInputStream inAlumno = new ObjectInputStream(new FileInputStream(FICHERO_DAT_ALUMNOS));
            ObjectInputStream inMatricula = new ObjectInputStream(new FileInputStream(FICHERO_DAT_MATRICULAS));
            ObjectInputStream inAsignatura = new ObjectInputStream(new FileInputStream(FICHERO_DAT_ASIGNATURAS));
            ObjectOutputStream out = null;

            //Lleno el ArrayList de alumnos
            while (!finArchivo) {
                try {
                    listaAlumnos.add((Alumno) inAlumno.readObject());
                } catch (EOFException ex) {
                    finArchivo = true;
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            //Lleno el ArrayList de matriculas
            finArchivo = false;
            while (!finArchivo) {
                try {
                    listaMatriculas.add((Matricula) inMatricula.readObject());
                } catch (EOFException ex) {
                    finArchivo = true;
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            //Lleno el ArrayList de asignaturas
            finArchivo = false;
            while (!finArchivo) {
                try {
                    listaAsignaturas.add((Asignatura) inAsignatura.readObject());
                } catch (EOFException ex) {
                    finArchivo = true;
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            //Recorro cada alumno del archivo
            for (Alumno alumno : listaAlumnos) {
                out = new ObjectOutputStream(new FileOutputStream((DIRECTORIO_ALUMNOS + "\\" + alumno.getNombreCompleto().replaceAll("\\s", "")) + ".txt"));

                //Recorro cada matricula del archivo
                for (Matricula matricula : listaMatriculas) {
                    //Compruebo si cada matricula se asocia con el alumno actual
                    if (alumno.getDni().equals(matricula.getDni())) {
                        for (Asignatura asignatura : listaAsignaturas) {
                            //Si se asocia recorro las asignaturas y compruebo si se asocian con el mismo codigo de asignatura de la matricula
                            if (matricula.getCodAsignatura() == asignatura.getCodAsignatura()) {
                                //Guardo las asignaturas relacionadas con el alumno actual en una lista
                                asignaturasAlumno.add(asignatura);
                            }
                        }
                    }
                }

                //Escribe cada asignatura del alumno en el fichero del alumno
                for (Asignatura asignatura : asignaturasAlumno) {
                    out.writeObject(asignatura);
                }

            }
            System.out.println("Ficheros volcados correctamente");

        } catch (IOException e) {
            System.out.println("No se pudo volcar los archivos  " + e.getMessage());
        }
    }*/
    //Nuevo Volcar
    private static void volcarAlumno() {
        // Variables
        boolean finArchivo = false;
        ArrayList<Alumno> listaAlumnos = new ArrayList<>();
        ArrayList<Matricula> listaMatriculas = new ArrayList<>();
        ArrayList<Asignatura> listaAsignaturas = new ArrayList<>();

        // Abrimos los streams de entrada para leer los archivos de Alumnos, Matriculas y Asignaturas
        try (ObjectInputStream inAlumno = new ObjectInputStream(new FileInputStream(FICHERO_DAT_ALUMNOS));
             ObjectInputStream inMatricula = new ObjectInputStream(new FileInputStream(FICHERO_DAT_MATRICULAS));
             ObjectInputStream inAsignatura = new ObjectInputStream(new FileInputStream(FICHERO_DAT_ASIGNATURAS))) {

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
                File archivoAlumno = new File(DIRECTORIO_ALUMNOS + "\\" + alumno.getDni().replaceAll("\\s", "") + ".txt");

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoAlumno))) {
                    // Escribir datos del alumno en el archivo
                    writer.write("Alumno: " + alumno.getNombreCompleto() + "\n");
                    writer.write("DNI: " + alumno.getDni() + "\n");
                    writer.write("Fecha de nacimiento: " + alumno.getFechaNac() + "\n");
                    writer.write("Dirección: " + alumno.getDireccion() + "\n");
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
                        writer.write("- " + asignatura.getNombreAsignatura() + " (Código: " + asignatura.getCodAsignatura() + ")\n");
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


    private static void borrar(String directorioFicheros) {
        File directorio = new File(directorioFicheros);

        if (directorio.isDirectory()) {
            File[] ficheros = directorio.listFiles();

            if (ficheros != null) {
                for (File fichero : ficheros) {
                    try {
                        if (fichero.isFile() && fichero.delete()) {
                            System.out.println("El fichero " + fichero.getName() + " se ha eliminado correctamente");
                        }
                    } catch (Exception e) {
                        System.err.println("ERROR: No se pudo borrar el archivo : " + fichero.getName() + e.getMessage());
                    }
                }
            } else {
                System.out.println("No se pudieron listar los ficheros del directorio " + directorio.getPath());

            }
        } else {
            System.out.println("La ruta proporcionada no es un directorio");
        }
    }


    private static void crearCarpetaAlumnos() {
        File directorioAlumnos = new File(DIRECTORIO_ALUMNOS);
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


}