package yourself.greenport.com.greenportyourself;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlantingTreeActivity extends AppCompatActivity {

    @Bind(R.id.navigation)
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planting_tree);
        ButterKnife.bind(this);

        navigation.setSelectedItemId(R.id.navigation_dashboard);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        intent = new Intent(getApplicationContext(), MapTrackingActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_notifications:
                        return true;
                }
                return false;
            }
        });
    }
}
