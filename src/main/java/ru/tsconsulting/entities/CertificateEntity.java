package ru.tsconsulting.entities;

import javax.persistence.*;
import java.util.Arrays;

/**
 * Created by avtsoy on 22.08.2017.
 */
@Entity
@Table(name = "CERTIFICATE", schema = "TEST_B", catalog = "")
public class CertificateEntity {
    private long id;
    private String name;
    private long serialNumber;
    private byte[] scan;

    @Id
    @Column(name = "ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "Serial number")
    public long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Basic
    @Column(name = "SCAN")
    public byte[] getScan() {
        return scan;
    }

    public void setScan(byte[] scan) {
        this.scan = scan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CertificateEntity that = (CertificateEntity) o;

        if (id != that.id) return false;
        if (serialNumber != that.serialNumber) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (!Arrays.equals(scan, that.scan)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (serialNumber ^ (serialNumber >>> 32));
        result = 31 * result + Arrays.hashCode(scan);
        return result;
    }
}
