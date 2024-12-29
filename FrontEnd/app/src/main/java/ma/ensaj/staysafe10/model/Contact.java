package ma.ensaj.staysafe10.model;

public class Contact {
    private Long id;
    private String name;
    private String number;

    public Contact(Long id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
