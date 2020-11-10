package pojo;

import java.io.Serializable;
import java.time.Instant;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "cpu")
public class Cpu implements Serializable {
    private static final long serialVersionUID = -6957361951748382519L;

    @Column(name = "time")
    private Instant time;

    @Column(name = "idle")
    private long idle;

    @Column(name = "user")
    private long user;

    @Column(name = "system")
    private long system;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public long getIdle() {
        return idle;
    }

    public void setIdle(long idle) {
        this.idle = idle;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getSystem() {
        return system;
    }

    public void setSystem(long system) {
        this.system = system;
    }
}
