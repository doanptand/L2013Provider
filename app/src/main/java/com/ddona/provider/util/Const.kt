package com.ddona.provider.util

object Const {
    const val USER_TABLE = "user"
    const val COL_ID = "_id"
    const val COL_USERNAME = "_username"
    const val COL_PASSWORD = "_password"
    const val COL_ROLE = "_role"

    const val QUERY_CREATE_USER_TABLE = """
        CREATE TABLE $USER_TABLE(
        $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $COL_USERNAME TEXT,
        $COL_PASSWORD TEXT,
        $COL_ROLE INTEGER);
    """
}