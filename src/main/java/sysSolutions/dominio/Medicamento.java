package sysSolutions.dominio;
import java.math.BigDecimal;
import java.time.LocalDate;

//clase que representa un medicamento en el sistema de gestión de medicamentos.
public class Medicamento {
    private int id;
    private String nombre;
    private BigDecimal contenido; // <-- CAMBIO AQUÍ
    private LocalDate fechaExpiracion;


    public Medicamento() {}

    public Medicamento(int id, String nombre, BigDecimal contenido, LocalDate fechaExpiracion) {
        this.id = id;
        this.nombre = nombre;
        this.contenido = contenido;
        this.fechaExpiracion = fechaExpiracion;
    }

    public Medicamento(String nombre, BigDecimal contenido, LocalDate fechaExpiracion) {
        this.nombre = nombre;
        this.contenido = contenido;
        this.fechaExpiracion = fechaExpiracion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getContenido() { return contenido; }
    public void setContenido(BigDecimal contenido) { this.contenido = contenido; }

    public LocalDate getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDate fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    @Override
    public String toString() {
        return nombre + " (" + contenido + ") expira: " + fechaExpiracion;
    }


}
