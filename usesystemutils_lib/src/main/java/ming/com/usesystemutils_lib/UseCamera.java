package ming.com.usesystemutils_lib;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * 使用系统相机
 * ==============
 * Author MingRuQi
 * E-mail mingruqi@sina.cn
 * DateTime 2018/12/20 10:41
 */
public class UseCamera {

    private final int PHOTOGRAPH = 0;//拍照并返回照片
    private final int PHOTOGRAPHSAVE = 1;//拍照并将照片保存到本地
    private File image;

    private AccessPermissionUtil permissionUtil;
    private PhotographCallBack callBack;
    private Activity activity;
    private String imageName;//照片存储路径

    public UseCamera(Activity activity, PhotographCallBack callBack) {
        this.callBack = callBack;
        this.activity = activity;
    }

    /**
     * 开始拍照
     *
     * @return
     */
    private void startPhotograph(final int type) {
        //检查是否有使用系统相机的权限
        permissionUtil = new AccessPermissionUtil(activity);
        permissionUtil.checkPermissions(new AccessPermissionUtil.RequestPerssionCallBack() {



            @Override
            public void onPermissionDenied(int requestCode, String[] permissions) {
                callBack.NoPermission(requestCode, permissions);
            }

            @Override
            public void onPermissionAllow(int requestCode, String[] permissions) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (type == PHOTOGRAPHSAVE) {
                    File file = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
                        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    }
                    if (file.exists()) {
                        file.mkdirs();
                    }
                    image = new File(file.getAbsolutePath(),imageName);
                    if (image.exists()) {
                        try {
                            image.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Utils.getIntentUri(activity,Uri.fromFile(image)));
                }
                activity.startActivityForResult(intent, AccessPermissionUtil.PERMISSIONS_REQUEST_GROUP_CODE);
            }
        }, Manifest.permission.CAMERA);
    }

    /**
     * ------------------------------------------------------------------------------------
     */


    /**
     * 拍照并返回照片
     *
     * @return 拍照后获取的位图
     */
    public void Photograph() {
        startPhotograph(PHOTOGRAPH);
    }


    /**
     * 拍照并存储到本地
     *
     * @param imageName 存储地址
     * @return 是否保存成功
     */
    public void Photograph(String imageName) {
        this.imageName = imageName;
        startPhotograph(PHOTOGRAPHSAVE);
    }


    /**
     * 设置权限检测回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void setRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 使用系统相机获取照片的回执
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AccessPermissionUtil.PERMISSIONS_REQUEST_GROUP_CODE) {
           /* if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                callBack.Success(bitmap);
            } else if (resultCode == RESULT_CANCELED) {
                callBack.Cancel();
            } else {
                callBack.Failure(data);
            }*/
            Bitmap b = BitmapFactory.decodeFile(image.getAbsolutePath());
            callBack.Success(b);
        }
    }

    /**
     * 使用系统相机的回调
     */
    public interface PhotographCallBack {
        /**
         * 没有权限时的操作
         */
        void NoPermission(int requestCode, String[] permissions);

        /**
         * 获取照片失败时操作
         */
        void Failure(Intent data);

        /**
         * 取消拍照
         */
        void Cancel();

        /**
         * 获取照片成功时才做
         */
        void Success(Bitmap bitmap);
    }
}
