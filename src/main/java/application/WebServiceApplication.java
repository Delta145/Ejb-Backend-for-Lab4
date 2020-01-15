package application;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.io.Serializable;

@ApplicationPath("/api")
public class WebServiceApplication extends Application implements Serializable {
}
