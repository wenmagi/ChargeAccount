/**
 * Copyright 2013 Ognyan Bankov
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wen.magi.baseframe.web;

import com.android.volley.toolbox.ImageLoader;
import com.wen.magi.baseframe.utils.LogUtils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {
    public BitmapLruCache(int maxSize) {
        super(maxSize);
    }


    @Override
    protected int sizeOf(String key, Bitmap value) {
        LogUtils.d("pic size is %s", value.getRowBytes() * value.getHeight());
        return value.getRowBytes() * value.getHeight();
    }


    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }


    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
