#include <jni.h>
#include <stdio.h>
#include "sqlite/sqlite3.h"
#include <stdlib.h>
#include <string>


void throw_sqlite3_exception(JNIEnv *env, sqlite3 *handle, int errcode) {
    const char *errmsg = sqlite3_errmsg(handle);
    jclass exClass = env->FindClass("uz/jamshid/nativedb/SQLiteException");
    env->ThrowNew(exClass, errmsg);
}

extern "C" {

    JNICALL

    jlong Java_uz_jamshid_nativedb_SQLiteDatabase_opendb(JNIEnv *env, jobject object, jstring filename) {

        char const *fileNameStr = env->GetStringUTFChars(filename, 0);

        sqlite3 *db = 0;
        int code = sqlite3_open(fileNameStr, &db);

        if(code != SQLITE_OK)
            throw_sqlite3_exception(env, db, code);

        if(fileNameStr!=0)
            env->ReleaseStringUTFChars(filename, fileNameStr);

        return (jlong) db;
    }

    void Java_uz_jamshid_nativedb_SQLiteDatabase_closedb(JNIEnv *env, jobject object, jlong sqliteHandle) {
        sqlite3 *handle = (sqlite3 *) (intptr_t) sqliteHandle;
        int err = sqlite3_close(handle);
        if (SQLITE_OK != err) {
            throw_sqlite3_exception(env, handle, err);
        }
    }

    jlong Java_uz_jamshid_nativedb_SQLitePreparedStatement_prepare(JNIEnv *env, jobject object, jlong sqliteHandle, jstring sql) {
        sqlite3 *handle = (sqlite3 *) (intptr_t) sqliteHandle;

        char const *sqlStr = env->GetStringUTFChars(sql, 0);

        sqlite3_stmt *stmt_handle;

        int errcode = sqlite3_prepare_v2(handle, sqlStr, -1, &stmt_handle, 0);

        if (SQLITE_OK != errcode) {
            throw_sqlite3_exception(env, handle, errcode);
        }

        if (sqlStr != 0) {
            env->ReleaseStringUTFChars(sql, sqlStr);
        }

        return (jlong) stmt_handle;
    }

    jint Java_uz_jamshid_nativedb_SQLitePreparedStatement_step(JNIEnv *env, jobject object, jlong statementHandle) {
        sqlite3_stmt *handle = (sqlite3_stmt *) (intptr_t) statementHandle;

        int errcode = sqlite3_step(handle);
        if (errcode == SQLITE_ROW) {
            return 0;
        } else if (errcode == SQLITE_DONE) {
            return 1;
        } else if (errcode == SQLITE_BUSY) {
            return -1;
        }

        throw_sqlite3_exception(env, sqlite3_db_handle(handle), errcode);

        return 0;
    }

    void Java_uz_jamshid_nativedb_SQLitePreparedStatement_finalize(JNIEnv *env, jobject object, jlong statementHandle) {
        sqlite3_finalize((sqlite3_stmt *) (intptr_t) statementHandle);
    }

    jstring Java_uz_jamshid_nativedb_SQLiteCursor_columnStringValue(JNIEnv *env, jobject object, jlong statementHandle, jint columnIndex) {
        sqlite3_stmt *handle = (sqlite3_stmt *) (intptr_t) statementHandle;
        const char *str = (const char *) sqlite3_column_text(handle, columnIndex);
        if (str != 0) {
            return env->NewStringUTF(str);
        }
        return 0;
    }

    jint Java_uz_jamshid_nativedb_SQLiteCursor_columnIntValue(JNIEnv *env, jobject object, jlong statementHandle, jint columnIndex) {
        sqlite3_stmt *handle = (sqlite3_stmt *) (intptr_t) statementHandle;
        int valType = sqlite3_column_type(handle, columnIndex);
        if (SQLITE_NULL == valType) {
            return 0;
        }
        return sqlite3_column_int(handle, columnIndex);
    }

    void Java_uz_jamshid_nativedb_MainActivity_begin(JNIEnv *env, jobject object, jlong sqliteHandle){
        sqlite3 *handle = (sqlite3 *) (intptr_t) sqliteHandle;
        sqlite3_exec(handle, "BEGIN", 0, 0, 0);
    }

    void Java_uz_jamshid_nativedb_MainActivity_commit(JNIEnv *env, jobject object, jlong sqliteHandle){
        sqlite3 *handle = (sqlite3 *) (intptr_t) sqliteHandle;
        sqlite3_exec(handle, "COMMIT", 0, 0, 0);
    }

    void Java_uz_jamshid_nativedb_MainActivity_end(JNIEnv *env, jobject object, jlong sqliteHandle){
        sqlite3 *handle = (sqlite3 *) (intptr_t) sqliteHandle;
        sqlite3_exec(handle, "END", 0, 0, 0);
    }
}