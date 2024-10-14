import java.io.Serializable;

public class Matricula implements Serializable {
    private int codMatricula;
    private String dni;
    private int codAsignatura;

    //Constructor
    public Matricula(int codMatricula,String dni,int codAsignatura){
        this.codMatricula = codMatricula;
        this.dni = dni;
        this.codAsignatura = codAsignatura;
    }

    //Getter
    public String getDni() {
        return dni;
    }
    public int getCodMatricula() {
        return codMatricula;
    }
    public int getCodAsignatura() {
        return codAsignatura;
    }
    //Setter
    public void setCodMatricula(int codMatricula) {
        this.codMatricula = codMatricula;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public void setCodAsignatura(int codAsignatura) {
        this.codAsignatura = codAsignatura;
    }
}
