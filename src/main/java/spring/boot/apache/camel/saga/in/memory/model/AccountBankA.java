package spring.boot.apache.camel.saga.in.memory.model;

import javax.persistence.*;

@Entity
@Table(name = "ACCOUNT_BANK_A")
public class AccountBankA {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "cif_number")
    private Long cifNumber;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    public AccountBankA() {
    }

    public AccountBankA(Long id, Long cifNumber, Long amount, String type, String status) {
        this.id = id;
        this.cifNumber = cifNumber;
        this.amount = amount;
        this.type = type;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCifNumber() {
        return cifNumber;
    }

    public void setCifNumber(Long cifNumber) {
        this.cifNumber = cifNumber;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AccountBankA{" +
                "id=" + id +
                ", cifNumber=" + cifNumber +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
