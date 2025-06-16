package sysSolutions.dominio;

public class Paciente {

    private int id;
    private String nombre;
    private Integer edad;
    private Character sexo;
    private String contacto;
    private String direccion;

    public Paciente () {
    }

    public Paciente (int id, String nombre, Integer edad, Character sexo, String contacto, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.sexo = sexo;
        this.contacto = contacto;
        this.direccion = direccion;
    }

    public Paciente(int id) {
    }

    public int getId() { return id;
    }
    public void setId(int id) { this.id = id;
    }

    public String getNombre() {return nombre;
    }
    public void setNombre(String nombre) {this.nombre = nombre;
    }
    public Integer getEdad() {return edad;
    }
    public void setEdad(Integer edad) {this.edad = edad;
    }
    public Character getSexo() {return sexo;
    }
    public void setSexo(Character sexo) {this.sexo = sexo;
    }
    public String getContacto() {return contacto;
    }
    public void setContacto(String contacto) {this.contacto = contacto;
    }
    public String getDireccion() {return direccion;
    }
    public void setDireccion(String direccion) {this.direccion = direccion;
    }


    public String getSexoDescripcion() {
        if (sexo == null) return "No especificado";
        return sexo == 'M' ? "Masculino" : sexo == 'F' ? "Femenino" : "No especificado";
    }

    @Override
    public String toString() {
        return nombre + " (" + (edad != null ? edad + " años" : "Edad no especificada") + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Paciente paciente = (Paciente) obj;
        return id == paciente.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
