package com.wen.magi.baseframe.activities;

import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.app.LoaderManager;
import android.content.Loader;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.base.BaseActivity;
import com.wen.magi.baseframe.models.PhoneContactItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MVEN on 16/8/23.
 * <p/>
 * email: magiwen@126.com.
 */


public class PhoneContactsActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CONTACTS_LOADER_ID = 1;
    private List<PhoneContactItem> mPhoneContacts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        //initLoader for Contactss
        getLoaderManager().initLoader(CONTACTS_LOADER_ID, null, this);
    }

    private void initUI() {

    }

    @Override
    protected void OnClickView(View v) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == CONTACTS_LOADER_ID) {
            Uri contactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI;

            String[] projection = {
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };
            String selection = null;
            String[] selectionArgs = {};
            String sortOrder = ContactsContract.Contacts.SORT_KEY_PRIMARY;
            return new CursorLoader(this, contactsUri, projection, selection, selectionArgs, sortOrder);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null)
            return;

        mPhoneContacts = contactsFromCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private List<PhoneContactItem> contactsFromCursor(Cursor data) {
        List<PhoneContactItem> contacts = new ArrayList<>();
        if (data.getCount() > 0) {
            data.moveToFirst();
            do {
                PhoneContactItem item = new PhoneContactItem();
                String name = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNum = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                item.strUserName = name;
                item.phoneNumber = phoneNum;
                contacts.add(item);
            } while (data.moveToNext());
        }
        return contacts;
    }
}
