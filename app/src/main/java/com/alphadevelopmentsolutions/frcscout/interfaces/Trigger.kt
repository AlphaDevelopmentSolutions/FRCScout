package com.alphadevelopmentsolutions.frcscout.interfaces

/**
 * Triggers for the database when inserting, updating and deleting
 */
object Trigger {
    /**
     * Gets triggers with specified parameters
     * @param tableName [String] name of table for trigger
     * @param includeDeleteTrigger [Boolean] boolean if the delete trigger should be included
     * @return [List] of [String] SQLite triggers
     */
    fun getTriggers(tableName: String, includeDeleteTrigger: Boolean = false): List<String> {

        return ArrayList<String>().apply {

            if (includeDeleteTrigger)
                add(
                    """
                        CREATE TRIGGER ${tableName}_delete 
                        BEFORE DELETE ON $tableName
                        WHEN (SELECT log_delete FROM ${TableName.LOG_DELETE} LIMIT 1) = 1
                        BEGIN 
                            INSERT OR IGNORE INTO ${TableName.DELETED_RECORDS} (`uuid`) 
                            VALUES (OLD.id);
                        END;
                    """.trimIndent()
                )

            add(
                """
                    CREATE TRIGGER ${tableName}_update
                    AFTER UPDATE ON $tableName
                    WHEN (SELECT log_draft FROM ${TableName.LOG_DRAFT} LIMIT 1) = 1 AND NEW.is_draft != 1
                    BEGIN
                        UPDATE $tableName SET is_draft = 1 WHERE id = NEW.id;
                    END;
                """.trimIndent()
            )

            add(
                """
                    CREATE TRIGGER ${tableName}_insert
                    AFTER INSERT ON $tableName
                    WHEN (SELECT log_draft FROM ${TableName.LOG_DRAFT} LIMIT 1) = 1 AND NEW.is_draft != 1
                    BEGIN
                        UPDATE $tableName SET is_draft = 1 WHERE id = NEW.id;
                    END;
                """.trimIndent()
            )
        }
    }
}