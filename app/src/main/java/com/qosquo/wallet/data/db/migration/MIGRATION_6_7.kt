package com.qosquo.wallet.data.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_6_7 = object : Migration(6,7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE 'categories' ('color_hex' TEXT NOT NULL, 'category_icon_id' INTEGER NOT NULL, " +
                "'id' INTEGER NOT NULL, 'category_name' TEXT NOT NULL, " +
                "'goal' REAL NOT NULL, 'type' INTEGER NOT NULL, " +
                "PRIMARY KEY('id'))")
        db.execSQL("DROP TABLE 'transactions';")
        db.execSQL("CREATE TABLE 'transactions' ('id' INTEGER NOT NULL, 'amount' REAL NOT NULL, " +
                "'account_id' INTEGER NOT NULL, 'category_id' INTEGER NOT NULL, " +
                "'date' INTEGER NOT NULL, 'notes' TEXT NOT NULL, " +
                "PRIMARY KEY('id'), " +
                "FOREIGN KEY ('account_id') REFERENCES accounts('id'), " +
                "FOREIGN KEY ('category_id') REFERENCES categories('id')" +
                ");")
        db.execSQL("CREATE INDEX 'index_transactions_id' ON transactions('id')")
    }
}