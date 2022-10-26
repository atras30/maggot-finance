package umn.ac.id.project.maggot;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.global.AuthenticatedUser;
import umn.ac.id.project.maggot.model.UserModel;

public class QrCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        UserModel.User user = AuthenticatedUser.getUser();
        TextView title = findViewById(R.id.title);
        title.setText("Hello, " + user.getFull_name() + "! Here is your QR Code.");

        ImageView qrCodeImage = findViewById(R.id.qr_code_image);

        try {
            String email = user.getEmail();
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(email, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap qrCodeBitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImage.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}