package sysSolutions.dominio;

public class user {
    private int id;
    private String name;
    private String email;
    private String passwordHash;
    private byte status;

    public user(){

    }

    public user(int id, String name, String email, String passwordHash, byte status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.status = status;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public byte getStatus() {
        return status;
    }
    public void setStatus(byte status) {
        this.status = status;
    }

    public String getEstatus()
    {
        String str="";
        switch (status)
        {
            case 1:
                str = "Activo";
            case 2:
                str = "Inactivo";
            break;
            default:
                str = "";

        }
        return str;
    }

}
