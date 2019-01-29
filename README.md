# NativeDB
Android project with sqlite3.c library with C++/Java implementation

opendb - Opens specified database file
sqlite3_open() 

closedb - Closes a previously opened database file
sqlite3_close()

prepare - Prepares a SQL statement ready for execution
sqlite3_prepare_v2()

step - Executes a SQL statement previously prepared by the sqlite3_prepare_v2() function
sqlite3_step()

finalize - Deletes a previously prepared SQL statement from memory
sqlite3_finalize()

columnIntValue, columnStringValue - Returns a data field from the results of a SQL retrieval operation where <type> is replaced by the data type of the data to be extracted (text, blob, bytes, int, int16 etc)
sqlite3_column_<type>()
  


Reference:
http://www.yolinux.com/TUTORIALS/SQLite.html
