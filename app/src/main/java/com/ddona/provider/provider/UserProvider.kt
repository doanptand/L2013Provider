package com.ddona.provider.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import com.ddona.provider.db.UserHelper
import com.ddona.provider.util.Const
import java.lang.IllegalArgumentException

class UserProvider : ContentProvider() {
    companion object {
        const val AUTHORITY = "com.ddona.provider.provider.UserProvider"
        private val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/user")
        private const val ALL_USER = 1
        private const val USER_ID = 2

        private val mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    }

    init {
        mUriMatcher.addURI(AUTHORITY, "user", ALL_USER)
        mUriMatcher.addURI(AUTHORITY, "user/#", USER_ID)
    }

    private lateinit var userHelper: UserHelper
    override fun onCreate(): Boolean {
        userHelper = UserHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {

        //content://com.ddona.provider.provider.UserProvider/user
        //content://com.ddona.provider.provider.UserProvider/user/1000
        val query = SQLiteQueryBuilder()//"SELECT * FROM"
        query.tables = Const.USER_TABLE //"SELECT * FROM user"
        if (mUriMatcher.match(uri) == -1) {
            throw IllegalArgumentException("Unknown uri: $uri for query data")
        }
        if (mUriMatcher.match(uri) == USER_ID) {
            //content://com.ddona.provider.provider.UserProvider/user/1000
            //selection:null
            //selectionArgs:null
            val id = uri.pathSegments[1]
            query.appendWhere("${Const.COL_ID}='$id'") //"SELECT * FROM user WHERE _id=100"
        }
        val db = userHelper.writableDatabase
        val cursor = query.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        if (mUriMatcher.match(uri) != ALL_USER) {
            throw IllegalArgumentException("Unknown Uri $uri for inserting data")
        }
        val db = userHelper.writableDatabase
        val returnId = db.insert(Const.USER_TABLE, null, values)
        if (returnId > -1) {
            //content://com.ddona.provider.provider.UserProvider/user/1000
            val resultUri = ContentUris.withAppendedId(CONTENT_URI, returnId)
            context!!.contentResolver.notifyChange(resultUri, null)
            return resultUri
        }
        throw IllegalArgumentException("Some error happen when trying to insert your data with uri: $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        var count: Int
        val db = userHelper.writableDatabase
        when (mUriMatcher.match(uri)) {
            ALL_USER -> {
//        content://com.ddona.provider.provider.UserProvider/user
//        selection: null
//        selectArgs: null
                count = db.delete(Const.USER_TABLE, null, null)
            }
            USER_ID -> {
                //content://com.ddona.provider.provider.UserProvider/user/100
                //selection: null
                //selectArgs: null
                val id = uri.pathSegments[1]
                count = db.delete(Const.USER_TABLE, "${Const.COL_ID}=?", arrayOf(id))

                //content://com.ddona.provider.provider.UserProvider/user/100
                //selection: _id=?
                //selectArgs: "100"
//                count = db.delete(Const.USER_TABLE, selection, selectionArgs)
            }
            else -> throw IllegalArgumentException("Unknown uri: $uri for deleting data")
        }
        return count
    }

//    override fun update(
//        uri: Uri,
//        values: ContentValues?,
//        selection: String?,
//        selectionArgs: Array<out String>?
//    ): Int {
//        //content://com.ddona.provider.provider.UserProvider/user/1000
//        //values : username: doanpt, password: doandeptrai, role: user
//        //selection: null
//        //selectArgs: null
//        if (mUriMatcher.match(uri) != USER_ID) {
//            throw IllegalArgumentException("Unknown uri $uri for updating data")
//        }
//        val id = uri.pathSegments[1]
//        val where = "${Const.COL_ID}=?"
//        val args = arrayOf(id!!)
//        val db = userHelper.writableDatabase
//        val count = db.update(Const.USER_TABLE, values, where, args)
//        if (count == 1) {
//            context!!.contentResolver.notifyChange(uri, null)
//            return count
//        }
//        throw IllegalArgumentException("Some error happen when updating data with uri: $uri")
//    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        //content://com.ddona.provider.provider.UserProvider/user/1000
        //values : username: doanpt, password: doandeptrai, role: user
        //selection: "_id=?"
        //selectArgs: "1000"
        if (mUriMatcher.match(uri) != USER_ID) {
            throw IllegalArgumentException("Unknown uri $uri for updating data")
        }
        val db = userHelper.writableDatabase
        val count = db.update(Const.USER_TABLE, values, selection, selectionArgs)
        if (count == 1) {
            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Some error happen when updating data with uri: $uri")
    }
}