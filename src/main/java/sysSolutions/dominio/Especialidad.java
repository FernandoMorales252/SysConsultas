package sysSolutions.dominio;

public class Especialidad {

        private int id;
        private String nombre;

        public Especialidad() {
        }

        public Especialidad(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
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

        @Override
        public String toString() {
            return nombre;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Especialidad that = (Especialidad) obj;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(id);
        }

    }
