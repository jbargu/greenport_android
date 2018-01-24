package yourself.greenport.com.greenportyourself;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MonthlyReportActivity extends AppCompatActivity {

    @Bind(R.id.navigation1)
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);
        ButterKnife.bind(this);


        navigation.setSelectedItemId(R.id.navigation_notifications);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        intent = new Intent(getApplicationContext(), MapTrackingActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_dashboard:
                        intent = new Intent(getApplicationContext(), PlantingTreeActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }
}
