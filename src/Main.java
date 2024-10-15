import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        Scanner sc = new Scanner(System.in);
        int opcion;
        String dni;

        //directorio de ficheros texto alumnos
        String rutaDirectorio = "Alumnos";

        File directorioAlumnos = new File(rutaDirectorio);
        if(!directorioAlumnos.exists()){
            directorioAlumnos.mkdir();
            System.out.println("El directorio Alumnos ha sido creado");
        }

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
                    System.out.println(mostrarAlumno(dni));
                    break;
                case 5:
                    volcarAlumno();
                    break;
                case 6:
                    borrar(rutaDirectorio);
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
        String dni,nombreCompleto,fechaNac,direccion,intentoAlumno;
        boolean retorno = true;
        boolean finArchivo = false;
        boolean alumnoExistente = false;
        Alumno alumnoIntroducido;
        ArrayList<Alumno> listaAlumno = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        ObjectInputStream inAlumno = new ObjectInputStream(new FileInputStream("Alumnos\\ALUMNOS.DAT"));

        //Recogida de datos del alumno
        System.out.println("Introduce los datos del alumno a introducir:");
        System.out.println("DNI:");
        dni = sc.nextLine();

        while(!finArchivo){
            try{
                listaAlumno.add((Alumno)inAlumno.readObject());
            }catch(EOFException ex){
                finArchivo = true;
            }catch(IOException | ClassNotFoundException ex){
                System.out.println(ex.getMessage());
            }
        }

            for(Alumno alumno : listaAlumno){
                intentoAlumno = alumno.getDni();
                if(intentoAlumno.equals(dni)){
                    alumnoExistente = true;
                    retorno = false;
                    System.out.println("Ya existe un alumno con ese DNI");
                }
            }

        if(!alumnoExistente){
            System.out.println("Nombre completo:");
            nombreCompleto = sc.nextLine();
            System.out.println("Fecha de nacimiento(Formato DD/MM/AAAA)");
            fechaNac = sc.nextLine();
            System.out.println("Introduce tu direccion");
            direccion = sc.nextLine();
            alumnoIntroducido = new Alumno(dni,nombreCompleto,fechaNac,direccion);

            //Introducido en "ALUMNOS.DAT"
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Alumnos\\ALUMNOS.DAT"))) {
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
        int codMatric,codAsig;
        Scanner sc = new Scanner(System.in);
        //Recogida de datos
        System.out.println("Introduce el codigo de la matricula");
        codMatric = sc.nextInt();
        sc.nextLine();
        System.out.println("Introduce el DNI del alumno");
        dni = sc.nextLine();
        System.out.println("Introduce el codigo de la asignatura");
        codAsig = sc.nextInt();
        Matricula matricula = new Matricula(codMatric,dni,codAsig);
        //Introducir en Alumnos\\MATRICULA.DAT
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Alumnos\\MATRICULA.DAT"))) {
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
        Scanner sc = new Scanner(System.in);
        //Recogida de datos
        System.out.println("Introduce el codigo de la asignatura");
        codAsig = sc.nextInt();
        sc.nextLine();
        System.out.println("Introduce el nombre de la asignatura");
        nombreAsig = sc.nextLine();
        Asignatura asignatura = new Asignatura(codAsig,nombreAsig);
        //Introducir en ASIGNATURA.DAT
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Alumnos\\ASIGNATURA.DAT"))) {
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
        boolean alumnoEncontrado = false;
        String retorno = "No se ha podido encontrar el alumno en el archivo 'ALUMNOS.DAT'";
        ArrayList<Alumno> listaAlumnos = new ArrayList<>();

        //Leer alumnos e introducirlos un una ArrayList
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

            //Buscar en la lista el alumno con el mismo dni que se pide por parametro en la función
            for(Alumno alumno : listaAlumnos){
                if(alumno.getDni().equals(dni)){
                    retorno = alumno.toString();
                    alumnoEncontrado = true;
                }
            }

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        /*Si se ha encontrado alumno el retorno será la información del alumno ,
         si no será que no se ha encontrado*/
        return retorno;
    }
    private static void volcarAlumno() {
        //Variables
        boolean finArchivo = false;
        boolean alumnoEncontrado = false;
        String retorno = "No se ha podido encontrar el alumno en el archivo 'ALUMNOS.DAT'";
        ArrayList<Alumno> listaAlumnos = new ArrayList<>();
        ArrayList<Matricula> listaMatriculas = new ArrayList<>();
        ArrayList<Asignatura> listaAsignaturas = new ArrayList<>();
        ArrayList<Asignatura> asignaturasAlumno = new ArrayList<>();

        try{
            ObjectInputStream inAlumno = new ObjectInputStream(new FileInputStream("ALUMNOS.DAT"));
            ObjectInputStream inMatricula = new ObjectInputStream(new FileInputStream("MATRICULAS.DAT"));
            ObjectInputStream inAsignatura = new ObjectInputStream(new FileInputStream("ASIGNATURAS.DAT"));
            ObjectOutputStream out = null;

            //Lleno el ArrayList de alumnos
            while(!finArchivo){
                try{
                    listaAlumnos.add((Alumno)inAlumno.readObject());
                }catch(EOFException ex){
                    finArchivo = true;
                }catch(IOException | ClassNotFoundException ex){
                    System.out.println(ex.getMessage());
                }
            }

            //Lleno el ArrayList de matriculas
            finArchivo = false;
            while(!finArchivo){
                try{
                    listaMatriculas.add((Matricula) inMatricula.readObject());
                }catch(EOFException ex){
                    finArchivo = true;
                }catch(IOException | ClassNotFoundException ex){
                    System.out.println(ex.getMessage());
                }
            }

            //Lleno el ArrayList de asignaturas
            finArchivo = false;
            while(!finArchivo){
                try{
                    listaAsignaturas.add((Asignatura) inAsignatura.readObject());
                }catch(EOFException ex){
                    finArchivo = true;
                }catch(IOException | ClassNotFoundException ex){
                    System.out.println(ex.getMessage());
                }
            }

            //Recorro cada alumno del archivo
            for(Alumno alumno : listaAlumnos){
                out = new ObjectOutputStream(new FileOutputStream(("\\Alumnos\\" + alumno.getNombreCompleto().replaceAll("\\s",""))+".txt"));

                //Recorro cada matricula del archivo
                for(Matricula matricula : listaMatriculas){
                    //Compruebo si cada matricula se asocia con el alumno actual
                    if(alumno.getDni().equals(matricula.getDni())){
                        for(Asignatura asignatura : listaAsignaturas){
                            //Si se asocia recorro las asignaturas y compruebo si se asocian con el mismo codigo de asignatura de la matricula
                            if(matricula.getCodAsignatura()==asignatura.getCodAsignatura()){
                                //Guardo las asignaturas relacionadas con el alumno actual en una lista
                                asignaturasAlumno.add(asignatura);
                            }
                        }
                    }
                }

            //Escribe cada asignatura del alumno en el fichero del alumno
            for(Asignatura asignatura : asignaturasAlumno){
                out.writeObject(asignatura);
            }

            }

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    private static void borrar(String directorioFicheros) {
        File directorio = new File(directorioFicheros);
        if(directorio.isDirectory()){ //Si es un directorio listar los archivos de dentro

            File[] ficheros = directorio.listFiles();

            if(ficheros!=null){
                for(File fichero : ficheros){
                    try{
                        if(fichero.delete()){
                            System.out.println("El fichero " + fichero.getName() + "se ha eliminado");
                        }
                    }catch (Exception e){
                        System.err.println("ERROR : El fichero " + fichero.getName() + "nos e ha podido eliminar " + e.getMessage());
                    }
                }
            }else{
                System.out.println( "No se pudieron listar los ficheros ");
            }

        }else{
            System.out.println("La ruta proporcionada no es un directorio, vuelva a configurar el directorio");
        }
    }

}