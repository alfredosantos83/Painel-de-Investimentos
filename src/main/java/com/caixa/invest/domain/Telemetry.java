package com.caixa.invest.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "telemetry")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Telemetry extends PanacheEntity {

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "response_time_ms", nullable = false)
    private Long responseTimeMs;

    @Column(name = "http_status")
    private Integer httpStatus;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "success")
    private Boolean success;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
        if (this.success == null) {
            this.success = this.httpStatus != null && this.httpStatus >= 200 && this.httpStatus < 300;
        }
    }
}
