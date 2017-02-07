package onepass.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Website.
 */
@Entity
@Table(name = "website")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Website implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "domain")
    private String domain;

    @ManyToOne
    private Entry belongs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public Website domain(String domain) {
        this.domain = domain;
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Entry getBelongs() {
        return belongs;
    }

    public Website belongs(Entry entry) {
        this.belongs = entry;
        return this;
    }

    public void setBelongs(Entry entry) {
        this.belongs = entry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Website website = (Website) o;
        if (website.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, website.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Website{" +
            "id=" + id +
            ", domain='" + domain + "'" +
            '}';
    }
}
