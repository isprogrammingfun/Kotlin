package ddwucom.mobile.part02_05

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Insets.add
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val addPhotoButton: Button by lazy {
        findViewById<Button>(R.id.addPhotoButton)
    }

    private val startPhotoFrameModeButton: Button by lazy{
        findViewById<Button>(R.id.startPhotoFrameModeButton)
    }

    private val imageViewList: List<ImageView> by lazy{
        mutableListOf<ImageView>().apply {
            add(findViewById(R.id.imageView11))
            add(findViewById(R.id.imageView12))
            add(findViewById(R.id.imageView13))
            add(findViewById(R.id.imageView21))
            add(findViewById(R.id.imageView22))
            add(findViewById(R.id.imageView23))
        }
    }


    private val imageUriList: MutableList<Uri> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //addPhotoButton, startPhotoFrameModeButton 을 초기화 해주는 메소드
        initAddPhotoButton()
        initStartPhotoFrameModeButton()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun initAddPhotoButton() {
         addPhotoButton.setOnClickListener{
                 when{
                     ContextCompat.checkSelfPermission(
                         this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                     ) == PackageManager.PERMISSION_GRANTED -> {
                         //권한이 잘 부여되었을때 갤러리에서 사진을 선택하는 기능
                         navigatePhotos()
                     }
                     shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                        //교육용 팝업 확인 후 권한 팝업을 띄우는 기능
                         showPermissionContextPopup()
                     }
                     else -> {
                        //권한을 요청하는 팝업
                         requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
                     }
                 }
             }
         }

    private fun initStartPhotoFrameModeButton() {
        startPhotoFrameModeButton.setOnClickListener {
            val intent = Intent(this, PhotoFrameActivity::class.java)
           //url 하나하나 꺼내와서 putextra 해준다
            imageUriList.forEachIndexed { index, uri ->
                intent.putExtra("photo$index", uri.toString())
            }

            //몇번째 photoindex까지 getExtra를 해야하는지 알려줌
            intent.putExtra("photoListSize", imageUriList.size)
            startActivity(intent)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //권한 부여
                    navigatePhotos()

                } else {
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                //
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            2000 -> {
                val selectedImageUri: Uri? = data?.data

                if (selectedImageUri != null) {

                    if (imageUriList.size == 6) {
                        Toast.makeText(this, "이미 사진이 꽉 찼습니다.", Toast.LENGTH_SHORT).show()
                        return
                    }

                    imageUriList.add(selectedImageUri)
                    imageViewList[imageUriList.size - 1].setImageURI(selectedImageUri)

                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }


            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }
    //contents provider에서 storage access framework(SAF)라는 기능을 사용해서
    //사진을 가져오기

    private fun navigatePhotos() {
        //SAF 기능을 실행시켜서 컨텐츠를 가져오는 안드로이드 안에 내장되어있는
        //액티비티를 실행
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("전자액자에 앱에서 사진을 불러오기 위해 권한이 필요합니다")
            .setPositiveButton("동의하기", {_, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            })
            .setNegativeButton("취소하기", {_, _ -> })
            .create()
            .show()

    }
}