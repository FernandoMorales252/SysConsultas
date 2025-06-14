package sysSolutions.dominio;


    public class Receta {
        private int id;
        private int citaId;
        private Medicamento medicamento;
        private String dosis;
        private String observaciones;
        private String pacienteNombre;
        private String doctorNombre;

        public Receta() {
        }

        public Receta(int id, int citaId, Medicamento medicamento, String dosis, String observaciones) {
            this.id = id;
            this.citaId = citaId;
            this.medicamento = medicamento;
            this.dosis = dosis;
            this.observaciones = observaciones;
        }

        public Receta(int id, int citaId, Medicamento medicamento, String dosis, String observaciones,
                      String pacienteNombre, String doctorNombre) {
            this.id = id;
            this.citaId = citaId;
            this.medicamento = medicamento;
            this.dosis = dosis;
            this.observaciones = observaciones;
            this.pacienteNombre = pacienteNombre;
            this.doctorNombre = doctorNombre;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCitaId() {
            return citaId;
        }

        public void setCitaId(int citaId) {
            if (citaId <= 0) throw new IllegalArgumentException("El ID de la cita debe ser positivo");
            this.citaId = citaId;
        }

        public Medicamento getMedicamento() {
            return medicamento;
        }

        public void setMedicamento(Medicamento medicamento) {
            if (medicamento == null)
                throw new IllegalArgumentException("El medicamento no puede ser nulo");
            this.medicamento = medicamento;
        }

        public String getDosis() {
            return dosis;
        }

        public void setDosis(String dosis) {
            this.dosis = dosis;
        }

        public String getObservaciones() {
            return observaciones;
        }

        public void setObservaciones(String observaciones) {
            this.observaciones = observaciones;
        }

        public String getPacienteNombre() {
            return pacienteNombre;
        }

        public void setPacienteNombre(String pacienteNombre) {
            this.pacienteNombre = pacienteNombre;
        }

        public String getDoctorNombre() {
            return doctorNombre;
        }

        public void setDoctorNombre(String doctorNombre) {
            this.doctorNombre = doctorNombre;
        }

        @Override
        public String toString() {
            return "Receta para " + (pacienteNombre != null ? pacienteNombre : "Cita ID: " + citaId) +
                    " - Medicamento: " + (medicamento != null ? medicamento.getNombre() : "No asignado");
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Receta)) return false;
            Receta receta = (Receta) o;
            return id == receta.id;
        }

       @Override
        public int hashCode() {
            return Integer.hashCode(id);
        }
    }
