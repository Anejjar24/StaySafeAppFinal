package ma.ensaj.staysafe10.ui.riskzone.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ma.ensaj.staysafe10.data.remote.RiskZoneService;
import ma.ensaj.staysafe10.model.RiskZone;
import ma.ensaj.staysafe10.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiskZoneViewModel extends ViewModel {
    private static final String TAG = "RiskZoneViewModel";
    private final MutableLiveData<List<RiskZone>> riskZones = new MutableLiveData<>();
    private final RiskZoneService riskZoneService;

    public RiskZoneViewModel() {
        riskZoneService = RetrofitClient.getRetrofitInstance().create(RiskZoneService.class);
    }

    public LiveData<List<RiskZone>> getRiskZones() {
        loadRiskZones();
        return riskZones;
    }
    private void loadRiskZones() {
        riskZoneService.getAllActiveRiskZones().enqueue(new Callback<List<RiskZone>>() {
            @Override
            public void onResponse(Call<List<RiskZone>> call, Response<List<RiskZone>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RiskZone> zones = response.body();
                    Log.d(TAG, "Received " + zones.size() + " risk zones");
                    for (RiskZone zone : zones) {
                        Log.d(TAG, "Zone: " + zone.getName() + ", Risk Level: " + zone.getRiskLevel());
                    }
                    riskZones.setValue(zones);
                }
            }

            @Override
            public void onFailure(Call<List<RiskZone>> call, Throwable t) {
                Log.e(TAG, "Error loading risk zones", t);
            }
        });
    }
}
