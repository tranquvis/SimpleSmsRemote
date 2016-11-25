package tranquvis.simplesmsremote.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import tranquvis.simplesmsremote.Adapters.LogListAdapter;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.R;

public class LogActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            DataManager.LoadLog(this);
        } catch (IOException e) {
            Toast.makeText(this, R.string.alert_load_log_failed, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_clear);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(LogActivity.this)
                        .setMessage(R.string.alert_sure_to_clear_log)
                        .setPositiveButton(R.string.simple_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DataManager.clearLog(LogActivity.this);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(R.string.simple_no, null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_log);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LogListAdapter(this, DataManager.getLog());
        mRecyclerView.setAdapter(mAdapter);
    }
}
