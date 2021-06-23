package com.miljanpeles.lib.permissions;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Klasa koja olaksava koriscenje permisija androida.
 * Pre svega u manifest se upisu permisije koje se koriste, a zatim se klasa koristi na sledeci nacin:
 *
     new PPermissions.Builder(this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .requestId(1)
                        .setListener(this)
                        .check();
 *
 */
public class PPermissions {

    private WeakReference<Activity> context;

    /**
     * Lista permisija koje trazimo.
     */
    protected ArrayList permissions;

    static OnRequestPermissionsBack listener;

    private PPermissions(Builder builder) {
        context = builder.context;
        permissions = new ArrayList<>(Arrays.asList(builder.permissions));
        listener = builder.listener;
        Intent callingIntent = PPermissionsActivity.getCallingIntent(context.get(), permissions,builder.requestId);
        context.get().startActivity(callingIntent);
    }

    /**
     * OnRequestPermissionBack listener, odgovor nakon checka
     */
    public interface OnRequestPermissionsBack {
        void onRequestBack(int requestId,@NonNull PPermissionsResponse response);
    }

    /**
     * Builder permisija.
     */
    public static class Builder {
        private WeakReference<Activity> context;
        private String[] permissions;
        private int requestId;

        protected OnRequestPermissionsBack listener;

        /**
         * Instancira builder
         * @param activity activity
         */
        public Builder(Activity activity) {
            this.context = new WeakReference<>(activity);
        }


        /**
         * Permisije koje trazimo
         * @param permissions primer: Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE
         * @return instancu buildera
         */
        public PPermissions.Builder withPermissions(String... permissions) {
            this.permissions = permissions;
            return this;
        }

        /**
         * Postavlja callback listener
         * @param listener listener
         * @return instancu buildera
         */
        public PPermissions.Builder setListener(OnRequestPermissionsBack listener) {
            this.listener = listener;
            return this;
        }

        /**
         * Postavlja request id
         * @param requestId id
         * @return instancu buildera
         */
        public PPermissions.Builder requestId(int requestId){
            this.requestId = requestId;
            return this;
        }

        /**
         * Proveri permisije
         * @return PPermission objekat
         */
        public PPermissions check() {
            return new PPermissions(this);
        }
    }



}
