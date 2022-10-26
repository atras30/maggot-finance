//package umn.ac.id.project.maggot;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.annotation.Nullable;
//
//import com.google.zxing.Result;
//
//import me.dm7.barcodescanner.zxing.ZXingScannerView;
//
//public class QrCodeScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
//    private ZXingScannerView mScannerView;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mScannerView = new ZXingScannerView(this);
//        mScannerView.setAspectTolerance(0.5f);
//        setContentView(mScannerView);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        mScannerView.setResultHandler(this);
//        mScannerView.startCamera();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        mScannerView.stopCamera();
//    }
//
//    @Override
//    public void handleResult(Result result) {
//        Intent intent = new Intent();
//        intent.putExtra("result", result.getText());
//        setResult(RESULT_OK, intent);
//        finish();
//    }
//}
