package sysSolutions.dominio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Cita {
    private int id;
    private int pacienteId;
    private String pacienteNombre; // Usado para mostrar el nombre
    private int doctorId;
    private String doctorNombre;   // Usado para mostrar el nombre
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;

    public Cita() {}

    public Cita(int id, int pacienteId, int doctorId, LocalDate fecha, LocalTime hora, String motivo) {
        this(id, pacienteId, null, doctorId, null, fecha, hora, motivo);
    }

    public Cita(int id, int pacienteId, String pacienteNombre, int doctorId, String doctorNombre,
                LocalDate fecha, LocalTime hora, String motivo) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.pacienteNombre = pacienteNombre;
        this.doctorId = doctorId;
        this.doctorNombre = doctorNombre;
        this.setFecha(fecha);
        this.setHora(hora);
        this.setMotivo(motivo);
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getPacienteId() { return pacienteId; }

    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }

    public String getPacienteNombre() { return pacienteNombre; }

    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }

    public int getDoctorId() { return doctorId; }

    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public String getDoctorNombre() { return doctorNombre; }

    public void setDoctorNombre(String doctorNombre) { this.doctorNombre = doctorNombre; }

    public LocalDate getFecha() { return fecha; }

    public void setFecha(LocalDate fecha) {
        if (fecha == null) throw new IllegalArgumentException("La fecha no puede ser nula");
        this.fecha = fecha;
    }

    public LocalTime getHora() { return hora; }

    public void setHora(LocalTime hora) {
        if (hora == null) throw new IllegalArgumentException("La hora no puede ser nula");
        this.hora = hora;
    }

    public String getMotivo() { return motivo; }

    public void setMotivo(String motivo) {
        this.motivo = (motivo == null || motivo.isBlank()) ? "No especificado" : motivo.trim();
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
        if (!(obj instanceof Cita)) return false;
        Cita cita = (Cita) obj;
        return id == cita.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
