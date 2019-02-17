/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.abbasi.awaz;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.android.volley.Request;
import com.facebook.drawee.backends.pipeline.Fresco;

import com.google.firebase.auth.FirebaseAuth;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;


public class StarterApplication extends Application {


  private static StarterApplication mInstance;
public String currentlyplaying="";


  @Override
  public void onCreate() {
    super.onCreate();
    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    // Add your initialization code here

    android.content.Context context = this.getApplicationContext();
    Fresco.initialize(this);
    mInstance = this;


  }


  private int vcid;

  public int getSomeVariable() {
    return vcid;
  }

  public void setSomeVariable(int someVariable) {
    this.vcid = someVariable;
  }

}
