package com.example.owntage.es2;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKScopes;

import com.example.owntage.es2.common.User;
import com.vk.sdk.util.VKUtil;

public class EndOfLevel extends Activity {
    private int col;
    private int level;
    private int min;
    private static final String KEY_TOKEN = "vk_token";
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_end_of_level);

        col=getIntent().getIntExtra("result",0);
        level=getIntent().getIntExtra("level",0);
        min=getIntent().getIntExtra("minimum",0);
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Button btnAgree=(Button)findViewById(R.id.agree);
        Button btnDisAgree=(Button)findViewById(R.id.disagree);
        TextView textQ=(TextView)findViewById(R.id.question);
        if(min>col)
        {
            btnAgree.setText(getResources().getText(R.string.yes));
            btnDisAgree.setText(getResources().getText(R.string.no));
            textQ.setText(getResources().getText(R.string.repeate));
        }
        else
        {
            btnAgree.setText(getResources().getText(R.string.agree_vk));
            btnDisAgree.setText(getResources().getText(R.string.desagree_vk));
            textQ.setText(getResources().getText(R.string.do_in_vk_next));
        }
    }
    public void onClickAgree(View view)
    {
        if(min>col)
        {
            level--;

        }
        else {
            WriteNode();
        }
        onClickDisagree(view);
    }
    private Intent intent;
    public void onClickDisagree(View view) {
        finish();
        if(view.getId()==R.id.disagree && min<col)
            return;
        if(level+1> MenuActivity.N)
            return;
        intent = new Intent(EndOfLevel.this, MainActivity.class);
        intent.putExtra("level_number", level + 1);
        if(view.getId()==R.id.disagree)
            startActivity(intent);
    }

    private void WriteNode()
    {
        VKAccessToken token=VKAccessToken.tokenFromSharedPreferences(this,KEY_TOKEN);
        if(token!=null)
        {
            onLoginIn(token);
        }
        else
        {
            VKSdk.login(this,VKScopes.WALL);
        }
    }

    protected void onLoginIn(VKAccessToken token)
    {
        Log.i(TAG,"onLoginIn: "+token);
        id=token.userId;
        startCurrentUserRequest();
    }

    protected void onLoginFailed(VKError error) {
        Log.w(TAG, "onLoggedFailed: " + error);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                res.saveTokenToSharedPreferences(EndOfLevel.this, KEY_TOKEN);
                onLoginIn(res);
            }

            @Override
            public void onError(VKError error) {
                onLoginFailed(error);
            }
        });
    }

    @SuppressWarnings("unchecked,deprecation")
    void startCurrentUserRequest() {
        VKRequest request = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID, id, VKApiConst.MESSAGE, getResources().getString(R.string.for_wall_before) + " " + col + " " + getResources().getString(R.string.for_wall_after)));

        request.attempts = 10;
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Log.i(TAG, "onComplete request");
                finish();
                startActivity(intent);
            }

            @Override
            public void onError(VKError error) {
                onLoginFailed(error);
            }
        });
    }

    private static final String TAG="EndOfLevel";
}
