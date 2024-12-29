package ma.ensaj.staysafe10.data.remote;
import java.util.List;

import ma.ensaj.staysafe10.model.CheckLocationResponse;
import ma.ensaj.staysafe10.model.Contact;
import ma.ensaj.staysafe10.model.Location;
import ma.ensaj.staysafe10.model.LocationRequest;
import ma.ensaj.staysafe10.model.RiskZone;
import retrofit2.Call;
import retrofit2.http.*;
public interface RiskZoneService {
    @GET("risk-zones")
    Call<List<RiskZone>> getAllActiveRiskZones();

    @POST("risk-zones")
    Call<RiskZone> createRiskZone(@Body RiskZone riskZone);

    @POST("risk-zones/check")
    Call<CheckLocationResponse> checkLocation(@Body LocationRequest location);
}