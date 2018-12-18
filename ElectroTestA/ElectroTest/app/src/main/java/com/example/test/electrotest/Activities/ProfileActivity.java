package com.example.test.electrotest.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.test.electrotest.App;
import com.example.test.electrotest.Models.ModelsInteract.UserSave;
import com.example.test.electrotest.Models.NetModels.UniversalActionNet;
import com.example.test.electrotest.R;
import com.example.test.electrotest.ServerInteract.AccountInteract;
import com.example.test.electrotest.ServerInteract.UserContextOperation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        new AccountInteract().loadProfileInfo(this);
    }


    public void Save(View view) {
        String nickname = ((EditText) findViewById(R.id.name)).getText().toString();
        UserSave userSave = new UserSave(UserContextOperation.getUserID(), nickname);
        Call<UniversalActionNet> netCall = App.getApi().SaveUser(userSave);
        final ProfileActivity profileActivity = this;
        netCall.enqueue(new Callback<UniversalActionNet>() {
            @Override
            public void onResponse(Call<UniversalActionNet> call, Response<UniversalActionNet> response) {
                Intent intent = new Intent(profileActivity, UserActivity.class);
                profileActivity.startActivity(intent);
            }

            @Override
            public void onFailure(Call<UniversalActionNet> call, Throwable t) {
            }
        });
    }

    public void changePhoto(View view) {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 100 &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                ((ImageView) findViewById(R.id.image)).setImageBitmap(imageBitmap);
                App.SetImage(imageBitmap, this);
            }
        }
    }
}
