import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Main {
    private static final String DIRECTORIO_ALUMNOS = "Alumnos";
    private static final String FICHERO_DAT_ALUMNOS = "Alumnos/ALUMNOS.DAT";
    private static final String FICHERO_DAT_MATRICULAS = "Alumnos/MATRICULA.DAT";
    private static final String FICHERO_DAT_ASIGNATURAS = "Alumnos/ASIGNATURA.DAT";
    private static final Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {
        int opcion;
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
                    mostrarAlumno();
                    break;
                case 5:
                    volcarAlumno();
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


    private static boolean introducirAlumnos() throws IOException {
        String dni, nombreCompleto, fechaNac, direccion, intentoAlumno;
        boolean retorno = true;
        boolean alumnoExistente = false;
        Alumno alumnoIntroducido;
        File archivoAlumno = new File(FICHERO_DAT_ALUMNOS);
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

    private static void introducirMatricula()  {
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
        File archivoMatriculas = new File(FICHERO_DAT_MATRICULAS);

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

    private static void introducirAsignatura() {
        String nombreAsig;
        int codAsig;

        // Recogida de datos
        System.out.println("Introduce el código de la asignatura:");
        codAsig = sc.nextInt();
        sc.nextLine();
        System.out.println("Introduce el nombre de la asignatura:");
        nombreAsig = sc.nextLine();

        // Verificar si la asignatura ya existe
        if (asignaturaExists(codAsig)) {
            System.out.println("Ya existe una asignatura con el código " + codAsig + " introduzca otro numero.");
            return;
        }
        Asignatura asignatura = new Asignatura(codAsig, nombreAsig);
        File archivoAsignaturas = new File(FICHERO_DAT_ASIGNATURAS);

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

    private static void mostrarAlumno() {
        System.out.println("Introduce el dni del alumno del cual quieres informacion");
        String dni = sc.nextLine();

        // Variables
        boolean finArchivo = false;
        boolean encontrado = false;
        ArrayList<Alumno> listaAlumnos = new ArrayList<>();

        // Leer alumnos e introducirlos en una ArrayList
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

        }  catch (FileNotFoundException e) {
            System.out.println("El archivo de alumnos no existe. Por favor, verifique la ruta.");
        } catch (IOException e) {
                System.out.println("Error al acceder al archivo: " + e.getMessage());
        }
    }


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
                        writer.write("- " + asignatura.getNombreAsignatura() +"\n");
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

    private static void borrar() {
        File directorio = new File(Main.DIRECTORIO_ALUMNOS);

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

    private static boolean asignaturaExists(int codAsig) {
        File archivoAsignaturas = new File(FICHERO_DAT_ASIGNATURAS);

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