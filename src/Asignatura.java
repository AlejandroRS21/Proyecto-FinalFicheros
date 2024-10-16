import java.io.Serializable;

public class Asignatura implements Serializable{
    private int codAsignatura;
    private String nombreAsignatura;

    //Constructor
    public Asignatura(int codAsig,String nombreAsig) {
        this.codAsignatura = codAsig;
        this.nombreAsignatura = nombreAsig;
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
