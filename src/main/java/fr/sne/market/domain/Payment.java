package fr.sne.market.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import fr.sne.market.domain.enumeration.PaymentType;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "allowed")
    private Boolean allowed;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @OneToOne(mappedBy = "payment")
    @JsonIgnore
    private MarketOrders order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Payment date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public Payment amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean isAllowed() {
        return allowed;
    }

    public Payment allowed(Boolean allowed) {
        this.allowed = allowed;
        return this;
    }

    public void setAllowed(Boolean allowed) {
        this.allowed = allowed;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public Payment paymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public MarketOrders getOrder() {
        return order;
    }

    public Payment order(MarketOrders marketOrders) {
        this.order = marketOrders;
        return this;
    }

    public void setOrder(MarketOrders marketOrders) {
        this.order = marketOrders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Payment payment = (Payment) o;
        if (payment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", amount='" + getAmount() + "'" +
            ", allowed='" + isAllowed() + "'" +
            ", paymentType='" + getPaymentType() + "'" +
            "}";
    }
}
