package sysSolutions.dominio;
import java.util.Objects;
import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {
    private int id;
    private int pacienteId;
    private String pacienteNombre; // Para mostrar el nombre del paciente
    private int doctorId;
    private String doctorNombre; // Para mostrar el nombre del doctor
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;

    public Cita() {
    }

    public Cita(int id, int pacienteId, int doctorId, LocalDate fecha, LocalTime hora, String motivo) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.doctorId = doctorId;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorNombre() {
        return doctorNombre;
    }

    public void setDoctorNombre(String doctorNombre) {
        this.doctorNombre = doctorNombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public String toString() {
        return (pacienteNombre != null ? pacienteNombre : "Paciente ID: " + pacienteId) +
                " - " + (doctorNombre != null ? doctorNombre : "Doctor ID: " + doctorId) +
                " (" + fecha + " " + hora + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cita cita = (Cita) obj;
        return id == cita.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

}
