package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ACCESS_HISTORY")
public class AccessHistory {
    public AccessHistory() {
    }

    @Id
    @SequenceGenerator(name = "accessHistoryGenerator", sequenceName = "access_history_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accessHistoryGenerator")
    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private LocalDateTime dateTime;

    @Column(name = "IP")
    private String ip;

    @Column(name="PRINCIPAL")
    private String principal;

    @Column(name="URL")
    private String url;

    @Column(name = "IS_AUTHENTICATED")
    private boolean isAuthenticated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    @Override
    public String toString() {
        return "AccessHistory{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", ip='" + ip + '\'' +
                ", principal='" + principal + '\'' +
                ", url='" + url + '\'' +
                ", isAuthenticated=" + isAuthenticated +
                '}';
    }
}
