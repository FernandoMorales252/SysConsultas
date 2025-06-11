package sysSolutions.dominio;

    public class Receta {
        private int id;
        private int citaId;
        private String medicamento;
        private String dosis;
        private String observaciones;

        // Campos adicionales para mostrar información de la cita
        private String pacienteNombre;
        private String doctorNombre;

        public Receta() {
        }

        public Receta(int id, int citaId, String medicamento, String dosis, String observaciones) {
            this.id = id;
            this.citaId = citaId;
            this.medicamento = medicamento;
            this.dosis = dosis;
            this.observaciones = observaciones;
        }

        // Getters y Setters
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
            this.citaId = citaId;
        }

        public String getMedicamento() {
            return medicamento;
        }

        public void setMedicamento(String medicamento) {
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
                    " - " + medicamento;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Receta receta = (Receta) obj;
            return id == receta.id;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(id);
        }

    }
