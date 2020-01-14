package com.viewparse.api;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;


/**
 * 联系人
 * 读写权限
 */
public class ContactUtil {

    /**
     * 联系人数据库cursor映射
     * 读写联系人权限
     *
     * @return
     */
    public static List getCursorMapList() {
        List list = new ArrayList();
        Map<String, Object> map = new HashMap<>();
        //id
        //姓名
        map.put("text", "姓名");
        map.put("name", "displayName");
        map.put("key", StructuredName.DISPLAY_NAME);
        map.put("mimeType", StructuredName.CONTENT_ITEM_TYPE);
        list.add(map);
        //昵称
        map.clear();
        map.put("text", "昵称");
        map.put("name", "nickName");
        map.put("key", Nickname.NAME);
        map.put("mimeType", Nickname.CONTENT_ITEM_TYPE);
        //姓氏
        map.clear();
        map.put("text", "姓氏");
        map.put("name", "familyName");
        map.put("key", StructuredName.FAMILY_NAME);
        map.put("mimeType", StructuredName.CONTENT_ITEM_TYPE);
        //中间名
        map.clear();
        map.put("text", "中间名");
        map.put("name", "middleName");
        map.put("key", StructuredName.MIDDLE_NAME);
        map.put("mimeType", StructuredName.CONTENT_ITEM_TYPE);
        //公司
        map.clear();
        map.put("text", "公司");
        map.put("name", "company");
        map.put("key", Organization.COMPANY);
        map.put("mimeType", Organization.CONTENT_ITEM_TYPE);
        map.put("cursorFilterKey", Organization.TYPE);
        map.put("cursorFilterValue", Organization.TYPE_CUSTOM);
        //职位
        map.clear();
        map.put("text", "职位");
        map.put("name", "jobTitle");
        map.put("key", Organization.TITLE);
        map.put("mimeType", Organization.CONTENT_ITEM_TYPE);
        map.put("cursorFilterKey", Organization.TYPE);
        map.put("cursorFilterValue", Organization.TYPE_CUSTOM);
        //手机
        map.clear();
        map.put("text", "手机");
        map.put("name", "phone");
        map.put("key", Phone.NUMBER);
        map.put("mimeType", Phone.CONTENT_ITEM_TYPE);
        map.put("cursorFilterKey", Phone.TYPE);
        map.put("cursorFilterValue", Phone.TYPE_MOBILE);
        //单位电话
        map.clear();
        map.put("text", "单位电话");
        map.put("name", "phoneWork");
        map.put("key", Phone.NUMBER);
        map.put("mimeType", Phone.CONTENT_ITEM_TYPE);
        map.put("cursorFilterKey", Phone.TYPE);
        map.put("cursorFilterValue", Phone.TYPE_WORK);
        //住宅电话
        map.clear();
        map.put("text", "住宅电话");
        map.put("name", "phoneHome");
        map.put("key", Phone.NUMBER);
        map.put("mimeType", Phone.CONTENT_ITEM_TYPE);
        map.put("cursorFilterKey", Phone.TYPE);
        map.put("cursorFilterValue", Phone.TYPE_HOME);
        //邮件
        map.clear();
        map.put("text", "邮件");
        map.put("name", "email");
        map.put("key", Email.DATA);
        map.put("mimeType", Email.CONTENT_ITEM_TYPE);
        //姓名拼音
        //QQ即时消息
        map.clear();
        map.put("text", "QQ即时消息");
        map.put("name", "protocolQQ");
        map.put("key", Im.DATA);
        map.put("mimeType", Im.CONTENT_ITEM_TYPE);
        map.put("cursorFilterKey", Im.PROTOCOL);
        map.put("cursorFilterValue", Im.PROTOCOL_QQ);
        //单位地址
        map.clear();
        map.put("text", "单位地址");
        map.put("name", "addressWork");
        map.put("key", StructuredPostal.DATA);
        map.put("mimeType", StructuredPostal.CONTENT_ITEM_TYPE);
        //网站
        map.clear();
        map.put("text", "网站");
        map.put("name", "website");
        map.put("key", Website.DATA);
        map.put("mimeType", Website.CONTENT_ITEM_TYPE);
        //生日
        map.clear();
        map.put("text", "生日");
        map.put("name", "birthday");
        map.put("key", Event.START_DATE);
        map.put("mimeType", Event.CONTENT_ITEM_TYPE);
        map.put("cursorFilterKey", Event.TYPE);
        map.put("cursorFilterValue", Event.TYPE_BIRTHDAY);
        //备注
        map.clear();
        map.put("text", "备注");
        map.put("name", "remark");
        map.put("key", Note.NOTE);
        map.put("mimeType", Note.CONTENT_ITEM_TYPE);
        return list;
    }

    /**
     * 查询联系人
     *
     * @param activity
     * @param name     姓名
     * @param no       手机号
     * @return
     */
    public static List<Contact> getContacts(AppCompatActivity activity, String name, String no) {
        List<Contact> Contacts = new ArrayList<>();
        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = null;
        if (name == null && no == null) {
            cursor = resolver.query(Phone.CONTENT_URI, null, null, null, null);
        } else if (name != null) {
            cursor = resolver.query(Phone.CONTENT_URI, null, ContactsContract.Contacts.DISPLAY_NAME + " like " + "'%" + name + "%'", null, null);
        } else if (no != null) {
            cursor = resolver.query(Phone.CONTENT_URI, null, Phone.NUMBER + " like " + "'%" + no + "%'", null, null);
        }
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            contact.id = Long.valueOf(cursor.getString(cursor.getColumnIndex(Data.RAW_CONTACT_ID)));
            contact.displayName = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
            contact.phone = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
            Contacts.add(contact);
        }
        cursor.close();
        return Contacts;
    }

    /**
     * 添加联系人
     *
     * @param activity
     * @param contact
     */
    public static void addContact(AppCompatActivity activity, Contact contact) {
        String name = contact.displayName;
        String phoneNumber = contact.phone;
        String email = contact.email;
        ContentValues values = new ContentValues();
        Uri rawContactUri = activity.getContentResolver().insert(RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();
        if (name != null) {
            values.put(Data.RAW_CONTACT_ID, rawContactId);
            values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
            values.put(StructuredName.DISPLAY_NAME, name);
            activity.getContentResolver().insert(Data.CONTENT_URI, values);
            values.clear();
        }
        if (phoneNumber != null) {
            values.put(Data.RAW_CONTACT_ID, rawContactId);
            values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
            values.put(Phone.NUMBER, phoneNumber);
            values.put(Phone.TYPE, Phone.TYPE_MOBILE);
            activity.getContentResolver().insert(Data.CONTENT_URI, values);
            values.clear();
        }
        if (email != null) {
            values.put(Data.RAW_CONTACT_ID, rawContactId);
            values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
            values.put(Email.DATA, email);
            values.put(Email.TYPE, Email.TYPE_WORK);
            activity.getContentResolver().insert(Data.CONTENT_URI, values);
        }
    }

    /**
     * 修改联系人
     *
     * @param activity
     * @param contact
     */
    public static void updateContact(AppCompatActivity activity, Contact contact) {
        final Uri CONTENT_URI = Data.CONTENT_URI;
        ContentValues values = new ContentValues();
        ContentResolver resolver = activity.getContentResolver();
        long rawContactId = contact.id;

        String name = contact.displayName;
        String phoneNumber = contact.phone;
        String avatar = contact.phone;
        if (name != null) {
            values.put(StructuredName.DISPLAY_NAME, name);
            int NAMES = resolver.update(CONTENT_URI, values, Data.MIMETYPE + "=? and raw_contact_id = ?", new String[]{StructuredName.CONTENT_ITEM_TYPE, rawContactId + ""});
            values.clear();
        }
        if (phoneNumber != null) {
            values.put(Phone.NUMBER, phoneNumber);
            resolver.update(CONTENT_URI, values, Data.MIMETYPE + "=? and raw_contact_id= ?", new String[]{Phone.CONTENT_ITEM_TYPE, rawContactId + ""});
            values.clear();
        }
        //头像
        if (avatar != null) {
            values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, avatar);
            resolver.update(CONTENT_URI, values, Data.MIMETYPE + "=? AND " + Data.RAW_CONTACT_ID + "= " + rawContactId, new String[]{ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE});
            values.clear();
        }
    }

    public static void deleteContacts(AppCompatActivity activity, List<Contact> list) {
        // 根据姓名求id
        ContentResolver resolver = activity.getContentResolver();
        for (Contact contact : list
        ) {
            String id = contact.id + "";
            Uri uri = Data.CONTENT_URI;
            resolver.delete(uri, Data.RAW_CONTACT_ID + "=?", new String[]{id + ""});
        }
    }

    //根据id删除
    public static void deleteContactsById(AppCompatActivity activity, String id) {
        List<String> ids = new ArrayList<>();
        ids.add(id);
        deleteContactsByIds(activity, ids);
    }

    //根据id批量删除
    public static void deleteContactsByIds(AppCompatActivity activity, List<String> ids) {
        List<Contact> contacts = new ArrayList<>();
        for (String id : ids
        ) {
            Contact contact = new Contact();
            contact.id = Long.valueOf(id);
            contacts.add(contact);
        }
        ContactUtil.deleteContacts(activity, contacts);
    }

    //根据姓名模糊删除
    public static void deleteContactsByName(AppCompatActivity activity, String name) {
        List<Contact> contacts = getContacts(activity, name, null);
        ContactUtil.deleteContacts(activity, contacts);
    }

}

