public class Alumno {
    private String dni;
    private String nombreCompleto;
    private String fechaNac;
    private String direccion;

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
}
