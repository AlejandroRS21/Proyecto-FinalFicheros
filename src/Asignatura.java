import java.io.Serializable;

public class Asignatura implements Serializable{
    private int codAsignatura;
    private String nombreAsignatura;

    //Constructor
    public Asignatura(int codAsignatura,String nombreAsignatura) {
        this.codAsignatura = codAsignatura;
        this.nombreAsignatura = nombreAsignatura;
    }

    //Getters and Setters
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
}
