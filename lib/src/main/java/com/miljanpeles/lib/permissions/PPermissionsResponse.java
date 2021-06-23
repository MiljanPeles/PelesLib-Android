package com.miljanpeles.lib.permissions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PPermissionsResponse {

    private final Map<String, Integer> mPerms;
    private final ArrayList<String> mUserPermission;
    private Activity mActivity;
    private final int requestId;

    /**
     * Instancira response
     * @param perms parametri
     * @param userPerm user permisije
     */
    PPermissionsResponse(Map<String, Integer> perms, ArrayList<String> userPerm, Activity activity, int requestId) {
        this.mPerms = perms;
        this.mUserPermission = userPerm;
        this.mActivity = activity;
        this.requestId = requestId;
    }

    /**
     * Odbijene permisije
     * @return listu stringova
     */
    public String[] deniedPermissions() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < mPerms.size(); i++) {
            if (mPerms.get(mUserPermission.get(i)) == PackageManager.PERMISSION_DENIED)
                list.add(mUserPermission.get(i));
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * Dobijene permisije
     * @return lista stringova
     */
    public String[] grantedPermissions() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < mPerms.size(); i++) {
            if (mPerms.get(mUserPermission.get(i)) == PackageManager.PERMISSION_GRANTED)
                list.add(mUserPermission.get(i));
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * Da li su sve permisije dobijene
     * @return true ako jesu, false ako nisu
     */
    public boolean isAllGranted() {
        int count = 0;
        for (int i = 0; i < mPerms.size(); i++) {
            if (mPerms.get(mUserPermission.get(i)) == PackageManager.PERMISSION_GRANTED)
                count++;
        }
        return count == mPerms.size();

    }

    /**
     * Da li su sve odbijene
     * @return true ako jesu, false ako nisu
     */
    public boolean isAllDenied() {
        int count = 0;
        for (int i = 0; i < mPerms.size(); i++) {
            if (mPerms.get(mUserPermission.get(i)) == PackageManager.PERMISSION_DENIED)
                count++;
        }
        return count == mPerms.size();

    }

    /**
     * Da li je makar jedna permisija odbijena
     * @return true ako jeste, false ako nije
     */
    public boolean hasDeniedPermission() {
        for (int i = 0; i < mPerms.size(); i++) {
            if (mPerms.get(mUserPermission.get(i)) == PackageManager.PERMISSION_DENIED)
                return true;
        }
        return false;
    }

    /**
     * Da li je user oznacio permisiju kao never ask again
     * @param permission permisija, primer Manifest.permission.CAMERA
     * @return true ako jeste, false ako nije
     */
    @TargetApi(Build.VERSION_CODES.M)
    public boolean isOnNeverAskAgain(String permission) {
        return (!mActivity.shouldShowRequestPermissionRationale(permission));
    }

    /**
     * Da li je odredjena permisija dobijena
     * @param permission permisija, primer Manifest.permission.CAMERA
     * @return true ako jeste, false ako nije
     */
    public boolean isGranted(String permission) {
        if(!mPerms.containsKey(permission)) return false ;
        return (mPerms.get(permission) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Da li je odredjena permisija odbijena
     * @param permission permisija, primer Manifest.permission.CAMERA
     * @return true ako jeste, false ako nije
     */
    public boolean isDenied(String permission) {
        return (mPerms.get(permission) == PackageManager.PERMISSION_DENIED);
    }


}
