package sysSolutions.dominio;

public class Doctor {
    private int id;
    private String nombre;
    private Especialidad especialidad;
    private String contacto;

    public Doctor() {
    }

    public Doctor(int id, String nombre, Especialidad especialidad, String contacto) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.contacto = contacto;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    @Override
    public String toString() {
        return nombre + " - " + (especialidad != null ? especialidad.getNombre() : "Sin especialidad");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Doctor doctor = (Doctor) obj;
        return id == doctor.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}






