package sysSolutions.dominio;

public class Doctor {

        private int id;
        private String nombre;
        private int especialidadId;
        private String especialidadNombre; // Para mostrar el nombre de la especialidad
        private String contacto;

        public Doctor() {
        }

        public Doctor(int id, String nombre, int especialidadId, String contacto) {
            this.id = id;
            this.nombre = nombre;
            this.especialidadId = especialidadId;
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

        public int getEspecialidadId() {
            return especialidadId;
        }

        public void setEspecialidadId(int especialidadId) {
            this.especialidadId = especialidadId;
        }

        public String getEspecialidadNombre() {
            return especialidadNombre;
        }

        public void setEspecialidadNombre(String especialidadNombre) {
            this.especialidadNombre = especialidadNombre;
        }

        public String getContacto() {
            return contacto;
        }

        public void setContacto(String contacto) {
            this.contacto = contacto;
        }

        @Override
        public String toString() {
            return nombre + " - " + (especialidadNombre != null ? especialidadNombre : "Sin especialidad");
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





