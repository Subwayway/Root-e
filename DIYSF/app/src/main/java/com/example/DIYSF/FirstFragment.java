package com.example.DIYSF;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class FirstFragment extends Fragment {

    private Button btnSelect;
    private Button btnServer;
    private ImageView PlantPic;
    private TextView PlantTxt;
    private Context context;

    //getInstance()의 파라미터는 firebase console에서 확인 가능('gs:// ... ')
    FirebaseStorage storage = FirebaseStorage.getInstance();

    //생성된 FirebaseStorage를 참조하는 storage 생성
    StorageReference storageRef = storage.getReferenceFromUrl("gs://diy-smartfarm.appspot.com").child("img/20200504_171338.jpg");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        context = container.getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSelect = view.findViewById(R.id.btnSelect);
        btnServer = view.findViewById(R.id.btnServer);
        PlantPic = view.findViewById(R.id.PlantPic);
        PlantTxt = view.findViewById(R.id.PlantTxt);

        btnServer.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try{
                    final File file = File.createTempFile("images", "jpg");
                    // Storage 에서 다운받아 저장시킬 임시파일
                    final FileDownloadTask fileDownloadTask = storageRef.getFile(file);
                    fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmapImage = BitmapFactory.decodeFile(file.getPath());
                            PlantTxt.setText(file.getName());
                            PlantPic.setImageBitmap(bitmapImage);
                            Toast.makeText(context, "Success !!", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Fail !!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }
                    });
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
        });

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
}
